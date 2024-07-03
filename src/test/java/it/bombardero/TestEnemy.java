package it.bombardero;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.bombardero.character.ai.impl.ChaseState;
import it.unibo.bombardero.character.ai.impl.EscapeState;
import it.unibo.bombardero.character.ai.impl.PatrolState;
import it.unibo.bombardero.map.api.IntPair;

/**
 * Test class for Enemy behavior testing.
 */
public class TestEnemy {

    private static final int STANDARD_ELAPSED_TIME = 100;
    private static final int STARTING_BOMBS = 1;
    private static final float SPEED = 0.01f;
    private static final float NEWSPEED = 0.05f;

    private MyGameManager manager;

    /**
     * Setup method executed before each test.
     */
    @BeforeEach
    public void setUp() {
        this.manager = new MyGameManager();
        this.manager.setEnemyCoord(0, 0);
        this.manager.getEnemy().setSpeed(SPEED);
    }

    /**
     * Test case: Enemy moves randomly when player is not in detection radius.
     */
    @Test
    public void testEnemyPatrolPlayerNotInDetectionRadiusMovesRandomly() {
        // outside ENEMY_DETECTION_RADIUS
        // CHECKSTYLE: MagicNumber OFF
        manager.setPlayerCoord(0, 5);
        // CHECKSTYLE: MagicNumber ON
        manager.getEnemy().update(STANDARD_ELAPSED_TIME, manager);

        // Verify enemy state is PATROL
        assertTrue(manager.getEnemy().isStateEqualTo(new PatrolState()));
        // We can't directly verify moveRandomly is called, but we can check if nextMove
        // is set through random movement
        assertTrue(manager.getEnemy().getNextMove().isPresent());
    }

    /**
     * Test case: Enemy changes to CHASE state when player is in detection radius.
     */
    @Test
    public void testEnemyPatrolPlayerInDetectionRadiusChangesToChaseState() {
        // Set player position within detection radius in TestGameManager
        manager.setPlayerCoord(0, 4);
        manager.updateGame(STANDARD_ELAPSED_TIME);

        // Verify enemy state is CHASE
        assertTrue(manager.getEnemy().isStateEqualTo(new ChaseState()));
    }

    /**
     * Test case: Enemy changes back to PATROL state when losing the player.
     */
    @Test
    public void testEnemyChaseLosesPlayerChangesToPatrolState() {
        // Set initial player position within detection radius in TestGameManager
        manager.setPlayerCoord(2, 1);
        manager.getEnemy().setNumBomb(0);
        // We need more than 1 sec to move between cells
        manager.updateGame(STANDARD_ELAPSED_TIME);

        // Verify enemy state is CHASE
        assertTrue(manager.getEnemy().isStateEqualTo(new ChaseState()));
        assertEquals(new IntPair(1, 0), manager.getEnemy().getIntCoordinate());

        // Set player moving away after initial detection
        // CHECKSTYLE: MagicNumber OFF
        manager.setPlayerCoord(3, 12);
        // CHECKSTYLE: MagicNumber ON
        manager.updateGame(STANDARD_ELAPSED_TIME);

        // Verify enemy state is PATROL
        assertTrue(manager.getEnemy().isStateEqualTo(new PatrolState()));
    }

    /**
     * Test case: Enemy escapes and then returns to PATROL state.
     */
    @Test
    public void testEnemyEscapeChangesToPatrol() {
        // Set enemy position inside a danger zone
        this.manager.getEnemy().setSpeed(NEWSPEED);
        this.manager.addBomb(new MyBomb(new IntPair(0, 1)), null);
        this.manager.getEnemy().update(STANDARD_ELAPSED_TIME, manager);

        assertTrue(manager.getEnemy().isStateEqualTo(new EscapeState()));
        this.manager.updateGame(STANDARD_ELAPSED_TIME);
        assertEquals(new IntPair(1, 0), manager.getEnemy().getIntCoordinate());
        this.manager.updateGame(STANDARD_ELAPSED_TIME);
        // Verify enemy state is Patrol
        assertTrue(manager.getEnemy().isStateEqualTo(new PatrolState()));

    }

    /**
     * Test case: Enemy places bomb when next to a breakable wall.
     */
    @Test
    public void testEnemyPatrolBreakableWallNextToEnemyPlacesBomb() {
        // Set enemy next to a breakable wall
        manager.setPlayerCoord(1, 2);
        manager.getEnemy().setNumBomb(STARTING_BOMBS); // 1 bomb added to enemy
        manager.getGameMap().addBreakableWall(new IntPair(0, 1));
        manager.getEnemy().update(STANDARD_ELAPSED_TIME, manager);

        assertTrue(manager.getEnemy().isStateEqualTo(new ChaseState()));
        manager.updateGame(STANDARD_ELAPSED_TIME);
        manager.updateGame(STANDARD_ELAPSED_TIME);
        // Verify bomb is placed on the enemy's position
        assertEquals(STARTING_BOMBS - 1, manager.getEnemy().getNumBomb());
    }
}
