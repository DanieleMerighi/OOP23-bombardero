package it.bombardero;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.bombardero.cell.powerup.api.PowerUp;
import it.unibo.bombardero.cell.powerup.api.PowerUpFactory;
import it.unibo.bombardero.cell.powerup.api.PowerUpType;
import it.unibo.bombardero.cell.powerup.impl.PowerUpFactoryImpl;
import it.unibo.bombardero.cell.powerup.impl.PowerUpImpl;
import it.unibo.bombardero.cell.powerup.impl.SkullEffect;
import it.unibo.bombardero.character.Character;
import it.unibo.bombardero.map.api.Pair;

/**
 * Unit tests for the {@link PowerUp} class.
 * <p>
 * This class contains tests to verify the behavior of the PowerUp, including
 * creation and effects.
 * </p>
 */
public class TestPowerUp {

    private static final int FPS = 60;
    private static final int SPAWN_INT_COORDINATES = 0;
    private static final int STANDARD_ELAPSED_TIME = 100;
    private static final int SECONDS_TO_MILLISECONDS = 1000;
    private MyGameManager manager;
    private final PowerUpFactory factory = new PowerUpFactoryImpl();
    private Pair powerUpSpawn;
    private PowerUp powerUP;

    /**
     * Sets up the test environment before each test.
     * Initializes the game manager and the spawn coordinates of the PowerUp.
     */
    @BeforeEach
    void setUp() {
        this.manager = new MyGameManager();
        powerUpSpawn = new Pair(SPAWN_INT_COORDINATES, SPAWN_INT_COORDINATES);
    }

    /**
     * Tests that the PowerUp gets created correctly.
     */
    @Test
    public void testCreating100PowerUp() {
        IntStream.range(0, 100)
            .forEach(n -> {
                this.manager = new MyGameManager(); // Reset character
                powerUP = factory.createPowerUp(powerUpSpawn);
                powerUP.applyEffect(this.manager.getPlayer());
        });
        if (this.manager.getPlayer().getResetEffect().isPresent()) {
            while (this.manager.getPlayer().getSkeletonEffectDuration() > 0) {
                this.manager.getPlayer().updateSkeleton(STANDARD_ELAPSED_TIME);
            }
        }
    }

    /**
     * Tests the skull effect.
     */
    @Test
    public void testRandomSkullEffect() {
        // Create skull powerup
        powerUP = new PowerUpImpl(PowerUpType.SKULL, powerUpSpawn, new SkullEffect());
        // Apply skull effect to the player
        powerUP.applyEffect(this.manager.getPlayer());

        assertEquals(SkullEffect.getEffectDurationInSeconds() * SECONDS_TO_MILLISECONDS,
            this.manager.getPlayer().getSkeletonEffectDuration());
        assertTrue(this.manager.getPlayer().getResetEffect().isPresent());

        while (this.manager.getPlayer().getSkeletonEffectDuration() > 0) {
            this.manager.getPlayer().updateSkeleton(STANDARD_ELAPSED_TIME);
        }

        // Asserts the skull duration is 0
        assertEquals(0L, this.manager.getPlayer().getSkeletonEffectDuration());
        // Asserts there is no Reset effect to run
        assertEquals(Optional.empty(), this.manager.getPlayer().getResetEffect());
        // Asserts the initial stats are resetted
        assertEquals(Character.getStartingSpeed(), this.manager.getPlayer().getSpeed());
        assertEquals(Character.getStartingFlameRange(), this.manager.getPlayer().getFlameRange());
    }
}
