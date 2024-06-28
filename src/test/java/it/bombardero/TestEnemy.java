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

public class TestEnemy {

    private static final int STANDARD_ELAPSED_TIME = 100;
    private static final int STARTING_BOMBS = 1;

    private TestGameManager manager;

    @BeforeEach
    void setUp() {
        this.manager = new TestGameManager();
        this.manager.setEnemyCoord(0, 0);
        this.manager.enemy.setSpeed(0.01f);
    }

    @Test
    public void testEnemyPatrol_PlayerNotInDetectionRadius_MovesRandomly() {
        // outside ENEMY_DETECTION_RADIUS
        manager.setPlayerCoord(0, 5);
        manager.enemy.update(STANDARD_ELAPSED_TIME);

        // Verify enemy state is PATROL
        assertEquals(Enemy.State.PATROL, manager.enemy.getState());
        // We can't directly verify moveRandomly is called, but we can check if nextMove
        // is set through random movement
        assertTrue(manager.enemy.getNextMove().isPresent());
    }

    @Test
    public void testEnemyPatrol_PlayerInDetectionRadius_ChangesToChaseState() {
        // Set player position within detection radius in TestGameManager
        manager.setPlayerCoord(0, 4);
        manager.updateGame(STANDARD_ELAPSED_TIME);

        // Verify enemy state is CHASE
        assertEquals(Enemy.State.CHASE, manager.enemy.getState());
    }

    @Test
    public void testEnemyChase_LosesPlayer_ChangesToPatrolState() {
        // Set initial player position within detection radius in TestGameManager
        manager.setPlayerCoord(0, 4);
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

    @Test
    public void testEnemyEscape_ChangesToPatrol() {
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

    @Test
    public void testEnemyPatrol_BreakableWallNextToEnemy_PlacesBomb() {
        // Set enemy next to a breakable wall
        manager.setPlayerCoord(0, 2);
        manager.enemy.setNumBomb(STARTING_BOMBS); // 1 bomb added to enemy
        manager.getGameMap().addBreakableWall(new Pair(0, 1));
        manager.enemy.update(STANDARD_ELAPSED_TIME);

        assertEquals(Enemy.State.CHASE, manager.enemy.getState());
        manager.updateGame(STANDARD_ELAPSED_TIME);
        manager.updateGame(STANDARD_ELAPSED_TIME);
        // Verify bomb is placed on the enemy's position 
        assertEquals(STARTING_BOMBS-1, manager.enemy.getNumBomb());
    }

    // is a class for simulating some aspects of the GameManager
    private static class TestGameManager implements GameManager {

        private Player player;
        private Enemy enemy;
        private GameMap map;

        public TestGameManager() {
            this.map = new GameMapImpl(false);
            this.enemy = new Enemy(this, new Coord(0, 0), new BombFactoryImpl(this));
            this.player = new Player(this, new Coord(0, 12), new BombFactoryImpl(this));
        }

        public void setPlayerCoord(int x, int y) {
            player.setCharacterPosition(new Coord(x+0.5f, y+0.5f));
        }

        public void setEnemyCoord(int x, int y) {
            enemy.setCharacterPosition(new Coord(x+0.5f, y+0.5f));
        }

        @Override
        public Player getPlayer() {
            return player;
        }

        @Override
        public void updateGame(long elapsed) {
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
        public boolean addBomb(Bomb b) {
            return map.addBomb(b, b.getPos());
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
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'getTimeLeft'");
        }

        @Override
        public void removePowerUp(Pair pos) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'removePowerUp'");
        }

        @Override
        public Optional<Bomb> getBomb(Pair pos) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'getBomb'");
        }
    }

    private static class MyBomb implements Bomb {

        private Pair pos;

        public MyBomb(Pair pos) {
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
        public void update(boolean condition) {
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
