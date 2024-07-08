package it.unibo.bombardero.map.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import it.unibo.bombardero.map.api.GenPair;
import it.unibo.bombardero.map.api.MatrixTraversalStrategy;

/**
 * An algorithm that traverses a matrix from side to side.
 */
public final class SideToSideTraversalStrategy implements MatrixTraversalStrategy {

    @Override
    public List<GenPair<Integer, Integer>> compute(final int rows, final int cols) {
        return IntStream
            .range(0, cols)
            .boxed()
            .flatMap(
                x -> 
                IntStream
                    .range(0, rows)
                    .boxed()
                    .map(y -> new GenPair<>(x, y))
            )
            .collect(Collectors.toList());
    }

}
