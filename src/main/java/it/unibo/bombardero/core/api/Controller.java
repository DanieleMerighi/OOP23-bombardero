package it.unibo.bombardero.core.api;

import java.util.Map;
import java.util.Optional;
import java.util.List;

import it.unibo.bombardero.cell.Cell;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.view.BombarderoViewMessages;
import it.unibo.bombardero.character.Character;

/**
 * This class models the game's controller, part of the
 * M.V.C. pattern. This controller is intended to mediate
 * communications between the View and the Model through 
 * various methods.
 */
public interface Controller {

    /**
     * Starts the game, entering the game loop.
     */
    void startGame();

    /** 
     * Ends the game by ending the game loop and triggering the ending screen
     * in the View.
     */
    void endGame();

    /**
     * Starts the game's Guide, a slightly different game mode intended for
     * explaining the game to a new user. The procedure is the same as the 
     * {@link #startGame()} method.
     * @see {@link it.unibo.guide.api.GuideManager}
     */
    void startGuide();

    /**
     * Ends the guide, ending the relative game loop. 
     */
    void endGuide();

    /** 
     *  Signals the View to display the end of the Guide game mode.
     */
    void displayEndGuide();

    /**
     * Handles the reaction of the game once the user's presses the 
     * escape sequence. This method should safely pause and unpause the
     * game. 
     */
    void escape();

    /** 
     * Updates the model and the view. 
     * @param elapsed the time elapsed since the last update
     */
    void update(long elapsed);

    /** 
     * Requests the view to display the message passed as argument.
     * @param message the {@link BombarderoViewMessage} message to be displayed
     * by the view.
     */
    void toggleMessage(BombarderoViewMessages message);

    /** 
     * Returns the pause state of the game.
     * @return true if the game is paused, false otherwise
     */
    boolean isGamePaused();

    /**
     * Tells wether any game mode has started or not.
     * @return wether any game mode has started. 
     */
    boolean isGameStarted();

    /**
     * Returns the main player of the current game instance. 
     * @return the main {@link Character}
     */
    Character getMainPlayer();

    /** 
     * Returns the enemies of the current game instance. 
     * @return a list of {@link Character} representing the enemies
     */
    List<Character> getEnemies();

    /**
     * Returns the {@link Map} object representing the arena of the current
     * game instance. 
     * @return the {@link Map} containing all the {@link Cell}s of the game.
     */
    Map<Pair, Cell> getMap();

    /**
     * Returns the time left in the game's timer, if the
     * model is keeping time. If the model is not keeping time it 
     * will return zero.
     * @return the time left in the game, in milliseconds. 
     */
    Optional<Long> getTimeLeft();

}
