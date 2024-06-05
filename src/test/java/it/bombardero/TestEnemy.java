package it.bombardero;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import it.unibo.bombardero.map.api.BombarderoTimer;
import it.unibo.bombardero.map.api.Coord;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.map.impl.GameMapImpl;
import it.unibo.bombardero.utils.Utils;

import java.util.List;
import java.util.Optional;
import java.util.Arrays;

public class TestEnemy {

    private static final int STANDARD_ELAPSED_TIME = 100;

    private TestGameManager manager;
    private BombFactory b;

    @BeforeEach
    void setUp() {
        this.manager = new TestGameManager();
        this.b = new BombFactoryImpl(manager, null);
        this.manager.setEnemyCoord(0, 0);
        this.manager.enemy.setSpeed(0.01f);
    }

    @Test
    public void testEnemyPatrol_PlayerNotInDetectionRadius_MovesRandomly() {
        // outside ENEMY_DETECTION_RADIUS
        this.manager.setPlayerCoord(0, 5);
        this.manager.enemy.update(STANDARD_ELAPSED_TIME);

        // Verify enemy state is PATROL
        assertEquals(Enemy.State.PATROL, this.manager.enemy.getState());
        // We can't directly verify moveRandomly is called, but we can check if nextMove
        // is set through random movement
        assertTrue(this.manager.enemy.getNextMove().isPresent());
    }

    @Test
    public void testEnemyPatrol_PlayerInDetectionRadius_ChangesToChaseState() {
        // Set player position within detection radius in TestGameManager
        this.manager.setPlayerCoord(0, 4);
        this.manager.updateGame(STANDARD_ELAPSED_TIME);

        // Verify enemy state is CHASE
        assertEquals(Enemy.State.CHASE, this.manager.enemy.getState());
    }

    @Test
    public void testEnemyChase_LosesPlayer_ChangesToPatrolState() {
        // Set initial player position within detection radius in TestGameManager
        this.manager.setPlayerCoord(0, 4);
        // We need more than 1 sec to move between cells
        this.manager.updateGame(STANDARD_ELAPSED_TIME);
        this.manager.updateGame(STANDARD_ELAPSED_TIME);

        // Verify enemy state is CHASE
        assertEquals(Enemy.State.CHASE, this.manager.enemy.getState());
        assertEquals(new Pair(0, 1), this.manager.enemy.getIntCoordinate());

        // Set player moving away after initial detection
        this.manager.setPlayerCoord(0, 12);
        this.manager.updateGame(STANDARD_ELAPSED_TIME);

        // Verify enemy state is PATROL
        assertEquals(Enemy.State.PATROL, this.manager.enemy.getState());
    }

    @Test
    public void testEnemyEscape_ChangesToPatrolState() {
        // Set enemy position inside a danger zone
        this.manager.getGameMap().addBomb(b.CreateBomb(Optional.empty(), new Pair(0, 1), 1), new Pair(0, 1));
        this.manager.enemy.update(STANDARD_ELAPSED_TIME);

        assertEquals(Enemy.State.ESCAPE, this.manager.enemy.getState());
        this.manager.updateGame(STANDARD_ELAPSED_TIME);
        this.manager.updateGame(STANDARD_ELAPSED_TIME);
        // Verify enemy state is WAITING
        assertEquals(Enemy.State.WAITING, this.manager.enemy.getState());
        assertEquals(new Pair(1, 0), this.manager.enemy.getIntCoordinate());
    }

    // @Test
    // public void testEnemyPatrol_BreakableWallNextToEnemy_PlacesBomb() {
    //     // Set enemy next to a breakable wall
    //     this.manager.setPlayerCoord(0, 2);
    //     this.manager.getGameMap().addBreakableWall(new Pair(0, 1));
    //     this.manager.enemy.update(STANDARD_ELAPSED_TIME);

    //     assertEquals(Enemy.State.CHASE, this.manager.enemy.getState());
    //     this.manager.updateGame();
    //     // Verify bomb is placed on the enemy's position
    //     assertTrue(this.manager.getGameMap().isBomb(new Pair(0, 0))); 
    //     assertEquals(Utils.ENEMY_STARTING_BOMBS-1, this.manager.enemy.getNumBomb());
    //     this.manager.updateGame();
    //     assertEquals(new Pair(2, 1), this.manager.enemy.getIntCoordinate());

    // }

    // this is a class for simulating some aspects of the GameManager
    private static class TestGameManager implements GameManager {

        private Player player;
        private Enemy enemy;
        private GameMap map;

        public TestGameManager() {
            this.map = new GameMapImpl(false);
            this.enemy = new Enemy(this, new Coord(0, 0), new BombFactoryImpl(this, null));
            this.player = new Player(this, new Coord(0, 12), new BombFactoryImpl(this, null));
        }

        public void setPlayerCoord(int row, int col) {
            this.player = new Player(this, new Coord(row, col), null);
        }

        public void setEnemyCoord(int row, int col) {
            this.enemy = new Enemy(this, new Coord(row, col), null);
        }

        @Override
        public Player getPlayer() {
            return this.player;
        }

        @Override
        public void updateGame(long elapsed) {
            // 60 fps
            for (int i = 0; i < 59; i++) {
                this.enemy.update(STANDARD_ELAPSED_TIME);
            }
            this.enemy.update(STANDARD_ELAPSED_TIME);
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
            return this.map;
        }

        @Override
        public boolean addBomb(BasicBomb bomb) {
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
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'getTimeLeft'");
        }
    }

}
