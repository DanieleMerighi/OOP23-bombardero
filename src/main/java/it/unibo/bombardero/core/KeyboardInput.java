package it.unibo.bombardero.core;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import it.unibo.bombardero.character.Direction;
import it.unibo.bombardero.core.api.Controller;

/**
 * This class implements KeyListener to handle keyboard input for the game.
 * It processes key events to control the player's movements and actions
 * such as placing bombs and using power-ups.
 * This class interacts with the Controller to update the game state based on
 * which keys get pressed.
 */
public class KeyboardInput implements KeyListener {

    // Controller instance to handle player actions
    private final Controller controller;

    // Directions for checks
    private boolean up;
    private boolean left;
    private boolean down;
    private boolean right;

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
     * L is used for the line bomb
     * P is used for the remote bomb to explode it
     */
    /**
     * Invoked when a key has been typed.
     * This method handles non-movement related key presses, like menu access, bomb
     * placement, or power-up activation.
     * 
     * @param e The KeyEvent representing the key typed event.
     */
    @Override
    public void keyTyped(final KeyEvent e) {
        switch (e.getKeyChar()) {
            // Opens the menu
            case KeyEvent.VK_ESCAPE -> controller.escape();
            // System.out.println("ESC");
            // calls player method to place a bomb
            case KeyEvent.VK_SPACE -> controller.getMainPlayer().setHasToPlaceBomb(true);
            // System.out.println("spazio");
            // calls powerup method to use line bomb powerup
            case 'l', 'L' -> {
                controller.getMainPlayer().setHasToPlaceLineBomb(true);
                // System.out.println("l");
            }
            // calls player method to explode remote bomb powerup
            case 'p', 'P' -> {
                controller.getMainPlayer().setHasToExplodeRemoteBomb(true);
                // System.out.println("p");
            }
            default -> {
            }
        }
    }

    /*
     * It's used the classic WASD configuration for the player movement:
     * W makes the player go UP
     * A makes the player go LEFT
     * S makes the player go SOUTH
     * D makes the player go RIGHT
     * 
     * every time one of the WASD key is pressed the player facing direction is set
     */
    /**
     * Invoked when a key has been pressed.
     * This method handles movement-related key presses (WASD configuration).
     * 
     * @param e The KeyEvent representing the key pressed event.
     */
    @Override
    public void keyPressed(final KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> {
                up = true;
                controller.getMainPlayer().setStationary(false);
                controller.getMainPlayer().setFacingDirection(Direction.UP);
                // System.out.println("UP");
            }
            case KeyEvent.VK_A -> {
                left = true;
                controller.getMainPlayer().setStationary(false);
                controller.getMainPlayer().setFacingDirection(Direction.LEFT);
                // System.out.println("LEFT");
            }
            case KeyEvent.VK_S -> {
                down = true;
                controller.getMainPlayer().setStationary(false);
                controller.getMainPlayer().setFacingDirection(Direction.DOWN);
                // System.out.println("DOWN");
            }
            case KeyEvent.VK_D -> {
                right = true;
                controller.getMainPlayer().setStationary(false);
                controller.getMainPlayer().setFacingDirection(Direction.RIGHT);
                // System.out.println("RIGHT");
            }
            default -> {
            }
        }
    }

    /**
     * Invoked when a key has been released.
     * Resets the movement direction when movement keys are released.
     * 
     * @param e The KeyEvent representing the key released event.
     */
    @Override
    public void keyReleased(final KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> {
                up = false;
            }
            case KeyEvent.VK_A -> {
                left = false;
            }
            case KeyEvent.VK_S -> {
                down = false;
            }
            case KeyEvent.VK_D -> {
                right = false;
            }
            default -> {
            }
        }
        checkIfStationary();
        checkIfAnotherDirectionIsPressed();
    }

    /**
     * If all the movement keys get released, stationary is set to true.
     */
    private void checkIfStationary() {
        if (!up && !left && !down && !right) {
            controller.getMainPlayer().setStationary(true);
        }
    }

    /**
     * When a key get released, it checks if a key was being pressed before
     * and sets the facing to that corrisponding direction.
     */
    private void checkIfAnotherDirectionIsPressed() {
        if (up) {
            controller.getMainPlayer().setFacingDirection(Direction.UP);
        }
        if (left) {
            controller.getMainPlayer().setFacingDirection(Direction.LEFT);
        }
        if (down) {
            controller.getMainPlayer().setFacingDirection(Direction.DOWN);
        }
        if (right) {
            controller.getMainPlayer().setFacingDirection(Direction.RIGHT);
        }
    }
}
