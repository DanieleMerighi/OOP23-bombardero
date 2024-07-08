package it.bombardero;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.bombardero.cell.FlameImpl;
import it.unibo.bombardero.character.Character;
import it.unibo.bombardero.character.Direction;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.GenPair;
import it.unibo.bombardero.physics.api.CollisionEngine;

/**
 * This class will test bounding Box.
 */
class TestCollision {
    private static final GenPair<Float, Float> TR_CORNER_CELL_0_2 = new GenPair<>(0.99f, 2f);
    private static final GenPair<Float, Float> CELL_1_0 = new GenPair<>(1.5f, 0.5f);
    private static final GenPair<Float, Float> CELL_0_1 = new GenPair<>(0.5f, 1.5f);
    private static final GenPair<Float, Float> TR_CORNER_CELL_2_0 = new GenPair<>(2f, 0.9f);
    private static final GenPair<Float, Float> DR_CORNER_CELL_0_0 = new GenPair<>(0.9f, 0.9f);
    private Character character;
    private GameMap gMap;
    private MyGameManager mgr;
    private CollisionEngine cEngine;

    /**
     * set GameManager character GameMap CollisionEngine.
     */
    @BeforeEach
    void init() {
        mgr = new MyGameManager();
        character = mgr.getPlayer();
        gMap = mgr.getGameMap();
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
        character.setCharacterPosition(DR_CORNER_CELL_0_0);
        gMap.addBreakableWall(new GenPair<Integer, Integer>(1, 0));
        character.setFacingDirection(Direction.RIGHT);
        assertTrue(
                character.getBoundingBox().isColliding(gMap.getMap().get(new GenPair<>(1, 1)).getBoundingBox().get()));
        assertTrue(
                character.getBoundingBox().isColliding(gMap.getMap().get(new GenPair<>(1, 0)).getBoundingBox().get()));
        cEngine.checkCharacterCollision(character, gMap);
        assertFalse(
                character.getBoundingBox().isColliding(gMap.getMap().get(new GenPair<>(1, 1)).getBoundingBox().get()));
        assertFalse(
                character.getBoundingBox().isColliding(gMap.getMap().get(new GenPair<>(1, 0)).getBoundingBox().get()));
        // Direction DOWN
        character.setCharacterPosition(DR_CORNER_CELL_0_0);
        gMap.addBreakableWall(new GenPair<Integer, Integer>(0, 1));
        character.setFacingDirection(Direction.DOWN);
        assertTrue(
                character.getBoundingBox().isColliding(gMap.getMap().get(new GenPair<>(1, 1)).getBoundingBox().get()));
        assertTrue(
                character.getBoundingBox().isColliding(gMap.getMap().get(new GenPair<>(0, 1)).getBoundingBox().get()));
        cEngine.checkCharacterCollision(character, gMap);
        assertFalse(
                character.getBoundingBox().isColliding(gMap.getMap().get(new GenPair<>(1, 1)).getBoundingBox().get()));
        assertFalse(
                character.getBoundingBox().isColliding(gMap.getMap().get(new GenPair<>(0, 1)).getBoundingBox().get()));
        // Direction UP
        character.setCharacterPosition(TR_CORNER_CELL_0_2);
        gMap.addBreakableWall(new GenPair<Integer, Integer>(0, 1));
        character.setFacingDirection(Direction.UP);
        assertTrue(
                character.getBoundingBox().isColliding(gMap.getMap().get(new GenPair<>(1, 1)).getBoundingBox().get()));
        assertTrue(
                character.getBoundingBox().isColliding(gMap.getMap().get(new GenPair<>(0, 1)).getBoundingBox().get()));
        cEngine.checkCharacterCollision(character, gMap);
        assertFalse(
                character.getBoundingBox().isColliding(gMap.getMap().get(new GenPair<>(0, 1)).getBoundingBox().get()));
        assertFalse(
                character.getBoundingBox().isColliding(gMap.getMap().get(new GenPair<>(1, 1)).getBoundingBox().get()));
        // Direction LEFT
        character.setCharacterPosition(TR_CORNER_CELL_2_0);
        gMap.addBreakableWall(new GenPair<Integer, Integer>(1, 0));
        character.setFacingDirection(Direction.LEFT);
        assertTrue(
                character.getBoundingBox().isColliding(gMap.getMap().get(new GenPair<>(1, 1)).getBoundingBox().get()));
        assertTrue(
                character.getBoundingBox().isColliding(gMap.getMap().get(new GenPair<>(1, 0)).getBoundingBox().get()));
        cEngine.checkCharacterCollision(character, gMap);
        assertFalse(
                character.getBoundingBox().isColliding(gMap.getMap().get(new GenPair<>(1, 1)).getBoundingBox().get()));
        assertFalse(
                character.getBoundingBox().isColliding(gMap.getMap().get(new GenPair<>(1, 0)).getBoundingBox().get()));
    }

    /**
     * test when a character is in the same cell of a flame or a pouwerUp the
     * collision work.
     */
    @Test
    void testFlameAndPowerUpCollision() {
        gMap.addFlame(new FlameImpl(null, new GenPair<Integer, Integer>(1, 0)), new GenPair<Integer, Integer>(1, 0));
        mgr.addPowerUp(new GenPair<Integer, Integer>(0, 1));
        character.setCharacterPosition(CELL_0_1);
        cEngine.checkFlameAndPowerUpCollision(character, gMap);
        assertFalse(gMap.isPowerUp(new GenPair<Integer, Integer>(0, 1)));
        character.setCharacterPosition(CELL_1_0);
        assertTrue(gMap.isFlame(new GenPair<Integer, Integer>(1, 0)));
        cEngine.checkFlameAndPowerUpCollision(character, gMap);
        assertFalse(character.isAlive());
    }
}
