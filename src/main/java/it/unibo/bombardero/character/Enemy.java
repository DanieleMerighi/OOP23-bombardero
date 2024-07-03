package it.unibo.bombardero.character;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import it.unibo.bombardero.cell.BombFactory;
import it.unibo.bombardero.cell.powerup.api.PowerUpType;
import it.unibo.bombardero.character.ai.api.EnemyState;
import it.unibo.bombardero.character.ai.impl.EnemyGraphReasonerImpl;
import it.unibo.bombardero.character.ai.impl.EscapeState;
import it.unibo.bombardero.character.ai.impl.GraphManagerImpl;
import it.unibo.bombardero.character.ai.impl.PatrolState;
import it.unibo.bombardero.character.ai.impl.RandomMovementStrategy;
import it.unibo.bombardero.character.ai.impl.ShortestMovementStrategy;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.Coord;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.physics.api.BoundingBox;
import it.unibo.bombardero.utils.Utils;

/**
 * Represents an enemy character within the game environment.
 * Enemies are intelligent entities that can move around the map, detect dangers
 * (bombs), and chase the player. Their behavior is governed by a state machine
 * with three states: PATROL, CHASE, and ESCAPE.
 */
public class Enemy extends Character {

    private Optional<Pair> nextMove = Optional.empty();
    private EnemyState currentState = new PatrolState(); // Initial state
    private EnemyGraphReasonerImpl graph;
    private int waitTimer = -1;
    private boolean attemptedPowerUp = false; // flag to indicate if power-up attempt failed
    private static final float TOLERANCE = 0.0001f; // A small tolerance value

    /**
     * Constructs a new Enemy with the specified parameters.
     * 
     * @param manager     the game manager that controls the game state
     * 
     * @param coord       the initial coordinates where the enemy is spawned
     * 
     * @param bombFactory the factory to create bombs
     */
    public Enemy(final Coord coord, final BombFactory bombFactory, BoundingBox bBox) {
        super(coord, bombFactory, bBox);
        setStationary(true);
        setFacingDirection(Direction.UP);
    }

    /**
     * Calculate distance between two cells.
     * 
     * @param coord1 the first coordinate
     * 
     * @param coord2 the second coordinate
     * 
     * @return the distance in integer value
     */
    public int calculateDistance(final Pair coord1, final Pair coord2) {
        return Math.abs(coord1.x() - coord2.x()) + Math.abs(coord1.y() - coord2.y());
    }

    /**
     * Checks if the player is within the enemy's detection radius using Manhattan
     * distance.
     *
     * @return true if the player is within detection radius, false otherwise.
     */
    public boolean isEnemyClose(final GameManager manager) {
        return getClosestEntity(manager).map(
                closestCoord -> calculateDistance(getIntCoordinate(), closestCoord) <= Utils.ENEMY_DETECTION_RADIUS)
                .orElse(false);
    }

    /**
     * Retrieves the closest entity (character) to the enemy's current position,
     * excluding itself and the player character.
     *
     * @return An Optional containing the closest entity's coordinates as a Pair,
     *         or empty if no other entities exist.
     */
    public Optional<Pair> getClosestEntity(final GameManager manager) {
        final Pair enemyCoord = getIntCoordinate();
        final List<Character> allEntities = new ArrayList<>(manager.getEnemies());
        allEntities.add(manager.getPlayer());

        return allEntities.stream()
                .map(Character::getIntCoordinate)
                .filter(coord -> !coord.equals(enemyCoord))
                .min((coord1, coord2) -> Integer.compare(calculateDistance(enemyCoord, coord1),
                        calculateDistance(enemyCoord, coord2)));
    }

    /**
     * Retrieves the closest entity (character) to the specified enemy position,
     * excluding itself and the player character.
     *
     * @param enemy the position of the enemy to find the closest entity to
     * @return An Optional containing the closest entity, or empty if no other
     *         entities exist at the specified position.
     */
    public Optional<Character> getClosestEntity(final GameManager manager, final Pair enemy) {
        final List<Character> allEntities = new ArrayList<>(manager.getEnemies());
        allEntities.add(manager.getPlayer());

        return allEntities.stream()
                .filter(e -> e.getIntCoordinate().equals(getIntCoordinate()))
                .findAny();
    }

    /**
     * Checks if a given cell is within the valid map boundaries.
     *
     * @param cell the cell to be checked
     * @return true if the cell is within map boundaries, false otherwise.
     */
    public boolean isValidCell(final Pair cell) {
        return cell.x() >= 0 && cell.y() >= 0 && cell.x() < Utils.MAP_COLS && cell.y() < Utils.MAP_ROWS;
    }

    /**
     * Checks if the current enemy state is equal to another enemy state.
     *
     * @param otherState The other enemy state to compare.
     * @return true if the current state is equal to the other state, false
     *         otherwise.
     */
    public boolean isStateEqualTo(final EnemyState otherState) {
        return currentState.equals(otherState);
    }

