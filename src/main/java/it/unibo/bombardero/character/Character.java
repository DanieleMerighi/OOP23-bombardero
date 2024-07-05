package it.unibo.bombardero.character;

import java.awt.geom.Point2D;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Optional;

import it.unibo.bombardero.cell.Bomb;
import it.unibo.bombardero.cell.Bomb.BombType;
import it.unibo.bombardero.cell.BombFactory;
import it.unibo.bombardero.cell.powerup.api.PowerUpType;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.GenPair;
import it.unibo.bombardero.physics.api.BoundingBox;
import it.unibo.bombardero.physics.impl.RectangleBoundingBox;

/**
 * Abstract class representing a character in the game.
 * Contains common properties and methods for characters.
 */
public abstract class Character {

    /**
     * Enum representing the type of the character.
     * <p>
     * There are two types of characters:
     * <ul>
     *   <li>{@link #PLAYER} - Represents a keyboard-controlled character.</li>
     *   <li>{@link #ENEMY} - Represents a character that operates autonomously.</li>
     * </ul>
     * </p>
     */
    public enum CharacterType {
        /**
         * Represents a keyboard-controlled character.
         */
        PLAYER,
        /**
         * Represents a character that operates autonomously.
         */
        ENEMY;
    }

    /**
     * The character's hitbox height.
     */
    public static final float BOUNDING_BOX_HEIGHT = 0.75f;
    /**
     * The character's hitbox width.
     */
    public static final float BOUNDING_BOX_WIDTH = 0.700f;
    /**
     * The starting speed of the character.
     */
    public static final float STARTING_SPEED = 0.05f;
    /**
     * The starting flame range of the character.
     */
    public static final int STARTING_FLAME_RANGE = 1;
    /**
     * The max speed of the character.
     */
    public static final float MAX_SPEED = 0.09f;
    /**
     * The max flame range of the character.
     */
    public static final int MAX_FLAME_RANGE = 8;

    private static final float BOUNDING_BOX_Y_OFFSET = 0.2f;
    private static final float BOUNDING_BOX_X_OFFSET = 0.3f;
    private static final float INCREASE_SPEED = 0.005f;
    private static final int STARTING_BOMBS = 1;
    private static final int MAX_BOMBS = 8;

    // Bomb Factory reference
    private final BombFactory bombFactory;

    // Position related
    private GenPair<Float, Float> coordinate; // Starting character coordinate
    // Indicates where the character is looking
    private Direction facingDirection = Direction.DOWN; // Starting character facingDirection
    private boolean stationary = true;

    // Hitbox of the Character
    private final BoundingBox bBox; // Solid area of the character

    // Game attribute related
    private boolean isAlive = true;
    private int numBomb = STARTING_BOMBS;
    private int flameRange = STARTING_FLAME_RANGE;

    private float speed = STARTING_SPEED;
    private Optional<PowerUpType> bombType = Optional.empty();
    private boolean lineBomb; // False by default

    private final Deque<Bomb> queuedBombs = new ArrayDeque<>();
    private final List<Bomb> bombsToBePlaced = new ArrayList<>();

    // Update related
    private boolean hasToPlaceBomb;
    private boolean hasToPlaceLineBomb;
    private boolean hasToExplodeRemoteBomb;

    // Skull effects
    private boolean constipation; // The character is unable to lay down bombs
    private boolean butterfingers; // The character's hand becomes slippery. The character rapidly lays down bombs

    // Skull manager
    private long skullEffectDuration; // Indicates the duration of the skull effect
    private Optional<Runnable> resetEffect = Optional.empty(); // Restores all stats modified by the skull

    /**
     * Constructs a new Character with the specified parameters.
     * 
     * @param coord         the initial coordinates where the character is spawned
     * @param bombFactory   the factory to create bombs
     */
    public Character(final GenPair<Float, Float> coord, final BombFactory bombFactory) {
        this.coordinate = coord;
        this.bombFactory = bombFactory;
        this.bBox = new RectangleBoundingBox(0, 0, Character.BOUNDING_BOX_WIDTH, Character.BOUNDING_BOX_HEIGHT);
    }

