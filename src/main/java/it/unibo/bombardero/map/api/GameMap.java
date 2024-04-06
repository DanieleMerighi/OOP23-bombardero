package it.unibo.bombardero.map.api;

import java.util.Map;
import java.util.Set;

import it.unibo.bombardero.cell.Bomb;
import it.unibo.bombardero.cell.Cell;
import it.unibo.bombardero.cell.Flame;

/**
 * The main class for managing all the aspects of the game arena
 * @author Federico Bagattoni
 */
public interface GameMap {

    void update();

    void addBomb(final Bomb bomb, Pair coordinate);
    
    void addFlame(Flame flame, Pair coordinate);

    void addUnbreakableWall(Pair coordinate, Cell wall);

    void addBreakableWall(Pair coordinate, Cell wall);

    boolean isBomb(Pair coordinate);

    boolean isBreakableWall(Pair coordinate);

    boolean isUnbreakableWall(Pair coordinate);

    boolean isFlame(Pair coordinate);

    boolean isEmpty(Pair coordinate);

    Set<Map.Entry<Pair, Cell>> getMap();

}
