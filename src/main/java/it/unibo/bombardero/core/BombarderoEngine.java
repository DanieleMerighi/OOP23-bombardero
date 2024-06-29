package it.unibo.bombardero.core;

import it.unibo.bombardero.core.api.Controller;
import it.unibo.bombardero.core.api.Engine;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.view.BombarderoGraphics;

public class BombarderoEngine extends Thread implements Engine {

    private final static long SLEEP_TIME = 16L; // Time during which the thread sleeps, equivalent to about 60FPS
    
    private final GameManager manager;
    private final BombarderoGraphics graphics;
    private Controller controller;
    private boolean isGameInterrupted;
    private boolean isGameOver;

    public BombarderoEngine(final Controller controller, final BombarderoGraphics graphics, final GameManager manager) {
        this.controller = controller;
        this.graphics = graphics;
        this.manager = manager;
    }
    
    @Override
    public void run() {
        long previousCycleStartTime = System.currentTimeMillis();
        while (!isGameOver) {
            final long currentCycleStartTime = System.currentTimeMillis();
            final long elapsed = currentCycleStartTime - previousCycleStartTime;
            if(!isGameInterrupted) {
                manager.updateGame(elapsed);
                graphics.update();
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
    public synchronized void pauseGameLoop() {
        /*try {
            BombarderoEngine.this.wait();
        } catch (InterruptedException e) {
            System.err.println("Exception thrown in main loop: interrupted exception calling Thread.wait()");
            e.printStackTrace();
        } */
        isGameInterrupted = true;
    }

    @Override
    public synchronized void resumeGameLoop() {
        /* if (this.isInterrupted()) {
            this.notify();
        } */
        isGameInterrupted = false;
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
        if(currentCycleElapsedTime < BombarderoEngine.SLEEP_TIME) {
            try {
                Thread.sleep(BombarderoEngine.SLEEP_TIME - currentCycleElapsedTime);
            } catch (IllegalArgumentException | InterruptedException e) {
                System.out.println("Exception in thread sleeping: " + e.getMessage());
            }
        }
    }
}
