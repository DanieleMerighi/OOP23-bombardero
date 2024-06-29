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
    private boolean isGameInterrupted = false;
    private boolean isGameOver = false;

    public BombarderoEngine(Controller controller, BombarderoGraphics graphics, GameManager manager) {
        this.controller = controller;
        this.graphics = graphics;
        this.manager = manager;
    }
    
    public void run() {
        long previousCycleStartTime = System.currentTimeMillis();
        while (!isGameOver) {
            long currentCycleStartTime = System.currentTimeMillis();
            long elapsed = currentCycleStartTime - previousCycleStartTime;
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
        System.out.println("Inizio end");
        isGameOver = true;
        System.out.println("Join fatta");
    }

    @Override
    public synchronized boolean isInterrupted() {
        return this.isGameInterrupted;
    }

    private void waitForNextFrame(long currentCycleStartTime) {
        long currentCycleElapsedTime = System.currentTimeMillis() - currentCycleStartTime;
        if(currentCycleElapsedTime < BombarderoEngine.SLEEP_TIME) {
            try {
                Thread.sleep(BombarderoEngine.SLEEP_TIME - currentCycleElapsedTime);
            } catch (IllegalArgumentException | InterruptedException e) {
                System.out.println("Exception in thread sleeping: " + e.getMessage());
            }
        }
    }
}
