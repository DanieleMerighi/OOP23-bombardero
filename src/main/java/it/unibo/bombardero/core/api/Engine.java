package it.unibo.bombardero.core.api;

/** 
 * This class represents the game engine, this class shall be run
 * in a separate {@code Thread}, therefore this class extends the {@code Runnable}
 * interface. 
 * <p>
 * The game engine has to have the following functionalities:
 * <ul>
 *  <li> Start the game loop
 *  <li> Pause the game loop 
 *  <li> Resume the game loop
 *  <li> End the game loop
 * </ul> 
 * @see Runnable
 * @see Thread
 * @author Federico Bagattoni
 */
public interface Engine extends Runnable {

    /**
     * The method containing the game loop, this will be run in
     * a separate Thread. 
     */
    void run();

    /** 
     * Starts running the in the {@#run()} method in a separate
     * thread. 
     */
    void startGameLoop();

    /** 
     * Ends the game loop, supposedly when the game has ended. 
     */
    void endGameLoop();

    /**
     * Wether the game loop is interrupted or not (and its waiting
     * got a notification).
     * @return wether the game loop thread is interrupted or not
     */
    boolean isInterrupted();
}
