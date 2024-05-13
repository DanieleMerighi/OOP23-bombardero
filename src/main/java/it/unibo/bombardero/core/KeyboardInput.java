package it.unibo.bombardero.core;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.character.Direction;
import it.unibo.bombardero.character.Player;

public class KeyboardInput implements KeyListener{

    Direction directionMovementPair = Direction.DEFAULT;

    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
    }

    // 
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                directionMovementPair = Direction.UP;
                break;
            case KeyEvent.VK_A:
                directionMovementPair = Direction.LEFT;
                break;
            case KeyEvent.VK_S:
                directionMovementPair = Direction.DOWN;
                break;
            case KeyEvent.VK_D:
                directionMovementPair = Direction.RIGHT;
                break;
            default:
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
                directionMovementPair = Direction.DEFAULT;
                break;
            default:
                break;
        }
    }
    
    public Direction getDirection(){
        return directionMovementPair;
    }

}
