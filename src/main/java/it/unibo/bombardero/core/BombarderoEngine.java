package it.unibo.bombardero.core;

import it.unibo.bombardero.core.api.Controller;
import it.unibo.bombardero.core.api.Engine;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.core.impl.BombarderoGameManager;
import it.unibo.bombardero.view.BombarderoGraphics;

public class BombarderoEngine extends Thread implements Engine {

    private final static long sleepTime = 16L; // Time during which the thread sleeps, equivalent to about 60FPS
    
    private GameManager gameManager;
    private BombarderoGraphics graphics;
    private Controller controller;

    public BombarderoEngine(Controller controller, BombarderoGraphics graphics) {
        this.controller = controller;
        this.graphics = graphics;
    }

    @Override
    public GameManager initGameManager() {
        gameManager = new BombarderoGameManager(controller);
        return gameManager;
    }
    
    public void run() {
        long previousCycleStartTime = System.currentTimeMillis();
        while (true) {
            long currentCycleStartTime = System.currentTimeMillis();
            long elapsed = currentCycleStartTime - previousCycleStartTime;
            gameManager.updateGame();
            graphics.update();
            waitForNextFrame(currentCycleStartTime);
            previousCycleStartTime = currentCycleStartTime;
        }
    }

    @Override
    public void startGameLoop() {
        this.start();
    }

    @Override
    public void pauseGameLoop() {
        try {
            this.wait();
        } catch (InterruptedException e) {
            System.err.println("Exception thrown in main loop: interrupted exception calling Thread.wait()");
            e.printStackTrace();
        }
    }

    @Override
    public void resumeGameLoop() {
        if (this.isInterrupted()) {
            this.notify();
        }
    }

    @Override
    public void endGameLoop() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'endGameLoop'");
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
