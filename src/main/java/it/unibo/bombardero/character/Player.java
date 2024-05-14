package it.unibo.bombardero.character;

import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.Coord;
import it.unibo.bombardero.cell.BombFactory;

/**
 * The class responsible for managing the player
 * 
 * @author Jacopo Turchi
 */
public class Player extends Character {

    // view attribute
    // private int facingDirection;

    /**
     * The Player's constructor method. It initialise the Player's parameter
     * 
     * @param manager is the reference to the GameManager
     * @param coord where the player get spawened
     * @param bombFactory is the reference to the BombFactory
     */
    public Player(final GameManager manager, final Coord coord, final BombFactory bombFactory) {
        super(manager, coord, bombFactory);
    }

    /**
     * Updates the Player interaction with the map
     */
    public void update() {
        // based on which key get pressed, the player moves in that direction
    }
}