    /**
     * Calculates the next move target based on the current enemy state.
     * This method delegates the behavior to the current state's `execute` method
     * and potentially updates the `nextMove` target based on danger zone detection
     * or pathfinding results.
     * 
     * @param time the elapsed time
     */
    private void computeNextDir(final long time, final GameManager manager) {
        GameMap gameMap = manager.getGameMap();
        graph = GraphManagerImpl.getGraphReasoner(gameMap, time);
        currentState.execute(this, manager);
        if (nextMove.isPresent()) {
            if (calculateDistance(getIntCoordinate(), nextMove.get()) > 1) {
                nextMove = new ShortestMovementStrategy().getNextMove(this, manager);
            }
            if (!isStateEqualTo(new EscapeState())
                    && gameMap.whichPowerUpType(nextMove.get()).map(c -> c == PowerUpType.SKULL).orElse(false)) {
                placeBomb(manager, CharacterType.ENEMY);
                nextMove = Optional.empty();
            } else if (gameMap.isBreakableWall(nextMove.get())
                    && graph.findNearestSafeCell(getIntCoordinate(), getFlameRange()).isPresent()) {
                placeBomb(manager, CharacterType.ENEMY);
            }
        } else {
            waitTimer++;
            if (waitTimer >= Utils.MAX_WAITING_TIME) {
                nextMove = new RandomMovementStrategy().getNextMove(this, manager);
            }
        }
    }

    /**
     * Updates the enemy's position and behavior within the game environment.
     * This method is called every frame to handle enemy movement and actions.
     */
    @Override
    public void update(final long elapsedTime, final GameManager manager) {
        GraphManagerImpl.initialize(manager.getGameMap());
        updateSkeleton(elapsedTime, manager, CharacterType.ENEMY);
        if (nextMove.isEmpty()) {
            computeNextDir(elapsedTime, manager);
        } else if (canMoveOn(manager.getGameMap() ,nextMove.get())) {
            setStationary(false);
            final Coord target = new Coord(nextMove.get().x() + 0.5f, nextMove.get().y() + 0.5f);
            final Coord currentPos = getCharacterPosition();

            // Calculate direction vector
            Coord dir = target.subtract(currentPos);

            // Restrict movement to 4 directions (up, down, left, right)
            dir = restrictToGridDirections(dir);

            // Aggiorna la posizione del nemico
            final Coord newPos = currentPos.sum(dir.multiply(getSpeed()));

            // Check if reached the target cell using a small tolerance
            if (isReachedTarget(newPos, target)) {
                setCharacterPosition(target);
                nextMove = Optional.empty(); // Clear target if reached
            } else {
                setCharacterPosition(newPos);
            }
        } else {
            nextMove = Optional.empty();
            setStationary(true);
        }
    }

    private boolean canMoveOn(final GameMap gameMap, final Pair cell) {
        return gameMap.isEmpty(cell) || gameMap.isPowerUp(cell) 
        || (gameMap.isBomb(cell) && getIntCoordinate().equals(cell));
    }

    private boolean isReachedTarget(final Coord currentPos, final Coord target) {
        final float distance = currentPos.distanceTo(target);
        // Use a small fixed tolerance value for the distance check
        return distance <= getSpeed() + TOLERANCE;
    }

    private Coord restrictToGridDirections(final Coord direction) {
        final Direction newDirection = (Math.abs(direction.x()) > Math.abs(direction.y()))
                ? (direction.x() > 0 ? Direction.RIGHT : Direction.LEFT)
                : (direction.y() > 0 ? Direction.DOWN : Direction.UP);
        setFacingDirection(newDirection);
        return new Coord(newDirection.x(), newDirection.y());
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
     * Sets the current state of the enemy.
     *
     * @param newState The new state to set for the enemy.
     */
    public void setState(final EnemyState newState) {
        this.currentState = newState;
    }

    /**
     * Retrieves the enemy graph reasoner used for pathfinding and reasoning.
     *
     * @return The enemy graph reasoner instance.
     */
    public EnemyGraphReasonerImpl getGraph() {
        return graph;
    }

    /**
     * Sets the next move target for the enemy.
     *
     * @param nextMove The optional pair representing the next move target.
     */
    public void setNextMove(final Optional<Pair> nextMove) {
        this.nextMove = nextMove;
    }

    /**
     * Checks if the enemy has attempted to acquire a power-up.
     *
     * @return true if the enemy has attempted to acquire a power-up, false
     *         otherwise.
     */
    public boolean isAttemptedPowerUp() {
        return attemptedPowerUp;
    }

    /**
     * Sets whether the enemy has attempted to acquire a power-up.
     *
     * @param attemptedPowerUp true if the enemy has attempted to acquire a
     *                         power-up, false otherwise.
     */
    public void setAttemptedPowerUp(final boolean attemptedPowerUp) {
        this.attemptedPowerUp = attemptedPowerUp;
    }
}
