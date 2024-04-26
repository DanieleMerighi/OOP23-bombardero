package it.unibo.bombardero.character.AI;

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

public class GraphBuilder {

    public static Graph<Pair, DefaultWeightedEdge> buildFromMap(GameMap map) {
        Graph<Pair, DefaultWeightedEdge> graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
        // Stream<Pair> validPos = map.getMap().entrySet().stream()
        //         .map(p -> p.getKey())
        //         .filter(p -> !(map.isUnbreakableWall(p) || map.isFlame(p) || map.isBomb(p)));

        Stream<Pair> validPos2 = IntStream.range(0, Utils.MAP_ROWS)
                .boxed()
                .flatMap(r -> IntStream.range(0, Utils.MAP_COLS).mapToObj(c -> new Pair(r, c))
                        .filter(p -> !(map.isUnbreakableWall(p) || map.isFlame(p) || map.isBomb(p))));

        validPos2.peek(p -> graph.addVertex(p)).forEach(p -> connectWithNeighbors(graph, p, map));

        return graph;
    }

    private static void connectWithNeighbors(Graph<Pair, DefaultWeightedEdge> graph, Pair p, GameMap map) {
        EnumSet.allOf(Direction.class).forEach(direction -> {
            Pair newCoord = new Pair(p.row() + direction.getDx(), p.col() + direction.getDy());
            if (isValidCell(newCoord.row(), newCoord.col())
                    && graph.containsVertex(newCoord)) {
                DefaultWeightedEdge e = graph.addEdge(p, newCoord);
                if (map.isBreakableWall(newCoord) || map.isBreakableWall(p)) {
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
}
