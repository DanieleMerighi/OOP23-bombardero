package it.bombardero;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.bombardero.cell.BasicBomb;
import it.unibo.bombardero.cell.Bomb;
import it.unibo.bombardero.cell.Cell.CellType;
import it.unibo.bombardero.cell.Flame.FlameType;
import it.unibo.bombardero.character.Character;
import it.unibo.bombardero.character.Enemy;
import it.unibo.bombardero.character.Player;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.Coord;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.map.impl.GameMapImpl;
import it.unibo.bombardero.utils.Utils;

import java.util.List;

public class TestEnemy {

    private TestGameManager manager;

    @BeforeEach
    void setUp() {
        this.manager = new TestGameManager();
    }

    @Test
    public void testEnemyPatrol_PlayerNotInDetectionRadius_MovesRandomly() {
        // outside ENEMY_DETECTION_RADIUS
        this.manager.setEnemyCoord(0, 0);
        this.manager.setPlayerCoord(0, 5);
        this.manager.enemy.update();

        // Verify enemy state is PATROL
        assertEquals(Enemy.State.PATROL, this.manager.enemy.getState());
        // Verify enemy doesn't have a planned path
        assertTrue(this.manager.enemy.getPath().isEmpty());
        // We can't directly verify moveRandomly is called, but we can check if nextMove
        // is set through random movement
        assertTrue(this.manager.enemy.getNextMove().isPresent());
    }

    @Test
    public void testEnemyPatrol_PlayerInDetectionRadius_ChangesToChaseState() {
        // Set player position within detection radius in TestGameManager
        this.manager.setEnemyCoord(0, 0);
        this.manager.setPlayerCoord(0, 4);
        this.manager.updateGame();

        // Verify enemy state is CHASE
        assertEquals(Enemy.State.CHASE, this.manager.enemy.getState());

        // Verify enemy attempts to find a path to the player (indirectly)
        // We can't directly access EnemyGraphReasoner, but we can check if the path is
        // populated
        // after computeNextDir is called
        assertTrue(!this.manager.enemy.getPath().isEmpty());
    }

    @Test
    public void testEnemyChase_LosesPlayer_ChangesToPatrolState() {
        // Set initial player position within detection radius in TestGameManager
        this.manager.setEnemyCoord(0, 0);
        this.manager.setPlayerCoord(0, 4);
        this.manager.updateGame();

        // Verify enemy state is CHASE
        assertEquals(Enemy.State.CHASE, this.manager.enemy.getState());
        assertEquals(new Pair(0, 1), this.manager.enemy.getIntCoordinate());

        // Set player moving away after initial detection
        this.manager.setPlayerCoord(0, 12);
        this.manager.updateGame();

        // Verify enemy state is PATROL
        assertEquals(Enemy.State.PATROL, this.manager.enemy.getState());
    }

    @Test
    public void testEnemyEscape_ChangesToPatrolState() {
        // Set enemy position inside a danger zone
        this.manager.setEnemyCoord(0, 0);
        //this.manager.getGameMap().addBomb(new Bomb(manager, new Pair(0, 2), CellType.BOMB_BASIC, 2), new Pair(0, 2));
        this.manager.enemy.update();
        //System.out.println(this.manager.enemy.getX() + " " + this.manager.enemy.getY());
        assertEquals(Enemy.State.ESCAPE, this.manager.enemy.getState());
        this.manager.updateGame();
        // Verify enemy state is PATROL
        assertEquals(Enemy.State.PATROL, this.manager.enemy.getState());
        assertEquals(new Pair(1, 0), this.manager.enemy.getIntCoordinate());
    }

    @Test
    public void testEnemyPatrol_BreakableWallNextToEnemy_PlacesBomb() {
        // Set enemy next to a breakable wall
        this.manager.setEnemyCoord(0, 0);
        this.manager.setPlayerCoord(0, 2);
        this.manager.getGameMap().addBreakableWall(new Pair(0, 1));
        this.manager.enemy.update();

        assertEquals(Enemy.State.CHASE, this.manager.enemy.getState());
        this.manager.updateGame();
        // Verify bomb is placed on the enemy's position
        assertTrue(this.manager.getGameMap().isBomb(new Pair(0, 0))); //ToDo
        assertEquals(Utils.ENEMY_STARTING_BOMBS-1, this.manager.enemy.getNumBomb());
        this.manager.updateGame();
        //this.manager.updateGame();
        assertEquals(new Pair(2, 1), this.manager.enemy.getIntCoordinate());

    }

    // this is a class for simulating some aspects of the GameManager
    private static class TestGameManager implements GameManager {

        private Player player;
        private Enemy enemy;
        private GameMap map;

        public TestGameManager() {
            this.map = new GameMapImpl(false);
            this.enemy = new Enemy(this, new Coord(0, 0), null);
            this.player = new Player(this, new Coord(0, 12), null);
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
        public void updateGame() {
            // 60 fps
            for (int i = 0; i < 59; i++) {
                this.enemy.update();
            }
            this.enemy.update();
        }

        @Override
        public void endGame() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'endGame'");
        }

        @Override
        public List<Character> getEnemies() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'getEnemies'");
        }

        @Override
        public GameMap getGameMap() {
            return this.map;
        }

        @Override
        public void addBomb(BasicBomb bomb) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'addBomb'");
        }

        @Override
        public void removeBomb(Pair pos) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'removeBomb'");
        }

        @Override
        public void addFlame(FlameType type, Pair pos) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'addFlame'");
        }

        @Override
        public void removeFlame(Pair pos) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'removeFlame'");
        }

        @Override
        public boolean removeWall(Pair pos) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'removeWall'");
        }
    }

}
