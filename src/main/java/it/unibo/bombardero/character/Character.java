package it.unibo.bombardero.character;

import java.util.Optional;

import it.unibo.bombardero.cell.BombFactory;
import it.unibo.bombardero.cell.PowerUp.PowerUpType;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.Coord;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.utils.Utils;

public abstract class Character {

    // constant for default setting
    private static final float STARTING_SPEED = 0.01f;
    private static final int STARTING_FLAME_RANGE = 1;
    private static final int STARTING_BOMBS = 1;
    private static final int WIDTH = 22;
    private static final int HEIGHT = 30;

    // game manager reference
    private final GameManager manager;

    // Bomb Factory reference
    private final BombFactory bombFactory;

    // position related
    private Coord coordinate;

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
        return new Pair((int)(this.coordinate.row() + HEIGHT / 2.0f / Utils.CELL_SIZE),
        (int)(this.coordinate.col() + WIDTH / 2.0f / Utils.CELL_SIZE));
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

    public void setCharacterPosition(final Coord c) {
        this.coordinate = c;
    }

    public int getNumBomb() {
        return numBomb;
    }

    public void setNumBomb(final int numBomb) {
        this.numBomb = numBomb;
    }

    public int getFlameRange() {
        return flameRange;
    }

    public void setFlameRange(final int flameRange) {
        this.flameRange = flameRange;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(final float speed) {
        this.speed = speed;
    }

    public Optional<PowerUpType> getBombType() {
        return bombType;
    }

    public void setBombType(final Optional<PowerUpType> bombType) {
        this.bombType = bombType;
    }

    public boolean isKick() {
        return kick;
    }

    public void setKick(final boolean kick) {
        this.kick = kick;
    }

    public boolean isLineBomb() {
        return lineBomb;
    }

    public void setLineBomb(final boolean lineBomb) {
        this.lineBomb = lineBomb;
    }

}
