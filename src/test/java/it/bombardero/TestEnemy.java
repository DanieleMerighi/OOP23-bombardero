package it.bombardero;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.bombardero.character.Character.CharacterType;
import it.unibo.bombardero.character.ai.impl.ChaseState;
import it.unibo.bombardero.character.ai.impl.EscapeState;
import it.unibo.bombardero.character.ai.impl.PatrolState;
import it.unibo.bombardero.core.api.Controller;
import it.unibo.bombardero.map.api.GenPair;

/**
 * Test class for Enemy behavior testing.
 */
class TestEnemy {

    private static final int STANDARD_ELAPSED_TIME = 100;
    private static final int STARTING_BOMBS = 1;
    private static final float SPEED = 0.01f;
    private static final float NEWSPEED = 0.05f;

    private MyGameManager manager;
    private Controller controller;

    /**
     * Setup method executed before each test.
     */
    @BeforeEach
    void setUp() {
        this.controller = new ControllerForTesting();
        this.manager = new MyGameManager();
        this.manager.setEnemyCoord(0, 0);
        this.manager.setEnemySpeed(SPEED);
    }

    /**
     * Test case: Enemy moves randomly when player is not in detection radius.
     */
    @Test
    void testEnemyPatrolPlayerNotInDetectionRadiusMovesRandomly() {
        // outside ENEMY_DETECTION_RADIUS
        // CHECKSTYLE: MagicNumber OFF
        manager.setPlayerCoord(0, 5);
        // CHECKSTYLE: MagicNumber ON
        manager.getEnemies().get(0).update(manager, STANDARD_ELAPSED_TIME, CharacterType.ENEMY);

        // Verify enemy state is PATROL
        assertTrue(manager.isEnemyStateEqualTo(new PatrolState()));
        // We can't directly verify moveRandomly is called, but we can check if nextMove
        // is set through random movement
        assertTrue(manager.getEnemyNextMove().isPresent());
    }

    /**
     * Test case: Enemy changes to CHASE state when player is in detection radius.
     */
    @Test
    void testEnemyPatrolPlayerInDetectionRadiusChangesToChaseState() {
        // Set player position within detection radius in TestGameManager
        manager.setPlayerCoord(0, 4);
        manager.updateGame(STANDARD_ELAPSED_TIME, controller);

        // Verify enemy state is CHASE
        assertTrue(manager.isEnemyStateEqualTo(new ChaseState()));
    }

    /**
     * Test case: Enemy changes back to PATROL state when losing the player.
     */
    @Test
    void testEnemyChaseLosesPlayerChangesToPatrolState() {
        // Set initial player position within detection radius in TestGameManager
        manager.setPlayerCoord(2, 1);
        manager.setEnemyBombs(0);
        // We need more than 1 sec to move between cells
        manager.updateGame(STANDARD_ELAPSED_TIME, controller);

        // Verify enemy state is CHASE
        assertTrue(manager.isEnemyStateEqualTo(new ChaseState()));
        assertEquals(new GenPair<Integer, Integer>(1, 0), manager.getEnemyCoord());

        // Set player moving away after initial detection
        // CHECKSTYLE: MagicNumber OFF
        manager.setPlayerCoord(3, 12);
        // CHECKSTYLE: MagicNumber ON
        manager.updateGame(STANDARD_ELAPSED_TIME, controller);

        // Verify enemy state is PATROL
        assertTrue(manager.isEnemyStateEqualTo(new PatrolState()));
    }

    /**
     * Test case: Enemy escapes and then returns to PATROL state.
     */
    @Test
    void testEnemyEscapeChangesToPatrol() {
        // Set enemy position inside a danger zone
        this.manager.setEnemySpeed(NEWSPEED);
        this.manager.addBomb(new GenPair<Integer, Integer>(0, 1));
        this.manager.enemySingleUpdate();

        assertTrue(manager.isEnemyStateEqualTo(new EscapeState()));
        this.manager.updateGame(STANDARD_ELAPSED_TIME, controller);
        assertEquals(new GenPair<Integer, Integer>(1, 0), manager.getEnemyCoord());
        this.manager.updateGame(STANDARD_ELAPSED_TIME, controller);
        // Verify enemy state is Patrol
        assertTrue(manager.isEnemyStateEqualTo(new PatrolState()));

    }

    /**
     * Test case: Enemy places bomb when next to a breakable wall.
     */
    @Test
    void testEnemyPatrolBreakableWallNextToEnemyPlacesBomb() {
        // Set enemy next to a breakable wall
        manager.setPlayerCoord(1, 2);
        manager.setEnemyBombs(STARTING_BOMBS);
        manager.addBreakableWall(new GenPair<Integer, Integer>(0, 1));
        manager.addBreakableWall(new GenPair<Integer, Integer>(2, 0));
        manager.enemySingleUpdate();

        assertTrue(manager.isEnemyStateEqualTo(new ChaseState()));
        manager.updateGame(STANDARD_ELAPSED_TIME, controller);
        manager.updateGame(STANDARD_ELAPSED_TIME, controller);
        // Verify bomb is placed on the enemy's position
        assertEquals(STARTING_BOMBS - 1, manager.getEnemyNumBombs());
    }
}
