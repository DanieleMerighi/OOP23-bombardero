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
    private EnemyGraphReasonerImpl graph;
    private List<Pair> path = new ArrayList<>();

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
                .filter(d -> !nextMove.map(move -> move.equals(currentCoord.sum(new Pair(d.x(), d.y())))).orElse(false))
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

    private void moveToClosestEntity() {
        Optional<Pair> closestEntityOpt = getClosestEntity();
        if (closestEntityOpt.isPresent()) {
            Pair closestEntity = closestEntityOpt.get();
            List<Pair> path = graph.findShortestPathToPlayer(getIntCoordinate(), closestEntity);
            if (!path.isEmpty()) {
                nextMove = Optional.of(path.get(0));
            }
        } else {
            // If no entity is found, fallback to random movement or patrol
            moveRandomly();
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
    }

    /**
     * Updates the enemy's position and behavior within the game environment.
     * This method is called every frame to handle enemy movement and actions.
     */
    @Override
    public void update(final long elapsedTime) {
        updateSkeleton(elapsedTime);
        if (nextMove.isEmpty()) {
            computeNextDir(elapsedTime);
        } else if (canMoveOn(nextMove.get())) {
            Coord target = new Coord(nextMove.get().x() + 0.5f, nextMove.get().y() + 0.5f);
            Coord currentPos = getCharacterPosition();

            // Calculate direction vector
            Coord dir = target.subtract(currentPos);

            // Restrict movement to 4 directions (up, down, left, right)
            dir = restrictToGridDirections(dir);

            // Aggiorna la posizione del nemico
            Coord newPos = currentPos.sum(dir.multiply(getSpeed()));

            // Check if reached the target cell using a small tolerance
            if (isReachedTarget(newPos, target)) {
                nextMove = Optional.empty(); // Clear target if reached
            } else {
                setCharacterPosition(newPos);
            }
        } else{
            nextMove = Optional.empty();
        }
    }

    private boolean canMoveOn(Pair cell) {
        // Check if the cell is empty
        if (this.manager.getGameMap().isEmpty(cell)) {
            return true;
        }
    
        // Check if the cell has a bomb, but the enemy is currently on it
        if (this.manager.getGameMap().isBomb(cell) && getIntCoordinate().equals(cell)) {
            return true;
        }
    
        // In all other cases, the enemy cannot move to the cell
        return false;
    }
    

    private boolean isReachedTarget(Coord currentPos, Coord target) {
        double distance = currentPos.distanceTo(target);
        // Use a small fixed tolerance value for the distance check
        return distance <= getSpeed();
    }

    private Coord restrictToGridDirections(Coord direction) {
        Direction newDirection;
        // Limita il movimento alle direzioni principali (orizzontale o verticale)
        if (Math.abs(direction.x()) > Math.abs(direction.y())) {
            // Movimento orizzontale
            newDirection = direction.x() > 0 ? Direction.RIGHT : Direction.LEFT;
        } else {
            // Movimento verticale
            newDirection = direction.y() > 0 ? Direction.DOWN : Direction.UP;
        }
        setFacingDirection(newDirection);
        return new Coord(newDirection.x(), newDirection.y());
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
                    enemy.moveToClosestEntity(); // Keep patrolling
                    if (enemy.manager.getGameMap().isBreakableWall(enemy.nextMove.get())) {
                        if (enemy.graph.canPlaceBomb(enemy.getIntCoordinate(), enemy.getFlameRange())) {
                            enemy.placeBomb();
                        }
                        enemy.nextMove = Optional.empty();
                    }
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
                if (enemy.graph.isInDangerZone(enemy.getIntCoordinate(), enemy.getFlameRange())) { // Detected bomb
                    enemy.currentState = State.ESCAPE;
                } else if (!enemy.isEnemyClose()) { // Lost sight of player
                    enemy.currentState = State.PATROL;
                } else {
                    if(enemy.path.isEmpty()) {
                        enemy.path = enemy.graph.findShortestPathToPlayer(enemy.getIntCoordinate(),
                        enemy.manager.getPlayer().getIntCoordinate());
                    }
                    enemy.nextMove = Optional.of(enemy.path.removeFirst());
                    if (enemy.manager.getGameMap().isBreakableWall(enemy.nextMove.get())) {
                        if (enemy.graph.canPlaceBomb(enemy.getIntCoordinate(), enemy.getFlameRange())) {
                            enemy.placeBomb();
                        }
                    } else {
                        Pair closeEnemy = enemy.getClosestEntity().get();
                        Pair currPos = enemy.getIntCoordinate();
                        if ((closeEnemy.x() == currPos.x() || closeEnemy.y() == currPos.y())
                                && !enemy.graph.isPathBlockedByWalls(closeEnemy, currPos)
                                && enemy.calculateDistance(currPos, closeEnemy) <= enemy.getFlameRange()) {
                            enemy.placeBomb();
                            enemy.currentState = ESCAPE;
                        }
                    }

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
                    if(enemy.nextMove.isEmpty()) {
                        enemy.currentState = WAITING;
                    }
                }
            }
        },
        WAITING {
            @Override
            void execute(Enemy enemy) {
                enemy.nextMove = Optional.empty(); // da cambiare, quando la bomba esplode allora ti muovi
                if (enemy.graph.findNearestBomb(enemy.getIntCoordinate()).isEmpty()) {
                    enemy.currentState = PATROL;
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
