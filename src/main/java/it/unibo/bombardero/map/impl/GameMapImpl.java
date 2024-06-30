package it.unibo.bombardero.map.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import it.unibo.bombardero.cell.Bomb;
import it.unibo.bombardero.cell.Cell;
import it.unibo.bombardero.cell.Wall;
import it.unibo.bombardero.cell.Cell.CellType;
import it.unibo.bombardero.cell.powerup.api.PowerUpFactory;
import it.unibo.bombardero.cell.powerup.impl.PowerUpFactoryImpl;
import it.unibo.bombardero.cell.Flame;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.MapGenerator;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.utils.Utils;

/**
 * This class implements the Game Map of the Bombardero game.
 * @author Federico Bagattoni
 */
public final class GameMapImpl implements GameMap {

    /* Using an HashMap to hold the information about the map's tiles: */
    private final Map<Pair, Cell> map = new HashMap<>();
    private final MapGenerator mapGenerator;
    private final PowerUpFactory powerupFactory = new PowerUpFactoryImpl();
    private final List<Pair> collapseOrder;
    private boolean collapseStarted;
    private int counter;

    /** 
     * Constructs a new Game Map generating unbreakable walls, to skip the wall generation use {@#GameMapImpl(boolean)}.
     */
    public GameMapImpl() {
        this.mapGenerator = new MapGeneratorImpl();
        placeBreakableWalls();
        placeUnbreakableWalls();
        collapseOrder = mapGenerator.generateCollapseOrder();
    }

    /**
     * Creates a new Game Map with the option to opt out of the walls generation.
     * @param wallGeneration wether the walls have to be generated or not
    */
    public GameMapImpl(final boolean wallGeneration) {
        this.mapGenerator = new MapGeneratorImpl();
        placeUnbreakableWalls();
        if (wallGeneration) { 
            placeBreakableWalls();
        }
        collapseOrder = mapGenerator.generateCollapseOrder();
    }

    @Override
    public void update(final long timeLeft) {
        if (collapseStarted && !collapseOrder.isEmpty()) {
            counter = (counter + 1)%GameMap.COLLAPSE_RATE;
            if (counter == 0) {
                addUnbreakableWall(collapseOrder.remove(0));
            }
        }
    }

    @Override
    public void triggerCollapse() {
        collapseStarted = true;

    }

    @Override
    public boolean addBomb(final Bomb bomb, final Pair coordinate) {
        /* NOTA: sarebbe una ridondanza controllare anche se è vuota perché se le collisioni
         *  sono fatte bene allora l'unica cosa per cui si deve controllare è la presenza della 
         * bomba */
        if (!isBomb(coordinate) && isEmpty(coordinate)) {
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
        this.map.put(coordinate , new Wall(Cell.CellType.WALL_UNBREAKABLE, coordinate));
    }

    @Override
    public void addBreakableWall(final Pair coordinate) {
        this.map.put(coordinate, new Wall(Cell.CellType.WALL_BREAKABLE, coordinate));
    }

    @Override
    public void removeBreakableWall(final Pair coordinate) {
        if (this.isBreakableWall(coordinate)) {
            this.map.remove(coordinate);
            /* TODO: add powerup spawn mechanism */
            this.map.put(coordinate, powerupFactory.createPowerUp(coordinate));
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
        return this.map.containsKey(coordinate) && this.map.get(coordinate).getCellType().equals(CellType.FLAME);
    }

    @Override
    public boolean isEmpty(final Pair coordinate) {
        return !this.map.containsKey(coordinate);
    }

    @Override
    public Map<Pair, Cell> getMap() {
        return Map.copyOf(this.map);
    }

    @Override
    public void removePowerUp(final Pair coordinate) {
        if(isPowerUp(coordinate)) {
            map.remove(coordinate);
        }
    }

    @Override
    public boolean isPowerUp(final Pair coordinate) {
        return this.map.containsKey(coordinate) && this.map.get(coordinate).getCellType().equals(CellType.POWERUP);
    }

    private void placeUnbreakableWalls() {
        mapGenerator.generateUnbreakableWalls().forEach(wallPosition -> addUnbreakableWall(wallPosition));
    }

    private void placeBreakableWalls() {
        mapGenerator.generateBreakableWalls(
            this,
            mapGenerator.getTotalWallsToGenerate(Utils.WALL_PRESENCE_RATE)).forEach(wallPosition -> addBreakableWall(wallPosition));
    }
 
}
