package it.unibo.bombardero.character;

import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.Coord;
import it.unibo.bombardero.cell.BombFactory;

/**
 * This class represents a Player in the game.
 * This class extends the Character class and is responsible for handling 
 * player-specific actions and behaviors.
 * 
 * Author: Jacopo Turchi
 */
public class Player extends Character {

    // view attribute
    // private int facingDirection;

    /**
     * Constructs a new Player instance with the specified parameters.
     * 
     * @param manager the game manager that controls the game state
     * @param coord the initial coordinates where the player is spawned
     * @param bombFactory the factory to create bombs
     */
    public Player(final GameManager manager, final Coord coord, final BombFactory bombFactory) {
        super(manager, coord, bombFactory);
    }

    /**
     * Updates the Player's interactions with the map.
     * This method should define how the player moves and interacts based on user input.
     */
    @Override
    public void update() {

    }
}
