package it.unibo.bombardero.core;

import it.unibo.bombardero.core.api.Controller;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.core.impl.BombarderoGameManager;
import it.unibo.bombardero.view.BombarderoGraphics;

public class BombarderoEngine {
    
    private final static long sleepTime = 16l; // Time during which the thread sleeps, equivalent to about 60FPS
    private GameManager gameManager;
    private BombarderoGraphics graphics;
    private Controller controller;

    public BombarderoEngine(Controller controller, BombarderoGraphics graphics) {
        this.controller = controller;
        this.graphics = graphics;
    }

    public void initGame() {
        this.gameManager = new BombarderoGameManager(controller);
    }   
    
    public void mainLoop() {
        
    }

    private void waitForNextFrame(long currentCycleStartTime) {
        long currentCycleElapsedTime = System.currentTimeMillis() - currentCycleStartTime;
        if(currentCycleElapsedTime < BombarderoEngine.sleepTime) {
            try {
                Thread.sleep(BombarderoEngine.sleepTime - currentCycleElapsedTime);
            } catch (IllegalArgumentException | InterruptedException e) {
                System.out.println("Exception in thread sleeping: " + e.getMessage());
            }
        }
    }
}
