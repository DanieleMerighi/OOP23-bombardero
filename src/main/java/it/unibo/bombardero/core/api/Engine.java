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
    @Override
    void run();

}
