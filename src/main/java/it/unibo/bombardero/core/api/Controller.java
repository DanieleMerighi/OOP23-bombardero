package it.unibo.bombardero.core.api;

import java.util.Map;
import java.util.List;

import it.unibo.bombardero.cell.Cell;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.character.Character;

public interface Controller {

    /**
     * Starts the game, entering the game loop and starting the game timer
     */
    void startGame();

    void pauseGame();

    void endGame();

    Character getMainPlayer();

    List<Character> getEnemies();

    Map<Pair, Cell> getMap();

    long getTimeLeft();

}
