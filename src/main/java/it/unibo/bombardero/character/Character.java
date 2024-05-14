package it.unibo.bombardero.character;

import java.util.Optional;

import it.unibo.bombardero.cell.PowerUp.PowerUpType;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.Coord;
import it.unibo.bombardero.map.api.Pair;

public abstract class Character {

    // constant for default setting
    private static final int STARTING_SPEED = 2;
    private static final int STARTING_FLAME_RANGE = 1;
    private static final int STARTING_BOMBS = 1;

    // game manager reference
    protected GameManager manager;

    // position related
    protected Coord coordinate;
    protected int width, height;
    
    // game attribute related
    protected boolean isAlive = true;
    protected int numBomb = STARTING_BOMBS;
    protected int flameRange = STARTING_FLAME_RANGE;
    protected int speed = STARTING_SPEED;
    protected Optional<PowerUpType> bombType = Optional.empty();
    protected boolean kick = false;
    protected boolean lineBomb = false;

    public Character(GameManager manager, Coord coord, int width, int height) {
        this.manager = manager;
        this.coordinate = coord;
        this.width = width;
        this.height = height;
    }

    public abstract void update();

    public boolean isAlive() {
        return isAlive;
    }

    public Pair getIntCoordinate() {
        return new Pair((int) Math.floor(this.coordinate.row() + this.height/2), 
                            (int) Math.floor(this.coordinate.col() + this.width/2));
    }

    public void kill() {
        isAlive = false;
    }
    
}
