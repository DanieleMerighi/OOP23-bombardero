package it.unibo.bombardero.character.ai.api;

import java.util.Optional;

import it.unibo.bombardero.character.Enemy;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.GenPair;

/**
 * This interface defines a strategy for enemies to move around the game map.
 */
public interface MovementStrategy {
    /**
     * Calculates the next move for the given enemy.
     *
     * @param enemy the enemy for which the next move is to be calculated
     * @param manager the game manager
     * @return an Optional containing the next move as a Pair of coordinates, or an empty Optional if no move is available
     */
    Optional<GenPair<Integer, Integer>> getNextMove(Enemy enemy, GameManager manager);

}
