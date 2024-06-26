package it.unibo.bombardero.character.ai.impl;

import java.util.List;
import java.util.Optional;

import it.unibo.bombardero.character.Enemy;
import it.unibo.bombardero.character.ai.api.MovementStrategy;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.Pair;

/**
 * The ChaseMovementStrategy class implements a movement strategy where the
 * enemy chases the closest entity.
 */
public class ChaseMovementStrategy implements MovementStrategy {

    /**
     * Calculates the next move for the given enemy based on the closest entity.
     *
     * @param enemy the enemy for which the next move is to be calculated
     * @param map   the game map on which the enemy is located
     * @return an Optional containing the next move as a Pair of coordinates, or an
     *         empty Optional if no move is available
     */
    @Override
    public Optional<Pair> getNextMove(final Enemy enemy, final GameManager manager) {
        return enemy.getClosestEntity(manager).flatMap(closestEntity -> {
            final List<Pair> path = enemy.getGraph().findShortestPathToPlayer(enemy.getIntCoordinate(), closestEntity);
            if (!path.isEmpty()) {
                return Optional.of(path.get(0));
            }
            return Optional.empty();
        });
    }
}
