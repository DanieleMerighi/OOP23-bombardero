package it.unibo.bombardero.map.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import it.unibo.bombardero.cell.Bomb;
import it.unibo.bombardero.cell.Cell;
import it.unibo.bombardero.cell.Cell.CellType;
import it.unibo.bombardero.cell.Flame;
import it.unibo.bombardero.cell.Wall;
import it.unibo.bombardero.cell.powerup.api.PowerUp;
import it.unibo.bombardero.cell.powerup.api.PowerUpFactory;
import it.unibo.bombardero.cell.powerup.api.PowerUpType;
import it.unibo.bombardero.cell.powerup.impl.PowerUpFactoryImpl;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.GenPair;
import it.unibo.bombardero.map.api.MapGenerator;
import it.unibo.bombardero.physics.impl.RectangleBoundingBox;
import it.unibo.bombardero.utils.Utils;

/**
 * This class implements the Game Map of the Bombardero game.
 * 
 * @author Federico Bagattoni
 */
public final class GameMapImpl implements GameMap {

    /* Using an HashMap to hold the information about the map's tiles: */
    private final Map<GenPair<Integer, Integer>, Cell> map = new HashMap<>();
    private final MapGenerator mapGenerator;
    private final PowerUpFactory powerupFactory = new PowerUpFactoryImpl();
    private final List<GenPair<Integer, Integer>> collapseOrder;
    private boolean collapseStarted;
    private int counter;

    /**
     * Constructs a new Game Map generating unbreakable walls, to skip the wall
     * generation use {@#GameMapImpl(boolean)}.
     */
    public GameMapImpl() {
        this.mapGenerator = new MapGeneratorImpl();
        placeUnbreakableWalls();
        placeBreakableWalls();
        collapseOrder = mapGenerator.generateCollapseOrder();
    }

    /**
     * Creates a new Game Map with the option to opt out of the walls generation.
     * 
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
            counter = (counter + 1) % GameMap.COLLAPSE_RATE;
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
    public boolean addBomb(final Bomb bomb, final GenPair<Integer, Integer> coordinate) {
        /*
         * NOTA: sarebbe una ridondanza controllare anche se è vuota perché se le
         * collisioni
         * sono fatte bene allora l'unica cosa per cui si deve controllare è la presenza
         * della
         * bomba
         */
        if (!isBomb(coordinate) && isEmpty(coordinate)) {
            map.put(new GenPair<Integer, Integer>(coordinate.x(), coordinate.y()), bomb);
            return true;
        }
        return false;
    }

    @Override
    public void addFlame(final Flame flame, final GenPair<Integer, Integer> coordinate) {
        this.map.put(new GenPair<Integer, Integer>(coordinate.x(), coordinate.y()), flame);
    }

    @Override
    public void addUnbreakableWall(final GenPair<Integer, Integer> coordinate) {
        this.map.put(new GenPair<Integer, Integer>(coordinate.x(), coordinate.y()),
                new Wall(Cell.CellType.WALL_UNBREAKABLE,
                        new RectangleBoundingBox(coordinate.x(), coordinate.y(), 1.0f, 1.0f)));
    }

    @Override
    public void addBreakableWall(final GenPair<Integer, Integer> coordinate) {
        this.map.put(new GenPair<Integer, Integer>(coordinate.x(), coordinate.y()),
                new Wall(Cell.CellType.WALL_BREAKABLE,
                        new RectangleBoundingBox(coordinate.x(), coordinate.y(), 1.0f, 1.0f)));
    }

    @Override
    public void removeBreakableWall(final GenPair<Integer, Integer> coordinate) {
        if (this.isBreakableWall(coordinate)) {
            this.map.remove(new GenPair<Integer, Integer>(coordinate.x(), coordinate.y()));
            this.map.put(new GenPair<Integer, Integer>(coordinate.x(), coordinate.y()),
                    powerupFactory.createPowerUp());
        }
    }

    @Override
    public void removeFlame(final GenPair<Integer, Integer> coordinate) {
        if (this.isFlame(coordinate)) {
            this.map.remove(new GenPair<Integer, Integer>(coordinate.x(), coordinate.y()));
        }
    }

    @Override
    public void removeBomb(final GenPair<Integer, Integer> coordinate) {
        if (this.isBomb(coordinate)) {
            this.map.remove(new GenPair<Integer, Integer>(coordinate.x(), coordinate.y()));
        }
    }

    @Override
    public boolean isBomb(final GenPair<Integer, Integer> coordinate) {
        return this.map.containsKey(new GenPair<Integer, Integer>(coordinate.x(), coordinate.y()))
                && this.map.get(new GenPair<Integer, Integer>(coordinate.x(), coordinate.y())) instanceof Bomb;
    }

    @Override
    public boolean isBreakableWall(final GenPair<Integer, Integer> coordinate) {
        return this.map.containsKey(new GenPair<Integer, Integer>(coordinate.x(), coordinate.y()))
                && this.map.get(new GenPair<Integer, Integer>(coordinate.x(), coordinate.y())).getCellType()
                        .equals(Cell.CellType.WALL_BREAKABLE);
    }

    @Override
    public boolean isUnbreakableWall(final GenPair<Integer, Integer> coordinate) {
        return this.map.containsKey(new GenPair<Integer, Integer>(coordinate.x(), coordinate.y()))
                && this.map.get(new GenPair<Integer, Integer>(coordinate.x(), coordinate.y())).getCellType()
                        .equals(Cell.CellType.WALL_UNBREAKABLE);
    }

    @Override
    public boolean isFlame(final GenPair<Integer, Integer> coordinate) {
        return this.map.containsKey(new GenPair<Integer, Integer>(coordinate.x(), coordinate.y()))
                && this.map.get(new GenPair<Integer, Integer>(coordinate.x(), coordinate.y())).getCellType()
                        .equals(CellType.FLAME);
    }

    @Override
    public boolean isEmpty(final GenPair<Integer, Integer> coordinate) {
        return !this.map.containsKey(new GenPair<Integer, Integer>(coordinate.x(), coordinate.y()));
    }

    @Override
    public Map<GenPair<Integer, Integer>, Cell> getMap() {
        return Map.copyOf(this.map);
    }

    @Override
    public void removePowerUp(final GenPair<Integer, Integer> coordinate) {
        if (isPowerUp(coordinate)) {
            map.remove(new GenPair<Integer, Integer>(coordinate.x(), coordinate.y()));
        }
    }

    @Override
    public boolean isPowerUp(final GenPair<Integer, Integer> coordinate) {
        return this.map.containsKey(new GenPair<Integer, Integer>(coordinate.x(), coordinate.y()))
                && this.map.get(new GenPair<Integer, Integer>(coordinate.x(), coordinate.y())).getCellType()
                        .equals(CellType.POWERUP);
    }

    @Override
    public Optional<PowerUpType> whichPowerUpType(final GenPair<Integer, Integer> coordinate) {
        if (this.isPowerUp(coordinate)) {
            final PowerUp powerup = (PowerUp) this.map.get(new GenPair<Integer, Integer>(coordinate.x(), coordinate.y()));
            return Optional.of(powerup.getType());
        }
        return Optional.empty();
    }

    private void placeUnbreakableWalls() {
        mapGenerator.generateUnbreakableWalls().forEach(wallPosition -> addUnbreakableWall(wallPosition));
    }

    private void placeBreakableWalls() {
        mapGenerator.generateBreakableWalls(
                this,
                mapGenerator.getTotalWallsToGenerate(Utils.WALL_PRESENCE_RATE))
                .forEach(wallPosition -> addBreakableWall(wallPosition));
    }

}
