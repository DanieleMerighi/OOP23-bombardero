package it.unibo.bombardero.map.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import it.unibo.bombardero.map.api.GenPair;
import it.unibo.bombardero.map.api.MatrixTraversalStrategy;

public class SideToSideTraversalStrategy implements MatrixTraversalStrategy {

    @Override
    public List<GenPair<Integer, Integer>> compute(int rows, int cols) {
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
