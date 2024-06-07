package it.unibo.bombardero.map.impl;

import java.util.HashMap;
import java.util.Map;

import it.unibo.bombardero.cell.BasicBomb;
import it.unibo.bombardero.cell.Bomb;
import it.unibo.bombardero.cell.Wall;
import it.unibo.bombardero.cell.powerup.api.PowerUpFactory;
import it.unibo.bombardero.cell.powerup.impl.PowerUpFactoryImpl;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.cell.Cell;
import it.unibo.bombardero.cell.Flame;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.MapManager;
import it.unibo.bombardero.map.api.Pair;

/**
 * This class implements the Game Map of the Bombardero game.
 * @author Federico Bagattoni
 */
public final class GameMapImpl implements GameMap {

    /* Using an HashMap to hold the information about the map's tiles: */
    private final Map<Pair, Cell> map = new HashMap<>();
    private final MapManager mapManager;
    private final PowerUpFactory powerupFactory = new PowerUpFactoryImpl();

    /** 
     * Constructs a new Game Map generating unbreakable walls, to skip the wall generation use {@#GameMapImpl(boolean)}.
     */
    public GameMapImpl() {
        this.mapManager = new MapManagerImpl(this);
        mapManager.placeUnbreakableWalls();
        mapManager.placeBreakableWalls();
    }

    /**
     * Creates a new Game Map with the option to opt out of the walls generation.
     * <p>
     * NOTE: This constructor has been created for testing purposes and allows to skip the 
     * obstacle generation, producing a map with only unbreakable obstacles.
     * The breakable obstacles can be generated later anyway (potential untested side effects)
     * by calling the manager's method.
     * </p>
     * @param wallGeneration wether the walls have to be generated or not
    */
    public GameMapImpl(final boolean wallGeneration) {
        this.mapManager = new MapManagerImpl(this);
        mapManager.placeUnbreakableWalls();
        if (wallGeneration) {
            mapManager.placeBreakableWalls();
        }
    }

    @Override
    public void update() {
        mapManager.update();
    }

    @Override
    public void triggerCollapse() {
        mapManager.triggerCollapse();
    }

    @Override
    public boolean addBomb(final BasicBomb bomb, final Pair coordinate) {
        /* NOTA: sarebbe una ridondanza controllare anche se è vuota perché se le collisioni
         *  sono fatte bene allora l'unica cosa per cui si deve controllare è la presenza della 
         * bomba */
        if (!isBomb(coordinate) || isEmpty(coordinate)) {
            map.put(coordinate, bomb);
            return true;
        }
        return false;
    }

    @Override
    public void addFlame(final Flame flame, final Pair coordinate) {
        this.map.put(coordinate, flame);
    }

    @Override
    public void addUnbreakableWall(final Pair coordinate) {
        this.map.put(coordinate, new Wall(Cell.CellType.WALL_UNBREAKABLE));
    }

    @Override
    public void addBreakableWall(final Pair coordinate) {
        this.map.put(coordinate, new Wall(Cell.CellType.WALL_BREAKABLE));
    }

    @Override
    public void removeBreakableWall(final Pair coordinate) {
        if (this.isBreakableWall(coordinate)) {
            this.map.remove(coordinate);
            /* TODO: add powerup spawn mechanism */
            /* this.map.put(coordinate, powerupFactory.createPowerUp()); */
        }
    }

    @Override
    public void removeFlame(final Pair coordinate) {
        if (this.isFlame(coordinate)) {
            this.map.remove(coordinate);
        }
    }

    @Override
    public void removeFlame(final Pair coordinate) {
        if (this.isFlame(coordinate)) {
            this.map.remove(coordinate);
        }
    }

    @Override
    public void removeBomb(final Pair coordinate) {
        if (this.isBomb(coordinate)) {
            this.map.remove(coordinate);
        }
    }

    @Override
    public boolean isBomb(final Pair coordinate) {
        return this.map.containsKey(coordinate) && this.map.get(coordinate) instanceof Bomb;
    }

    @Override
    public boolean isBreakableWall(final Pair coordinate) {
        return this.map.containsKey(coordinate) 
            && this.map.get(coordinate).getCellType().equals(Cell.CellType.WALL_BREAKABLE);
    }

    @Override
    public boolean isUnbreakableWall(final Pair coordinate) {
        return this.map.containsKey(coordinate)
            && this.map.get(coordinate).getCellType().equals(Cell.CellType.WALL_UNBREAKABLE);
    }

    @Override
    public boolean isFlame(final Pair coordinate) {
        return this.map.containsKey(coordinate) && this.map.get(coordinate) instanceof Flame;
    }

    @Override
    public boolean isEmpty(final Pair coordinate) {
        return !this.map.containsKey(coordinate);
    }

    @Override
    public Map<Pair, Cell> getMap() {
        return Map.copyOf(this.map);
    }
 
}
