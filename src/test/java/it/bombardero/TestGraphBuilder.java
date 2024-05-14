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

    private GameMap map;
    private Graph<Pair, DefaultWeightedEdge> graph;

    @BeforeEach
    public void setup() {
        map = new GameMapImpl(false);
    }

    @Test
    public void testBaseMapSize() {
        this.graph = GraphBuilder.buildFromMap(map);
        // the base map (built with only unbreakable wall) have 133 vertex
        assertEquals(133, this.graph.vertexSet().size());
        assertEquals(168, this.graph.edgeSet().size());
        // adding (or removing) breakable wall should not change the number of vertex or
        // edges
        map.addBreakableWall(new Pair(0, 2));
        map.addBreakableWall(new Pair(0, 3));
        map.addBreakableWall(new Pair(0, 4));

        this.graph = GraphBuilder.buildFromMap(map);

        assertEquals(133, this.graph.vertexSet().size());
        assertEquals(168, this.graph.edgeSet().size());
    }

    @Test
    public void testMapSizeWithObastacles() {
        GameMap baseMap = new GameMapImpl(true);
        baseMap.addFlame(new Flame(CellType.FLAME, Flame.FlameType.FLAME_BODY_HORIZONTAL), new Pair(0, 2));
        baseMap.addFlame(new Flame(CellType.FLAME, Flame.FlameType.FLAME_BODY_HORIZONTAL), new Pair(0, 3));
        baseMap.addFlame(new Flame(CellType.FLAME, Flame.FlameType.FLAME_BODY_HORIZONTAL), new Pair(0, 4));
        //baseMap.addBomb(new Bomb(null, CellType.BOMB_BASIC, 0), new Pair(5, 0));
        this.graph = GraphBuilder.buildFromMap(baseMap);
        assertEquals(133, this.graph.vertexSet().size());
        assertEquals(168, this.graph.edgeSet().size());
    }

    @Test
    public void testEdgeWeights() {
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
}
