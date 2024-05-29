package it.bombardero;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.bombardero.cell.BasicBomb;
import it.unibo.bombardero.cell.Flame.FlameType;
import it.unibo.bombardero.cell.powerup.api.PowerUp;
import it.unibo.bombardero.cell.powerup.api.PowerUpFactory;
import it.unibo.bombardero.cell.powerup.impl.PowerUpFactoryImpl;
import it.unibo.bombardero.character.Character;
import it.unibo.bombardero.character.Player;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.BombarderoTimer;
import it.unibo.bombardero.map.api.Coord;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.map.impl.GameMapImpl;

import java.util.List;
import java.util.stream.IntStream;

public class TestPowerUp {

    private TestGameManager manager;
    PowerUpFactory factory = new PowerUpFactoryImpl();

    @BeforeEach
    void setUp() {
        this.manager = new TestGameManager();
    }

    @Test
    public void Test100PowerUp() {
        IntStream.range(0, 100).forEach(n -> {
            PowerUp powerUP = factory.createPowerUp();
            powerUP.applyEffect(this.manager.getPlayer());
            if (this.manager.getPlayer().getResetEffect().isPresent()) {
                this.manager.getPlayer().updateSkeleton(1000*9);
            }
            System.out.println(powerUP.getType());
            System.out.println("flame " + this.manager.getPlayer().getFlameRange());
            System.out.println("numBomb " + this.manager.getPlayer().getNumBomb());
            System.out.println("speed " + this.manager.getPlayer().getSpeed());
            System.out.println("typebomb " + this.manager.getPlayer().getBombType());
            System.out.println("resetTask " + this.manager.getPlayer().getResetEffect());
            System.out.println("\n\n\n\n");
        });
    }

    private static class TestGameManager implements GameManager {

        private Player player;
        private GameMap map;

        public TestGameManager() {
            this.map = new GameMapImpl(false);
            this.player = new Player(this, new Coord(0, 0), null);
        }

        @Override
        public Player getPlayer() {
            return this.player;
        }

        @Override
        public void updateGame() {
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

        @Override
        public void startTimer() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'startTimer'");
        }

        @Override
        public BombarderoTimer getTimer() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'getTimer'");
        }
    }

}
