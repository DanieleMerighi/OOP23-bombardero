package it.bombardero;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.bombardero.cell.BasicBomb;
import it.unibo.bombardero.cell.BombFactory;
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

import java.util.List;
import java.util.Optional;
import java.util.Arrays;

public class TestEnemy {

    private static final int STANDARD_ELAPSED_TIME = 100;
    private static final int STARTING_BOMBS = 1;

    private TestGameManager manager;
    private BombFactory b;

    @BeforeEach
    void setUp() {
        this.manager = new TestGameManager();
        this.b = new BombFactoryImpl(manager);
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
        //manager.updateGame(STANDARD_ELAPSED_TIME);

        // Verify enemy state is CHASE
        assertEquals(Enemy.State.CHASE, manager.enemy.getState());
        assertEquals(new Pair(0, 3), manager.enemy.getIntCoordinate());

        // Set player moving away after initial detection
        manager.setPlayerCoord(3, 12);
        manager.updateGame(STANDARD_ELAPSED_TIME);

        // Verify enemy state is PATROL
        assertEquals(Enemy.State.PATROL, manager.enemy.getState());
    }

    @Test
    public void testEnemyEscape_ChangesToWaiting() {
        // Set enemy position inside a danger zone
        this.manager.getGameMap().addBomb(b.CreateBomb(null), new Pair(0, 1));
        this.manager.enemy.update(STANDARD_ELAPSED_TIME);

        assertEquals(Enemy.State.ESCAPE, this.manager.enemy.getState());
        this.manager.updateGame(STANDARD_ELAPSED_TIME);
        this.manager.updateGame(STANDARD_ELAPSED_TIME);
        // Verify enemy state is WAITING
        assertEquals(Enemy.State.WAITING, manager.enemy.getState());
        assertEquals(new Pair(1, 0), manager.enemy.getIntCoordinate());
    }

    @Test
    public void testEnemyPatrol_BreakableWallNextToEnemy_PlacesBomb() {
        // Set enemy next to a breakable wall
        manager.setPlayerCoord(0, 2);
        manager.enemy.setNumBomb(STARTING_BOMBS);
        manager.getGameMap().addBreakableWall(new Pair(0, 1));
        manager.enemy.update(STANDARD_ELAPSED_TIME);

        assertEquals(Enemy.State.CHASE, manager.enemy.getState());
        manager.updateGame(STANDARD_ELAPSED_TIME);
        // Verify bomb is placed on the enemy's position
        assertTrue(manager.getGameMap().isBomb(new Pair(0, 0))); 
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
        public boolean addBomb(BasicBomb b) {
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

    }

}
