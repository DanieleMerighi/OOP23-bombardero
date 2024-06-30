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
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.utils.Utils;

/**
 * This class implements the Map Manager concept, a class that manages 
 * the dynamic aspects of a specific instance of Game Map passed to the 
 * constructor.
 * @author Federico Bagattoni
 */
public class MapGeneratorImpl implements MapGenerator {

    /* This number and List represent the twelve cells on which nothing can spawn except the player: */
    /* NOTE: the number "12" does NOT depend from the arena's size, however the "MAP_CORNERS" Set does. */
    private static final int MAP_CORNERS_NUMBER = 12;
    private final Set<Pair> mapCorners = new HashSet<>();

    /** 
     * Creates a new Map Manager managing a the map passed as argument.
     * @param map the map to manage
     */
    public MapGeneratorImpl() {
        this.computeMapCorners();
    }

    @Override
    public int getTotalWallsToGenerate(final double wallSpawnRate) {
        return (int) Math.floor(
            (Utils.MAP_COLS * Utils.MAP_ROWS
                - Math.floorDiv(Utils.MAP_COLS, 2)
                * Math.floorDiv(Utils.MAP_ROWS, 2)
                - MAP_CORNERS_NUMBER
            ) * wallSpawnRate
        );
    }

    @Override
    public Set<Pair> generateUnbreakableWalls() {
        return IntStream
            .range(0, Utils.MAP_ROWS)
            .filter(num -> num % 2 != 0)
            .boxed()
            .flatMap(x -> IntStream
                .range(0, Utils.MAP_COLS)
                .filter(num -> num % 2 != 0)
                .mapToObj(y -> new Pair(x, y))
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
    public Set<Pair> generateBreakableWalls(final GameMap map, final int totalWallsToGenerate) {
        final Random rnd = new Random();
        Pair coordinate;
        final Set<Pair> walls = new HashSet<>();
        int counter = totalWallsToGenerate;
        while (counter != 0) {
            do {
                coordinate = new Pair(rnd.nextInt(Utils.MAP_COLS), rnd.nextInt(Utils.MAP_ROWS));
            } while (!map.isEmpty(coordinate) || this.mapCorners.contains(coordinate) || walls.contains(coordinate));
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
    public List<Pair> generateCollapseOrder() {
        final List<Pair> order = new ArrayList<>();
        int top = 0, bottom = Utils.MAP_ROWS - 1, left = 0, right = Utils.MAP_COLS - 1;
        while (top <= bottom && left <= right) {

            for (int i = left; i <= right; i++) {
                order.add(new Pair(top, i));
            }
            top++;
            for (int i = top; i <= bottom; i++) {
                order.add(new Pair(i, right));
            }
            right--;
            if (top <= bottom) {
                for (int i = right; i >= left; i--) {
                    order.add(new Pair(bottom, i));
                } 
                bottom--;
            }
            if (left <= right) {
                for (int i = bottom; i >= top; i--) {
                    order.add(new Pair(i, left));
                }
                left++;
            }
        }
        return order;
    }

    /** 
     * To be called during the manager's initialization, it computes the twelve cells 
     * on which nothing can spawn except the player
     */
    private void computeMapCorners() {
        this.mapCorners.add(new Pair(0, 0));
        this.mapCorners.add(new Pair(0, 1));
        this.mapCorners.add(new Pair(1, 0));

        this.mapCorners.add(new Pair(Utils.MAP_ROWS - 1, Utils.MAP_COLS - 1));
        this.mapCorners.add(new Pair(Utils.MAP_ROWS - 1, Utils.MAP_COLS - 2));
        this.mapCorners.add(new Pair(Utils.MAP_ROWS - 2, Utils.MAP_COLS - 1));

        this.mapCorners.add(new Pair(Utils.MAP_ROWS - 1, 0));
        this.mapCorners.add(new Pair(Utils.MAP_ROWS - 1, 1));
        this.mapCorners.add(new Pair(Utils.MAP_ROWS - 2, 0));

        this.mapCorners.add(new Pair(0, Utils.MAP_COLS - 1));
        this.mapCorners.add(new Pair(1, Utils.MAP_COLS - 1));
        this.mapCorners.add(new Pair(0, Utils.MAP_COLS - 2));

    }
}
