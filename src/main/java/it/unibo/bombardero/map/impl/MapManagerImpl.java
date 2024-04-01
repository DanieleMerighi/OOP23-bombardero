package it.unibo.bombardero.map.impl;

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

    public MapManagerImpl(GameMap map) {
        this.map = map;
    }

    @Override
    public void generateBreakableWalls() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'generateBreakableWalls'");
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
    public void placeNextWall() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'placeNextWall'");
    }

    @Override
    public void triggerArenaCollapse() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'triggerArenaCollpse'");
    }
    
}