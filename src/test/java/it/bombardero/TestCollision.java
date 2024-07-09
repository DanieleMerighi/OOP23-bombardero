package it.bombardero;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.bombardero.cell.FlameImpl;
import it.unibo.bombardero.character.Character;
import it.unibo.bombardero.character.Direction;
import it.unibo.bombardero.map.api.GenPair;
import it.unibo.bombardero.physics.api.CollisionEngine;

/**
 * This class will test bounding Box.
 */
class TestCollision {
    private static final GenPair<Float, Float> L_CORNER_CELL_2_1 = new GenPair<>(2f, 1.5f);
    private static final GenPair<Float, Float> R_CORNER_CELL_0_1 = new GenPair<>(0.9f, 1.5f);
    private static final GenPair<Float, Float> U_CORNER_CELL_1_2 = new GenPair<>(1.5f, 2f);
    private static final GenPair<Float, Float> D_CORNER_CELL_1_0 = new GenPair<>(1.5f, 0.9f);
    private Character character;
    private MyGameManager mgr;
    private CollisionEngine cEngine;

    /**
     * set GameManager character GameMap CollisionEngine.
     */
    @BeforeEach
    void init() {
        mgr = new MyGameManager();
        character = mgr.getPlayers().get(0);
        cEngine = mgr.getCollisionEngine();
    }

    /**
     * test when a character is colliding with an adiacent cell
     * and the collision are checked and computed , then the character is not
     * collideing anymore.
     */
    @Test
    void testCharacterBoundingCollision() {
        // Direction RIGHT
        testDirection(Direction.RIGHT, R_CORNER_CELL_0_1);
        // Direction DOWN
        testDirection(Direction.DOWN, D_CORNER_CELL_1_0);
        // Direction UP
        testDirection(Direction.UP, U_CORNER_CELL_1_2);
        // Direction LEFT
        testDirection(Direction.LEFT, L_CORNER_CELL_2_1);
    }

private void testDirection(final Direction dir, final GenPair<Float, Float> characterPosition) {
        character.setCharacterPosition(characterPosition);
        character.setFacingDirection(dir);
        assertTrue(character.getBoundingBox()
            .isColliding(mgr.getGameMap().getMap().get(new GenPair<>(1, 1)).getBoundingBox().get()));
        cEngine.checkCharacterCollision(character, mgr.getGameMap());
        assertFalse(character.getBoundingBox().
            isColliding(mgr.getGameMap().getMap().get(new GenPair<>(1, 1)).getBoundingBox().get()));
}

    /**
     * test when a character is in the same cell of a flame or a pouwerUp the
     * collision work.
     */
    @Test
    void testFlameAndPowerUpCollision() {
        mgr.getGameMap().addFlame(new FlameImpl(null, new GenPair<Integer, Integer>(1, 0)),
                new GenPair<Integer, Integer>(1, 0));
        mgr.addPowerUp(new GenPair<Integer, Integer>(0, 1));
        character.setCharacterPosition(D_CORNER_CELL_1_0);
        assertTrue(mgr.getGameMap().isFlame(new GenPair<Integer, Integer>(1, 0)));
        cEngine.checkFlameAndPowerUpCollision(character, mgr.getGameMap());
        assertFalse(character.isAlive());
        character.setCharacterPosition(R_CORNER_CELL_0_1);
        cEngine.checkFlameAndPowerUpCollision(character, mgr.getGameMap());
        assertFalse(mgr.getGameMap().isPowerUp(new GenPair<Integer, Integer>(0, 1)));
    }
}
