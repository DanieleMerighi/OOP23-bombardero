package it.unibo.bombardero.character.ai.impl;

import java.util.Optional;
import java.util.List;

import it.unibo.bombardero.character.Enemy;
import it.unibo.bombardero.character.ai.api.MovementStrategy;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.Pair;

/**
 * The ShortestMovementStrategy class implements a movement strategy where the
 * enemy chases nearest cell in a path.
 */
public class ShortestMovementStrategy implements MovementStrategy {

    /**
     * Calculates the next move for the given enemy, it tooks the first coordinate
     * in a shortest path.
     *
     * @param enemy the enemy for which the next move is to be calculated
     * @param map   the game map on which the enemy is located
     * @return an Optional containing the next move as a Pair of coordinates, or an
     *         empty Optional if no move is available
     */
    @Override
    public Optional<Pair> getNextMove(final Enemy enemy, final GameManager manager) {
        if (enemy.getNextMove().isEmpty()) {
            return new RandomMovementStrategy().getNextMove(enemy, manager);
        }
        final List<Pair> l = enemy.getGraph().findShortestPathToPlayer(enemy.getIntCoordinate(), enemy.getNextMove().get());
        if (l.isEmpty()) {
            return new RandomMovementStrategy().getNextMove(enemy, manager);
        } else {
            return Optional.of(l.get(0));
        }
    }
}
