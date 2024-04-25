package it.unibo.bombardero.core.api;

import java.util.Set;

import it.unibo.bombardero.cell.BasicBomb;
import it.unibo.bombardero.cell.Cell.CellType;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.character.Character;

public interface GameManager {

    void updateGame();

    void endGame();

    Set<Character> getEnemies();

    Character getPlayer();

    GameMap getGameMap();

    void addBomb(BasicBomb bomb, Pair pos);
    
    void removeBomb(Pair pos);

    void addFlame(CellType type,Pair pos);
    
    void removeFlame(Pair pos);
    
    void removeWall(Pair pos);
}   
