package it.unibo.bombardero.core.api;

/** 
 * This class represents the game engine, this class shall be run
 * in a separate {@code Thread}, therefore this class extends the {@code Runnable}
 * interface. 
 * <p>
 * The game engine has to have the following functionalities:
 * <ul>
 *  <li> Initialise the game model
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
     * Initialises and returns the game manager, also keeping a reference
     * of such element for further use. This method basically creates all
     * the objects in the game.
     * @return the initialised game manager
     */
    GameManager initGameManager();

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
     * Pauses the game loop by making wait the thread running it.
     */
    void pauseGameLoop();

    /**
     * Resumes the main loop, if it was stopped.
     */
    void resumeGameLoop();

    /** 
     * Ends the game loop, supposedly when the game has ended. 
     */
    void endGameLoop();

}