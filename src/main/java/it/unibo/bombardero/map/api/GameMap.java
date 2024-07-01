package it.unibo.bombardero.map.api;

import java.util.Map;
import java.util.Optional;

import it.unibo.bombardero.cell.Bomb;
import it.unibo.bombardero.cell.Cell;
import it.unibo.bombardero.cell.Flame;
import it.unibo.bombardero.cell.powerup.api.PowerUpType;

/**
 * The main class for managing all the aspects of the game arena.
 * @author Federico Bagattoni
 */
public interface GameMap {

    static int COLLAPSE_RATE = 5;

    /**
     * Updates the dynamic aspect of the map, calling the {@code update()} method of
     * {@link MapGenerator}.
     */
    void update(long timeLeft);
 
    /** 
     * Starts collapsing the map adding one wall at a time,
     * in a spiral way, every 60 ticks.
     */
    void triggerCollapse();

    /**
     * Checks if the coordinate is free, if so adds the bomb to the cell.
     * @param bomb the bomb to be added
     * @param coordinate where the bomb is to be placed
     * @return true if the bomb is placed correctly
     */
    boolean addBomb(Bomb bomb, Pair coordinate);

    /**
     * Adds a flame's reference to the map.
     * @param flame the flame to be added
     * @param coordinate where the flame is to be placed
     */
    void addFlame(Flame flame, Pair coordinate);

    /** 
     * Creates and adds an unbreakable wall to the map.
     * @param coordinate where the wall is to be placed
     */
    void addUnbreakableWall(Pair coordinate);

    /**
     * Creates and adds a breakable wall to the map.
     * @param coordinate where the wall is to be placed
     */
    void addBreakableWall(Pair coordinate);

    /** 
     * Removes the breakable wall at the coordinate and eventually spawns a powerup.
     * @param coordinate the position of the wall to remove
     */
    void removeBreakableWall(Pair coordinate);

    /**
     * Removes the bomb at the given coordinate.
     * @param coordinate the coordinate where to remove the bomb
     */
    void removeBomb(Pair coordinate);

    /**
     * Removes the powerUp at the given coordinate.
     * @param coordinate the position of the wall to remove
     */
    void removePowerUp(Pair coordinate);

    /**
     * @param coordinate
     * @return if the Cell with this coordinates is a PowerUp
     */
    boolean isPowerUp(Pair coordinate);

    /**
     * @param coordinate
     * @return which type of PowerUp the Cell is
     */
    Optional<PowerUpType> whichPowerUpType(Pair coordinate);

    /**
     * remove the falme at the given coordinate
     * @param coordinate
     */
    void removeFlame(Pair coordinate);

    /** 
     * Returns true if the cell at the coordinate is a Bomb.
     * @param coordinate the coordinate to check 
     * @return wether the cell at the coordinate is a Bomb
     */
    boolean isBomb(Pair coordinate);

    /**
     * Returns true if the cell at the coordinate is a Breakable Wall.
     * @param coordinate the coordinate to check 
     * @return wether the cell at the coordinate is a Breakable Wall
     */
    boolean isBreakableWall(Pair coordinate);

    /**
     * Returns true if the cell at the coordinate is an Unbreakable Wall.
     * @param coordinate the coordinate to check 
     * @return wether the cell at the coordinate is an Unbreakable Wall
     */
    boolean isUnbreakableWall(Pair coordinate);

    /**
     * Returns true if the cell at the coordinate is a Flame.
     * @param coordinate the coordinate to check 
     * @return wether the cell at the coordinate is a Flame
     */
    boolean isFlame(Pair coordinate);

    /**
     * Returns true if the cell at the coordinate is empty.
     * @param coordinate the coordinate to check 
     * @return wether the cell is empty or not
     */
    boolean isEmpty(Pair coordinate);

    /**
     * Returns a copy of the Map used to map the coordinates to the elements of the game.
     * @return a copy of the map
     */
    Map<Pair, Cell> getMap();

}
