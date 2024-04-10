package it.unibo.bombardero.map.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.Set;
import java.util.HashSet;

import it.unibo.bombardero.cell.BreakableWall;
import it.unibo.bombardero.cell.UnbreakableWall;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.MapManager;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.utils.Utils;

public class MapManagerImpl implements MapManager {

    /* This number and List represent the twelve cells on which nothing can spawn except the player: */
    /* NOTE: the number "12" does NOT depend from the arena's size, however the "MAP_CORNERS" Set does. */
    private static final int MAP_CORNERS_NUMBER = 12;
    private final Set<Pair> MAP_CORNERS = new HashSet<Pair>();

    private final GameMap map;
    private List<Pair> wallCollapseOrder;
    private boolean collapseStarted = false;

    public MapManagerImpl(GameMap map) {
        this.map = map;
        this.computeMapCorners();
    }

    @Override
    public void update() {
        if(collapseStarted) {
            placeNextWall();
        }
    }

    @Override
    public void placeBreakableWalls() {
        int totalWallsToGenerate = (int)Math.floor(
            ((Utils.MAP_COLS * Utils.MAP_ROWS) - (Math.floorDiv(Utils.MAP_COLS, 2) * Math.floorDiv(Utils.MAP_ROWS, 2)) - MAP_CORNERS_NUMBER) * Utils.WALL_PRESENCE_RATE
        );
        Random rnd = new Random();
        Pair coordinate;
        while(totalWallsToGenerate != 0) {
            do {
                coordinate = new Pair(rnd.nextInt(Utils.MAP_COLS), rnd.nextInt(Utils.MAP_ROWS));
            } while (!map.isEmpty(coordinate) || this.MAP_CORNERS.contains(coordinate));
            this.map.addBreakableWall(coordinate, new BreakableWall());
            totalWallsToGenerate--;
        }
    }

    @Override
    public void placeUnbreakableWalls() {
        IntStream
            .range(0, Utils.MAP_ROWS)
            .filter(num -> num%2 != 0)
            .boxed()
            .flatMap(x -> IntStream
                .range(0, Utils.MAP_COLS)
                .filter(num -> num%2 != 0)
                .mapToObj(y -> new Pair(x, y))
            )
            .forEach(coord -> map.addUnbreakableWall(coord, new UnbreakableWall()));
    }

    @Override
    public void triggerArenaCollapse() {
        this.collapseStarted = true;
        this.wallCollapseOrder = computeCollapseOrder();
    }
    
    /** 
     * Places the next wall in the collapse order, removing it from the list
     */
    private void placeNextWall() {
        this.map.addUnbreakableWall(
            this.wallCollapseOrder.remove(0),
            new UnbreakableWall()
        );
    }

    /** 
     * Computes the order in which the arena will collapse, applying and algorithm 
     * of spiral traversal to the game map
     */
    private List<Pair> computeCollapseOrder() {
        List<Pair> order = new ArrayList<>();
        int top = 0, bottom = Utils.MAP_ROWS - 1, left = 0, right = Utils.MAP_COLS - 1;
        while(top <= bottom && left <= right) {

            for (int i = left; i <= right; i++) {
                order.add(new Pair(top, i));
            }
            top++;
            for (int i = top; i <= bottom; i++) {
                order.add(new Pair(i, right));
            }
            right--;
            if(top <= bottom) {
                for (int i = right; i >= left; i--) {
                    order.add(new Pair(bottom, i));
                } 
                bottom--;
            }
            if (left <= right) {
                for (int i = bottom; i >= top; i--) {
                    order.add(new Pair(i, left));
                }
                left++;
            }
        }
        return order;
    }
    

    /** 
     * To be called during the manager's initialization, it computes the twelve cells 
     * on which nothing can spawn except the player
     */
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