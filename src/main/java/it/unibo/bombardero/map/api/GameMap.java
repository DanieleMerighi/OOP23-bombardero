package it.unibo.bombardero.map.api;

/**
 * The main class for managing all the aspects of the game arena
 * @author Federico Bagattoni
 */
public interface GameMap {

    void addBomb(Pair coordinate);

    void addEnemy();

    void addPlayer();

    void removeEnemy();

    boolean isPlayer();

    boolean isEnemy();

    boolean isBomb();

    boolean isBreakableWall();

    boolean isUnbreakableWall();

    boolean isExplosion();
}
