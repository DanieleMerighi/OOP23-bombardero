package it.bombardero;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import org.junit.jupiter.api.BeforeEach;

import it.unibo.bombardero.cell.Bomb;
import it.unibo.bombardero.cell.Cell.CellType;
import it.unibo.bombardero.character.AI.EnemyGraphReasoner;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.map.impl.GameMapImpl;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

public class TestReasoner {

    private GameMap map;

    @BeforeEach
    public void setup() {
        map = new GameMapImpl(false);
    }

    // this way I can test different scenarios by passing the right
    // parameters and avoiding duplicate tests
    @ParameterizedTest
    @CsvSource({
            "0,0, 0,2, 2, true", // horizontal bomb
            "0,0, 0,2, 1, false", // outside range
            "0,0, 0,3, 2, false",
            "0,0, 2,0, 2, true", // vertical bomb
            "0,0, 0,2, 1, false", // outside range
            "0,0, 3,0, 2, false",
            "1,0, 0,2, 4, false",
            "0,0, 0,0, 2, true"
    })
    public void testIsInDangerZone(int enemyX, int enemyY, int bombX, int bombY, int explRadius, boolean expected) {
        Pair enemyCoord = new Pair(enemyX, enemyY);
        Pair bombCell = new Pair(bombX, bombY);

        map.addBomb(new Bomb(null, bombCell, CellType.BOMB_BASIC, explRadius), bombCell);
        assertTrue(map.isBomb(bombCell));

        EnemyGraphReasoner reasoner = new EnemyGraphReasoner(map);
        assertFalse(reasoner.isPathBlockedByWalls(enemyCoord, bombCell));

        assertEquals(expected, reasoner.isInDangerZone(enemyCoord, explRadius));
    }

    @ParameterizedTest
    @CsvSource({
            "0,0, 2,0, false", // No walls in the path
            "0,0, 2,1, false", // No walls in the path
            "0,0, 2,2, false", // No walls in the path
            "1,0, 1,2, true", // Wall horizontally blocking the path
            "0,1, 2,1, true", // Wall vertically blocking the path
            "2,1, 0,1, true" // even in the reverse order
    })
    public void testIsPathBlockedByWalls(int enemyX, int enemyY, int endCellX, int endCellY, boolean expectedBlocked) {
        Pair enemyCoord = new Pair(enemyX, enemyY);
        Pair endCell = new Pair(endCellX, endCellY);

        EnemyGraphReasoner reasoner = new EnemyGraphReasoner(map);

        boolean isBlocked = reasoner.isPathBlockedByWalls(enemyCoord, endCell);
        assertEquals(expectedBlocked, isBlocked);
    }

   /*
    * 
    This code represents a path as a string. 
    The string format consists of a series of cell coordinates separated by semicolons (;). 
    Each cell coordinate is specified in the format 'row:column', where 'row' and 'column' 
    are integers representing the position of the cell in a grid
    */
    @ParameterizedTest
    @CsvSource({
            "0,0, 0,2, 2, 0:1;0:2", // Player 2 cells right, path length 2
            "0,0, 2,0, 2, 1:0;2:0", // Player 2 cells down, path length 2
            "4,0, 1,2, 5, 3:0;2:0;2:1;2:2;1:2", // a path block by a wall
    })
    public void testFindShortestPathToPlayer(int enemyX, int enemyY, int playerX, int playerY,
            int expectedPathLength, String expectedPathString) {
        Pair enemyCoord = new Pair(enemyX, enemyY);
        Pair playerCoord = new Pair(playerX, playerY);

        map.addBreakableWall(new Pair(4, 1));

        EnemyGraphReasoner reasoner = new EnemyGraphReasoner(map);

        List<Pair> actualPath = reasoner.findShortestPathToPlayer(enemyCoord, playerCoord);
        assertEquals(expectedPathLength, actualPath.size());

        // parse the expected path string into a List<Pair>
        List<Pair> expectedPath = parseExpectedPath(expectedPathString);
        for (int i = 0; i < expectedPathLength; i++) {
            assertEquals(expectedPath.get(i), actualPath.get(i));
        }
    }

    // Helper method to parse the expected path string
    private List<Pair> parseExpectedPath(String pathString) {
        List<Pair> path = new ArrayList<>();
        String[] coordinates = pathString.split(";"); // Split on comma only if preceded by colon
        for (String coordinate : coordinates) {
            String[] xy = coordinate.split(":"); // Assuming format "x:y" for each coordinate
            int x = Integer.parseInt(xy[0]);
            int y = Integer.parseInt(xy[1]);
            path.add(new Pair(x, y));
        }
        return path;
    }

    @ParameterizedTest
    @CsvSource({
            "0,0, 0,2, 2, 1,0", // Bomb to the right, safe space down
            "12,0, 12,2, 2, 11,0", // Bomb to the left, safe space above
            "1,0, 1,2, 2, -1,-1", // no needed a safe space
            "0,0, 0,0, 4, 1,2", // Bomb to the right, safe space down (different range)
            "0,0, 0,0, 2, 2,0", // Bomb to the right, safe space down (different range)
    })
    public void testFindNearestSafeSpace(int enemyX, int enemyY, int bombX, int bombY, int explRadius,
            int expectedSafeSpaceX, int expectedSafeSpaceY) {
        Pair enemyCoord = new Pair(enemyX, enemyY);
        Pair bombCell = new Pair(bombX, bombY);

        map.addBomb(new Bomb(null, bombCell, CellType.BOMB_BASIC, explRadius), bombCell);
        map.addBreakableWall(new Pair(expectedSafeSpaceX, expectedSafeSpaceY));
        assertTrue(map.isBomb(bombCell));

        EnemyGraphReasoner reasoner = new EnemyGraphReasoner(map);

        Optional<Pair> safeSpace = reasoner.findNearestSafeSpace(enemyCoord, explRadius);
        if(expectedSafeSpaceX != -1 && expectedSafeSpaceY != -1) {
            assertTrue(safeSpace.isPresent());
            assertEquals(new Pair(expectedSafeSpaceX, expectedSafeSpaceY), safeSpace.get());
        } else {
            assertTrue(safeSpace.isEmpty());
        }   
    }

}
