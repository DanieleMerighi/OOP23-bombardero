package it.unibo.bombardero.map.impl;

import java.util.List;
import java.util.stream.IntStream;

import it.unibo.bombardero.cell.UnbreakableWall;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.MapManager;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.utils.Utils;

public class MapManagerImpl implements MapManager {

    private final GameMap map;
    private List<Pair> nextWallToCollapse;

    public MapManagerImpl(GameMap map) {
        this.map = map;
    }

    @Override
    public GameMap getMap() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMap'");
    }

    @Override
    public void generateBreakableWalls() {
        /* new Random()
            .ints(0, Utils.MAP_COLS)
            .boxed()
            .mapToObj(x -> new Pair(IntStream.ints(0, Utils.MAP_ROWS).findAny(), x))
            .filter(coord -> map.isEmpty(coord))
            .forEach(coord -> map.addBreakableWall(coord));
        */
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
        while(top <= bottom && left <= right) {

            for (int i = left; i <= right; i++) {
                this.nextWallToCollapse.add(new Pair(top, i));
            }
            top++;
            for (int i = top; i <= bottom; i++) {
                this.nextWallToCollapse.add(new Pair(i, right));
            }
            right--;
            if(top <= bottom) {
                for (int i = right; i >= left; i--) {
                    this.nextWallToCollapse.add(new Pair(bottom, i));
                } 
                bottom--;
            }
            if (left <= right) {
                for (int i = bottom; i >= top; i--) {
                    this.nextWallToCollapse.add(new Pair(i, left));
                }
                left++;
            }
        }
    }

    @Override
    public void update() {
        placeNextWall();
    }
    
    /**
     * Places the next wall into the map, if present. Then updates to the next wall to place. 
     */
    private void placeNextWall() {
        /* if(this.nextWallToCollapse.isPresent()) {
            this.map.addUnbreakableWall(nextWallToCollapse.get(), new UnbreakableWall());

        } */
    }
}