    /**
     * Updates the character's state.
     * This method should be implemented by subclasses to define character-specific
     * behavior.
     * 
     * @param manager       the game manager
     * @param elapsedTime   the time elapsed since the last update
     */
    public abstract void update(GameManager manager, long elapsedTime);

    /**
     * Updates the skull's effects.
     * 
     * @param manager       the game manager
     * @param elapsedTime   the time elapsed since the last update
     * @param type          the type of character
     */
    public void updateSkull(final GameManager manager, final long elapsedTime, final CharacterType type) {
        if (this.skullEffectDuration > 0) { // Continues until the duration reaches zero
            this.skullEffectDuration -= elapsedTime;
            if (this.skullEffectDuration <= 0) { // When the effect ends the character's stats get resetted
                setSkullEffectDuration(0);
                this.resetEffect.ifPresent(Runnable::run); // If there's a effect to reset, it runs the reset effect
                this.resetEffect = Optional.empty(); // Clear the reset effect after it has run
            }
            if (this.butterfingers) { // If the character has butterfingers, he places a bomb
                placeBomb(manager, type);
            }
        }
    }

    /**
     * Checks if the character is alive.
     * 
     * @return true if the character is alive, false otherwise
     */
    public boolean isAlive() {
        return this.isAlive;
    }

    /**
     * Kills the character, setting its alive status to false.
     */
    public void kill() {
        this.isAlive = false;
    }

