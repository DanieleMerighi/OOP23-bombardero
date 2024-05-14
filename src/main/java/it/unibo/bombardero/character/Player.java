package it.unibo.bombardero.character;

import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.Coord;
import it.unibo.bombardero.cell.BombFactory;

public class Player extends Character {

    // view attribute
    // private int facingDirection;

    public Player(final GameManager manager, final Coord coord, final BombFactory bombFactory) {
        super(manager, coord, bombFactory);
    }

    public void update() {
        // based on which key get pressed, the player moves in that direction
    }
}
