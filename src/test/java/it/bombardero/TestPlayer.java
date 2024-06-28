package it.bombardero;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.bombardero.cell.BasicBomb;
import it.unibo.bombardero.cell.Bomb;
import it.unibo.bombardero.cell.Flame.FlameType;
import it.unibo.bombardero.character.Character;
import it.unibo.bombardero.character.Direction;
import it.unibo.bombardero.character.Player;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.BombarderoTimer;
import it.unibo.bombardero.map.api.Coord;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.map.impl.GameMapImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class TestPlayer {

    private static final int STANDARD_ELAPSED_TIME = 100;
    private TestGameManager manager;
    

    @BeforeEach
    void setUp() {
        this.manager = new TestGameManager();
    }

    @Test
    public void TestPlayerLookingDirections() {
        // outside ENEMY_DETECTION_RADIUS
        int coord = 10;
        this.manager.setPlayerCoord(coord, coord);

        for (Direction dir : Direction.values()) {
            this.manager.getPlayer().setFacingDirection(dir);
            this.manager.getPlayer().update(STANDARD_ELAPSED_TIME);
            assertEquals(dir, manager.getPlayer().getFacingDirection());
        }
    }

    // @Test
    // public void TestPlayerMovingDirections() {
    //     // Setting player spown
    //     float spawnRow = 10.0f;
    //     float spawnCol = 10.0f;
    //     Coord spawnCoord = new Coord(spawnRow, spawnCol);
    //     this.manager.getPlayer().setCharacterPosition(spawnCoord);

    //     // Setting player direction
    //     this.manager.getPlayer().setFacingDirection(Direction.RIGHT);

    //     // Setting player speed
    //     this.manager.getPlayer().setSpeed(0.02f);

    //     // Setting the number of update and calling them
    //     int updateNumeber = 60; // Number of updates done
    //     IntStream.range(0, updateNumeber).forEach(n -> this.manager.getPlayer().update(STANDARD_ELAPSED_TIME));

    //     assertEquals(spawnCoord.sum(new Coord(
    //             this.manager.getPlayer().getSpeed() * this.manager.getPlayer().getFacingDirection().getDy()
    //                     * updateNumeber,
    //             this.manager.getPlayer().getSpeed() * this.manager.getPlayer().getFacingDirection().getDx()
    //                     * updateNumeber)),
    //             manager.getPlayer().getCharacterPosition());
    // }

    private static class TestGameManager implements GameManager {

        private Player player;
        private GameMap map;

        public TestGameManager() {
            this.map = new GameMapImpl(false);
            this.player = new Player(this, new Coord(0, 0), null);
        }

        public void setPlayerCoord(int row, int col) {
            this.player = new Player(this, new Coord(row, col), null);
        }

        @Override
        public Player getPlayer() {
            return this.player;
        }

        @Override
        public void updateGame(long elapsed) {
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
        public boolean addBomb(Bomb bomb) {
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

}
