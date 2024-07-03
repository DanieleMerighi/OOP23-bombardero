package it.bombardero;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.bombardero.character.Character;
import it.unibo.bombardero.character.Direction;
import it.unibo.bombardero.character.Player;
import it.unibo.bombardero.map.api.Functions;
import it.unibo.bombardero.map.api.GenPair;

/**
 * Unit tests for the {@link Player} class.
 * <p>
 * This class contains tests to verify the behavior of the Player character, including
 * direction facing and movement mechanics.
 * </p>
 */
class TestPlayer {

    private static final int FPS = 60;
    private static final float STARTING_COORD = 5.0f;
    private static final int STANDARD_ELAPSED_TIME = 100;
    private MyGameManager manager;

    private float spawnX;
    private float spawnY;
    private GenPair<Float, Float> spawnCoord;
    private GenPair<Float, Float> expectedCoord;

    /**
     * Sets up the test environment before each test.
     * Initializes the game manager, spawn coordinates, and player position.
     */
    @BeforeEach
    void setUp() {
        this.manager = new MyGameManager();
        spawnX = STARTING_COORD;
        spawnY = STARTING_COORD;
        spawnCoord  = new GenPair<Float, Float>(spawnX, spawnY);
        expectedCoord = new GenPair<Float, Float>(spawnX, spawnY);
        this.manager.getPlayer().setCharacterPosition(spawnCoord);
    }

    /**
     * Tests that the player correctly faces each direction.
     */
    @Test
    void testPlayerLookingDirections() {
        this.manager.getPlayer().setCharacterPosition(spawnCoord);
        for (final Direction dir : Direction.values()) {
            this.manager.getPlayer().setFacingDirection(dir);
            this.manager.getPlayer().update(STANDARD_ELAPSED_TIME, manager);
            assertEquals(dir, manager.getPlayer().getFacingDirection());
        }
    }

    /**
     * Tests the player's movement in the right direction over a series of updates.
     */
    @Test
    void testPlayerMovingDirections() {
        // Setting player direction
        this.manager.getPlayer().setFacingDirection(Direction.RIGHT);
        // Setting player to not stationary
        this.manager.getPlayer().setStationary(false);
        // Setting player speed
        this.manager.getPlayer().setSpeed(Character.getStartingSpeed());

        // Setting the number of update and calling them
        final int updateNumeber = FPS; // Number of updates done
        IntStream.range(0, updateNumeber).forEach(n -> this.manager.getPlayer().update(STANDARD_ELAPSED_TIME, manager));

        roundPlayerCoordinateToThreeDecimal();
        // Sums the spawn coordinates with the movement done
        expectedCoord = expectedCoord.apply(Functions.sumFloat(calculateExpectedDeltaMovement(updateNumeber)));

        assertEquals(expectedCoord, manager.getPlayer().getCharacterPosition());
     }

    /**
     * Calculates the expected delta movement of the player based on the number of updates.
     *
     * @param updateNumeber the number of updates
     * @return the expected delta movement as a {@link GenPair<Float, Float>}
     */
    private GenPair<Float, Float> calculateExpectedDeltaMovement(final int updateNumeber) {
        return new GenPair<Float, Float>(
                this.manager.getPlayer().getSpeed() * this.manager.getPlayer().getFacingDirection().x()
                        * updateNumeber,
                this.manager.getPlayer().getSpeed() * this.manager.getPlayer().getFacingDirection().y()
                        * updateNumeber);
    }

    /**
     * Rounds the player's coordinates to three decimal places for comparison.
     */
    private void roundPlayerCoordinateToThreeDecimal() {
        this.manager.getPlayer().setCharacterPosition(
            new GenPair<Float, Float>(
                Math.round(this.manager.getPlayer().getCharacterPosition().x() * 1000.0f) / 1000.0f,
                Math.round(this.manager.getPlayer().getCharacterPosition().y() * 1000.0f) / 1000.0f
            ));
    }
}
