package it.unibo.bombardero.core;

import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.view.BombarderoGraphics;

public class BombarderoEngine {
    
    private final static long sleepTime = 16l; // Time during which the thread sleeps, equivalent to about 60FPS
    private GameManager gameManager;
    private BombarderoGraphics graphics;

    public BombarderoEngine(GameManager manager, BombarderoGraphics graphics) {
        this.gameManager = manager;
        this.graphics = graphics;
    }

    public void initGame() {
        
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
