package it.bombardero;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.bombardero.cell.Bomb;
import it.unibo.bombardero.cell.BombFactoryImpl;
import it.unibo.bombardero.cell.Flame.FlameType;
import it.unibo.bombardero.character.Character;
import it.unibo.bombardero.character.Enemy;
import it.unibo.bombardero.character.Player;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.Coord;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.map.impl.GameMapImpl;
import it.unibo.bombardero.physics.api.BoundingBox;

import java.util.List;
import java.util.Optional;
import java.util.Arrays;

/**
 * Test class for Enemy behavior testing.
 */
public class TestEnemy {

    private static final int STANDARD_ELAPSED_TIME = 100;
    private static final int STARTING_BOMBS = 1;

    private TestGameManager manager;

    /**
     * Setup method executed before each test.
     */
    @BeforeEach
    public void setUp() {
        this.manager = new TestGameManager();
        this.manager.setEnemyCoord(0, 0);
        this.manager.enemy.setSpeed(0.01f);
    }

    /**
     * Test case: Enemy moves randomly when player is not in detection radius.
     */
    @Test
    public void testEnemyPatrolPlayerNotInDetectionRadiusMovesRandomly() {
        // outside ENEMY_DETECTION_RADIUS
        manager.setPlayerCoord(0, 5);
        manager.enemy.update(STANDARD_ELAPSED_TIME);

        // Verify enemy state is PATROL
        assertEquals(Enemy.State.PATROL, manager.enemy.getState());
        // We can't directly verify moveRandomly is called, but we can check if nextMove
        // is set through random movement
        assertTrue(manager.enemy.getNextMove().isPresent());
    }

    /**
     * Test case: Enemy changes to CHASE state when player is in detection radius.
     */
    @Test
    public void testEnemyPatrolPlayerInDetectionRadiusChangesToChaseState() {
        // Set player position within detection radius in TestGameManager
        manager.setPlayerCoord(0, 4);
        manager.updateGame(STANDARD_ELAPSED_TIME);

        // Verify enemy state is CHASE
        assertEquals(Enemy.State.CHASE, manager.enemy.getState());
    }

    /**
     * Test case: Enemy changes back to PATROL state when losing the player.
     */
    @Test
    public void testEnemyChaseLosesPlayerChangesToPatrolState() {
        // Set initial player position within detection radius in TestGameManager
        manager.setPlayerCoord(2, 1);
        manager.enemy.setNumBomb(0);
        // We need more than 1 sec to move between cells
        manager.updateGame(STANDARD_ELAPSED_TIME);

        // Verify enemy state is CHASE
        assertEquals(Enemy.State.CHASE, manager.enemy.getState());
        assertEquals(new Pair(1, 0), manager.enemy.getIntCoordinate());

        // Set player moving away after initial detection
        manager.setPlayerCoord(3, 12);
        manager.updateGame(STANDARD_ELAPSED_TIME);

        // Verify enemy state is PATROL
        assertEquals(Enemy.State.PATROL, manager.enemy.getState());
    }

    /**
     * Test case: Enemy escapes and then returns to PATROL state.
     */
    @Test
    public void testEnemyEscapeChangesToPatrol() {
        // Set enemy position inside a danger zone
        this.manager.enemy.setSpeed(0.05f);
        this.manager.addBomb(new MyBomb(new Pair(0, 1)));
        this.manager.enemy.update(STANDARD_ELAPSED_TIME);

        assertEquals(Enemy.State.ESCAPE, this.manager.enemy.getState());
        this.manager.updateGame(STANDARD_ELAPSED_TIME);
        assertEquals(new Pair(1, 0), manager.enemy.getIntCoordinate());
        this.manager.updateGame(STANDARD_ELAPSED_TIME);
        // Verify enemy state is Patrol
        assertEquals(Enemy.State.PATROL, manager.enemy.getState());

    }

    /**
     * Test case: Enemy places bomb when next to a breakable wall.
     */
    @Test
    public void testEnemyPatrolBreakableWallNextToEnemyPlacesBomb() {
        // Set enemy next to a breakable wall
        manager.setPlayerCoord(1, 2);
        manager.enemy.setNumBomb(STARTING_BOMBS); // 1 bomb added to enemy
        manager.getGameMap().addBreakableWall(new Pair(0, 1));
        manager.enemy.update(STANDARD_ELAPSED_TIME);

        assertEquals(Enemy.State.CHASE, manager.enemy.getState());
        manager.updateGame(STANDARD_ELAPSED_TIME);
        manager.updateGame(STANDARD_ELAPSED_TIME);
        // Verify bomb is placed on the enemy's position
        assertEquals(STARTING_BOMBS - 1, manager.enemy.getNumBomb());
    }

    @SuppressWarnings("CPD-START")

    /**
     * is a class for simulating some aspects of the GameManager
     */
    private static class TestGameManager implements GameManager {

        private Player player;
        private Enemy enemy;
        private GameMap map;

        TestGameManager() {
            this.map = new GameMapImpl(false);
            this.enemy = new Enemy(this, new Coord(0, 0), new BombFactoryImpl(this));
            this.player = new Player(this, new Coord(0, 12), new BombFactoryImpl(this));
        }

        public void setPlayerCoord(final int x, final int y) {
            player.setCharacterPosition(new Coord(x + 0.5f, y + 0.5f));
        }

        public void setEnemyCoord(final int x, final int y) {
            enemy.setCharacterPosition(new Coord(x + 0.5f, y + 0.5f));
        }

        @Override
        public Player getPlayer() {
            return player;
        }

        @Override
        public void updateGame(final long elapsed) {
            // 60 fps
            for (int i = 0; i < 59; i++) {
                enemy.update(STANDARD_ELAPSED_TIME);
            }
            enemy.update(STANDARD_ELAPSED_TIME);
        }

        @Override
        public void endGame() {
            throw new UnsupportedOperationException("Unimplemented method 'endGame'");
        }

        @Override
        public List<Character> getEnemies() {
            return Arrays.asList(enemy);
        }

        @Override
        public GameMap getGameMap() {
            return map;
        }

        @Override
        public boolean addBomb(final Bomb b) {
            return map.addBomb(b, b.getPos());
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
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'getTimeLeft'");
        }

        @Override
        public void removePowerUp(final Pair pos) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'removePowerUp'");
        }

        @Override
        public Optional<Bomb> getBomb(final Pair pos) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'getBomb'");
        }
    }

    private static class MyBomb implements Bomb {

        private Pair pos;

        MyBomb(final Pair pos) {
            this.pos = pos;
        }

        @Override
        public boolean getBoundingCollision() {
            throw new UnsupportedOperationException("Unimplemented method 'getBoundingCollision'");
        }

        @Override
        public CellType getCellType() {
            return CellType.BOMB;
        }

        @Override
        public BoundingBox getBoundingBox() {
            throw new UnsupportedOperationException("Unimplemented method 'getBoundingBox'");
        }

        @Override
        public boolean isExploded() {
            throw new UnsupportedOperationException("Unimplemented method 'isExploded'");
        }

        @Override
        public void update(final boolean condition) {
            throw new UnsupportedOperationException("Unimplemented method 'update'");
        }

        @Override
        public void update() {
            throw new UnsupportedOperationException("Unimplemented method 'update'");
        }

        @Override
        public BombType getBombType() {
            throw new UnsupportedOperationException("Unimplemented method 'getBombType'");
        }

        @Override
        public int getRange() {
            throw new UnsupportedOperationException("Unimplemented method 'getRange'");
        }

        @Override
        public Pair getPos() {
            return this.pos;
        }
    }

}
