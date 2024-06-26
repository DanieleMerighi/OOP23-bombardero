package it.unibo.bombardero.core;

import it.unibo.bombardero.core.api.Controller;
import it.unibo.bombardero.core.api.Engine;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.core.impl.BombarderoController;
import it.unibo.bombardero.view.BombarderoGraphics;
/**
 * This class implements the concept of the game engine expressed 
 * in the {@link Engine} interface; to do so it extends the Thread 
 * class so that the {@link  #run()} method can be run in a separate
 * thread, as per the interface's instructions.
 * <p>
 * This class has the ability to pause the game loop, if necessary,
 * and obviously, stop it.
 */
public final class BombarderoEngine extends Thread implements Engine {

    private final static long SLEEP_TIME = 16L; // Time during which the thread sleeps, equivalent to about 60FPS
    
    //private final GameManager manager;
    //private final BombarderoGraphics graphics;
    private Controller controller;
    private boolean isGameInterrupted;
    private boolean isGameOver;

    /**
     * Creates a new BombarderoEngine, associating it to a Controller 
     * , a Graphics engine and a GameManager.
     * @param controller
     * @param graphics
     * @param manager
     */
    public BombarderoEngine() {
        this.controller = new BombarderoController();
        startGameLoop();
    }

    @Override
    public void run() {
        long previousCycleStartTime = System.currentTimeMillis();
        while (!isGameOver) {
            final long currentCycleStartTime = System.currentTimeMillis();
            final long elapsed = currentCycleStartTime - previousCycleStartTime;
            if (controller.isGameStarted() && !controller.isGamePaused()) {
                controller.updateModel(elapsed);
            }
            waitForNextFrame(currentCycleStartTime);
            previousCycleStartTime = currentCycleStartTime;
        }
        this.interrupt();
    }

    @Override
    public void startGameLoop() {
        isGameOver = false;
        this.start();
    }

    @Override
    public void endGameLoop() {
        isGameOver = true;
    }

    @Override
    public synchronized boolean isInterrupted() {
        return this.isGameInterrupted;
    }

    private void waitForNextFrame(final long currentCycleStartTime) {
        final long currentCycleElapsedTime = System.currentTimeMillis() - currentCycleStartTime;
        if (currentCycleElapsedTime < BombarderoEngine.SLEEP_TIME) {
            try {
                Thread.sleep(BombarderoEngine.SLEEP_TIME - currentCycleElapsedTime);
            } catch (IllegalArgumentException | InterruptedException e) {
                System.out.println("Exception in thread sleeping: " + e.getMessage());
            }
        }
    }

}