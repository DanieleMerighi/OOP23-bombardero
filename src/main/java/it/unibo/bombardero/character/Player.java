package it.unibo.bombardero.character;

import java.util.Optional;
import it.unibo.bombardero.cell.PowerUp.PowerUpType;
import it.unibo.bombardero.core.KeyboardInput;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.Coord;
import it.unibo.bombardero.cell.Bomb;
import it.unibo.bombardero.cell.BombFactory;
import it.unibo.bombardero.cell.Bomb.BombType;

public class Player extends Character {

    //private Optional<PowerUpType> skull;

    //view attribute
    //private int facingDirection;


    public Player(final GameManager manager, final Coord coord) {
        super(manager, coord);
    }

    public void placeBomb() {
        if (hasBombsLeft()) {
            /*if(manager.addBomb(BombFactory.CreateBomb(bombType, getCoord(), flameRange))
                numBomb--;   */
        }
    }

    public void update() {
        //based on which key get pressed, the player moves in that direction
    }
}
