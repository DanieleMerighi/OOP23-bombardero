package it.unibo.bombardero.map.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.cell.Bomb;
import it.unibo.bombardero.cell.BreakableWall;
import it.unibo.bombardero.cell.Cell;
import it.unibo.bombardero.cell.Flame;
import it.unibo.bombardero.cell.UnbreakableWall;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.MapManager;
import it.unibo.bombardero.map.api.Pair;

public class GameMapImpl implements GameMap {

    private final Map<Pair,Cell> map = new HashMap<>(); /* Using an HashMap to hold the information about the map's tiles */
    private final MapManager manager;

    public GameMapImpl() {
        this.manager = new MapManagerImpl(this);
        manager.placeUnbreakableWalls();
        manager.placeBreakableWalls();
    }

    @Override
    public void update() {
        this.manager.update();
    }

    @Override
    public void addBomb(final Bomb bomb, final Pair coordinate) {
        this.map.put(coordinate, bomb);
    }

    @Override
    public void addFlame(Flame flame, Pair coordinate) {
        this.map.put(coordinate, flame);
    }

    @Override
    public void addUnbreakableWall(Pair coordinate, Cell wall) {
        this.map.put(coordinate, wall);
    }

    @Override
    public void addBreakableWall(Pair coordinate, Cell wall) {
        this.map.put(coordinate, wall);
    }

    @Override
    public boolean isBomb(Pair coordinate) {
        return this.map.containsKey(coordinate) && this.map.get(coordinate) instanceof Bomb;
    }

    @Override
    public boolean isBreakableWall(Pair coordinate) {
        return this.map.containsKey(coordinate) && this.map.get(coordinate) instanceof BreakableWall;
    }

    @Override
    public boolean isUnbreakableWall(Pair coordinate) {
        return this.map.containsKey(coordinate) && this.map.get(coordinate) instanceof UnbreakableWall;
    }

    @Override
    public boolean isFlame(Pair coordinate) {
        return this.map.containsKey(coordinate) && this.map.get(coordinate) instanceof Flame;
    }

    @Override
    public boolean isEmpty(Pair coordinate) {
        return !this.map.containsKey(coordinate);
    }

    @Override
    public Set<Entry<Pair, Cell>> getMap() {
        return Map.copyOf(this.map).entrySet();
    }
    
}
