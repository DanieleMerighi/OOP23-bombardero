package it.unibo.bombardero.character.AI.impl;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.utils.Utils;
import it.unibo.bombardero.character.Direction;

import java.util.EnumSet;

/**
 * This utility class provides a static method to build a JGraphT graph
 * representation
 * from a game map.
 */
public final class GraphBuilderImpl {

    private static final double BREAKABLEWALL_WEIGHT = 2.5;
    private static final int STANDARD_WEIGHT = 1;

    // Private constructor to prevent instantiation
    private GraphBuilderImpl() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Constructs a weighted graph representing the game map.
     * This method takes a GameMap object as input and creates a SimpleWeightedGraph
     * using JGraphT library. Vertices in the graph represent walkable cells (Pairs
     * of x
     * and column coordinates), and edges represent valid connections (movement
     * paths)
     * between walkable cells.
     *
     * @param map the game map object used to identify walkable and unbreakable
     *            cells
     * @return a weighted graph representing the game map
     */
    public static Graph<Pair, DefaultWeightedEdge> buildFromMap(final GameMap map) {
        Graph<Pair, DefaultWeightedEdge> graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

        Stream<Pair> validcells = IntStream.range(0, Utils.MAP_ROWS)
                .boxed()
                .flatMap(r -> IntStream.range(0, Utils.MAP_COLS).mapToObj(c -> new Pair(r, c))
                        .filter(p -> !map.isUnbreakableWall(p)));

        validcells.peek(p -> graph.addVertex(p)).forEach(p -> connectWithNeighbors(graph, p, map));

        return graph;
    }

    /**
     * Connects a cell with its valid walkable neighbors in the game map graph.
     * This method iterates through all cardinal directions (up, down, left, right)
     * and checks if a neighbor exists at that location.
     *
     * @param graph the graph representing the game map
     * @param p     the current cell to connect with neighbors
     * @param map   the game map object used to identify walkable cells and walls
     */
    private static void connectWithNeighbors(final Graph<Pair, DefaultWeightedEdge> graph, final Pair p,
            final GameMap map) {
        EnumSet.allOf(Direction.class)
                .stream()
                .forEach(direction -> {
                    Pair newCoord = new Pair(p.x() + direction.x(), p.y() + direction.y());
                    if (isValidCell(newCoord.x(), newCoord.y())
                            && graph.containsVertex(newCoord)) {
                        DefaultWeightedEdge e = graph.addEdge(p, newCoord);
                        if (map.isBreakableWall(newCoord) || map.isBreakableWall(p)) {
                            graph.setEdgeWeight(e, BREAKABLEWALL_WEIGHT);
                        } else {
                            graph.setEdgeWeight(e, STANDARD_WEIGHT);
                        }
                    }
                });
    }

    private static boolean isValidCell(final int x, final int y) {
        return x >= 0 && x < Utils.MAP_ROWS && y >= 0 && y < Utils.MAP_COLS;
    }
}
