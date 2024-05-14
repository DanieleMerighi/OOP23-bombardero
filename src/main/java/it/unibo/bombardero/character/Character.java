package it.unibo.bombardero.character;

import java.util.Optional;

import it.unibo.bombardero.cell.BombFactory;
import it.unibo.bombardero.cell.PowerUp.PowerUpType;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.Coord;
import it.unibo.bombardero.map.api.Pair;

public abstract class Character {

    // constant for default setting
    private static final float STARTING_SPEED = 2;
    private static final int STARTING_FLAME_RANGE = 1;
    private static final int STARTING_BOMBS = 1;
    private static final int WIDTH = 22;
    private static final int HEIGHT = 30;

    // game manager reference
    private final GameManager manager;

    // Bomb Factory reference
    private final BombFactory bombFactory;

    // position related
    private final Coord coordinate;

    // game attribute related
    private boolean isAlive = true;
    private int numBomb = STARTING_BOMBS;
    private int flameRange = STARTING_FLAME_RANGE;
    private float speed = STARTING_SPEED;
    private Optional<PowerUpType> bombType = Optional.empty();
    private boolean kick = false;
    private boolean lineBomb = false;

    public Character(GameManager manager, Coord coord, BombFactory bombFactory) {
        this.manager = manager;
        this.coordinate = coord;
        this.bombFactory = bombFactory;
    }

    public abstract void update();

    public boolean isAlive() {
        return isAlive;
    }

    public Pair getIntCoordinate() {
        return new Pair((int) Math.floor(this.coordinate.row() + this.HEIGHT / 2),
                (int) Math.floor(this.coordinate.col() + this.WIDTH / 2));
    }

    public abstract void placeBomb();

    public boolean hasBombsLeft() {
        return this.numBomb > 0;
    }

    public void kill() {
        isAlive = false;
    }

    public GameManager getManager() {
        return manager;
    }

    public BombFactory getFactory() {
        return bombFactory;
    }

    public Coord getCharacterPosition() {
        return coordinate;
    }

    public int getNumBomb() {
        return numBomb;
    }

    public void setNumBomb(int numBomb) {
        this.numBomb = numBomb;
    }

    public int getFlameRange() {
        return flameRange;
    }

    public void setFlameRange(int flameRange) {
        this.flameRange = flameRange;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public Optional<PowerUpType> getBombType() {
        return bombType;
    }

    public void setBombType(Optional<PowerUpType> bombType) {
        this.bombType = bombType;
    }

    public boolean isKick() {
        return kick;
    }

    public void setKick(boolean kick) {
        this.kick = kick;
    }

    public boolean isLineBomb() {
        return lineBomb;
    }

    public void setLineBomb(boolean lineBomb) {
        this.lineBomb = lineBomb;
    }

}
