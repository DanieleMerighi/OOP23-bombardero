package it.bombardero;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Flow.Subscriber;

import javax.annotation.processing.SupportedSourceVersion;
import javax.swing.text.AbstractDocument.BranchElement;

import org.apache.http.protocol.SyncBasicHttpContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.qos.logback.classic.pattern.Util;
import it.unibo.bombardero.cell.Cell;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.impl.GameMapImpl;
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

    private GameMap map;

    /**
     * Computes the numbers to check according to the map size reported on the Utils class.
     */
    @BeforeAll void init() {
        this.map = new GameMapImpl();
        TOTAL_CELLS = Utils.MAP_ROWS * Utils.MAP_COLS;
        EXPECTED_UNBREAKABLE_WALLS_NUMBER = (int)Math.floor(
            Math.floorDiv(Utils.MAP_ROWS, 2) * Math.floorDiv(Utils.MAP_COLS, 2)
            );
            EXPECTED_BREAKABLE_WALLS_NUMBER = (int)Math.floor(
            (TOTAL_CELLS - EXPECTED_UNBREAKABLE_WALLS_NUMBER - MAP_CORNERS_QTY) * Utils.WALL_PRESENCE_RATE
            );

        String[][] printableMap = new String[Utils.MAP_ROWS][Utils.MAP_COLS];
        this.map.getMap().entrySet().stream()
            .forEach(entry -> printableMap[entry.getKey().row()][entry.getKey().col()] = fromClassToInteger(entry.getKey()));
        print2DArray(printableMap);
    } 

    /** 
     * Tests wether the unbreakable walls have been correctly placed, according to the standard scheme.
     * The user running this method can visually appreciate if the placement went accordingly.
     */
    @Test void testUnbreakableWallsInitialization() {
        long unbreakableWallsPresent = this.map.getMap().entrySet().stream()
            .filter(entry -> this.map.isUnbreakableWall(entry.getKey()))
            .count();

        assertEquals(unbreakableWallsPresent, EXPECTED_UNBREAKABLE_WALLS_NUMBER);
        System.out.println("OK, Unbreakable walls present: " + unbreakableWallsPresent);
    }

    /** 
     * Tests wether all the breakable walls that had to be placed were correctly placed. 
     * The user running this method can visually appreciate if the random generation creates a balanced map layout.
     */
    @Test void testBreakableWallsPlacement() {
        long breakableWallsGenerated = this.map.getMap().entrySet().stream()
        .filter(entry -> this.map.isBreakableWall(entry.getKey()))
        .count();

        assertEquals(breakableWallsGenerated, EXPECTED_BREAKABLE_WALLS_NUMBER);
        System.out.println("OK, Breakable walls generated: " + breakableWallsGenerated);

        assertTrue( this.map.getMap().entrySet().stream()
            .filter(entry -> this.map.isBreakableWall(entry.getKey()))
            .anyMatch(entry -> !this.MAP_CORNERS.contains(entry.getKey()))
        );
        System.out.println("OK, no breakable walls have spawned in the map's corners");
    }

    /** 
     * Begins the collapsing phase and tests if it goes as expected
     */
    @Test void testMapCollapse() {

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

    private void print2DArray(String[][] arr) {
        for(int i = 0; i < Utils.MAP_ROWS; i++) {
            for(int j = 0; j < Utils.MAP_COLS; j++) {
                String output = arr[i][j] == null ? "E" : arr[i][j];
                System.out.print(output + "  ");
            }
            System.out.print("\n");
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
