package it.unibo.bombardero.character;

import java.util.Optional;
import java.util.List;
import java.util.Random;

import it.unibo.bombardero.cell.BombFactory;
import it.unibo.bombardero.character.AI.*;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.Coord;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.utils.Utils;

import java.util.EnumSet;
import java.util.stream.Collectors;

/**
 * Represents an enemy character within the game environment.
 * Enemies are intelligent entities that can move around the map, detect dangers
 * (bombs), and chase the player. Their behavior is governed by a state machine
 * with three states: PATROL, CHASE, and ESCAPE.
 */
public class Enemy extends Character {

    Optional<Pair> nextMove = Optional.empty();
    private State currentState = State.PATROL; // Initial state
    private int movementTimer = 0; // Timer for movement updates
    private EnemyGraphReasonerImpl graph;

    /*
     * Constructs a new Enemy with the specified parameters.
     * 
     * @param manager     the game manager that controls the game state
     * @param coord       the initial coordinates where the enemy is spawned
     * @param bombFactory the factory to create bombs
     */
    public Enemy(GameManager manager, Coord coord, BombFactory bombFactory) {
        super(manager, coord, bombFactory);
        graph = new EnemyGraphReasonerImpl(manager.getGameMap());
    }

    /**
     * Checks if the player is within the enemy's detection radius using Manhattan distance.
     *
     * @return true if the player is within detection radius, false otherwise.
     */
    private boolean isEnemyClose() {
        Pair enemyCoord = getIntCoordinate();
        Pair playerCoord = super.manager.getPlayer().getIntCoordinate();
        int detectionRadius = Utils.ENEMY_DETECTION_RADIUS; // Assuming detection radius is defined in Utils

        // Calculate Manhattan distance between enemy and player
        int distance = Math.abs(enemyCoord.row() - playerCoord.row())
                + Math.abs(enemyCoord.col() - playerCoord.col());

        return distance <= detectionRadius; // Check if player is within detection radius
    }

    /*
    private void placeBomb(Pair targetCell) {
        if (hasBombsLeft() && isValidCell(targetCell)
                && super.manager.getGameMap().isEmpty(targetCell)) {
            // map.addBomb(null, targetCell);
            //numBombs--;
        }
    }
    */ 

    // when the enemy doesn't know where to move he choose randomly
    private void moveRandomly() {
        Pair currentCoord = getIntCoordinate();
        List<Direction> dirs = EnumSet.allOf(Direction.class)
                .stream()
                .filter(d -> d != Direction.DEFAULT)
                .collect(Collectors.toList());
        while (!dirs.isEmpty()) {
            Direction randomDirection = dirs.remove(new Random().nextInt(dirs.size()));
            Pair p = currentCoord.sum(new Pair(randomDirection.getDx(), randomDirection.getDy()));
            if (isValidCell(p) && super.manager.getGameMap().isEmpty(p)) {
                nextMove = Optional.of(p);
                break;
            }
        }
        if (nextMove.isEmpty()) {
            System.out.println("Enemy: Stuck! No valid random move found.");
        }
    }

    /**
     * Checks if a given cell is within the valid map boundaries.
     *
     * @param cell the cell to be checked
     * @return true if the cell is within map boundaries, false otherwise.
     */
    private boolean isValidCell(Pair cell) {
        return cell.row() >= 0 && cell.col() >= 0 && cell.row() <= Utils.MAP_ROWS && cell.col() <= Utils.MAP_COLS;
    }

     /**
     * Calculates the next move target based on the current enemy state.
     * This method delegates the behavior to the current state's `execute` method
     * and potentially updates the `nextMove` target based on danger zone detection
     * or pathfinding results.
     */
    private void computeNextDir() {
        graph = new EnemyGraphReasonerImpl(super.manager.getGameMap());
        currentState.execute(this); // Delegate behavior to current state
        // nextMove.ifPresent(cell -> {
        //     if(graph.isInDangerZone(cell, getFlameRange())) {
        //         nextMove = Optional.empty();
        //     }
        //     if (this.getManager().getGameMap().isBreakableWall(cell)) {
        //         placeBomb();
        //         // change the state of the enemy?
        //     }
        // });
        if(nextMove.isPresent()) {
            Pair cell = nextMove.get();
            if(graph.isInDangerZone(cell, getFlameRange())) {
                nextMove = Optional.empty();
            }
            if (super.manager.getGameMap().isBreakableWall(cell)) {
                placeBomb();
                // change the state of the enemy?
            }
        }
    }

