package it.unibo.bombardero.map.impl;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.IntStream;

import it.unibo.bombardero.cell.BreakableWall;
import it.unibo.bombardero.cell.UnbreakableWall;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.MapManager;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.utils.Utils;

public class MapManagerImpl implements MapManager {

    private static final double WALL_PRESENCE_RATE = 0.8; /* This number control how much walls to generate in relation to the free space */
    private final Set<Pair> MAP_CORNERS = new TreeSet<Pair>();

    private final GameMap map;
    private final GameManager manager;
    private List<Pair> wallCollapseOrder;
    private boolean collapseStarted = false;

    public MapManagerImpl(GameMap map, GameManager manager) {
        this.map = map;
        this.manager = manager;
        this.computeMapCorners();
    }

    @Override
    public void update() {
        if(collapseStarted) {
            placeNextWall();
        }
    }

    @Override
    public void generateBreakableWalls() {
        int totalWallsToGenerate = (int)Math.floor(Math.floorDiv(Utils.MAP_COLS, 2) + Math.floorDiv(Utils.MAP_ROWS, 2) * 0.8);
        Random rnd = new Random();
        Pair coordinate;
        while(totalWallsToGenerate != 0) {
            do {
                coordinate = new Pair(rnd.nextInt(Utils.MAP_COLS), rnd.nextInt(Utils.MAP_ROWS));
            } while (!map.isEmpty(coordinate) && this.MAP_CORNERS.contains(coordinate));
            this.map.addBreakableWall(coordinate, new BreakableWall());
            totalWallsToGenerate--;
        }
    }

    @Override
    public void placeUnbreakableWalls() {
        IntStream
            .range(0, Utils.MAP_ROWS)
            .filter(num -> num%2 == 0)
            .boxed()
            .flatMap(x -> IntStream
                .range(0, Utils.MAP_COLS)
                .mapToObj(y -> new Pair(x, y))
            )
            .forEach(coord -> map.addUnbreakableWall(coord, new UnbreakableWall()));
    }

    @Override
    public void triggerArenaCollapse() {
        int top = 0, bottom = Utils.MAP_ROWS - 1, left = 0, right = Utils.MAP_COLS - 1;
        this.collapseStarted = true;
        while(top <= bottom && left <= right) {

            for (int i = left; i <= right; i++) {
                this.wallCollapseOrder.add(new Pair(top, i));
            }
            top++;
            for (int i = top; i <= bottom; i++) {
                this.wallCollapseOrder.add(new Pair(i, right));
            }
            right--;
            if(top <= bottom) {
                for (int i = right; i >= left; i--) {
                    this.wallCollapseOrder.add(new Pair(bottom, i));
                } 
                bottom--;
            }
            if (left <= right) {
                for (int i = bottom; i >= top; i--) {
                    this.wallCollapseOrder.add(new Pair(i, left));
                }
                left++;
            }
        }
    }
    
    /** 
     * Places the next wall in the collapse order, removing it from the list
     */
    private void placeNextWall() {
        this.map.addUnbreakableWall(this.wallCollapseOrder.remove(0), new UnbreakableWall());
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