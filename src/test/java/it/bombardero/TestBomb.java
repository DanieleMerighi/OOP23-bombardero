package it.bombardero;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.bombardero.bomb.api.Bomb;
import it.unibo.bombardero.bomb.api.BombFactory;
import it.unibo.bombardero.bomb.api.Bomb.BombType;
import it.unibo.bombardero.bomb.impl.BombFactoryImpl;
import it.unibo.bombardero.cell.FlameImpl;
import it.unibo.bombardero.map.api.GenPair;
import it.unibo.bombardero.character.Character;

/**
 * this class test bomb interaction whith the map and if they are
 * placed well.
 */
public class TestBomb {

    private static final int TIME_TO_EXPLODE = 3000;
    private BombFactory bombFactory;
    private MyGameManager mgr;

    /**
     * set a new BombFactory and a Manager.
     */
    @BeforeEach
    void init() {
        bombFactory = new BombFactoryImpl();
        mgr = new MyGameManager();
    }

    /**
     * test if bomb are placed in the right position and test when they explode and
     * interact whith other type of cell.
     */
    @Test
    void testPlacingAndExplodeBomb() {
        final Bomb bomb = bombFactory.createBasicBomb(1, new GenPair<Integer, Integer>(2, 2));
        assertEquals(bomb.getBombType(), BombType.BOMB_BASIC);
        mgr.getGameMap().addBomb(bomb, new GenPair<Integer, Integer>(2, 2));
        assertTrue(mgr.getGameMap().isBomb(new GenPair<Integer, Integer>(2, 2)));
        mgr.getGameMap().addBreakableWall(new GenPair<Integer, Integer>(2, 1));
        mgr.getGameMap().addUnbreakableWall(new GenPair<Integer, Integer>(1, 2));
        bomb.update(TIME_TO_EXPLODE);
        assertTrue(bomb.isExploded());
        bomb.computeFlame(mgr.getGameMap())
                .forEach(entry -> mgr.getGameMap().addFlame(new FlameImpl(entry.getValue(), entry.getKey()),
                        entry.getKey()));
        assertTrue(mgr.getGameMap().isPowerUp(new GenPair<Integer, Integer>(2, 1)));
        assertTrue(mgr.getGameMap().isUnbreakableWall(new GenPair<Integer, Integer>(1, 2)));
        assertTrue(mgr.getGameMap().isFlame(new GenPair<Integer, Integer>(2, 3)));
        assertTrue(mgr.getGameMap().isFlame(new GenPair<Integer, Integer>(3, 2)));
    }

    /**
     * test the behaviour of the piercing Bomb so it can pierce in to breackable
     * wall
     * and leave a powerUp when destroy the last wall.
     */
    @Test
    void testPircingBomb() {
        final Bomb piercingBomb = bombFactory.createPiercingBomb(2, new GenPair<Integer, Integer>(2, 2));
        assertEquals(piercingBomb.getBombType(), BombType.BOMB_PIERCING);
        mgr.getGameMap().addBreakableWall(new GenPair<Integer, Integer>(2, 3));
        mgr.getGameMap().addBreakableWall(new GenPair<Integer, Integer>(2, 4));
        mgr.getGameMap().addUnbreakableWall(new GenPair<Integer, Integer>(2, 1));
        piercingBomb.update(TIME_TO_EXPLODE);
        assertTrue(piercingBomb.isExploded());
        piercingBomb.computeFlame(mgr.getGameMap())
                .forEach(entry -> mgr.getGameMap().addFlame(new FlameImpl(entry.getValue(), entry.getKey()),
                        entry.getKey()));
        assertTrue(mgr.getGameMap().isFlame(new GenPair<Integer, Integer>(2, 3)));
        assertTrue(mgr.getGameMap().isPowerUp(new GenPair<Integer, Integer>(2, 4)));
        assertTrue(mgr.getGameMap().isUnbreakableWall(new GenPair<Integer, Integer>(2, 1)));
        assertTrue(mgr.getGameMap().isEmpty(new GenPair<Integer, Integer>(2, 0)));
        assertTrue(mgr.getGameMap().isFlame(new GenPair<Integer, Integer>(1, 2)));
        assertTrue(mgr.getGameMap().isFlame(new GenPair<Integer, Integer>(0, 2)));
        assertTrue(mgr.getGameMap().isFlame(new GenPair<Integer, Integer>(3, 2)));
        assertTrue(mgr.getGameMap().isFlame(new GenPair<Integer, Integer>(4, 2)));
    }

    /**
     * Test if PowerBomb has MAX_RANGE.
     */
    @Test
    void testPowerBomb() {
        final Bomb powerBomb = bombFactory.createPowerBomb(new GenPair<Integer, Integer>(2, 2));
        assertEquals(powerBomb.getBombType(), BombType.BOMB_POWER);
        assertEquals(powerBomb.getRange(), Character.MAX_FLAME_RANGE);
        powerBomb.update(TIME_TO_EXPLODE);
        assertTrue(powerBomb.isExploded());
    }

    /**
     * test if remoteBomb explode on normal update.
     */
    @Test
    void testRemoteBomb() {
        final Bomb remoteBomb = bombFactory.createRemoteBomb(1, new GenPair<Integer, Integer>(2, 2));
        assertEquals(remoteBomb.getBombType(), BombType.BOMB_REMOTE);
        remoteBomb.update(TIME_TO_EXPLODE);
        assertFalse(remoteBomb.isExploded());
        remoteBomb.update(true);
        assertTrue(remoteBomb.isExploded());
    }
}
