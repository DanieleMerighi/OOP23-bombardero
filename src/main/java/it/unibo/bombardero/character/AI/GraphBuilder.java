package it.unibo.bombardero.character.AI;

import java.util.stream.Stream;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.utils.Utils;
import it.unibo.bombardero.character.Direction;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

public class GraphBuilder {

    public static Graph<Pair,DefaultWeightedEdge> buildFromMap(GameMap map) {
        Graph<Pair, DefaultWeightedEdge> graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
        Stream<Pair> validPos = map.getMap().entrySet().stream()
        .map(p -> p.getKey())
        .filter(p -> !(map.isUnbreakableWall(p) || map.isFlame(p) || map.isBomb(p)));
        validPos.forEach(p -> graph.addVertex(p));

        // Add edges (connections between adjacent cells) to the graph 
        validPos.forEach(p -> connectWithNeighbors(graph, p, map));


        return graph;
    }

    private static void connectWithNeighbors(Graph<Pair, DefaultWeightedEdge> graph, Pair p, GameMap map) {
        EnumSet.allOf(Direction.class).forEach(direction -> {
            Pair newCoord  = new Pair(p.row() + direction.getDx(), p.col() + direction.getDy());
            if (isValidCell(newCoord.row(), newCoord.col()) 
            && graph.containsVertex(newCoord)) {
                DefaultWeightedEdge e = graph.addEdge(p, newCoord);
                if(map.isBreakableWall(newCoord)) {
                    graph.setEdgeWeight(e, 2.5);
                } else {
                    graph.setEdgeWeight(e, 1);
                }
            }
        });
    }

    private static boolean isValidCell(int row, int col) {
        return row >= 0 && row < Utils.MAP_ROWS && col >= 0 && col < Utils.MAP_COLS;
    }


    public static Optional<List<Pair>> findShortestPath(Graph<Pair, DefaultWeightedEdge> graph, Pair source, Pair target) {
        DijkstraShortestPath<Pair, DefaultWeightedEdge> dijkstra = new DijkstraShortestPath<>(graph);
        GraphPath<Pair, DefaultWeightedEdge> path = dijkstra.getPath(source, target);
        return path == null ? Optional.empty() : Optional.of(path.getVertexList().subList(1, path.getVertexList().size()));
    }
    
}
