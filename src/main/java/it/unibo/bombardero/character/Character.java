package it.unibo.bombardero.character;

import java.util.Optional;

import it.unibo.bombardero.cell.BombFactory;
import it.unibo.bombardero.cell.PowerUp.PowerUpType;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.Coord;
import it.unibo.bombardero.map.api.Pair;

/**
 * Abstract class representing a character in the game.
 * Contains common properties and methods for characters.
 * 
 * @author Jacopo Turchi
 */
public abstract class Character {

    // Constants for default settings
    private static final float STARTING_SPEED = 0.01f;
    private static final int STARTING_FLAME_RANGE = 1;
    private static final int STARTING_BOMBS = 1;
    private static final int WIDTH = 22;
    private static final int HEIGHT = 30;

    // Game manager reference
    private final GameManager manager;

    // Bomb Factory reference
    private final BombFactory bombFactory;

    // Position related
    private Coord coordinate;

    // Game attribute related
    private boolean isAlive = true;
    private int numBomb = STARTING_BOMBS;
    private int flameRange = STARTING_FLAME_RANGE;
    private float speed = STARTING_SPEED;
    private Optional<PowerUpType> bombType = Optional.empty();
    private boolean kick = false;
    private boolean lineBomb = false;

    /**
     * Constructs a new Character with the specified parameters.
     * 
     * @param manager     the game manager
     * @param coord       the initial coordinates of the character
     * @param bombFactory the factory to create bombs
     */
    public Character(GameManager manager, Coord coord, BombFactory bombFactory) {
        this.manager = manager;
        this.coordinate = coord;
        this.bombFactory = bombFactory;
    }

    /**
     * Updates the character's state.
     * This method should be implemented by subclasses to define character-specific
     * behavior.
     */
    public abstract void update();

    /**
     * Checks if the character is alive.
     * 
     * @return true if the character is alive, false otherwise
     */
    public boolean isAlive() {
        return isAlive;
    }

    /**
     * Gets the integer coordinates of the character, adjusted by the character's width and height.
     * 
     * @return the map's corrisponding integer coordinates of the character
     */
    public Pair getIntCoordinate() {
        return new Pair((int) Math.floor(this.coordinate.row() + HEIGHT / 2),
                (int) Math.floor(this.coordinate.col() + WIDTH / 2));
    }

    /**
     * Places a bomb at the character's current location if they have bombs left.
     */
    public void placeBomb() {
        if (hasBombsLeft()) {
            System.out.println("bombPlaced");
            /*if (this.manager.addBomb(this.bombFactory.CreateBomb(this.bombType, getIntCoordinate(), this.flameRange))) {
                numBomb--;
            }*/
        }
    }

    /**
     * Checks if the character has bombs left to place.
     * 
     * @return true if the character has bombs left, false otherwise
     */
    public boolean hasBombsLeft() {
        return this.numBomb > 0;
    }

    /**
     * Kills the character, setting its alive status to false.
     */
    public void kill() {
        isAlive = false;
    }

    /**
     * Gets the game manager associated with this character.
     * 
     * @return the game manager
     */
    public GameManager getManager() {
        return manager;
    }

    /**
     * Gets the bomb factory associated with this character.
     * 
     * @return the bomb factory
     */
    public BombFactory getFactory() {
        return bombFactory;
    }

    /**
     * Gets the current float position of the character.
     * 
     * @return the character's float coordinates
     */
    public Coord getCharacterPosition() {
        return coordinate;
    }

    /**
     * Sets the current float position of the character.
     * 
     * @param c the new coordinates of the character
     */
    public void setCharacterPosition(final Coord c) {
        this.coordinate = c;
    }

    /**
     * Gets the number of bombs the character has left.
     * 
     * @return the number of bombs
     */
    public int getNumBomb() {
        return numBomb;
    }

    /**
     * Sets the number of bombs the character has left.
     * 
     * @param numBomb the new number of bombs
     */
    public void setNumBomb(final int numBomb) {
        this.numBomb = numBomb;
    }

    /**
     * Gets the flame range of the bombs placed by the character.
     * 
     * @return the flame range
     */
    public int getFlameRange() {
        return flameRange;
    }

    /**
     * Sets the flame range of the bombs placed by the character.
     * 
     * @param flameRange the new flame range
     */
    public void setFlameRange(final int flameRange) {
        this.flameRange = flameRange;
    }

    /**
     * Gets the speed of the character.
     * 
     * @return the speed
     */
    public float getSpeed() {
        return speed;
    }

    /**
     * Sets the speed of the character.
     * 
     * @param speed the new speed
     */
    public void setSpeed(final float speed) {
        this.speed = speed;
    }

    /**
     * Gets the type of bomb the character can place.
     * 
     * @return an Optional containing the bomb type
     */
    public Optional<PowerUpType> getBombType() {
        return bombType;
    }

    /**
     * Sets the type of bomb the character can place.
     * 
     * @param bombType the new bomb type
     */
    public void setBombType(final Optional<PowerUpType> bombType) {
        this.bombType = bombType;
    }

    /**
     * Checks if the character can kick bombs.
     * 
     * @return true if the character can kick bombs, false otherwise
     */
    public boolean isKick() {
        return kick;
    }

    /**
     * Sets the character's ability to kick bombs.
     * 
     * @param kick true to enable bomb kicking, false to disable
     */
    public void setKick(final boolean kick) {
        this.kick = kick;
    }

    /**
     * Checks if the character can use the power-up "line bomb".
     * 
     * @return true if the character can use the power-up "line bomb", false otherwise
     */
    public boolean isLineBomb() {
        return lineBomb;
    }

    /**
     * Sets the character's ability to use the power-up "line bomb".
     * 
     * @param lineBomb true to enable the power-up "line bomb", false to disable
     */
    public void setLineBomb(final boolean lineBomb) {
        this.lineBomb = lineBomb;
    }

}
