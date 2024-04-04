package it.unibo.bombardero.core.api;

import java.util.List;

import it.unibo.bombardero.map.api.GameMap;

public interface GameManager {

    void updateGame();

    void endGame();

    List<Character> getCharacter();

    GameMap getMap();

    
}   
