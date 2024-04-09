package it.unibo.bombardero.core.api;

import java.util.Set;

import it.unibo.bombardero.cell.Bomb;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.character.Character;

public interface GameManager {

    void updateGame();

    void endGame();

    Set<Character> getEnemies();

    Character getPlayer();

    GameMap getGameMap();

    void explodeBomb(Bomb bomb);
}   
