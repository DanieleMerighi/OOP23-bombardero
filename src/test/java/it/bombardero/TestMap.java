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
import it.unibo.bombardero.map.impl.GameMapImpl;
import it.unibo.bombardero.map.api.MapManager;
import it.unibo.bombardero.map.impl.MapManagerImpl;
import it.unibo.bombardero.utils.Utils;
import it.unibo.bombardero.map.api.Pair;

/**
 * @author Federico Bagattoni
 */
public class TestMap {

    private static int TOTAL_CELLS;
    private static int EXPECTED_UNBREAKABLE_WALLS_NUMBER;
    private static int EXPECTED_BREAKABLE_WALLS_NUMBER;
    private final static int MAP_CORNERS_QTY = 12;

    private final Set<Pair> MAP_CORNERS = new HashSet<>();
    private final Set<Pair> EXPECTED_UNBREAKABLE_WALLS_COORDINATES = new HashSet<>();

    private GameMap map;

    @BeforeAll
    static void initConstants() {
        TOTAL_CELLS = Utils.MAP_ROWS * Utils.MAP_COLS;
        EXPECTED_UNBREAKABLE_WALLS_NUMBER = (int)Math.floor(
            Math.floorDiv(Utils.MAP_ROWS, 2) * Math.floorDiv(Utils.MAP_COLS, 2)
        );
        EXPECTED_BREAKABLE_WALLS_NUMBER = (int)Math.floor(
            (TOTAL_CELLS - EXPECTED_UNBREAKABLE_WALLS_NUMBER - MAP_CORNERS_QTY) * Utils.WALL_PRESENCE_RATE
        );
    }

    /**
     * Computes the numbers to check according to the map size reported on the Utils class.
     */
    @BeforeEach
    void init() {
        map = new GameMapImpl();
        computeMapCorners();
        computeUnbreakableWallsCoordinates();
        /* print2DArray(generatePrintableMap()); */
    } 

    /** 
     * Tests wether the unbreakable walls have been correctly placed, according to the standard scheme.
     */
    @Test
    void testUnbreakableWallsInitialization() {
        long unbreakableWallsPresent = this.map.getMap().entrySet().stream()
            .filter(entry -> this.map.isUnbreakableWall(entry.getKey()))
            .count();

        assertEquals(EXPECTED_UNBREAKABLE_WALLS_NUMBER, unbreakableWallsPresent);
        System.out.println("OK, all the unbreakable walls are present: " + unbreakableWallsPresent);

        assertEquals(
            EXPECTED_UNBREAKABLE_WALLS_COORDINATES,
            map.getMap().entrySet().stream()
                .map(entry -> entry.getKey())
                .filter(pair -> map.isUnbreakableWall(pair))
                .collect(Collectors.toSet())
        );
        System.out.println("OK, all the unbreakable walls are in the right position");
    }

    /** 
     * Tests wether all the breakable walls that had to be placed were correctly placed.
     */
    @Test
    void testBreakableWallsNumber() {
        assertEquals(
            EXPECTED_BREAKABLE_WALLS_NUMBER,
            map.getMap().entrySet().stream()
                .filter(entry -> map.isBreakableWall(entry.getKey()))
                .count()
        );
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
                .anyMatch(entry -> !this.MAP_CORNERS.contains(entry.getKey()))
        );
    }

    /** 
     * Begins the collapsing phase and tests if it goes as expected, one block at a time 
     * util all the map has collapsed
     * TODO: keep coding... and print correctly map
     */
    @Test
    void testMapCollapse() {
        /* Create the manager and print the map before the collapse */
        MapManager manager = new MapManagerImpl(this.map);
        String[][] collapsingMap = generatePrintableMap();
        print2DArray(collapsingMap);

        manager.triggerArenaCollapse();
        for(int i = 0; i < TOTAL_CELLS; i++) {
            manager.update();

        }

        /* Final check if every cell is a wall (print the map to make sure) */
        print2DArray(generatePrintableMap());
        assertTrue(
            map.getMap().entrySet().stream()
                .allMatch(entry -> map.isUnbreakableWall(entry.getKey()))
        );
    }

    private String fromClassToInteger(Pair coordinate) {
        if(this.map.isBreakableWall(coordinate)) {
            return "B";
        }
        else if(this.map.isUnbreakableWall(coordinate)) {
            return "U";
        }
        else if(this.map.isEmpty(coordinate)) {
            return "E";
        }
        return "O";
    }

    private String[][] generatePrintableMap() {
        String[][] printableMap = new String[Utils.MAP_ROWS][Utils.MAP_COLS];
        this.map.getMap().entrySet().stream()
            .forEach(entry -> printableMap[entry.getKey().row()][entry.getKey().col()] = fromClassToInteger(entry.getKey()));
        return printableMap;
    }

    private void print2DArray(String[][] arr) {
        for(int i = 0; i < Utils.MAP_ROWS; i++) {
            for(int j = 0; j < Utils.MAP_COLS; j++) {
                String output = arr[i][j] == null ? "E" : arr[i][j];
                System.out.print(output + "  ");
            }
            System.out.println();
        }
        System.out.print("\n\n");
    }

    private void computeUnbreakableWallsCoordinates() {
        for(int i = 1; i < Utils.MAP_ROWS; i += 2) {
            for(int j = 1; j < Utils.MAP_COLS; j += 2) {
                this.EXPECTED_UNBREAKABLE_WALLS_COORDINATES.add(new Pair(i, j));
            }
        }
    }

    private void computeMapCorners() {
        this.MAP_CORNERS.add(new Pair(0, 0));
        this.MAP_CORNERS.add(new Pair(0, 1));
        this.MAP_CORNERS.add(new Pair(1, 0));

        this.MAP_CORNERS.add(new Pair(Utils.MAP_ROWS - 1, Utils.MAP_COLS - 1));
        this.MAP_CORNERS.add(new Pair(Utils.MAP_ROWS - 1, Utils.MAP_COLS - 2));
        this.MAP_CORNERS.add(new Pair(Utils.MAP_ROWS - 2, Utils.MAP_COLS - 1));
        
        this.MAP_CORNERS.add(new Pair(Utils.MAP_ROWS - 1, 0));
        this.MAP_CORNERS.add(new Pair(Utils.MAP_ROWS - 1, 1));
        this.MAP_CORNERS.add(new Pair(Utils.MAP_ROWS - 2, 0));
        
        this.MAP_CORNERS.add(new Pair(0, Utils.MAP_COLS - 1));
        this.MAP_CORNERS.add(new Pair(1, Utils.MAP_COLS - 1));
        this.MAP_CORNERS.add(new Pair(0, Utils.MAP_COLS - 2));
    }

}
