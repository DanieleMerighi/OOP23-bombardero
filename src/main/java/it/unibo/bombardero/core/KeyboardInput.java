package it.unibo.bombardero.core;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.character.Direction;
import it.unibo.bombardero.character.Player;

public class KeyboardInput implements KeyListener{

    private Direction directionMovementPair = Direction.DEFAULT;

    /*
     * MENU, BOMB and POWER-UPs controls
     * 
     * ESC is used to access the game menu and pause the game
     * SPACE is used to place the bomb under the player
     * L is used for the line bomb (punch?)
     * P is used for the remote bomb to explode it
     */
    public void keyTyped(KeyEvent e) {
        switch (e.getKeyChar()) {
            //verifica
            case KeyEvent.VK_ESCAPE:
                //opens the menu
                System.out.println("ESC");
                break;
            case ' ':
                //calls player method to place a bomb
                System.out.println("spazio");
                break;
            case 'l':
                System.out.println("l");
                break;
            case 'p':
                //calls powerup method? check if the player has the remote bomb
                System.out.println("p");
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
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                this.directionMovementPair = Direction.UP;
                break;
            case KeyEvent.VK_A:
                this.directionMovementPair = Direction.LEFT;
                break;
            case KeyEvent.VK_S:
                this.directionMovementPair = Direction.DOWN;
                break;
            case KeyEvent.VK_D:
                this.directionMovementPair = Direction.RIGHT;
                break;
        }
    }

    //If all the movement key get relased the player stops moving
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_A:
            case KeyEvent.VK_S:
            case KeyEvent.VK_D:
                this.directionMovementPair = Direction.DEFAULT;
                break;
        }
    }
    
    public Direction getDirection(){
        return this.directionMovementPair;
    }

}
