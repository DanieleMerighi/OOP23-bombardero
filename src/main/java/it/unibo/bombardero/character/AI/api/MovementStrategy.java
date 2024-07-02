package it.unibo.bombardero.character.ai.api;

import java.util.Optional;

import it.unibo.bombardero.character.Enemy;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.Pair;

/**
 * This interface defines a strategy for enemies to move around the game map.
 */
public interface MovementStrategy {
    /**
     * Calculates the next move for the given enemy.
     *
     * @param enemy the enemy for which the next move is to be calculated
     * @param map the game map on which the enemy is located
     * @return an Optional containing the next move as a Pair of coordinates, or an empty Optional if no move is available
     */
    Optional<Pair> getNextMove(Enemy enemy, GameManager manager);

}
