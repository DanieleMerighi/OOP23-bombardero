package it.bombardero;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import it.unibo.bombardero.character.ai.api.EnemyGraphReasoner;
import it.unibo.bombardero.character.ai.impl.EnemyGraphReasonerImpl;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.GenPair;
import it.unibo.bombardero.map.impl.GameMapImpl;

/**
 * Unit tests for the EnemyGraphReasonerImpl class.
 */
class TestReasoner {

    private final GameMap map;

    /**
     * Constructor to initialize map.
     */
    TestReasoner() {
        map = new GameMapImpl(false);
    }

    /**
     * Test case to verify if a specific cell is in the danger zone of a bomb.
     * 
     * @param enemyX     X coordinate of the enemy.
     * @param enemyY     Y coordinate of the enemy.
     * @param bombX      X coordinate of the bomb.
     * @param bombY      Y coordinate of the bomb.
     * @param explRadius Explosion radius of the bomb.
     * @param expected   Expected result indicating if the enemy is in danger zone.
     */
    @ParameterizedTest
    @CsvSource({
            "0,0, 0,2, 3, true", // vertical bomb
            "0,0, 0,2, 1, false", // outside range
            "0,0, 0,3, 2, false",
            "0,0, 2,0, 2, true", // horizzontal bomb
            "0,0, 0,2, 1, false", // outside range
            "0,0, 3,0, 2, false",
            "1,0, 0,0, 4, true"
    })
    void testIsInDangerZone(final int enemyX, final int enemyY, final int bombX, final int bombY,
            final int explRadius, final boolean expected) {
        final GenPair<Integer, Integer> enemyCoord = new GenPair<>(enemyX, enemyY);
        final GenPair<Integer, Integer> bombCell = new GenPair<>(bombX, bombY);

        map.addBomb(new MyBomb(enemyCoord), bombCell);
        assertTrue(map.isBomb(bombCell));

        final EnemyGraphReasoner reasoner = new EnemyGraphReasonerImpl(map);
        assertFalse(reasoner.isPathBlockedByWalls(map, enemyCoord, bombCell));

        assertEquals(expected, reasoner.isInDangerZone(map, enemyCoord, explRadius));
    }

    /**
     * Test case to verify if a path from an enemy to a cell is blocked by walls.
     * 
     * @param enemyX          X coordinate of the enemy.
     * @param enemyY          Y coordinate of the enemy.
     * @param endCellX        X coordinate of the cell that i want consider.
     * @param endCellY        Y coordinate of the cell that i want consider.
     * @param expectedBlocked Expected result indicating if the path is blocked by
     *                        walls.
     */
    @ParameterizedTest
    @CsvSource({
            "0,0, 2,0, false", // No walls in the path
            "0,0, 2,1, true", // No diagonal path
            "1,0, 1,2, true", // Wall horizontally blocking the path
            "0,1, 2,1, true", // Wall vertically blocking the path
            "2,1, 0,1, true" // even in the reverse order
    })
    void testIsPathBlockedByWalls(final int enemyX, final int enemyY, final int endCellX, final int endCellY,
            final boolean expectedBlocked) {
        final GenPair<Integer, Integer> enemyCoord = new GenPair<>(enemyX, enemyY);
        final GenPair<Integer, Integer> endCell = new GenPair<>(endCellX, endCellY);

        final EnemyGraphReasoner reasoner = new EnemyGraphReasonerImpl(map);

        final boolean isBlocked = reasoner.isPathBlockedByWalls(map, enemyCoord, endCell);
        assertEquals(expectedBlocked, isBlocked);
    }

