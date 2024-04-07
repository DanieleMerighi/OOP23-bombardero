package it.bombardero;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.concurrent.Flow.Subscriber;

import javax.annotation.processing.SupportedSourceVersion;

import org.apache.http.protocol.SyncBasicHttpContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.bombardero.cell.Cell;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.impl.GameMapImpl;
import it.unibo.bombardero.utils.Utils;
import it.unibo.bombardero.map.api.Pair;

/**
 * @author Federico Bagattoni
 */
public class TestMap {

    private GameMap map;

    @BeforeEach void init() {
        this.map = new GameMapImpl();
    } 

    /** 
     * Tests wether the unbreakable walls have been correctly placed, according to the standard scheme.
     * The user running this method can visually appreciate if the placement went accordingly.
     */
    @Test void testUnbreakableWallsInitialization() {
        String[][] printableMap = new String[Utils.MAP_ROWS][Utils.MAP_COLS];
        this.map.getMap().stream()
            .forEach(entry -> printableMap[entry.getKey().row()][entry.getKey().col()] = fromClassToInteger(entry.getKey()));
        
        long breakableWallsGenerated = this.map.getMap().stream()
            .filter(entry -> this.map.isBreakableWall(entry.getKey()))
            .count();
        long unbreakableWallsPresent = this.map.getMap().stream()
            .filter(entry -> this.map.isUnbreakableWall(entry.getKey()))
            .count();
        
        print2DArray(printableMap);
        // assertEquals(unbreakableWallsPresent, );
        System.out.println("OK, Unbreakable walls present: " + unbreakableWallsPresent);

        System.out.println("OK, Breakable walls generated: " + breakableWallsGenerated);
    }

    /** 
     * Tests wether all the breakable walls that had to be placed were correctly placed. 
     * The user running this method can visually appreciate if the random generation creates a balanced map layout.
     */
    @Test void testBreakableWallsPresence() {

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

}
