package it.bombardero;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.event.KeyEvent;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.bombardero.cell.powerup.api.PowerUp.PowerUpType;
import it.unibo.bombardero.character.Direction;
import it.unibo.bombardero.core.KeyboardInput;
import it.unibo.bombardero.core.api.Controller;


/**
 * Test class for KeyboardInput to verify the handling of keyboard events.
 */
class TestInput {

    private KeyboardInput keyboardInput;
    private Controller controller;
    private KeyEvent keyEvent;
    private int playerIndex;

    /**
     * Sets up the test environment before each test.
     */
    @BeforeEach
    void setUp() {
        // Initialize the test controller
        this.controller = new ControllerForTesting();
        this.playerIndex = 0;
        // Initialize KeyboardInput with the test controller
        this.keyboardInput = new KeyboardInput(controller, playerIndex);
    }

    /**
     * Tests the ESC key press.
     */
    @Test
    void testKeyTypedEscape() {
        // Simulate pressing the ESC key (Menu button)
        keyEvent = new KeyEvent(new java.awt.Component() {
        }, KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED, (char) KeyEvent.VK_ESCAPE);
        keyboardInput.keyTyped(keyEvent);
        // Verify that the escape method was called on the controller
        assertTrue(controller.isGamePaused());
    }

    /**
     * Tests the SPACE key press.
     */
    @Test
    void testKeyTypedSpace() {
        pressSpace();

        // Verify that the setHasToPlaceBomb method was called on the player
        assertTrue(controller.getPlayers().get(playerIndex).isHasToPlaceBomb());
    }

    /**
     * Tests the 'L' key press.
     */
    @Test
    void testKeyTypedLineBomb() {
        // Set line bomb powerup to true
        controller.getPlayers().get(playerIndex).setLineBomb(true);
        // Simulate pressing the 'L' key (place line bomb button)
        keyEvent = new KeyEvent(new java.awt.Component() {
        }, KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED, 'L');
        keyboardInput.keyTyped(keyEvent);

        // Verify that the setHasToPlaceLineBomb method was called on the player
        assertTrue(controller.getPlayers().get(playerIndex).isHasToPlaceLineBomb());
    }

    /**
     * Tests the 'P' key press.
     */
    @Test
    void testKeyTypedRemoteBomb() {
        // Set type bomb to remote bomb
        controller.getPlayers().get(playerIndex).setBombType(Optional.of(PowerUpType.REMOTE_BOMB));
        // Simulate pressing the 'P' key (explode remote bomb button)
        keyEvent = new KeyEvent(new java.awt.Component() {
        }, KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED, 'P');
        keyboardInput.keyTyped(keyEvent);

        // Verify that the setHasToExplodeRemoteBomb method was called on the player
        assertTrue(controller.getPlayers().get(playerIndex).isHasToExplodeRemoteBomb());
    }

    /**
     * Tests the 'W' key press and release.
     */
    @Test
    void testKeyW() {
        pressW();
        // Verify that the player is not stationary after pressing 'W'
        assertFalse(controller.getPlayers().get(playerIndex).isStationary());
        // Verify that the player is facing the UP direction
        assertEquals(Direction.UP, controller.getPlayers().get(playerIndex).getFacingDirection());

        releaseW();
        // Verify that the player is stationary after releasing 'W'
        assertTrue(controller.getPlayers().get(playerIndex).isStationary());
        // Verify that the player is still facing the UP direction
        assertEquals(Direction.UP, controller.getPlayers().get(playerIndex).getFacingDirection());
    }

    /**
     * Tests the 'A' key press and release.
     */
    @Test
    void testKeyA() {
        pressA();
        // Verify that the player is not stationary after pressing 'A'
        assertFalse(controller.getPlayers().get(playerIndex).isStationary());
        // Verify that the player is facing the LEFT direction
        assertEquals(Direction.LEFT, controller.getPlayers().get(playerIndex).getFacingDirection());

        releaseA();
        // Verify that the player is stationary after releasing 'A'
        assertTrue(controller.getPlayers().get(playerIndex).isStationary());
        // Verify that the player is still facing the LEFT direction
        assertEquals(Direction.LEFT, controller.getPlayers().get(playerIndex).getFacingDirection());
    }

    /**
     * Tests the 'S' key press and release.
     */
    @Test
    void testKeyS() {
        pressS();
        // Verify that the player is not stationary after pressing 'S'
        assertFalse(controller.getPlayers().get(playerIndex).isStationary());
        // Verify that the player is facing the DOWN direction
        assertEquals(Direction.DOWN, controller.getPlayers().get(playerIndex).getFacingDirection());

        releaseS();
        // Verify that the player is stationary after releasing 'S'
        assertTrue(controller.getPlayers().get(playerIndex).isStationary());
        // Verify that the player is still facing the DOWN direction
        assertEquals(Direction.DOWN, controller.getPlayers().get(playerIndex).getFacingDirection());
    }