    /*
     * 
     * This code represents a path as a string.
     * The string format consists of a series of cell coordinates separated by
     * semicolons (;).
     * Each cell coordinate is specified in the format 'x:column', where 'x' and
     * 'column'
     * are integers representing the position of the cell in a grid
     */
    /**
     * Test case to verify finding the shortest path from an enemy to a player.
     * 
     * @param enemyX             X coordinate of the enemy.
     * @param enemyY             Y coordinate of the enemy.
     * @param playerX            X coordinate of the player.
     * @param playerY            Y coordinate of the player.
     * @param expectedPathLength Expected length of the shortest path.
     * @param expectedPathString Expected path string representation.
     */
    @ParameterizedTest
    @CsvSource({
            "0,0, 0,2, 2, 0:1;0:2", // Player 2 cells right, path length 2
            "0,0, 2,0, 2, 1:0;2:0", // Player 2 cells down, path length 2
            "4,0, 1,2, 5, 3:0;2:0;2:1;2:2;1:2", // a path block by a wall
    })
    void testFindShortestPathToPlayer(final int enemyX, final int enemyY, final int playerX, final int playerY,
            final int expectedPathLength, final String expectedPathString) {
        final GenPair<Integer, Integer> enemyCoord = new GenPair<>(enemyX, enemyY);
        final GenPair<Integer, Integer> playerCoord = new GenPair<>(playerX, playerY);

        map.addBreakableWall(new GenPair<Integer, Integer>(4, 1));

        final EnemyGraphReasoner reasoner = new EnemyGraphReasonerImpl(map);

        final List<GenPair<Integer, Integer>> actualPath = reasoner.findShortestPathToPlayer(map, enemyCoord, playerCoord);
        assertEquals(expectedPathLength, actualPath.size());

        // parse the expected path string into a List<Pair>
        final List<GenPair<Integer, Integer>> expectedPath = parseExpectedPath(expectedPathString);
        for (int i = 0; i < expectedPathLength; i++) {
            assertEquals(expectedPath.get(i), actualPath.get(i));
        }
    }

    /**
     * Test case to verify finding the nearest safe cell from an enemy after a bomb
     * explosion.
     * 
     * @param enemyX             X coordinate of the enemy.
     * @param enemyY             Y coordinate of the enemy.
     * @param bombX              X coordinate of the bomb.
     * @param bombY              Y coordinate of the bomb.
     * @param explRadius         Explosion radius of the bomb.
     * @param expectedSafeSpaceX Expected X coordinate of the nearest safe cell.
     * @param expectedSafeSpaceY Expected Y coordinate of the nearest safe cell.
     */
    @ParameterizedTest
    @CsvSource({
            "0,0, 0,2, 3, 1,0", // Bomb to the right, safe space down
            "12,0, 12,2, 2, 11,0", // Bomb to the left, safe space above
    })
    void testFindNearestSafeSpace(final int enemyX, final int enemyY, final int bombX, final int bombY,
            final int explRadius,
            final int expectedSafeSpaceX, final int expectedSafeSpaceY) {
        final GenPair<Integer, Integer> enemyCoord = new GenPair<>(enemyX, enemyY);
        final GenPair<Integer, Integer> bombCell = new GenPair<>(bombX, bombY);

        map.addBomb(new MyBomb(enemyCoord), bombCell);
        assertTrue(map.isBomb(bombCell));

        final EnemyGraphReasoner reasoner = new EnemyGraphReasonerImpl(map);

        final Optional<GenPair<Integer, Integer>> safeSpace = reasoner.findNearestSafeCell(map, enemyCoord, explRadius);

        assertTrue(safeSpace.isPresent());
        assertEquals(expectedSafeSpaceX, safeSpace.get().x());
        assertEquals(expectedSafeSpaceY, safeSpace.get().y());

    }

    // Helper method to parse the expected path string
    private List<GenPair<Integer, Integer>> parseExpectedPath(final String pathString) {
        final List<GenPair<Integer, Integer>> path = new ArrayList<>();
        final String[] coordinates = pathString.split(";"); // Split on comma only if preceded by colon
        for (final String coordinate : coordinates) {
            final String[] xy = coordinate.split(":"); // Assuming format "x:y" for each coordinate
            final int x = Integer.parseInt(xy[0]);
            final int y = Integer.parseInt(xy[1]);
            path.add(new GenPair<Integer, Integer>(x, y));
        }
        return path;
    }
}
