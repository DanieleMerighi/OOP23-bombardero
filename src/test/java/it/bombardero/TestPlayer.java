package it.bombardero;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.bombardero.cell.BasicBomb;
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
import java.util.stream.IntStream;

public class TestPlayer {

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
            this.manager.getPlayer().update();
            assertEquals(manager.getPlayer().getFacingDirection(), dir);
        }
    }

    @Test
    public void TestPlayerMovingDirections() {
        int coord = 0;
        Coord floatCoord = new Coord(0.0f, 0.0f);
        this.manager.getPlayer().setCharacterPosition(floatCoord);

        this.manager.getPlayer().setFacingDirection(Direction.RIGHT);

        int max=1; // 60 mosse
        this.manager.getPlayer().setSpeed(0.01f);
        IntStream.range(0, max).forEach(n -> this.manager.getPlayer().update());
        
        assertEquals(manager.getPlayer().getCharacterPosition(), new Coord(0.0f, Float.sum(0.01f, 0.04f)));
    }

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
        public void updateGame() {
            // 60 fps
            for (int i = 0; i < 59; i++) {
                this.player.update();
            }
            this.player.update();
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
        public boolean addBomb(BasicBomb bomb) {
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
