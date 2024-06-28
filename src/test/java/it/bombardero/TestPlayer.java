package it.bombardero;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.bombardero.cell.Bomb;
import it.unibo.bombardero.cell.BombFactoryImpl;
import it.unibo.bombardero.cell.Flame.FlameType;
import it.unibo.bombardero.character.Character;
import it.unibo.bombardero.character.Direction;
import it.unibo.bombardero.character.Player;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.Coord;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.map.impl.GameMapImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * Unit tests for the {@link Player} class.
 * <p>
 * This class contains tests to verify the behavior of the Player character, including
 * direction facing and movement mechanics.
 * </p>
 */
public class TestPlayer {

    private static final int FPS = 60;
    private static final float STARTING_COORD = 5.0f;
    private static final int STANDARD_ELAPSED_TIME = 100;
    private TestGameManager manager;

    private float spawnX;
    private float spawnY;
    private Coord spawnCoord;
    private Coord expectedCoord;

    /**
     * Sets up the test environment before each test.
     * Initializes the game manager, spawn coordinates, and player position.
     */
    @BeforeEach
    void setUp() {
        this.manager = new TestGameManager();
        spawnX = STARTING_COORD;
        spawnY = STARTING_COORD;
        spawnCoord  = new Coord(spawnX, spawnY);
        expectedCoord = new Coord(spawnX, spawnY);
        this.manager.getPlayer().setCharacterPosition(spawnCoord);
    }

    /**
     * Tests that the player correctly faces each direction.
     */
    @Test
    public void testPlayerLookingDirections() {
        this.manager.getPlayer().setCharacterPosition(spawnCoord);
        for (final Direction dir : Direction.values()) {
            this.manager.getPlayer().setFacingDirection(dir);
            this.manager.getPlayer().update(STANDARD_ELAPSED_TIME);
            assertEquals(dir, manager.getPlayer().getFacingDirection());
        }
    }

    /**
     * Tests the player's movement in the right direction over a series of updates.
     */
    @Test
    public void testPlayerMovingDirections() {
        // Setting player direction
        this.manager.getPlayer().setFacingDirection(Direction.RIGHT);
        // Setting player to not stationary
        this.manager.getPlayer().setStationary(false);
        // Setting player speed
        this.manager.getPlayer().setSpeed(Character.getStartingSpeed());

        // Setting the number of update and calling them
        final int updateNumeber = FPS; // Number of updates done
        IntStream.range(0, updateNumeber).forEach(n -> this.manager.getPlayer().update(STANDARD_ELAPSED_TIME));
        
        roundPlayerCoordinateToThreeDecimal();
        // Sums the spawn coordinates with the movement done
        expectedCoord = expectedCoord.sum(calculateExpectedDeltaMovement(updateNumeber));

        assertEquals(expectedCoord, manager.getPlayer().getCharacterPosition());
     }

    /**
     * Calculates the expected delta movement of the player based on the number of updates.
     *
     * @param updateNumeber the number of updates
     * @return the expected delta movement as a {@link Coord}
     */
    private Coord calculateExpectedDeltaMovement(final int updateNumeber) {
        return new Coord(
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
            new Coord(
                Math.round(this.manager.getPlayer().getCharacterPosition().x() * 1000.0f) / 1000.0f,
                Math.round(this.manager.getPlayer().getCharacterPosition().y() * 1000.0f) / 1000.0f
            ));
    }

    @SuppressWarnings("CPD-START")

    /**
     * A test implementation of the {@link GameManager} interface for use in tests.
     */
    private static class TestGameManager implements GameManager {

        private Player player;
        private final GameMap map;

        /**
         * Constructs a new {@link TestGameManager}.
         * Initializes the game map and player.
         */
        TestGameManager() {
            this.map = new GameMapImpl(false);
            this.player = new Player(this, new Coord(STARTING_COORD, STARTING_COORD), new BombFactoryImpl(this));
        }

        @Override
        public Player getPlayer() {
            return this.player;
        }

        @Override
        public void updateGame(final long elapsed) {
            throw new UnsupportedOperationException("Unimplemented method 'updateGame'");
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
        public boolean addBomb(final Bomb bomb) {
            throw new UnsupportedOperationException("Unimplemented method 'addBomb'");
        }

        @Override
        public void removeBomb(final Pair pos) {
            throw new UnsupportedOperationException("Unimplemented method 'removeBomb'");
        }

        @Override
        public void addFlame(final FlameType type, final Pair pos) {
            throw new UnsupportedOperationException("Unimplemented method 'addFlame'");
        }

        @Override
        public void removeFlame(final Pair pos) {
            throw new UnsupportedOperationException("Unimplemented method 'removeFlame'");
        }

        @Override
        public boolean removeWall(final Pair pos) {
            throw new UnsupportedOperationException("Unimplemented method 'removeWall'");
        }

        @Override
        public long getTimeLeft() {
            throw new UnsupportedOperationException("Unimplemented method 'getTimeLeft'");
        }

        @Override
        public void removePowerUp(final Pair pos) {
            throw new UnsupportedOperationException("Unimplemented method 'removePowerUp'");
        }

        @Override
        public Optional<Bomb> getBomb(final Pair pos) {
            throw new UnsupportedOperationException("Unimplemented method 'getBomb'");
        }
    }

}
