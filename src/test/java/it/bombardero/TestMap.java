package it.bombardero;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.GenPair;
import it.unibo.bombardero.map.impl.GameMapImpl;
import it.unibo.bombardero.utils.Utils;

/**
 * Unit tests for the {@link GameMap} class.
 * <p>
 * This class contains tests to verify the behavior of the GameMap, including
 * initiation and features like the wall collapse.
 * </p>
 * @author Federico Bagattoni
 */
class TestMap {

    private static int totalCells;
    private static int expectedUnbreakableWallsNumber;
    private static int expectedBreakableWallsNumber;
    private static final int MAP_CORNERS_QTY = 12;

    private final Set<GenPair<Integer, Integer>> mapCorners = new HashSet<>();
    private final Set<GenPair<Integer, Integer>> expectedUnbreakableWallsCoordinates = new HashSet<>();

    private GameMap map;

    @BeforeAll
    static void initConstants() {
        totalCells = Utils.MAP_ROWS * Utils.MAP_COLS;
        expectedUnbreakableWallsNumber = (int) Math.floor(
                Math.floorDiv(Utils.MAP_ROWS, 2) * Math.floorDiv(Utils.MAP_COLS, 2));
        expectedBreakableWallsNumber = (int) Math.floor(
                (totalCells - expectedUnbreakableWallsNumber - MAP_CORNERS_QTY) * Utils.WALL_PRESENCE_RATE);
    }

    /**
     * Computes the numbers to check according to the map size reported on the Utils
     * class.
     */
    @BeforeEach
    void init() {
        map = new GameMapImpl();
        computeMapCorners();
        computeUnbreakableWallsCoordinates();
        /* print2DArray(generatePrintableMap()); */
    }

    /**
     * Tests wether the unbreakable walls have been correctly placed, according to
     * the standard scheme.
     */
    @Test
    void testUnbreakableWallsInitialization() {
        final long unbreakableWallsPresent = this.map.getMap().entrySet().stream()
                .filter(entry -> this.map.isUnbreakableWall(entry.getKey()))
                .count();

        assertEquals(expectedUnbreakableWallsNumber, unbreakableWallsPresent);
        System.out.println("OK, all the unbreakable walls are present: " + unbreakableWallsPresent);

        assertEquals(
                expectedUnbreakableWallsCoordinates,
                map.getMap().entrySet().stream()
                        .map(entry -> entry.getKey())
                        .filter(pair -> map.isUnbreakableWall(pair))
                        .collect(Collectors.toSet()));
        System.out.println("OK, all the unbreakable walls are in the right position");
    }

    /**
     * Tests wether all the breakable walls that had to be placed were correctly
     * placed.
     */
    @Test
    void testBreakableWallsNumber() {
        assertEquals(
                expectedBreakableWallsNumber,
                map.getMap().entrySet().stream()
                        .filter(entry -> map.isBreakableWall(entry.getKey()))
                        .count());
    }

    /**
     * Checks if any breakable walls has generated in the twelve cells
     * where the player spawns in the map.
     */
    @Test
    void checkBreakableWallsInCorners() {
        assertTrue(
                map.getMap().entrySet().stream()
                        .filter(entry -> this.map.isBreakableWall(entry.getKey()))
                        .anyMatch(entry -> !this.mapCorners.contains(entry.getKey())));
    }

    /**
     * Begins the collapsing phase and tests if it goes as expected, one block at a
     * time util all the map has collapsed.
     * 
     */
    // @Test
    // void testMapCollapse() {
    // /* Create the manager and print the map before the collapse */
    // MapManager manager = new MapManagerImpl(this.map);
    // String[][] collapsingMap = generatePrintableMap();
    // print2DArray(collapsingMap);

    // manager.triggerCollapse();
    // for(int i = 0; i < TOTAL_CELLS; i++) {
    // manager.update();

    // }

    // computeMatrixTraversal();

    // /* Final check if every cell is a wall (print the map to make sure) */
    // print2DArray(generatePrintableMap());
    // assertTrue(
    // map.getMap().entrySet().stream()
    // .allMatch(entry -> map.isUnbreakableWall(entry.getKey()))
    // );
    // }

