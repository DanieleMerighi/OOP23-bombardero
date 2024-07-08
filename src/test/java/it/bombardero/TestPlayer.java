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

    private int playerIndex;

    private GenPair<Float, Float> spawnCoord;
    private GenPair<Float, Float> expectedCoord;

    /**
     * Sets up the test environment before each test.
     * Initializes the game manager, spawn coordinates, and player position.
     */
    @BeforeEach
    void setUp() {
        this.manager = new MyGameManager();
        this.playerIndex = 0;
        this.spawnCoord  = new GenPair<>(STARTING_COORD, STARTING_COORD);
        this.expectedCoord = new GenPair<>(STARTING_COORD, STARTING_COORD);
        this.manager.getPlayers().get(playerIndex).setCharacterPosition(spawnCoord);
    }

    /**
     * Tests that the player correctly faces each direction.
     */
    @Test
    void testPlayerLookingDirections() {
        this.manager.getPlayers().get(playerIndex).setCharacterPosition(spawnCoord);
        for (final Direction dir : Direction.values()) {
            this.manager.getPlayers().get(playerIndex).setFacingDirection(dir);
            this.manager.getPlayers().get(playerIndex).update(manager, STANDARD_ELAPSED_TIME);
            assertEquals(dir, manager.getPlayers().get(playerIndex).getFacingDirection());
        }
    }

    /**
     * Tests the player's movement in the right direction over a series of updates.
     */
    @Test
    void testPlayerMovingDirections() {
        // Setting player direction
        this.manager.getPlayers().get(playerIndex).setFacingDirection(Direction.RIGHT);
        // Setting player to not stationary
        this.manager.getPlayers().get(playerIndex).setStationary(false);
        // Setting player speed
        this.manager.getPlayers().get(playerIndex).setSpeed(Character.STARTING_SPEED);

        // Setting the number of update and calling them
        final int updateNumeber = FPS; // Number of updates done
        IntStream.range(0, updateNumeber).forEach(n -> this.manager.getPlayers().get(playerIndex).update(manager, STANDARD_ELAPSED_TIME));

        roundPlayerCoordinateToThreeDecimal();
        // Sums the spawn coordinates with the movement done
        expectedCoord = expectedCoord.apply(Functions.sumFloat(calculateExpectedDeltaMovement(updateNumeber)));

        assertEquals(expectedCoord, manager.getPlayers().get(playerIndex).getCharacterPosition());
     }

    /**
     * Calculates the expected delta movement of the player based on the number of updates.
     *
     * @param updateNumeber the number of updates
     * @return the expected delta movement as a {@link GenPair<Float, Float>}
     */
    private GenPair<Float, Float> calculateExpectedDeltaMovement(final int updateNumeber) {
        return new GenPair<Float, Float>(
                this.manager.getPlayers().get(playerIndex).getSpeed() * this.manager.getPlayers().get(playerIndex).getFacingDirection().x()
                        * updateNumeber,
                this.manager.getPlayers().get(playerIndex).getSpeed() * this.manager.getPlayers().get(playerIndex).getFacingDirection().y()
                        * updateNumeber);
    }

    /**
     * Rounds the player's coordinates to three decimal places for comparison.
     */
    private void roundPlayerCoordinateToThreeDecimal() {
        this.manager.getPlayers().get(playerIndex).setCharacterPosition(
            new GenPair<Float, Float>(
                Math.round(this.manager.getPlayers().get(playerIndex).getCharacterPosition().x() * 1000.0f) / 1000.0f,
                Math.round(this.manager.getPlayers().get(playerIndex).getCharacterPosition().y() * 1000.0f) / 1000.0f
            ));
    }
}
