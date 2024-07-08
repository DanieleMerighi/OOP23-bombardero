package it.unibo.bombardero.core.api;

import java.util.List;
import java.util.Optional;

import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.character.Character;

/** 
 * This interface models the game's Manger, the central
 * class of the model. 
 * This class contains the main update method for all the 
 * updatable objects in the Model, keeps the time and manages
 * other events such as the game's guide.
 * <p>
 * The manager initialises and contains
 * all the game's entities. It also serves as gateway to the model
 * from the game's {@link Engine} and {@link Controller}. 
 * <p>
 */
public interface GameManager {

    /**
     * Updates all the dynamic entities of the game (the ones that
     * need to be updated). If needed it can pass to such entities
     * the elapsed time from the previous update.
     * @param elapsed the time passed from the previous update.
     * @param controller the game's controller
     */
    void updateGame(long elapsed, Controller controller);

    /** 
     * Returns a list of the enemies in the current game instance.
     * @return a list of {@link @Character} representing the game's enemies. 
     */
    List<Character> getEnemies();

    /** 
     * Returns the main player of the current game's instance. 
     * @return the main {@link Character} of the game.
     */
    List<Character> getPlayers();

    /** 
     * Returns the {@link GameMap} of the current game instance.
     * @return the {@link GameMap} of the game.
     */
    GameMap getGameMap();

    /**
     * Returns the time left in the game, in milliseconds, if the manager is 
     * keeping the time.
     * @return the time left, if the time is being kept, otherwise nothing
     */
    Optional<Long> getTimeLeft();
}
