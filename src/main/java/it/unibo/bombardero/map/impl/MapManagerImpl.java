package it.unibo.bombardero.map.impl;

import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import it.unibo.bombardero.cell.UnbreakableWall;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.MapManager;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.utils.Utils;

public class MapManagerImpl implements MapManager {

    private final GameMap map;
    private Optional<Pair> nextWallToCollapse = Optional.empty();

    public MapManagerImpl(GameMap map) {
        this.map = map;
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
        this.nextWallToCollapse = Optional.of(new Pair(Utils.MAP_ROWS, Utils.MAP_COLS));
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