package it.bombardero;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.map.impl.GameMapImpl;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.unibo.bombardero.cell.Bomb;
import it.unibo.bombardero.cell.Flame;
import it.unibo.bombardero.cell.Cell.CellType;
import it.unibo.bombardero.character.AI.*;

public class TestGraphBuilder {

    private Graph<Pair, DefaultWeightedEdge> graph;

    @Test
    public void testBaseMapSize() {
        GameMap baseMap = new GameMapImpl(true);
        this.graph = GraphBuilder.buildFromMap(baseMap);
        // the base map (built with only unbreakable wall) have 133 vertex
        assertEquals(133, this.graph.vertexSet().size());
        assertEquals(168, this.graph.edgeSet().size());
        // adding (or removing) breakable wall should not change the number of vertex or
        // edges
        baseMap.addBreakableWall(new Pair(0, 2));
        baseMap.addBreakableWall(new Pair(0, 3));
        baseMap.addBreakableWall(new Pair(0, 4));
        assertEquals(133, this.graph.vertexSet().size());
        assertEquals(168, this.graph.edgeSet().size());
    }

    @Test
    public void testMapSizeWithObastacles() {
        GameMap baseMap = new GameMapImpl(true);
        baseMap.addFlame(new Flame(CellType.FLAME_BODY_HORIZONTAL), new Pair(0, 2));
        baseMap.addFlame(new Flame(CellType.FLAME_BODY_HORIZONTAL), new Pair(0, 3));
        baseMap.addFlame(new Flame(CellType.FLAME_BODY_HORIZONTAL), new Pair(0, 4));
        //baseMap.addBomb(new Bomb(null, CellType.BOMB_BASIC, 0), new Pair(5, 0));
        this.graph = GraphBuilder.buildFromMap(baseMap);
        assertEquals(129, this.graph.vertexSet().size());
        assertEquals(160, this.graph.edgeSet().size());
    }

    @Test
    public void testEdgeWeights() {
        GameMap map = new GameMapImpl(false);
        map.addBreakableWall(new Pair(1, 2));
        graph = GraphBuilder.buildFromMap(map);

        Pair p1 = new Pair(0, 2); // grass
        Pair p2 = new Pair(1, 2); // Breakable wall
        Pair p3 = new Pair(2, 2); // Grass
        Pair p4 = new Pair(3, 2); // Grass
        Pair p5 = new Pair(1, 1); // Unbreakable wall

        assertTrue(map.isEmpty(p1));
        assertTrue(map.isBreakableWall(p2));
        assertTrue(map.isEmpty(p3));
        assertTrue(map.isEmpty(p4));
        assertTrue(map.isUnbreakableWall(p5));

        assertTrue(graph.containsEdge(p1, p2));
        assertTrue(graph.containsEdge(p2, p3));
        assertTrue(graph.containsEdge(p3, p4));
        assertFalse(graph.containsEdge(p2, p5));
        assertFalse(graph.containsEdge(p1, p3));

        assertEquals(2.5, graph.getEdgeWeight(graph.getEdge(p1, p2)));
        assertEquals(2.5, graph.getEdgeWeight(graph.getEdge(p2, p3)));
        assertEquals(1.0, graph.getEdgeWeight(graph.getEdge(p3, p4)));
    }

    @Test
    public void testConnections() {
        GameMap map = new GameMapImpl(false);
        map.addBreakableWall(new Pair(1, 2));
        graph = GraphBuilder.buildFromMap(map);

        Pair p1 = new Pair(0, 2); // grass
        Pair p2 = new Pair(1, 2); // Breakable wall
        Pair p3 = new Pair(2, 2); // Grass
        Pair p4 = new Pair(3, 2); // Grass
        Pair p5 = new Pair(1, 1); // Unbreakable wall

        assertTrue(map.isEmpty(p1));
        assertTrue(map.isBreakableWall(p2));
        assertTrue(map.isEmpty(p3));
        assertTrue(map.isEmpty(p4));
        assertTrue(map.isUnbreakableWall(p5));

        assertTrue(graph.containsEdge(p1, p2));
        assertTrue(graph.containsEdge(p2, p3));
        assertTrue(graph.containsEdge(p3, p4));
        assertFalse(graph.containsEdge(p2, p5));
        assertFalse(graph.containsEdge(p1, p3));

        assertEquals(2.5, graph.getEdgeWeight(graph.getEdge(p1, p2)));
        assertEquals(2.5, graph.getEdgeWeight(graph.getEdge(p2, p3)));
        assertEquals(1.0, graph.getEdgeWeight(graph.getEdge(p3, p4)));
    }

    // @Test
    // public void testBaseMapShortestPath() {
    // // percorso più breve tra [6,7] e Player [1,1]
    // List<Pair> l = List.of(
    // new Pair(5, 7),
    // new Pair(4, 7),
    // new Pair(3, 7),
    // new Pair(3, 6),
    // new Pair(3, 5),
    // new Pair(2, 5),
    // new Pair(1, 5),
    // new Pair(1, 4),
    // new Pair(1, 3),
    // new Pair(1, 2),
    // new Pair(1, 1)
    // );
    // Optional<List<Pair>> path = GraphBuilder.findShortestPath(this.graph,new
    // Pair(6,7),new Pair(1, 1));
    // assertEquals(11, path.get().size());
    // assertEquals(l, path.get());
    // // percorso inesistente tra due celle
    // path = GraphBuilder.findShortestPath(this.graph,new Pair(3,8),new Pair(1,
    // 11));
    // System.out.println(path);
    // assertEquals(false, path.isPresent());
    // }

}
