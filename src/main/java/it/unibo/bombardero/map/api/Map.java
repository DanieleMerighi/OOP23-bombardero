package it.unibo.bombardero.Map.api;

/**
 * The main class for managing all the aspects of the game arena
 * @author Federico Bagattoni
 */
public interface Map {

    void addBomb();

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
