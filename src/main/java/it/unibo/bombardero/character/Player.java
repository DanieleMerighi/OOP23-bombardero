package it.unibo.bombardero.character;

import java.util.Optional;
import it.unibo.bombardero.cell.PowerUp.PowerUpType;
import it.unibo.bombardero.core.api.GameManager;

public class Player extends Character {

    //game attribute
    private int numBomb;
    private int flameRange;
    private int speed;
    private Optional<PowerUpType> bombType;
    private Optional<PowerUpType> kick;
    private Optional<PowerUpType> skull;

    //view attribute
    private int facingDirection;


    public Player(GameManager manager, float x, float y, int width, int height) {
        super(manager, x, y, width, height);
    }

    public void update() {
        //based on which key get pressed, the player moves in that direction
    }
    
}
