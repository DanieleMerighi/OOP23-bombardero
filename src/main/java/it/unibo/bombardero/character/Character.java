package it.unibo.bombardero.character;

import java.util.Optional;

import it.unibo.bombardero.cell.BombFactory;
import it.unibo.bombardero.cell.powerup.api.PowerUpType;
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
    private static final float STARTING_SPEED = 0.05f;
    private static final float INCREASE_SPEED = 0.005f;
    private static final int STARTING_FLAME_RANGE = 1;
    private static final int STARTING_BOMBS = 1;

    // Constants for controls
    private static final float MAX_SPEED = 0.09f;
    public static final int MAX_FLAME_RANGE = 8;
    private static final int MAX_BOMBS = 8;

    // Game manager reference
    private final GameManager manager;

    // Bomb Factory reference
    private final BombFactory bombFactory;

    // Position related
    private Coord coordinate; // Starting character coordinate
    private Direction facingDirection = Direction.DEFAULT; // Starting character facingDirection

    // Game attribute related
    private boolean isAlive = true;
    private int numBomb = STARTING_BOMBS;
    private int flameRange = STARTING_FLAME_RANGE;
    private float speed = STARTING_SPEED;
    private Optional<PowerUpType> bombType = Optional.empty();
    private boolean kick; // False by default
    private boolean lineBomb;

    /**
     * Constructs a new Character with the specified parameters.
     * 
     * @param manager     the game manager that controls the game state
     * @param coord       the initial coordinates where the character is spawned
     * @param bombFactory the factory to create bombs
     */
    public Character(final GameManager manager, final Coord coord, final BombFactory bombFactory) {
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
     * Gets the integer coordinates of the character, adjusted by the character's
     * width and height.
     * 
     * @return the map's corrisponding integer coordinates of the character
     */
    public Pair getIntCoordinate() {
        return new Pair((int) Math.floor(this.coordinate.row()),
                (int) Math.floor(this.coordinate.col()));
    }

    /**
     * Places a bomb at the character's current location if he has bombs left.
     */
    public void placeBomb() {
        if (hasBombsLeft() && this.manager
                .addBomb(this.bombFactory.CreateBomb(this.bombType, getIntCoordinate(), this.flameRange))) {
            this.numBomb--;
            System.out.println("bomb placed");
        }
    }

    /**
     * Places a bomb at the given coordinates if the character has bombs left.
     * 
     * @param coordinate the bomb's coordinate
     * 
     * @return true if the character has placed the bomb, false otherwise
     */
    public boolean placeBomb(final Pair coordinate) {
        if (hasBombsLeft() && this.manager
                .addBomb(this.bombFactory.CreateBomb(this.bombType, coordinate, this.flameRange))) {
            this.numBomb--;
            System.out.println("line bomb placed");
            return true;
        }
        return false;
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
     * Gets the current looking direction of the character.
     * 
     * @return the character's looking direction
     */
    public Direction getFacingDirection() {
        return this.facingDirection;
    }

    /**
     * Sets the current looking direction of the character.
     * 
     * @param direction the new looking direction of the character
     */
    public void setFacingDirection(final Direction direction) {
        this.facingDirection = direction;
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
     * Increse the number of bombs the character has left.
     * 
     */
    public void increaseNumBomb() {
        if (this.numBomb < MAX_BOMBS) {
            this.numBomb++;
        }
    }

    /**
     * Decrease the number of bombs the character has left.
     * 
     */
    public void decreaseNumBomb() {
        if (this.numBomb > STARTING_BOMBS) {
            this.numBomb--;
        }
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
     * Increse the flame range of the bombs placed by the character.
     * 
     */
    public void increaseFlameRange() {
        if (this.flameRange < MAX_FLAME_RANGE) {
            this.flameRange++;
        }
    }

    /**
     * Decrease the flame range of the bombs placed by the character.
     * 
     */
    public void decreaseFlameRange() {
        if (this.flameRange > STARTING_FLAME_RANGE) {
            this.flameRange--;
        }
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
     * Increse the speed of the character.
     * 
     */
    public void increaseSpeed() {
        if (this.speed < MAX_SPEED) {
            this.speed += INCREASE_SPEED;
        }
    }

    /**
     * Decrease the speed of the character.
     * 
     */
    public void decreaseSpeed() {
        if (this.speed > STARTING_SPEED) {
            this.speed -= 0.005f;
        }
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
    public boolean hasKick() {
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
     * @return true if the character can use the power-up "line bomb", false
     *         otherwise
     */
    public boolean hasLineBomb() {
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
