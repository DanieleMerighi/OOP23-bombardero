package it.unibo.bombardero.core.api;

import java.util.Map;

import it.unibo.bombardero.cell.Cell;
import it.unibo.bombardero.map.api.Pair;

public interface Controller {
    
    //qui andrebbero inseriti i comandi imput

    void startGame();

    void getMainPlayer();

    void getEnemies();

    Map<Pair, Cell> getMap();

}
