package it.unibo.bombardero.core.api;

import java.util.List;
import java.util.Optional;

import it.unibo.bombardero.cell.BasicBomb;
import it.unibo.bombardero.cell.Bomb;
import it.unibo.bombardero.cell.Flame.FlameType;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.character.Character;

public interface GameManager {

    void updateGame(final long elapsed);

    void endGame();

    List<Character> getEnemies();

    Character getPlayer();

    GameMap getGameMap();

    boolean addBomb(Bomb bomb);
    
    void removeBomb(Pair pos);

    Optional<Bomb> getBomb(Pair pos);

    void addFlame(FlameType type,Pair pos);

    void removePowerUp(final Pair pos);
    
    void removeFlame(Pair pos);
    
    boolean removeWall(Pair pos);

    long getTimeLeft();
}   
