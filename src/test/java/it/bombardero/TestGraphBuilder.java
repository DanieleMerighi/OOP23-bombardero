package it.bombardero;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.stream.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.map.impl.GameMapImpl;
import it.unibo.bombardero.utils.Utils;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.unibo.bombardero.cell.BreakableWall;
import it.unibo.bombardero.character.AI.*;

// public class TestGraphBuilder {

//     private Graph<Pair, DefaultWeightedEdge> graph;

//     @BeforeEach 
//     void initBaseMap() {
//         GameMap miniMap = new GameMapImpl(false);
//         miniMap.addBreakableWall(new Pair(2, 0), new BreakableWall());
//         miniMap.addBreakableWall(new Pair(3, 0), new BreakableWall());
//         miniMap.addBreakableWall(new Pair(2, 0), new BreakableWall());
//         this.graph = GraphBuilder.buildFromMap(miniMap);
//     }

//     @Test
//     public void testBaseMapSize() {
//         assertEquals(98, this.graph.vertexSet().size());
//         assertEquals(94, this.graph.edgeSet().size());
//     }

//     @Test
//     public void testBaseMapShortestPath() {
//         // percorso più breve tra [6,7] e Player [1,1]
//         List<Pair> l = List.of(
//             new Pair(5, 7),
//             new Pair(4, 7),
//             new Pair(3, 7),
//             new Pair(3, 6),
//             new Pair(3, 5),
//             new Pair(2, 5),
//             new Pair(1, 5),
//             new Pair(1, 4),
//             new Pair(1, 3),
//             new Pair(1, 2),
//             new Pair(1, 1)
//         );
//         Optional<List<Pair>> path = GraphBuilder.findShortestPath(this.graph,new Pair(6,7),new Pair(1, 1));
//         assertEquals(11, path.get().size());
//         assertEquals(l, path.get());
//         // percorso inesistente tra due celle
//         path = GraphBuilder.findShortestPath(this.graph,new Pair(3,8),new Pair(1, 11));
//         System.out.println(path);
//         assertEquals(false, path.isPresent());
//     }

    
// }
