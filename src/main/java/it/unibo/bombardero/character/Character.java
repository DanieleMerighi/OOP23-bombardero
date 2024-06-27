package it.unibo.bombardero.character;

import java.util.Optional;
import java.util.Deque;
import java.awt.geom.Point2D;
import java.util.ArrayDeque;

import it.unibo.bombardero.cell.BasicBomb;
import it.unibo.bombardero.cell.BombFactory;
import it.unibo.bombardero.cell.Bomb.BombType;
import it.unibo.bombardero.cell.powerup.api.PowerUpType;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.Coord;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.physics.api.BoundingBox;
import it.unibo.bombardero.physics.impl.RectangleBoundingBox;

/**
 * Abstract class representing a character in the game.
 * Contains common properties and methods for characters.
 */
public abstract class Character {

    // Constants for default settings
    private static final float BOUNDING_BOX_HEIGHT = 0.75f;
    private static final float BOUNDING_BOX_WIDTH = 0.700f;
    private static final float BOUNDING_BOX_Y_OFFSET = 0.2f;
    private static final float BOUNDING_BOX_X_OFFSET = 0.3f;
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

    // Bomb Factory reference
    private final BombFactory bombFactory;

    // Position related
    private Coord coordinate; // Starting character coordinate
    // Indicates where the character is looking
    private Direction facingDirection = Direction.DOWN; // Starting character facingDirection
    private boolean stationary = true;

    // Hit box of the Character
    private BoundingBox bBox; // Solid area of the character

    // Game attribute related
    private boolean isAlive = true;
    private int numBomb = STARTING_BOMBS;
    private int flameRange = STARTING_FLAME_RANGE;
    private float speed = STARTING_SPEED;
    private Optional<PowerUpType> bombType = Optional.empty();
    private boolean lineBomb;   // False by default
    private final Deque<BasicBomb> bombQueue = new ArrayDeque<>();

    public Deque<BasicBomb> getBombQueue() {
        return new ArrayDeque<>(bombQueue);
    }

    // Update related
    private boolean hasToPlaceBomb;
    private boolean hasToPlaceLineBomb;
    private boolean hasToExplodeRemoteBomb;

    // Skull effects
    private boolean constipation; // The character is unable to lay down bombs
    private boolean butterfingers; // The character's hand becomes slippery. The character rapidly lays down bombs

    // Skull manager
    private long skeletonEffectDuration; // Indicates the duration of the skull effect
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
        this.bBox = new RectangleBoundingBox(new Point2D.Float(0, 0), BOUNDING_BOX_WIDTH, BOUNDING_BOX_HEIGHT);
    }

    /**
     * Updates the character's state.
     * This method should be implemented by subclasses to define character-specific
     * behavior.
     * 
     * @param elapsedTime the time elapsed since the last update
     */
    public abstract void update(final long elapsedTime);

    /**
     * Updates the skeleton's effects.
     * 
     * @param elapsedTime the time elapsed since the last update
     */
    public void updateSkeleton(final long elapsedTime) {
        if (this.skeletonEffectDuration > 0) { // Continues until the duration reaches zero
            this.skeletonEffectDuration -= elapsedTime;
            if (this.skeletonEffectDuration <= 0) { // When the effect ends the character's stats get resetted
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
     * @return bounding box of the character
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
        return placeBombImpl(this.bombFactory.CreateBomb(this, coordinate));
    }

    private boolean placeBombImpl(final BasicBomb bomb) {
        if (hasBombsLeft() && !this.constipation && this.manager
                .addBomb(bomb)) {
            this.numBomb--;
            bombQueue.addLast(bomb);
            return true;
        }
        return false;
    }

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

    /**
     * Explodes the first Remote Bomb placed by the character if present.
     */
    public void explodeRemoteBomb() {
        if (hasPlacedRemoteBomb()) { // Checks if there's a remote bomb to explode.
            // Finds the first remote bomb occurrence.
            final BasicBomb remoteBomb = bombQueue.stream()
                    .filter(bomb -> bomb.getBombType().equals(BombType.BOMB_REMOTE))
                    .findFirst()
                    .get();
            remoteBomb.update(true); // Initialises the explosion process.
            removeBombFromDeque(remoteBomb); // Removes the bomb from the deque.
            // System.out.println("exploded remote bomb\n\n");
        }
    }

    /**
     * Removes the exploded bomb if present in the deque.
     * 
     * @param explodedBomb the exploded bomb that needs to be removed
     */
    public void removeBombFromDeque(final BasicBomb explodedBomb) {
        if (!bombQueue.isEmpty()) {
            // System.out.println("removed bomb\n\n");
            bombQueue.removeFirstOccurrence(explodedBomb);
        }
    }

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
     * @param hasToExplodeRemoteBomb true to cause the character to explode a remote
     *                               bomb,
     *                               false otherwise
     */
    public void setHasToExplodeRemoteBomb(final boolean hasToExplodeRemoteBomb) {
        // Checks if the character has the remote-bomb PowerUp
        if (bombType.equals(Optional.of(PowerUpType.REMOTE_BOMB))) {
            this.hasToExplodeRemoteBomb = hasToExplodeRemoteBomb;
        }
    }

    /*
     * Checks whether the character has placed a remote bomb or not.
     */
    private boolean hasPlacedRemoteBomb() {
        return bombQueue.stream()
                .anyMatch(bomb -> bomb.getBombType().equals(BombType.BOMB_REMOTE));
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
     * @param coordinates the new coordinates of the character
     */
    public void setCharacterPosition(final Coord coordinates) {
        this.coordinate = coordinates;
        bBox.move(new Point2D.Float(coordinates.x() - (float) BOUNDING_BOX_X_OFFSET, coordinates.y() - BOUNDING_BOX_Y_OFFSET));
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
        // Checks if the character has the line-bomb PowerUp
        if (hasLineBomb()) {
            this.hasToPlaceLineBomb = hasToPlaceLineBomb;
        }
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
     * Sets the skeleton effect's duration.
     * 
     * @param duration the duration of the skeleton effect
     */
    public void setSkeletonEffectDuration(final long duration) {
        this.skeletonEffectDuration = duration;
    }

    // TODO: write better javadoc
    /**
     * Gets the reset effect.
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
