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
 * This class will test bounding Box
 */
public class TestCollision {
    Character character;
    GameMap gMap;
    MyGameManager mgr;
    CollisionEngine cEngine;

    @BeforeEach
    void init() {
        mgr = new MyGameManager();
        character = mgr.getPlayer();
        gMap = mgr.getGameMap();
        cEngine = mgr.getCollisionEngine();
    }

    /**
     * test when a character is colliding with an adiacent cell
     * and the collision are checked and computed , then the character is not collideing anymore.
     */
    @Test
    void testCharacterBoundingCollision() {
        //Direction RIGHT
        character.setCharacterPosition(new GenPair<Float, Float>(0.9f, 0.9f));
        gMap.addBreakableWall(new GenPair<Integer,Integer>(1, 0));
        character.setFacingDirection(Direction.RIGHT);
        assertTrue(character.getBoundingBox().isColliding(gMap.getMap().get(new GenPair<>(1, 1)).getBoundingBox().get()));
        assertTrue(character.getBoundingBox().isColliding(gMap.getMap().get(new GenPair<>(1, 0)).getBoundingBox().get()));
        cEngine.checkCharacterCollision(character, gMap);
        assertFalse(character.getBoundingBox().isColliding(gMap.getMap().get(new GenPair<>(1, 1)).getBoundingBox().get()));
        assertFalse(character.getBoundingBox().isColliding(gMap.getMap().get(new GenPair<>(1, 0)).getBoundingBox().get()));
        //Direction DOWN
        character.setCharacterPosition(new GenPair<Float, Float>(0.9f, 0.9f));
        gMap.addBreakableWall(new GenPair<Integer,Integer>(0, 1));
        character.setFacingDirection(Direction.DOWN);
        assertTrue(character.getBoundingBox().isColliding(gMap.getMap().get(new GenPair<>(1, 1)).getBoundingBox().get()));
        assertTrue(character.getBoundingBox().isColliding(gMap.getMap().get(new GenPair<>(0, 1)).getBoundingBox().get()));
        cEngine.checkCharacterCollision(character, gMap);
        assertFalse(character.getBoundingBox().isColliding(gMap.getMap().get(new GenPair<>(1, 1)).getBoundingBox().get()));
        assertFalse(character.getBoundingBox().isColliding(gMap.getMap().get(new GenPair<>(0, 1)).getBoundingBox().get()));
        //Direction UP
        character.setCharacterPosition(new GenPair<Float, Float>(0.9f, 2f));
        gMap.addBreakableWall(new GenPair<Integer,Integer>(0, 1));
        character.setFacingDirection(Direction.UP);
        assertTrue(character.getBoundingBox().isColliding(gMap.getMap().get(new GenPair<>(1, 1)).getBoundingBox().get()));
        assertTrue(character.getBoundingBox().isColliding(gMap.getMap().get(new GenPair<>(0, 1)).getBoundingBox().get()));
        cEngine.checkCharacterCollision(character, gMap);
        assertFalse(character.getBoundingBox().isColliding(gMap.getMap().get(new GenPair<>(0, 1)).getBoundingBox().get()));
        assertFalse(character.getBoundingBox().isColliding(gMap.getMap().get(new GenPair<>(1, 1)).getBoundingBox().get()));
        //Direction LEFT
        character.setCharacterPosition(new GenPair<Float, Float>(2f, 0.9f));
        gMap.addBreakableWall(new GenPair<Integer,Integer>(1, 0));
        character.setFacingDirection(Direction.LEFT);
        assertTrue(character.getBoundingBox().isColliding(gMap.getMap().get(new GenPair<>(1, 1)).getBoundingBox().get()));
        assertTrue(character.getBoundingBox().isColliding(gMap.getMap().get(new GenPair<>(1, 0)).getBoundingBox().get()));
        cEngine.checkCharacterCollision(character, gMap);
        assertFalse(character.getBoundingBox().isColliding(gMap.getMap().get(new GenPair<>(1, 1)).getBoundingBox().get()));
        assertFalse(character.getBoundingBox().isColliding(gMap.getMap().get(new GenPair<>(1, 0)).getBoundingBox().get()));
    }

    /**
     * test when a character is in the same cell of a flame or a pouwerUp the collision work.
     */
    @Test
    void testFlameAndPowerUpCollision() {
        gMap.addFlame(new FlameImpl(null, new GenPair<Integer,Integer>(1, 0))
            , new GenPair<Integer,Integer>(1, 0));
        mgr.addPowerUp(new GenPair<Integer,Integer>(0, 1));
        character.setCharacterPosition(new GenPair<Float,Float>(0.5f, 1.5f));
        cEngine.checkFlameAndPowerUpCollision(character, gMap);
        assertFalse(gMap.isPowerUp(new GenPair<Integer,Integer>(0, 1)));
        character.setCharacterPosition(new GenPair<Float,Float>(1.5f, 0.5f));
        assertTrue(gMap.isFlame(new GenPair<Integer,Integer>(1, 0)));
        cEngine.checkFlameAndPowerUpCollision(character, gMap);
        assertFalse(character.isAlive());
    }
}
