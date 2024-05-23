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
    private Boolean isGameInterrupted = false;

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
                if(!isGameInterrupted) {
                    gameManager.updateGame();
                    graphics.update();
                }
                waitForNextFrame(currentCycleStartTime);
                previousCycleStartTime = currentCycleStartTime;
        }
    }

    @Override
    public void startGameLoop() {
        this.start();
    }

    @Override
    public synchronized void pauseGameLoop() {
        /*try {
            BombarderoEngine.this.wait();
        } catch (InterruptedException e) {
            System.err.println("Exception thrown in main loop: interrupted exception calling Thread.wait()");
            e.printStackTrace();
        } */
        isGameInterrupted = true;
        System.out.println("Game interrupted");
    }

    @Override
    public synchronized void resumeGameLoop() {
        /* if (this.isInterrupted()) {
            this.notify();
        } */
        isGameInterrupted = false;
        System.out.println("Game resumed");
    }

    @Override
    public void endGameLoop() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'endGameLoop'");
    }

    @Override
    public synchronized boolean isInterrupted() {
        return this.isGameInterrupted;
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