    /**
     * Tests the 'D' key press and release.
     */
    @Test
    void testKeyD() {
        pressD();
        // Verify that the player is not stationary after pressing 'D'
        assertFalse(controller.getPlayers().get(playerIndex).isStationary());
        // Verify that the player is facing the RIGHT direction
        assertEquals(Direction.RIGHT, controller.getPlayers().get(playerIndex).getFacingDirection());

        releaseD();
        // Verify that the player is stationary after releasing 'D'
        assertTrue(controller.getPlayers().get(playerIndex).isStationary());
        // Verify that the player is still facing the RIGHT direction
        assertEquals(Direction.RIGHT, controller.getPlayers().get(playerIndex).getFacingDirection());
    }

    /**
     * Tests a specific sequence of key presses and releases for complex movement.
     */
    @Test
    void testParticularMovement() {
        pressW();
        pressA();
        // Verify that the player is not stationary
        assertFalse(controller.getPlayers().get(playerIndex).isStationary());
        // Verify that the player is facing the LEFT direction
        assertEquals(Direction.LEFT, controller.getPlayers().get(playerIndex).getFacingDirection());

        releaseA();
        // Verify that the player is not stationary
        assertFalse(controller.getPlayers().get(playerIndex).isStationary());
        // Verify that the player is facing the UP direction
        assertEquals(Direction.UP, controller.getPlayers().get(playerIndex).getFacingDirection());

        releaseW();
        // Verify that the player is stationary
        assertTrue(controller.getPlayers().get(playerIndex).isStationary());
        // Verify that the player is still facing the UP direction
        assertEquals(Direction.UP, controller.getPlayers().get(playerIndex).getFacingDirection());
    }

    /**
     * Tests placing a bomb while the player is moving.
     */
    @Test
    void testPlacingBombWhileMoving() {
        pressW();
        pressSpace();
        // Verify that the player is not stationary
        assertFalse(controller.getPlayers().get(playerIndex).isStationary());
        // Verify that the player is facing the UP direction
        assertEquals(Direction.UP, controller.getPlayers().get(playerIndex).getFacingDirection());
        // Verify that the setHasToPlaceBomb method was called on the player
        assertTrue(controller.getPlayers().get(playerIndex).isHasToPlaceBomb());
    }

    /**
     * Simulates pressing the 'W' key.
     */
    private void pressW() {
        // Simulate pressing the W key (the player goes up)
        keyEvent = new KeyEvent(new java.awt.Component() {
        }, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_W, 'W');
        keyboardInput.keyPressed(keyEvent);
    }

    /**
     * Simulates pressing the 'A' key.
     */
    private void pressA() {
        // Simulate pressing the A key (the player goes left)
        keyEvent = new KeyEvent(new java.awt.Component() {
        }, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_A, 'A');
        keyboardInput.keyPressed(keyEvent);
    }

    /**
     * Simulates pressing the 'S' key.
     */
    private void pressS() {
        // Simulate pressing the S key (the player goes down)
        keyEvent = new KeyEvent(new java.awt.Component() {
        }, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_S, 'S');
        keyboardInput.keyPressed(keyEvent);
    }

    /**
     * Simulates pressing the 'D' key.
     */
    private void pressD() {
        // Simulate pressing the D key (the player goes right)
        keyEvent = new KeyEvent(new java.awt.Component() {
        }, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_D, 'D');
        keyboardInput.keyPressed(keyEvent);
    }

    /**
     * Simulates realising the 'W' key.
     */
    private void releaseW() {
        // Simulate releasing the W key (the player stops going up)
        keyEvent = new KeyEvent(new java.awt.Component() {
        }, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_W, 'W');
        keyboardInput.keyReleased(keyEvent);
    }

    /**
     * Simulates realising the 'A' key.
     */
    private void releaseA() {
        // Simulate releasing the A key (the player stops going left)
        keyEvent = new KeyEvent(new java.awt.Component() {
        }, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_A, 'A');
        keyboardInput.keyReleased(keyEvent);
    }

    /**
     * Simulates realising the 'S' key.
     */
    private void releaseS() {
        // Simulate releasing the S key (the player stops going down)
        keyEvent = new KeyEvent(new java.awt.Component() {
        }, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_S, 'S');
        keyboardInput.keyReleased(keyEvent);
    }

    /**
     * Simulates realising the 'D' key.
     */
    private void releaseD() {
        // Simulate releasing the D key (the player stops going right)
        keyEvent = new KeyEvent(new java.awt.Component() {
        }, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_D, 'D');
        keyboardInput.keyReleased(keyEvent);
    }

    /**
     * Simulates pressing the SPACE key.
     */
    private void pressSpace() {
        // Simulates pressing the SPACE key (place bomb button)
        keyEvent = new KeyEvent(new java.awt.Component() {
        }, KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED, ' ');
        keyboardInput.keyTyped(keyEvent);
    }

}