    /**
     * Gets the integer coordinates of the character.
     * 
     * @return the map's corrisponding integer coordinates of the character
     */
    public GenPair<Integer, Integer> getIntCoordinate() {
        return new GenPair<Integer, Integer>((int) Math.floor(this.coordinate.x()),
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
     * @param manager   the game manager
     * @param type      the type of character
     * 
     * @return true if the character has placed the bomb, false otherwise
     */
    public boolean placeBomb(final GameManager manager, final CharacterType type) {
        return placeBombImpl(manager, createBomb(getIntCoordinate(), type));
    }

    /**
     * Places a bomb at the given coordinates if the character has bombs left.
     * 
     * @param manager   the game manager
     * @param coordinate the bomb's coordinate
     * 
     * @return true if the character has placed the bomb, false otherwise
     */
    public boolean placeBomb(final GameManager manager, final GenPair<Integer, Integer> coordinate) {
        return placeBombImpl(manager, createBomb(coordinate, CharacterType.PLAYER));
    }

    /**
     * Checks if the character has to place a bomb.
     * 
     * @return true if the character has to place a bomb, false otherwise
     */
    public boolean isHasToPlaceBomb() {
        return this.hasToPlaceBomb;
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
            final Bomb remoteBomb = queuedBombs.stream()
                    .filter(bomb -> bomb.getBombType().equals(BombType.BOMB_REMOTE))
                    .findFirst()
                    .get();
            remoteBomb.update(true); // Initialises the explosion process.
            removeBombFromDeque(remoteBomb); // Removes the bomb from the deque.
        }
    }

    /**
     * Removes the exploded bomb if present in the deque.
     * 
     * @param explodedBomb the exploded bomb that needs to be removed
     */
    public void removeBombFromDeque(final Bomb explodedBomb) {
        if (!queuedBombs.isEmpty() && queuedBombs.removeFirstOccurrence(explodedBomb)) {
            this.increaseNumBomb();

        }
    }

    /**
     * Gets the bomb deque.
     * 
     * @return the bomb deque
     */
    public Deque<Bomb> getQueuedBombs() {
        return new ArrayDeque<>(queuedBombs);
    }

    /**
     * Checks if the character has to explode a remote bomb.
     * 
     * @return true if the character has to explode a remote bomb, false otherwise
     */
    public boolean isHasToExplodeRemoteBomb() {
        return this.hasToExplodeRemoteBomb;
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
        return this.bombFactory;
    }

    /**
     * Gets the current float position of the character.
     * 
     * @return the character's float coordinates
     */
    public GenPair<Float, Float> getCharacterPosition() {
        return this.coordinate;
    }

    /**
     * Sets the current float position of the character.
     * 
     * @param coordinates the new coordinates of the character
     */
    public void setCharacterPosition(final GenPair<Float, Float> coordinates) {
        this.coordinate = coordinates;
        bBox.move(new Point2D.Float(coordinates.x() - (float) BOUNDING_BOX_X_OFFSET,
                coordinates.y() - BOUNDING_BOX_Y_OFFSET));
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
        return this.stationary;
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
        return this.numBomb;
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
        return this.flameRange;
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
        return this.speed;
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
        return this.bombType;
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
        return this.lineBomb;
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
    public boolean isHasToPlaceLineBomb() {
        return this.hasToPlaceLineBomb;
    }

    /**
     * Sets whether the character should place a line bomb.
     * 
     * @param hasToPlaceLineBomb true to cause the character to place a line bomb,
     *                           false otherwise
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
        return this.constipation;
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
        return this.butterfingers;
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
     * Gets the duration of the skull effect.
     * 
     * @return the skullEffectDuration
     */
    public long getSkullEffectDuration() {
        return this.skullEffectDuration;
    }

    /**
     * Sets the skull effect's duration.
     * 
     * @param duration the duration of the skull effect
     */
    public void setSkullEffectDuration(final long duration) {
        this.skullEffectDuration = duration;
    }

    // TODO: write better javadoc
    /**
     * Gets the reset effect used to reset the Character's stats after the skull effect ends.
     * 
     * @return the reset effect
     */
    public Optional<Runnable> getResetEffect() {
        return this.resetEffect;
    }

    /**
     * Sets the reset effect used to reset the Character's stats after the skull effect ends.
     * 
     * @param resetEffect the reset effect
     */
    public void setResetEffect(final Runnable resetEffect) {
        this.resetEffect = Optional.of(resetEffect);
    }

    /**
     * Gets the list of bombs pending to be placed.
     * 
     * @return the list of bombs
     */
    public List<Bomb> getBombsToBePlaced() {
        return List.copyOf(this.bombsToBePlaced);
    }

    /**
     * Resets the list of bombs pending to be placed.
     * 
     */
    public void resetBombsList() {
        this.bombsToBePlaced.clear();
    }

    /**
     * General implementation of the placeBomb method.
     * 
     * @param manager   the game manager
     * @param bomb      the bomb that needs to be placed
     * @return          true if the bomb was placed
     */
    private boolean placeBombImpl(final GameManager manager, final Bomb bomb) {
        if (hasBombsLeft() && !this.constipation && canPlaceBomb(manager, bomb.getPos())) {
            this.numBomb--;
            bombsToBePlaced.add(bomb);
            queuedBombs.addLast(bomb);
            return true;
        }
        return false;
    }

    /**
     * Checks whether the character can place a bomb at the given coordinates.
     * 
     * @param manager       the manager
     * @param coordinate    the bomb's coordinates
     * 
     * @return true if he can place the bomb at the given coordinates.
     */
    private boolean canPlaceBomb(final GameManager manager, final GenPair<Integer, Integer> coordinate) {
        return manager.getGameMap().isEmpty(coordinate);
    }

    /**
     * Checks whether the character has placed a remote bomb or not.
     * 
     * @return true if he has to
     */
    private boolean hasPlacedRemoteBomb() {
        return this.queuedBombs.stream()
                .anyMatch(bomb -> bomb.getBombType().equals(BombType.BOMB_REMOTE));
    }

    /**
     * Creates a bomb based on the type of bomb and the type of character.
     * 
     * @param coordinate    the coordinate of the bomb
     * @param type          the type of character who creates it
     * 
     * @return              the bomb created
     */
    private Bomb createBomb(final GenPair<Integer, Integer> coordinate, final CharacterType type) {
        if (!getBombType().isPresent()) {
            return this.bombFactory.createBasicBomb(this.getFlameRange(), coordinate);
        }
        switch (this.getBombType().get()) {
            case PIERCING_BOMB:
                return this.bombFactory.createPiercingBomb(this.getFlameRange(), coordinate);
            case REMOTE_BOMB:
                return type.equals(CharacterType.ENEMY) ? this.bombFactory.createBasicBomb(this.getFlameRange(), coordinate)
                        : this.bombFactory.createRemoteBomb(this.getFlameRange(), coordinate);
            case POWER_BOMB:
                return this.bombFactory.createPowerBomb(coordinate);
            default:
                return null;
        }
    }
}