    /**
     * Updates the enemy's position and behavior within the game environment.
     * This method is called every frame to handle enemy movement and actions.
     */
    @Override
    public void update(final long elapsedTime) {
        movementTimer += 1;
        updateSkeleton(elapsedTime);
        // Every 60 frames (assuming 60 fps), call computeNextDir to get the next target
        if (movementTimer >= 60 || nextMove.isEmpty()) {
            movementTimer = movementTimer >= 60 ? 0 : movementTimer;
            computeNextDir();
        }

        if (nextMove.isPresent()) {
            Pair target = nextMove.get();
            Coord direction = getCharacterPosition().subtract(target); // Calculate direction vector

            // Restrict movement to 4 directions (up, down, left, right)
            direction = restrictToGridDirections(direction);
            setCharacterPosition(getCharacterPosition().sum(direction.multiply(getSpeed())));

            // Check if reached the target cell using a small tolerance
            if (Math.abs(target.row() - getCharacterPosition().row()) < getSpeed()/2 &&
                    Math.abs(target.col() - getCharacterPosition().col()) < getSpeed()/2) {
                nextMove = Optional.empty(); // Clear target if reached
            }
        }
    }

    // Helper method to restrict movement to 4 directions (up, down, left, right)
    private Coord restrictToGridDirections(Coord direction) {
        // Check if the direction is already aligned with one of the 4 grid directions
        if (Math.abs(direction.row()) > Math.abs(direction.col())) {
            // Vertical movement
            direction = new Coord(Math.signum(direction.row()) * 1, 0);
        } else {
            // Horizontal movement
            direction = new Coord(0, Math.signum(direction.col()) * 1);
        }
        return direction;
    }

    /**
     * Represents the different behavioral states of an enemy in the game.
     * Each state defines specific actions the enemy takes based on its current
     * situation (e.g., danger zone, player proximity).
     */
    public enum State {
        /**
         * In patrol state, the enemy moves randomly within the map.
         * It checks for dangers (bombs) and nearby players regularly.
         * - Transitions to ESCAPE if it detects a bomb within its flame range.
         * - Transitions to CHASE if it detects the player nearby.
         */
        PATROL {
            @Override
            void execute(Enemy enemy) {
                if (enemy.graph.isInDangerZone(enemy.getIntCoordinate(), enemy.getFlameRange())) { // Detected bomb
                    enemy.currentState = State.ESCAPE;
                } else if (enemy.isEnemyClose()) { // Detected player
                    enemy.currentState = State.CHASE;
                } else {
                    enemy.moveRandomly(); // Keep patrolling
                }
            }
        },
        /**
         * In chase state, the enemy actively seeks the player.
         * It finds the shortest path to the player's current position and sets it
         * as the next move target.
         * - Transitions back to PATROL if it loses sight of the player.
         */
        CHASE {
            @Override
            void execute(Enemy enemy) {
                if (!enemy.isEnemyClose()) { // Lost sight of player
                    enemy.currentState = State.PATROL;
                } else {
                    List<Pair> path = enemy.graph.findShortestPathToPlayer(enemy.getIntCoordinate(),
                            enemy.manager.getPlayer().getIntCoordinate());
                    enemy.nextMove = Optional.of(path.get(0));
                }
            }
        },
        /**
         * In escape state, the enemy tries to move away from a detected danger zone.
         * It finds the nearest safe space outside the bomb's explosion radius
         * and sets it as the next move target.
         * - Transitions back to PATROL if it reaches a safe space.
         */
        ESCAPE {
            @Override
            void execute(Enemy enemy) {
                if (!enemy.graph.isInDangerZone(enemy.getIntCoordinate(), enemy.getFlameRange())) { // Safe now
                    enemy.currentState = State.PATROL;
                } else {
                    enemy.nextMove = enemy.graph.findNearestSafeSpace(enemy.getIntCoordinate(), enemy.getFlameRange());
                }
            }
        };

        abstract void execute(Enemy enemy);
    }

    /**
     * Retrieves the enemy's current target position, if any.
     * This method returns an Optional object containing the next move target as a
     * Pair (row, column) if a target exists. Otherwise, it returns an empty
     * Optional.
     *
     * @return An Optional object containing the next move target (Pair) or empty if
     *         none.
     */
    public Optional<Pair> getNextMove() {
        return nextMove;
    }

    /**
     * Retrieves the enemy's current behavioral state.
     * This method returns the current State enum value representing the enemy's
     * current behavior (PATROL, CHASE, or ESCAPE).
     *
     * @return The current State enum value of the enemy.
     */
    public State getState() {
        return currentState;
    }
}
