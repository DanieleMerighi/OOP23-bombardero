package it.unibo.bombardero.core;

public class BombarderoEngine {
    
    private final static long sleepTime = 16l; // Time during which the thread sleeps, equivalent to about 60FPS

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
