package it.unibo.bombardero.character;

import java.util.Optional;
import java.util.Deque;
import java.awt.geom.Point2D;
import java.util.ArrayDeque;


import it.unibo.bombardero.cell.BasicBomb;
import it.unibo.bombardero.cell.BombFactory;
import it.unibo.bombardero.cell.powerup.api.PowerUpType;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.Coord;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.physics.api.BoundingBox;
import it.unibo.bombardero.physics.api.CollisionEngine;
import it.unibo.bombardero.physics.impl.BombarderoCollision;
import it.unibo.bombardero.physics.impl.RectangleBoundingBox;

/**
 * Abstract class representing a character in the game.
 * Contains common properties and methods for characters.
 */
public abstract class Character {

    // Constants for default settings
    public static final float STARTING_SPEED = 0.05f;
    private static final float INCREASE_SPEED = 0.005f;
    public static final int STARTING_FLAME_RANGE = 1;
    private static final int STARTING_BOMBS = 1;

    // Constants for controls
    public static final float MAX_SPEED = 0.09f;
    public static final int MAX_FLAME_RANGE = 8;
    private static final int MAX_BOMBS = 8;

    // Game manager reference
    protected final GameManager manager;
    // TODO: Remove getManager method and make manager protected. Use it with
    // super.manager

    // Bomb Factory reference
    private final BombFactory bombFactory;

    // Position related
    private Coord coordinate; // Starting character coordinate
    private Direction facingDirection = Direction.DOWN; // Starting character facingDirection
    private boolean stationary = true;
    private BoundingBox bBox; // Solid area of the character

    // Physics part of character
    // private BoundingBox bBox; //TODO change with point 2D

    // Game attribute related
    private boolean isAlive = true;
    private int numBomb = STARTING_BOMBS;
    private int flameRange = STARTING_FLAME_RANGE;
    private float speed = STARTING_SPEED;
    private Optional<PowerUpType> bombType = Optional.empty();
    private boolean kick; // False by default
    private boolean lineBomb;
    private final Deque<BasicBomb> remoteBombQueue = new ArrayDeque<>();
    /*
     * TODO: Gestire la rimozione di una bomba se è esplosa per concatenazione con
     * un'altra
     * usare character.removeRemoteBomb(BasicBomb bomb); ogni volta che si sta per
     * far esplodere una remote bomb
     */

    // Update related
    private boolean hasToPlaceBomb;
    private boolean hasToPlaceLineBomb;
    private boolean hasToExplodeRemoteBomb;

    // Skull effects
    private boolean constipation; // The character is unable to lay down bombs
    private boolean butterfingers; // The character's hand becomes slippery. The character rapidly lay down bombs

    // Skull manager
    private long effectDuration; // Indicates the duration of the skull effect
    private Optional<Runnable> resetEffect = Optional.empty(); // Restores all stats modified by the skull

    /**
     * Constructs a new Character with the specified parameters.
     * 
     * @param manager     the game manager that controls the game state
     * @param coord       the initial coordinates where the character is spawned
     * @param bombFactory the factory to create bombs
     */
    public Character(final GameManager manager, final Coord coord, final BombFactory bombFactory) {
        this.manager = manager; // TODO: Solve manager, a copy?
        this.coordinate = coord;
        this.bombFactory = bombFactory;
        this.bBox = new RectangleBoundingBox(new Point2D.Float(0.1562f, 0.844f) , 0.781f , 0.875f);
    }

    /**
     * Updates the character's state.
     * This method should be implemented by subclasses to define character-specific
     * behavior.
     * 
     * @param elapsedTime the time elapsed since the last update
     */
    public abstract void update(long elapsedTime);

    /**
     * Updates the skeleton's effects.
     * 
     * @param elapsedTime the time elapsed since the last update
     */
    public void updateSkeleton(final long elapsedTime) {
        if (this.effectDuration > 0) { // Continues until the duration reaches zero
            this.effectDuration -= elapsedTime;
            if (this.effectDuration <= 0) { // When the effect ends the character's stats get resetted
                this.resetEffect.ifPresent(Runnable::run); // If there's a effect to reset, it runs the reset effect
                this.resetEffect = Optional.empty(); // Clear the reset effect after it has run
            }
            if (this.butterfingers) { // If the character has butterfingers, he places a bomb
                placeBomb();
            }
        }
    }

    /**
     * Checks if the character is alive.
     * 
     * @return true if the character is alive, false otherwise
     */
    public boolean isAlive() {
        return isAlive;
    }

    /**
     * Kills the character, setting its alive status to false.
     */
    public void kill() {
        isAlive = false;
    }

    /**
     * Gets the integer coordinates of the character
     * 
     * @return the map's corrisponding integer coordinates of the character
     */
    public Pair getIntCoordinate() {
        return new Pair((int) Math.floor(this.coordinate.x()),
                (int) Math.floor(this.coordinate.y()));
    }

    /**
     * @return bounding box of the charachters
     */
    public BoundingBox getBoundingBox() {
        return bBox;
    }
    
    /**
     * Places a bomb at the character's current location if he has bombs left.
     * 
     * @return true if the character has placed the bomb, false otherwise
     */
    public boolean placeBomb() {
        return placeBombImpl(this.bombFactory.CreateBomb(this));
    }

