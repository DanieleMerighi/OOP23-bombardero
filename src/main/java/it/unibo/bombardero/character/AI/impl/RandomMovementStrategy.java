package it.unibo.bombardero.character.ai.impl;

import java.util.Optional;

import it.unibo.bombardero.character.Enemy;
import it.unibo.bombardero.character.ai.api.MovementStrategy;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.GenPair;

/**
 * The RandomMovementStrategy class implements a movement strategy where the
 * enemy moves randomly.
 */
public class RandomMovementStrategy implements MovementStrategy {
    /**
     * Calculates the next move for the given enemy in a total random way.
     *
     * @param enemy   the enemy for which the next move is to be calculated
     * @param manager the game manager
     * @return an Optional containing the next move as a Pair of coordinates, or an
     *         empty Optional if no move is available
     */
    @Override
    public Optional<GenPair<Integer, Integer>> getNextMove(final Enemy enemy, final GameManager manager) {
        return enemy.getGraph().findNearestCell(manager.getGameMap(), enemy.getIntCoordinate());
    }
}
