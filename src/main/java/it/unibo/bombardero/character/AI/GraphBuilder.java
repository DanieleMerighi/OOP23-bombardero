package it.unibo.bombardero.character.AI;

import java.util.stream.Stream;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.utils.Utils;
import it.unibo.bombardero.character.Direction;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

public class GraphBuilder {

    public static Graph<Pair,DefaultEdge> buildFromMap(GameMap map) {
        Graph<Pair, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
        Stream<Pair> validPos = map.getMap().entrySet().stream()
        .map(p -> p.getKey())
        .filter(p -> !(map.isUnbreakableWall(p) || map.isFlame(p)));
        validPos.forEach(p -> graph.addVertex(p));

        // Add edges (connections between adjacent cells) to the graph 
        validPos.forEach(p -> connectWithNeighbors(graph, p));


        return graph;
    }

    private static void connectWithNeighbors(Graph<Pair, DefaultEdge> graph, Pair p) {
        EnumSet.allOf(Direction.class).forEach(direction -> {
            int newRow = p.row() + direction.getDx();
            int newCol = p.col() + direction.getDy();
            if (isValidCell(newRow, newCol) 
            && graph.containsVertex(new Pair(newRow, newCol))) {
                graph.addEdge(p, new Pair(newRow, newCol));
            }
        });
    }

    private static boolean isValidCell(int row, int col) {
        return row >= 0 && row < Utils.MAP_ROWS && col >= 0 && col < Utils.MAP_COLS;
    }


    public static Optional<List<Pair>> findShortestPath(Graph<Pair, DefaultEdge> graph, Pair source, Pair target) {
        DijkstraShortestPath<Pair, DefaultEdge> dijkstra = new DijkstraShortestPath<>(graph);
        GraphPath<Pair, DefaultEdge> path = dijkstra.getPath(source, target);
        return path == null ? Optional.empty() : Optional.of(path.getVertexList().subList(1, path.getVertexList().size()));
    }
    
}
