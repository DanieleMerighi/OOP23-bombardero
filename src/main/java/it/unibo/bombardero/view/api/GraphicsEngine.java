package it.unibo.bombardero.view.api;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import it.unibo.bombardero.cell.Cell;
import it.unibo.bombardero.character.Character;
import it.unibo.bombardero.map.api.GenPair;
import it.unibo.bombardero.view.BombarderoViewMessages;
import it.unibo.bombardero.view.ResizingEngine;
import it.unibo.bombardero.view.ResourceGetter;

/**
 * The game's graphics engine that directly talks with the game's controller
 * from which it receives request.
 */
public interface GraphicsEngine {

    /**
     * The different types of cards that the graphics can display.
     */
    enum ViewCards {
        /**
         * The game.
         */
        GAME("game"),

        /**
         * The main menu.
         */
        MENU("menu"),

        /**
         * The game's guide.
         */
        GUIDE("guide"),

        /**
         * The end of the game.
         */
        END("end");

        private String id;

        ViewCards(final String cardName) {
            id = cardName;
        }

        /**
         * Returns the textual version of the enum's entry.
         * @return the string related to the enum's entry
         */
        public String getStringId() {
            return this.id;
        }
    }

    /**
     * The ways the game can end and can be displayed.
     */
    enum EndGameState {

        /**
         * The user has won.
         */
        WIN,

        /**
         * The user lost the game.
         */
        LOSE;
    }

    /**
     * Shows the requested card to the user. 
     * @param cardName the requested card
     * @see ViewCards
     */
    void showGameScreen(ViewCards cardName);

    /**
     * Updates the graphics view, rendering the elements passed as parameters, such as
     * the map, the various characters and the eventual time left to be displayed in the timer.
     * @param map the game map
     * @param playerList the players to be displayed
     * @param enemiesList the enemies to be displayed
     * @param timeLeft optionally, the time left in the match
     */
    void update(
        Map<GenPair<Integer, Integer>, Cell> map,
        List<Character> playerList,
        List<Character> enemiesList,
        Optional<Long> timeLeft
    );

    /** 
     * Sets the paused view in the currently active card, for example
     * darkening the view and showing a few buttons.
     */
    void setPausedView();

    /**
     * Unsets the paused view set by the {@link #setPausedView()} method.
     */
    void setUnpausedView();

    /**
     * Requests the currently active card to show the requested message. 
     * @param message the message to be displayed
     * @see BombarderoViewMessages
     */
    void setMessage(BombarderoViewMessages message);

    /**
     * Shows the end screen on the currently active card, requesting to display
     * the particular {@link EndGameState} requested.
     * @param gameState the state that has to be displayed in the card (e.g. victory/defeat)
     * @see EndGameState
     */
    void showEndScreen(EndGameState gameState);

    /**
     * Returns the graphic's {@link ResizingEngine} used to resize all the
     * graphics elements in the view.
     * @return the {@link ResizingEngine}
    */
    ResizingEngine getResizingEngine();

    /**
     * Returns the {@link ResourceGetter} used by the graphics to get
     * the game's assets.
     * @return the graphic's {@link ResourceGetter}
     */
    ResourceGetter getResourceGetter();

    /**
     * Resets the view, supposedly after the end of a phase.
     * Clearing everything that has been displayed during the game.
     */
    void clearView();
}
