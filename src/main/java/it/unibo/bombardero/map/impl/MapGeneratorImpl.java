package it.unibo.bombardero.map.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.MapGenerator;
import it.unibo.bombardero.map.api.MatrixTraversalStrategy;
import it.unibo.bombardero.map.api.GenPair;
import it.unibo.bombardero.utils.Utils;

/**
 * This class implements the Map Manager concept, a class that manages 
 * the dynamic aspects of a specific instance of Game Map passed to the 
 * constructor.
 * @author Federico Bagattoni
 */
public final class MapGeneratorImpl implements MapGenerator {

    /* This number and List represent the twelve cells on which nothing can spawn except the player: */
    /* NOTE: the number "12" does NOT depend from the arena's size, however the "MAP_CORNERS" Set does. */
    private static final int MAP_CORNERS_NUMBER = 12;
    private final Set<GenPair<Integer, Integer>> mapCorners = new HashSet<>();

    /** 
     * Creates a new Map Manager managing a the map passed as argument.
     */
    public MapGeneratorImpl() {
        this.computeMapCorners();
    }

    @Override
    public int getTotalWallsToGenerate(final double wallSpawnRate) {
        return (int) Math.floor(
            (Utils.MAP_COLS * Utils.MAP_ROWS
                - Math.floorDiv(Utils.MAP_COLS, 2) * Math.floorDiv(Utils.MAP_ROWS, 2)
                - MAP_CORNERS_NUMBER
            ) * wallSpawnRate
        );
    }

    @Override
    public Set<GenPair<Integer, Integer>> generateUnbreakableWalls() {
        return IntStream
            .range(0, Utils.MAP_ROWS)
            .filter(num -> num % 2 != 0)
            .boxed()
            .flatMap(x -> IntStream
                .range(0, Utils.MAP_COLS)
                .filter(num -> num % 2 != 0)
                .mapToObj(y -> new GenPair<Integer, Integer>(x, y))
            )
            .collect(Collectors.toSet());
    }

    /**
     * Generates the number of walls requested, every element satisfies the costraint of
     * not being generated in the corners and over other obstacles placed before the generation.
     * @param totalWallsToGenerate the number of walls to be generated
     * @return a Set containing all the generated coordinates
     */
    @Override
    public Set<GenPair<Integer, Integer>> generateBreakableWalls(final GameMap map, final int totalWallsToGenerate) {
        final Random rnd = new Random();
        final Set<GenPair<Integer, Integer>> unbreakableWalls = generateUnbreakableWalls();
        GenPair<Integer, Integer> coordinate;
        final Set<GenPair<Integer, Integer>> walls = new HashSet<>();
        int counter = totalWallsToGenerate;
        while (counter > 0) {
            do {
                coordinate = new GenPair<>(rnd.nextInt(Utils.MAP_COLS), rnd.nextInt(Utils.MAP_ROWS));
            } while (
                !map.isEmpty(coordinate)
                || this.mapCorners.contains(coordinate)
                || walls.contains(coordinate)
                || unbreakableWalls.contains(coordinate));
            walls.add(coordinate);
            counter--;
        }
        return walls;
    }

    /** 
     * Computes the order in which the arena will collapse, applying and algorithm 
     * of spiral traversal to the game map.
     * @return the list of walls in collpase-order, the first element being the first to fall 
     */
    @Override
    public List<GenPair<Integer, Integer>> generateCollapseOrder(final MatrixTraversalStrategy strategy) {
        return strategy.compute(GameMap.ROWS, GameMap.COLS);
    }

    /** 
     * To be called during the manager's initialization, it computes the twelve cells 
     * on which nothing can spawn except the player.
     */
    private void computeMapCorners() {
        this.mapCorners.add(new GenPair<>(0, 0));
        this.mapCorners.add(new GenPair<>(0, 1));
        this.mapCorners.add(new GenPair<>(1, 0));

        this.mapCorners.add(new GenPair<>(Utils.MAP_ROWS - 1, Utils.MAP_COLS - 1));
        this.mapCorners.add(new GenPair<>(Utils.MAP_ROWS - 1, Utils.MAP_COLS - 2));
        this.mapCorners.add(new GenPair<>(Utils.MAP_ROWS - 2, Utils.MAP_COLS - 1));

        this.mapCorners.add(new GenPair<>(Utils.MAP_ROWS - 1, 0));
        this.mapCorners.add(new GenPair<>(Utils.MAP_ROWS - 1, 1));
        this.mapCorners.add(new GenPair<>(Utils.MAP_ROWS - 2, 0));

        this.mapCorners.add(new GenPair<>(0, Utils.MAP_COLS - 1));
        this.mapCorners.add(new GenPair<>(1, Utils.MAP_COLS - 1));
        this.mapCorners.add(new GenPair<>(0, Utils.MAP_COLS - 2));

    }
}
