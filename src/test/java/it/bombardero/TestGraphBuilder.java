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

import it.unibo.bombardero.cell.Flame;
import it.unibo.bombardero.cell.Cell.CellType;
import it.unibo.bombardero.character.ai.impl.GraphBuilderImpl;

/**
 * Unit tests for the GraphBuilderImpl class.
 */
public class TestGraphBuilder {

    private GameMap map;
    private Graph<Pair, DefaultWeightedEdge> graph;

    private static final int VERTEX_SIZE = 133;
    private static final int NODE_SIZE = 168;
    private static final double WALL_WEIGHT = 2.5;
    private static final double STANDARD_WEIGHT = 1.0;

    /**
     * Setup method executed before each test.
     */
    @BeforeEach
    public void setup() {
        map = new GameMapImpl(false);
    }

    /**
     * Test case to verify base map size and structure.
     */
    @Test
    public void testBaseMapSize() {
        this.graph = GraphBuilderImpl.buildFromMap(map);
        // the base map (built with only unbreakable wall) have 133 vertex
        assertEquals(VERTEX_SIZE, this.graph.vertexSet().size());
        assertEquals(NODE_SIZE, this.graph.edgeSet().size());
        // adding (or removing) breakable wall should not change the number of vertex or
        // edges
        map.addBreakableWall(new Pair(0, 2));
        map.addBreakableWall(new Pair(0, 3));
        map.addBreakableWall(new Pair(0, 4));

        this.graph = GraphBuilderImpl.buildFromMap(map);

        assertEquals(VERTEX_SIZE, this.graph.vertexSet().size());
        assertEquals(NODE_SIZE, this.graph.edgeSet().size());
    }

    /**
     * Test case to verify map size and structure with obstacles.
     */
    @Test
    public void testMapSizeWithObastacles() {
        GameMap baseMap = new GameMapImpl(true);
        // CHECKSTYLE: MagicNumber OFF
        baseMap.addFlame(new Flame(CellType.FLAME, null, new Pair(0, 2)), new Pair(0, 2));
        baseMap.addFlame(new Flame(CellType.FLAME, null, new Pair(0, 4)), new Pair(0, 4));
        baseMap.addFlame(new Flame(CellType.FLAME, null, new Pair(5, 6)), new Pair(5, 6));
        // CHECKSTYLE: MagicNumber ON
        this.graph = GraphBuilderImpl.buildFromMap(baseMap);
        assertEquals(VERTEX_SIZE, this.graph.vertexSet().size());
        assertEquals(NODE_SIZE, this.graph.edgeSet().size());
    }

    /**
     * Test case to verify edge weights in the graph.
     */
    @Test
    public void testEdgeWeights() {
        map.addBreakableWall(new Pair(1, 2));
        graph = GraphBuilderImpl.buildFromMap(map);

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

        assertEquals(WALL_WEIGHT, graph.getEdgeWeight(graph.getEdge(p1, p2)));
        assertEquals(WALL_WEIGHT, graph.getEdgeWeight(graph.getEdge(p2, p3)));
        assertEquals(STANDARD_WEIGHT, graph.getEdgeWeight(graph.getEdge(p3, p4)));
    }
}
