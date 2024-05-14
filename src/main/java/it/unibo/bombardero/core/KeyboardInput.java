package it.unibo.bombardero.core;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import it.unibo.bombardero.character.Direction;
import it.unibo.bombardero.core.api.Controller;

public class KeyboardInput implements KeyListener {

    // controller
    private final Controller controller;
    // 
    private Direction directionMovementPair = Direction.DEFAULT;

    public KeyboardInput (final Controller controller) {
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
    public void keyTyped(final KeyEvent e) {
        switch (e.getKeyChar()) {
            case KeyEvent.VK_ESCAPE:
                // opens the menu
                // System.out.println("ESC");
                break;
            case KeyEvent.VK_SPACE:
                // calls player method to place a bomb
                // System.out.println("spazio");
                controller.getMainPlayer().placeBomb();
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

    public Direction getDirection() {
        return this.directionMovementPair;
    }

    /* 
     * al posto di direction e get direction quando viene premuto un tasto 
     * faccio direttamente il set della player direction così sa dove andare
    */

}
