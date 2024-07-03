package it.unibo.bombardero.character.ai.impl;

import java.util.Optional;
import java.util.List;
import java.util.EnumSet;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.commons.math3.util.MathArrays.Function;

import it.unibo.bombardero.character.Direction;
import it.unibo.bombardero.character.Enemy;
import it.unibo.bombardero.character.ai.api.MovementStrategy;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.Functions;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.GenPair;

/**
 * The RandomMovementStrategy class implements a movement strategy where the
 * enemy moves randomly.
 */
public class RandomMovementStrategy implements MovementStrategy {

    private static final Random RANDOM = new Random();

    /**
     * Calculates the next move for the given enemy in a total random way.
     *
     * @param enemy the enemy for which the next move is to be calculated
     * @param map   the game map on which the enemy is located
     * @return an Optional containing the next move as a Pair of coordinates, or an
     *         empty Optional if no move is available
     */
    @Override
    public Optional<GenPair<Integer, Integer>> getNextMove(final Enemy enemy, final GameManager manager) {
        final GameMap map = manager.getGameMap();
        final GenPair<Integer, Integer> currentCoord = enemy.getIntCoordinate();
        final List<Direction> dirs = EnumSet.allOf(Direction.class)
                .stream()
                .filter(d -> !enemy.getNextMove().map(move -> move.equals(currentCoord.apply(Functions.sumInt(new GenPair<Integer, Integer> (d.x(), d.y())))))
                        .orElse(false))
                .collect(Collectors.toList());
        while (!dirs.isEmpty()) {
            final Direction randomDirection = dirs.remove(RANDOM.nextInt(dirs.size()));
            final GenPair<Integer, Integer> p = currentCoord.apply(Functions.sumInt(new GenPair<Integer, Integer>(randomDirection.x(), randomDirection.y())));
            if (enemy.isValidCell(p)
                    && (map.isEmpty(p) || map.isBreakableWall(p)
                            || map.isPowerUp(p))) {
                return Optional.of(p);
            }
        }
        return Optional.empty();
    }
}
