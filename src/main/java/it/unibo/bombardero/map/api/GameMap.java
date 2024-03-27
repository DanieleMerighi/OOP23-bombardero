package it.unibo.bombardero.map.api;

import it.unibo.bombardero.cell.Bomb;
import it.unibo.bombardero.cell.Cell;

/**
 * The main class for managing all the aspects of the game arena
 * @author Federico Bagattoni
 */
public interface GameMap {

    void addBomb(final Bomb bomb, Pair coordinate);

    void addEnemy();

    void addPlayer();

    void addUnbreakableWall();

    void addBreakableWall(Pair coord, Cell wall);

    void removeEnemy();

    boolean isPlayer();

    boolean isEnemy();

    boolean isBomb(Pair coordinate);

    boolean isBreakableWall(Pair coordinate);

    boolean isUnbreakableWall(Pair coordinate);

    boolean isFlame(Pair coordinate);
}
