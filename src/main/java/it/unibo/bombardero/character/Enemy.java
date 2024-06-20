package it.unibo.bombardero.character;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import it.unibo.bombardero.cell.BombFactory;
import it.unibo.bombardero.character.AI.impl.EnemyGraphReasonerImpl;
import it.unibo.bombardero.character.AI.impl.GraphManagerImpl;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.Coord;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.utils.Utils;

import java.util.EnumSet;
import java.util.stream.Collectors;
import java.util.stream.*;

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
     * @param manager the game manager that controls the game state
     * 
     * @param coord the initial coordinates where the enemy is spawned
     * 
     * @param bombFactory the factory to create bombs
     */
    public Enemy(GameManager manager, Coord coord, BombFactory bombFactory) {
        super(manager, coord, bombFactory);
        GraphManagerImpl.initialize(manager.getGameMap());
    }

    private int calculateDistance(Pair coord1, Pair coord2) {
        return Math.abs(coord1.x() - coord2.x()) + Math.abs(coord1.y() - coord2.y());
    }

    /**
     * Checks if the player is within the enemy's detection radius using Manhattan
     * distance.
     *
     * @return true if the player is within detection radius, false otherwise.
     */
    private boolean isEnemyClose() {
        return getClosestEntity().map(
                closestCoord -> calculateDistance(getIntCoordinate(), closestCoord) <= Utils.ENEMY_DETECTION_RADIUS)
                .orElse(false);
    }

    private Optional<Pair> getClosestEntity() {
        Pair enemyCoord = getIntCoordinate();
        List<Character> allEntities = new ArrayList<>(super.manager.getEnemies());
        allEntities.add(super.manager.getPlayer());

        return allEntities.stream()
                .map(Character::getIntCoordinate)
                .filter(coord -> !coord.equals(enemyCoord))
                .min((coord1, coord2) -> Integer.compare(calculateDistance(enemyCoord, coord1),
                        calculateDistance(enemyCoord, coord2)));
    }

    // when the enemy doesn't know where to move he choose randomly
    private void moveRandomly() {
        Pair currentCoord = getIntCoordinate();
        List<Direction> dirs = EnumSet.allOf(Direction.class)
                .stream()
                .filter(d -> d != Direction.DEFAULT)
                .collect(Collectors.toList());
        while (!dirs.isEmpty()) {
            Direction randomDirection = dirs.remove(new Random().nextInt(dirs.size()));
            Pair p = currentCoord.sum(new Pair(randomDirection.x(), randomDirection.y()));
            if (isValidCell(p)
                    && (super.manager.getGameMap().isEmpty(p) || super.manager.getGameMap().isBreakableWall(p))) {
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
        return cell.x() >= 0 && cell.y() >= 0 && cell.x() < Utils.MAP_COLS && cell.y() < Utils.MAP_ROWS;
    }

    /**
     * Calculates the next move target based on the current enemy state.
     * This method delegates the behavior to the current state's `execute` method
     * and potentially updates the `nextMove` target based on danger zone detection
     * or pathfinding results.
     */
    private void computeNextDir(long time) {
        GameMap gameMap = super.manager.getGameMap();
        graph = GraphManagerImpl.getGraphReasoner(gameMap, time);
        currentState.execute(this); // Delegate behavior to current state
        if (nextMove.isPresent() && currentState != State.ESCAPE) {
            Pair cell = nextMove.get();
            if (currentState == State.CHASE) {
                Pair closeEnemy = getClosestEntity().get();
                Pair currPos = getIntCoordinate();
                if ((closeEnemy.x() == currPos.x() || closeEnemy.y() == currPos.y())
                        && !graph.isPathBlockedByWalls(closeEnemy, getIntCoordinate())
                        && calculateDistance(currPos, closeEnemy) <= getFlameRange()) {
                    placeBomb();
                }
            }
            if (gameMap.isUnbreakableWall(cell)) {
                nextMove = Optional.empty();
            } else if (gameMap.isBreakableWall(cell)) {
                placeBomb();
                nextMove = Optional.empty();
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
            computeNextDir(elapsedTime);
        }

        if (nextMove.isPresent()) {
            Coord target = new Coord(nextMove.get().x() + 0.5f, nextMove.get().y() + 0.5f);
            Coord currentPos = getCharacterPosition();
            Pair currentCoord = getIntCoordinate();

            // Calculate direction vector
            Coord dir = target.subtract(currentPos);

            // Restrict movement to 4 directions (up, down, left, right)
            dir = restrictToGridDirections(dir);
            setFacingDirection(dir);

            // Aggiorna la posizione del nemico
            Coord newPos = currentPos.sum(dir.multiply(getSpeed()));

            // Check if reached the target cell using a small tolerance
            if (currentCoord.equals(nextMove.get()) &&
                    Math.abs(newPos.subtract(target).x()) <= getSpeed() / 2 &&
                    Math.abs(newPos.subtract(target).y()) <= getSpeed() / 2) {
                nextMove = Optional.empty(); // Clear target if reached
            } else {
                setCharacterPosition(newPos);
            }
        }
    }

    private void setFacingDirection(Coord dir) {
        Direction newDirection = Stream.of(Direction.values())
            .filter(d -> !d.equals(Direction.DEFAULT))
            .filter(d -> d.x() == Integer.signum((int) dir.x()) || d.y() == Integer.signum((int) dir.y()))
            .findFirst()
            .orElse(Direction.DEFAULT);
    
        setFacingDirection(newDirection);
    }

    private Coord restrictToGridDirections(Coord direction) {
        // Limita il movimento alle direzioni principali (orizzontale o verticale)
        if (Math.abs(direction.x()) > Math.abs(direction.y())) {
            // Movimento orizzontale
            return new Coord(Math.signum(direction.x()), 0);
        } else {
            // Movimento verticale
            return new Coord(0, Math.signum(direction.y()));
        }
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
                    enemy.currentState = State.WAITING;
                } else {
                    enemy.nextMove = enemy.graph.findNearestSafeCell(enemy.getIntCoordinate(), enemy.getFlameRange());
                }
            }
        },
        WAITING {
            @Override
            void execute(Enemy enemy) {
                enemy.nextMove = Optional.empty(); // da cambiare, quando la bomba esplode allora ti muovi
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
