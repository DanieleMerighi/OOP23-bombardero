package it.unibo.bombardero.map.impl;

import java.util.HashMap;
import java.util.Map;

import it.unibo.bombardero.cell.BasicBomb;
import it.unibo.bombardero.cell.Bomb;
import it.unibo.bombardero.cell.Wall;
import it.unibo.bombardero.cell.Cell;
import it.unibo.bombardero.cell.Flame;
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

    /* 
     * This constructor has been created for testing purposes and allow to skip the 
     * obstacle generation, producing a map with only unbreakable obstacles.
     * The breakable obstacles can be generated later anyway (untested side effects, potentially 
     * stunning the application) by calling the manager's method.
     * @param wallGeneration wether the walls have to be generated or not
    */
    public GameMapImpl(boolean wallGeneration) {
        this.manager = new MapManagerImpl(this);
        manager.placeUnbreakableWalls();
        if(wallGeneration) {
            manager.placeBreakableWalls();
        }
    }

    @Override
    public void update() {
        this.manager.update();
    }

    @Override
    public void addBomb(final BasicBomb bomb) {
        this.map.put(bomb.getPos(), bomb);
    }

    @Override
    public void addFlame(Flame flame, Pair coordinate) {
        this.map.put(coordinate, flame);
    }

    @Override
    public void addUnbreakableWall(Pair coordinate) {
        this.map.put(coordinate, new Wall(Cell.CellType.WALL_UNBREAKABLE));
    }

    @Override
    public void addBreakableWall(Pair coordinate) {
        this.map.put(coordinate, new Wall(Cell.CellType.WALL_BREAKABLE));
    }

    @Override
    public void removeBreakableWall(Pair coordinate) {
        if(this.isBreakableWall(coordinate)) {
            this.map.remove(coordinate);
            /* TODO: add powerup spawn mechanism */
        }
    }

    @Override
    public void removeBomb(Pair coordinate) {
        if(this.isBomb(coordinate)) {
            this.map.remove(coordinate);
        }
    }

    @Override
    public boolean isBomb(Pair coordinate) {
        return this.map.containsKey(coordinate) && this.map.get(coordinate) instanceof Bomb;
    }

    @Override
    public boolean isBreakableWall(Pair coordinate) {
        return this.map.containsKey(coordinate) && 
            this.map.get(coordinate).getCellType().equals(Cell.CellType.WALL_UNBREAKABLE);
    }

    @Override
    public boolean isUnbreakableWall(Pair coordinate) {
        return this.map.containsKey(coordinate) && 
            this.map.get(coordinate).getCellType().equals(Cell.CellType.WALL_BREAKABLE);
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
    public Map<Pair, Cell> getMap() {
        return Map.copyOf(this.map);
    }
    
}
