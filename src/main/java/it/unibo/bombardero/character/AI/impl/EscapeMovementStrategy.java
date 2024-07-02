package it.unibo.bombardero.character.AI.impl;

import java.util.Optional;

import it.unibo.bombardero.character.Enemy;
import it.unibo.bombardero.character.AI.api.MovementStrategy;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.Pair;

/**
 * The RandomMovementStrategy class implements a movement strategy where the
 * enemy moves to the first safe cell.
 */
public class EscapeMovementStrategy implements MovementStrategy {

    /**
     * Calculates the next move for the given enemy, it search the nearest safeCell.
     *
     * @param enemy the enemy for which the next move is to be calculated
     * @param map   the game map on which the enemy is located
     * @return an Optional containing the next move as a Pair of coordinates, or an
     *         empty Optional if no move is available
     */
    @Override
    public Optional<Pair> getNextMove(final Enemy enemy, final GameManager manager) {
        return enemy.getGraph().findNearestSafeCell(enemy.getIntCoordinate(), enemy.getFlameRange());
    }

    /**
     * Checks if one movement strategy is equal to EscapeMovementStrategy.
     *
     * @param obj the other movement strategy to compare with
     * @return true if the two movement strategies are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof EscapeMovementStrategy;
    }

}