    /**
     * Places a bomb at the given coordinates if the character has bombs left.
     * 
     * @param coordinate the bomb's coordinate
     * 
     * @return true if the character has placed the bomb, false otherwise
     */
    public boolean placeBomb(final Pair coordinate) {
        return placeBombImpl(this.bombFactory.CreateBomb(this));
    }

    private boolean placeBombImpl(final BasicBomb bomb) {
        if (hasBombsLeft() && !this.constipation && this.manager
                .addBomb(bomb)) {
            this.numBomb--;
            if (bombType.equals(Optional.of(PowerUpType.REMOTE_BOMB))) { // If character has the remote bomb
                remoteBombQueue.addLast(bomb);
            }
            return true;
        }
        return false;
    }

    // TODO: Cambia nome, no get
    /**
     * Checks if the character has to place a bomb.
     * 
     * @return true if the character has to place a bomb, false otherwise
     */
    public boolean getHasToPlaceBomb() {
        return hasToPlaceBomb;
    }

    /**
     * Sets whether the character should place a bomb.
     * 
     * @param hasToPlaceBomb true to cause the character to place a bomb, false
     *                       otherwise
     */
    public void setHasToPlaceBomb(final boolean hasToPlaceBomb) {
        this.hasToPlaceBomb = hasToPlaceBomb;
    }

    // fa esplodere la prima remote piazzata della coda
    public void explodeRemoteBomb() {
        if (!remoteBombQueue.isEmpty()) {
            final BasicBomb bomb = remoteBombQueue.removeFirst();
            System.out.println("exploded remote bomb\n\n");
            bomb.computeFlame(bomb);
        }
    }

    // Quando si sta per far esplodere una bomba remote per concatenazione viene
    // chiamato questo metodo
    public void removeRemoteBomb(final BasicBomb remoteBomb) {
        if (!remoteBombQueue.isEmpty()) {
            System.out.println("removed remote bomb\n\n");
            remoteBombQueue.removeFirstOccurrence(remoteBomb);
        }
    }

    // TODO: Togli get, cambia nome
    /**
     * Checks if the character has to explode a remote bomb.
     * 
     * @return true if the character has to explode a remote bomb, false otherwise
     */
    public boolean getHasToExplodeRemoteBomb() {
        return hasToExplodeRemoteBomb;
    }

    /**
     * Sets whether the character should explode a remote bomb.
     * 
     * @param hasToPlaceBomb true to cause the character to explode a remote bomb,
     *                       false otherwise
     */
    public void setHasToExplodeRemoteBomb(final boolean hasToExplodeRemoteBomb) {
        this.hasToExplodeRemoteBomb = hasToExplodeRemoteBomb;
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
     * Checks if the character is stationary.
     * 
     * @return true if the character is stationary, false otherwise
     */
    public boolean isStationary() {
        return stationary;
    }

    /**
     * Sets whether the character is stanionary.
     * 
     * @param stationary true if the character is stationary, false otherwise
     */
    public void setStationary(final boolean stationary) {
        this.stationary = stationary;
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
            this.speed -= INCREASE_SPEED;
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

    // TODO: Togli get, cambia nome
    /**
     * Checks if the character has to place a line bomb.
     * 
     * @return true if the character has to place a line bomb, false otherwise
     */
    public boolean getHasToPlaceLineBomb() {
        return hasToPlaceLineBomb;
    }

    /**
     * Sets whether the character should place a line bomb.
     * 
     * @param hasToPlaceBomb true to cause the character to place a line bomb, false
     *                       otherwise
     */
    public void setHasToPlaceLineBomb(final boolean hasToPlaceLineBomb) {
        this.hasToPlaceLineBomb = hasToPlaceLineBomb;
    }

    /**
     * Checks if the character has constipation.
     * 
     * @return true if the character has constipation, false otherwise
     */
    public boolean hasConstipation() {
        return constipation;
    }

    /**
     * Sets whether the character should have constipation.
     * 
     * @param constipation true to cause the character constipation, false
     *                     otherwise
     */
    public void setConstipation(final boolean constipation) {
        this.constipation = constipation;
    }

    /**
     * Checks if the character has butterfingers.
     * 
     * @return true if the character has butterfingers, false otherwise
     */
    public boolean hasButterfingers() {
        return butterfingers;
    }

    /**
     * Sets whether the character should have butterfingers.
     * 
     * @param butterfingers true to cause the character butterfingers, false
     *                      otherwise
     */
    public void setButterfingers(final boolean butterfingers) {
        this.butterfingers = butterfingers;
    }

    /**
     * Sets the effect's duration.
     * 
     * @param duration the duration of the effect
     */
    public void setEffectDuration(final long duration) {
        this.effectDuration = duration;
    }

    //TODO: write better javadoc
    /**
     * Gets the reset effect
     * 
     * @return the reset effect
     */
    public Optional<Runnable> getResetEffect() {
        return resetEffect;
    }

    /**
     * Sets the reset effect.
     * 
     * @param resetEffect the reset effect
     */
    public void setResetEffect(final Runnable resetEffect) {
        this.resetEffect = Optional.of(resetEffect);
    }
}
