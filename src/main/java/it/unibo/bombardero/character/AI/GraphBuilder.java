package it.unibo.bombardero.character.AI;

import java.util.stream.IntStream;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.unibo.bombardero.utils.Utils;
import java.util.EnumSet;

public class GraphBuilder {

    enum Direction {
        UP(-1, 0),
        DOWN(1, 0),
        LEFT(0, -1),
        RIGHT(0, 1);

        private final int dx;
        private final int dy;

        Direction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        public int getDx() {
            return dx;
        }

        public int getDy() {
            return dy;
        }
    }

    public static Graph<Integer,DefaultEdge> buildFromMap(int[][] map) {
        int numRows = map.length;
        int numCols = map[0].length;
        
        Graph<Integer, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);

        // Add vertices (grid cells) to the graph
        IntStream.range(0, numRows)
                .forEach(row ->
                        IntStream.range(0, numCols)
                                .filter(col -> map[row][col] == Utils.UNBREAKABLE_WALL || map[row][col] == Utils.WALL)
                                .forEach(col -> graph.addVertex(row * numCols + col)));

        // Add edges (connections between adjacent cells) to the graph
        IntStream.range(0, numRows)
                .forEach(row ->
                        IntStream.range(0, numCols)
                                .filter(col -> map[row][col] == Utils.UNBREAKABLE_WALL || map[row][col] == Utils.WALL)
                                .forEach(col -> connectWithNeighbors(graph, row, col, numRows, numCols)));

        return graph;
    }

    private static void connectWithNeighbors(Graph<Integer, DefaultEdge> graph, int row, int col, int numRows, int numCols) {
        EnumSet.allOf(Direction.class).forEach(direction -> {
            int newRow = row + direction.getDx();
            int newCol = col + direction.getDy();
            if (isValidCell(newRow, newCol, numRows, numCols) && graph.containsVertex(newRow * numCols + newCol)) {
                graph.addEdge(row * numCols + col, newRow * numCols + newCol);
            }
        });
    }

    private static boolean isValidCell(int row, int col, int numRows, int numCols) {
        return row >= 0 && row < numRows && col >= 0 && col < numCols;
    }
    
}