    /**
     * 
     * @param coordinate
     * @return a String
     */
    private String fromClassToInteger(final GenPair<Integer, Integer> coordinate) {
        if (this.map.isBreakableWall(coordinate)) {
            return "B";
        } else if (this.map.isUnbreakableWall(coordinate)) {
            return "U";
        } else if (this.map.isEmpty(coordinate)) {
            return "E";
        }
        return "O";
    }

    private String[][] generatePrintableMap() {
        final String[][] printableMap = new String[Utils.MAP_ROWS][Utils.MAP_COLS];
        this.map.getMap().entrySet().stream()
                .forEach(entry -> printableMap[entry.getKey().x()][entry.getKey().y()] = fromClassToInteger(
                        entry.getKey()));
        return printableMap;
    }

    private void print2DArray(final String[][] arr) {
        for (int i = 0; i < Utils.MAP_ROWS; i++) {
            for (int j = 0; j < Utils.MAP_COLS; j++) {
                final String output = arr[i][j] == null ? "E" : arr[i][j];
                System.out.print(output + "  ");
            }
            System.out.println();
        }
        System.out.print("\n\n");
    }

    private void printPlain2DArray(final String[][] arr) {
        for (int i = 0; i < Utils.MAP_ROWS; i++) {
            for (int j = 0; j < Utils.MAP_COLS; j++) {
                System.out.print(arr[i][j] + "  ");
            }
            System.out.println();
        }
        System.out.print("\n\n");
    }

    private void computeUnbreakableWallsCoordinates() {
        for (int i = 1; i < Utils.MAP_ROWS; i += 2) {
            for (int j = 1; j < Utils.MAP_COLS; j += 2) {
                this.expectedUnbreakableWallsCoordinates.add(new GenPair<Integer, Integer>(i, j));
            }
        }
    }

    private void computeMapCorners() {
        this.mapCorners.add(new GenPair<Integer, Integer>(0, 0));
        this.mapCorners.add(new GenPair<Integer, Integer>(0, 1));
        this.mapCorners.add(new GenPair<Integer, Integer>(1, 0));

        this.mapCorners.add(new GenPair<Integer, Integer>(Utils.MAP_ROWS - 1, Utils.MAP_COLS - 1));
        this.mapCorners.add(new GenPair<Integer, Integer>(Utils.MAP_ROWS - 1, Utils.MAP_COLS - 2));
        this.mapCorners.add(new GenPair<Integer, Integer>(Utils.MAP_ROWS - 2, Utils.MAP_COLS - 1));

        this.mapCorners.add(new GenPair<Integer, Integer>(Utils.MAP_ROWS - 1, 0));
        this.mapCorners.add(new GenPair<Integer, Integer>(Utils.MAP_ROWS - 1, 1));
        this.mapCorners.add(new GenPair<Integer, Integer>(Utils.MAP_ROWS - 2, 0));

        this.mapCorners.add(new GenPair<Integer, Integer>(0, Utils.MAP_COLS - 1));
        this.mapCorners.add(new GenPair<Integer, Integer>(1, Utils.MAP_COLS - 1));
        this.mapCorners.add(new GenPair<Integer, Integer>(0, Utils.MAP_COLS - 2));
    }

    private void computeMatrixTraversal() {
        final String[][] matrixTraversal = new String[Utils.MAP_ROWS][Utils.MAP_COLS];
        int top = 0, left = 0;
        final int bottom = Utils.MAP_COLS - 1;
        int right = Utils.MAP_ROWS - 1, count = 0;
        while (top <= bottom && left <= right) {
            for (int i = left; i <= right; i++) {
                matrixTraversal[top][i] = Integer.toString(count);
                count++;
            }
            top++;
            for (int i = top; i <= bottom; i++) {
                matrixTraversal[i][right] = Integer.toString(count);
                count++;
            }
            right--;
            if (top <= bottom) {
                for (int i = bottom; i >= top; i--) {
                    matrixTraversal[i][left] = Integer.toString(count);
                    count++;
                }
                left++;
            }

            if (left <= right) {
                for (int i = bottom; i >= top; i--) {
                    matrixTraversal[i][left] = Integer.toString(count);
                    count++;
                }
                left++;
            }
        }
        printPlain2DArray(matrixTraversal);
    }

}
