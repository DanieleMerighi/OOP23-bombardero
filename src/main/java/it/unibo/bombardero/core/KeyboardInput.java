package it.unibo.bombardero.core;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import it.unibo.bombardero.character.Direction;
import it.unibo.bombardero.core.api.Controller;

/**
 * This class implements KeyListener to handle keyboard input for the game.
 * It processes key events to control the player's movements and actions 
 * such as placing bombs and using power-ups.
 * This class interacts with the Controller to update the game state based on which keys get pressed.
 * 
 * @author Jacopo Turchi
 */
public class KeyboardInput implements KeyListener {

    // Controller instance to handle player actions
    private final Controller controller;

    // Current direction of player movement
    private Direction directionMovementPair = Direction.DEFAULT;

    /**
     * Constructs a new KeyboardInput istance.
     *
     * @param controller The controller instance to handle player actions.
     */
    public KeyboardInput(final Controller controller) {
        this.controller = controller;
    }
 
    /*
     * MENU, BOMB and POWER-UPs controls
     * 
     * ESC is used to access the game menu and pause the game
     * SPACE is used to place the bomb under the player
     * L is used for the line bomb (punch?)
     * P is used for the remote bomb to explode it
     */
    /**
     * Invoked when a key has been typed.
     * This method handles non-movement related key presses, like menu access, bomb placement, or power-up activation.
     * 
     * @param e The KeyEvent representing the key typed event.
     */
    public void keyTyped(final KeyEvent e) {
        switch (e.getKeyChar()) {
            case KeyEvent.VK_ESCAPE:
                // opens the menu
                // System.out.println("ESC");
                break;
            case KeyEvent.VK_SPACE:
                // calls player method to place a bomb
                // System.out.println("spazio");
                // controller.getMainPlayer().placeBomb();
                break;
            case 'l':
            case 'L':
                // calls powerup method? check if the player has the power-up
                // System.out.println("l");
                break;
            case 'p':
            case 'P':
                // calls powerup method? check if the player has the remote bomb
                // System.out.println("p");
                break;
            default:
                break;
        }
    }

    /*
     * It's used the classic WASD configuration for the player movement:
     * W makes the player go UP
     * A makes the player go LEFT
     * S makes the player go SOUTH
     * D makes the player go RIGHT
     */
    // every time a key is pressed the player direction is set
     /**
     * Invoked when a key has been pressed.
     * This method handles movement-related key presses (WASD configuration).
     * 
     * @param e The KeyEvent representing the key pressed event.
     */
    public void keyPressed(final KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                this.directionMovementPair = Direction.UP;
                // System.out.println("UP");
                break;
            case KeyEvent.VK_A:
                this.directionMovementPair = Direction.LEFT;
                // System.out.println("LEFT");
                break;
            case KeyEvent.VK_S:
                this.directionMovementPair = Direction.DOWN;
                // System.out.println("DOWN");
                break;
            case KeyEvent.VK_D:
                this.directionMovementPair = Direction.RIGHT;
                // System.out.println("RIGHT");
                break;
            default:
                break;
        }
    }

    // If all the movement key get relased the player stops moving
     /**
     * Invoked when a key has been released.
     * Resets the movement direction when movement keys are released.
     * 
     * @param e The KeyEvent representing the key released event.
     */
    public void keyReleased(final KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_A:
            case KeyEvent.VK_S:
            case KeyEvent.VK_D:
                // System.out.println("relased");
                this.directionMovementPair = Direction.DEFAULT;
                break;
            default:
                break;
        }
    }

    /**
     * Gets the current direction of movement.
     * 
     * @return The current direction of movement.
     */
    public Direction getDirection() {
        return this.directionMovementPair;
    }

    /* 
     * al posto di direction e get direction quando viene premuto un tasto 
     * faccio direttamente il set della player direction così sa dove andare
    */

}
