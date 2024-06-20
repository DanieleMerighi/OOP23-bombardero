package it.unibo.bombardero.core.api;

import java.util.Map;
import java.util.List;

import it.unibo.bombardero.cell.AbstractCell;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.view.BombarderoViewMessages;
import it.unibo.bombardero.character.Character;

public interface Controller {

    /**
     * Starts the game, entering the game loop and starting the game timer
     */
    void startGame();

    void endGame();

    void startGuide();
    
    void escape();

    void toggleMessage(final BombarderoViewMessages message);

    boolean isGamePaused();

    Character getMainPlayer();

    List<Character> getEnemies();

    Map<Pair, AbstractCell> getMap();

    long getTimeLeft();

}
