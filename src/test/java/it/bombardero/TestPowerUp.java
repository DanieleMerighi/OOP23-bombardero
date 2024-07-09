package it.bombardero;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.bombardero.cell.powerup.api.PowerUp;
import it.unibo.bombardero.cell.powerup.api.PowerUp.PowerUpType;
import it.unibo.bombardero.cell.powerup.api.PowerUpFactory;
import it.unibo.bombardero.cell.powerup.impl.PowerUpFactoryImpl;
import it.unibo.bombardero.cell.powerup.impl.PowerUpImpl;
import it.unibo.bombardero.cell.powerup.impl.SkullEffect;
import it.unibo.bombardero.character.Character;
import it.unibo.bombardero.character.Character.CharacterType;

/**
 * Unit tests for the {@link PowerUp} class.
 * <p>
 * This class contains tests to verify the behavior of the PowerUp, including
 * creation and effects.
 * </p>
 */
class TestPowerUp {

    private static final int STANDARD_ELAPSED_TIME = 100;
    private static final long SECONDS_TO_MILLISECONDS = 1000;
    private MyGameManager manager;
    private int playerIndex;
    private final PowerUpFactory factory = new PowerUpFactoryImpl();
    private PowerUp powerUP;

    /**
     * Sets up the test environment before each test.
     * Initializes the game manager and the spawn coordinates of the PowerUp.
     */
    @BeforeEach
    void setUp() {
        this.manager = new MyGameManager();
        this.playerIndex = 0;
    }

    /**
     * Tests that the PowerUp gets created correctly.
     */
    @Test
    void testPowerUpCreation() {
        powerUP = factory.createPowerUp();
        assertNotNull(powerUP);

        powerUP = factory.createPowerUp(PowerUpType.PLUS_ONE_BOMB);
        assertNotNull(powerUP);
        assertEquals(PowerUpType.PLUS_ONE_BOMB, powerUP.getType());

    }

    /**
     * Tests the skull effect.
     */
    @Test
    void testRandomSkullEffect() {
        // Create skull powerup
        powerUP = new PowerUpImpl(PowerUpType.SKULL, new SkullEffect());
        // Apply skull effect to the player
        powerUP.applyEffect(this.manager.getPlayers().get(playerIndex));

        assertEquals(SkullEffect.EFFECT_DURATION_IN_SECONDS * SECONDS_TO_MILLISECONDS,
            this.manager.getPlayers().get(playerIndex).getSkullEffectDuration());
        assertTrue(this.manager.getPlayers().get(playerIndex).getResetEffect().isPresent());

        while (this.manager.getPlayers().get(playerIndex).getSkullEffectDuration() > 0) {
            this.manager.getPlayers().get(playerIndex).update(manager, STANDARD_ELAPSED_TIME, CharacterType.PLAYER);
        }

        // Asserts the skull duration is 0
        assertEquals(0L, this.manager.getPlayers().get(playerIndex).getSkullEffectDuration());
        // Asserts there is no Reset effect to run
        assertEquals(Optional.empty(), this.manager.getPlayers().get(playerIndex).getResetEffect());
        // Asserts the initial stats are resetted
        assertEquals(Character.STARTING_SPEED, this.manager.getPlayers().get(playerIndex).getSpeed());
        assertEquals(Character.STARTING_FLAME_RANGE, this.manager.getPlayers().get(playerIndex).getFlameRange());
    }
}
