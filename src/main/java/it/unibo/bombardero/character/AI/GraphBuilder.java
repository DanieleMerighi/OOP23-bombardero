package it.unibo.bombardero.character.AI;

import java.util.stream.IntStream;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.utils.Utils;
import it.unibo.bombardero.character.Direction;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

public class GraphBuilder {

    public static Graph<Pair,DefaultEdge> buildFromMap(int[][] map) {
        int numRows = map.length;
        int numCols = map[0].length;
        
        Graph<Pair, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);

        // Add vertices (grid cells) to the graph
        IntStream.range(0, numRows)
                .forEach(row ->
                        IntStream.range(0, numCols)
                                .filter(col -> !isCellBlocked(map, row, col))
                                .forEach(col -> graph.addVertex(new Pair(row, col))));

        // Add edges (connections between adjacent cells) to the graph
        IntStream.range(0, numRows)
                .forEach(row ->
                        IntStream.range(0, numCols)
                                .filter(col -> map[row][col] == Utils.GRASS)
                                .forEach(col -> connectWithNeighbors(graph, map, row, col, numRows, numCols)));

        return graph;
    }

    private static void connectWithNeighbors(Graph<Pair, DefaultEdge> graph, int[][] map, int row, int col, int numRows, int numCols) {
        EnumSet.allOf(Direction.class).forEach(direction -> {
            int newRow = row + direction.getDx();
            int newCol = col + direction.getDy();
            if (isValidCell(newRow, newCol, numRows, numCols) 
            && graph.containsVertex(new Pair(newRow, newCol))
            && !isCellBlocked(map, newRow, newCol)) {
                graph.addEdge(new Pair(row, col), new Pair(newRow, newCol));
            }
        });
    }

    private static boolean isValidCell(int row, int col, int numRows, int numCols) {
        return row >= 0 && row < numRows && col >= 0 && col < numCols;
    }

    private static boolean isCellBlocked(int[][] map, int row, int col) {
        return map[row][col] == Utils.UNBREAKABLE_WALL || map[row][col] == Utils.EXPLOSION;
    }

    public static Optional<List<Pair>> findShortestPath(Graph<Pair, DefaultEdge> graph, Pair source, Pair target) {
        DijkstraShortestPath<Pair, DefaultEdge> dijkstra = new DijkstraShortestPath<>(graph);
        GraphPath<Pair, DefaultEdge> path = dijkstra.getPath(source, target);
        return path == null ? Optional.empty() : Optional.of(path.getVertexList().subList(1, path.getVertexList().size()));
    }
    
}
