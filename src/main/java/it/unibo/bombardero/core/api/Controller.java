package it.unibo.bombardero.core.api;

import java.util.Map;
import java.util.Set;

import it.unibo.bombardero.cell.Cell;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.character.Character;

public interface Controller {
    
    //qui andrebbero inseriti i comandi imput

    void startGame();

    Character getMainPlayer();

    Set<Character> getEnemies();

    Map<Pair, Cell> getMap();

}
