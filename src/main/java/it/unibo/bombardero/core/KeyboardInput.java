package it.unibo.bombardero.core;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import it.unibo.bombardero.cell.powerUp.impl.*;
import it.unibo.bombardero.character.Direction;
import it.unibo.bombardero.core.api.Controller;

/**
 * This class implements KeyListener to handle keyboard input for the game.
 * It processes key events to control the player's movements and actions
 * such as placing bombs and using power-ups.
 * This class interacts with the Controller to update the game state based on
 * which keys get pressed.
 * 
 * @author Jacopo Turchi
 */
public class KeyboardInput implements KeyListener {

    // Controller instance to handle player actions
    private final Controller controller;

    // Directions for checks
    private boolean UP;
    private boolean LEFT;
    private boolean DOWN;
    private boolean RIGHT;

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
     * This method handles non-movement related key presses, like menu access, bomb
     * placement, or power-up activation.
     * 
     * @param e The KeyEvent representing the key typed event.
     */
    public void keyTyped(final KeyEvent e) {
        switch (e.getKeyChar()) {
            // Opens the menu
            case KeyEvent.VK_ESCAPE -> controller.escape();
                // System.out.println("ESC");
            // calls player method to place a bomb
            case KeyEvent.VK_SPACE -> controller.getMainPlayer().placeBomb();
            // System.out.println("spazio");
            // calls powerup method to use line bomb powerup
            case 'l', 'L' -> {// calls powerup method? check if the player has the power-up
                PowerUpImpl.placeLineBomb(controller.getMainPlayer(), controller.getMap());
                System.out.println("l");}
            // calls powerup method to explode remote bomb powerup
            case 'p', 'P' -> // calls powerup method? check if the player has the remote bomb
                System.out.println("p");
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
    public void keyPressed(final KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> {
                UP = true;
                controller.getMainPlayer().setFacingDirection(Direction.UP);
                // System.out.println("UP");
            }
            case KeyEvent.VK_A -> {
                LEFT = true;
                controller.getMainPlayer().setFacingDirection(Direction.LEFT);
                // System.out.println("LEFT");
            }
            case KeyEvent.VK_S -> {
                DOWN = true;
                controller.getMainPlayer().setFacingDirection(Direction.DOWN);
                // System.out.println("DOWN");
            }
            case KeyEvent.VK_D -> {
                RIGHT = true;
                controller.getMainPlayer().setFacingDirection(Direction.RIGHT);
                // System.out.println("RIGHT");
            }
            default -> {
            }
        }
    }

    /*
     * If all the movement key get relased the direcion is set to default
     * and the player stops moving
     */
    /**
     * Invoked when a key has been released.
     * Resets the movement direction when movement keys are released.
     * 
     * @param e The KeyEvent representing the key released event.
     */
    public void keyReleased(final KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W ->
                UP = false;
            case KeyEvent.VK_A ->
                LEFT = false;
            case KeyEvent.VK_S ->
                DOWN = false;
            case KeyEvent.VK_D ->
                RIGHT = false;
            default -> {
            }
        }
        if (!UP && !LEFT && !DOWN && !RIGHT)
            controller.getMainPlayer().setFacingDirection(Direction.DEFAULT);
    }
}
