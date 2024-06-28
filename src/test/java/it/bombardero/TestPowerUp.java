package it.bombardero;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.bombardero.cell.Bomb;
import it.unibo.bombardero.cell.BombFactoryImpl;
import it.unibo.bombardero.cell.Flame.FlameType;
import it.unibo.bombardero.cell.powerup.api.PowerUp;
import it.unibo.bombardero.cell.powerup.api.PowerUpFactory;
import it.unibo.bombardero.cell.powerup.api.PowerUpType;
import it.unibo.bombardero.cell.powerup.impl.PowerUpFactoryImpl;
import it.unibo.bombardero.cell.powerup.impl.PowerUpImpl;
import it.unibo.bombardero.cell.powerup.impl.SkullEffect;
import it.unibo.bombardero.character.Character;
import it.unibo.bombardero.character.Player;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.Coord;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.map.impl.GameMapImpl;

import java.util.List;
import java.util.stream.IntStream;
import java.util.Optional;


public class TestPowerUp {

    private static final int FPS = 60;
    private static final int SPAWN_INT_COORDINATES = 0;
    private static final int STANDARD_ELAPSED_TIME = 100;
    private static final int SECONDS_TO_MILLISECONDS = 1000;
    private TestGameManager manager;
    private final PowerUpFactory factory = new PowerUpFactoryImpl();
    private Pair powerUpSpawn;
    private PowerUp powerUP;

    @BeforeEach
    void setUp() {
        this.manager = new TestGameManager();
        powerUpSpawn = new Pair(SPAWN_INT_COORDINATES, SPAWN_INT_COORDINATES);
    }

    @Test
    public void testCreating100PowerUp() {
        IntStream.range(0, 100)
            .forEach(n -> {
                this.manager = new TestGameManager(); // Reset character
                powerUP = factory.createPowerUp(powerUpSpawn);
                powerUP.applyEffect(this.manager.getPlayer());
        });
        if (this.manager.getPlayer().getResetEffect().isPresent()) {
            while (this.manager.getPlayer().getSkeletonEffectDuration() > 0) {
                this.manager.getPlayer().updateSkeleton(STANDARD_ELAPSED_TIME);
            }
        }
    }

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
        assertEquals(0l, this.manager.getPlayer().getSkeletonEffectDuration());
        // Asserts there is no Reset effect to run
        assertEquals(Optional.empty(), this.manager.getPlayer().getResetEffect());
        // Asserts the initial stats are resetted
        assertEquals(Character.getStartingSpeed(), this.manager.getPlayer().getSpeed());
        assertEquals(Character.getStartingFlameRange(), this.manager.getPlayer().getFlameRange());
    }

    @SuppressWarnings("CPD-START")

    private static class TestGameManager implements GameManager {

        private Character character;
        private GameMap map;

        public TestGameManager() {
            this.map = new GameMapImpl(false);
            this.character = new Player(this, new Coord(0, 0), new BombFactoryImpl(this));
        }

        @Override
        public Character getPlayer() {
            return this.character;
        }

        @Override
        public void updateGame(long elapsed) {
        }

        @Override
        public void endGame() {
            throw new UnsupportedOperationException("Unimplemented method 'endGame'");
        }

        @Override
        public List<Character> getEnemies() {
            throw new UnsupportedOperationException("Unimplemented method 'getEnemies'");
        }

        @Override
        public GameMap getGameMap() {
            return this.map;
        }

        @Override
        public boolean addBomb(Bomb bomb) {
            throw new UnsupportedOperationException("Unimplemented method 'addBomb'");
        }

        @Override
        public void removeBomb(Pair pos) {
            throw new UnsupportedOperationException("Unimplemented method 'removeBomb'");
        }

        @Override
        public void addFlame(FlameType type, Pair pos) {
            throw new UnsupportedOperationException("Unimplemented method 'addFlame'");
        }

        @Override
        public void removeFlame(Pair pos) {
            throw new UnsupportedOperationException("Unimplemented method 'removeFlame'");
        }

        @Override
        public boolean removeWall(Pair pos) {
            throw new UnsupportedOperationException("Unimplemented method 'removeWall'");
        }

        @Override
        public long getTimeLeft() {
            throw new UnsupportedOperationException("Unimplemented method 'getTimeLeft'");
        }

        @Override
        public void removePowerUp(Pair pos) {
            throw new UnsupportedOperationException("Unimplemented method 'removePowerUp'");
        }

        @Override
        public Optional<Bomb> getBomb(Pair pos) {
            throw new UnsupportedOperationException("Unimplemented method 'getBomb'");
        }
    }

}
