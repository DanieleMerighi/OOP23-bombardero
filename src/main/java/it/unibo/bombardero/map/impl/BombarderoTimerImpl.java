package it.unibo.bombardero.map.impl;

import it.unibo.bombardero.map.api.BombarderoTimer;

/** 
 * This class is a simple timer that keeps track of the time left
 * and returns it in both milliseconds and formatted {@code mm:ss}
 * format.
 * <p> The game time is hard-coded into the class and is equal to two minutes.
 */
public final class BombarderoTimerImpl implements BombarderoTimer {

    private final static long GAME_TIME = 120000L;
    private final static long GAME_OVER = 0L;

    private long startTime = 0L;
    private long timeLeft = 0L;

    @Override
    public void startTimer() {
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void updateTimer() {
        this.timeLeft = GAME_TIME - (System.currentTimeMillis() - startTime);
        if (timeLeft <= 0) {
            timeLeft = 0;
        }
    }

    @Override
    public long getTimeLeft() {
        return this.timeLeft;
    }
    
    @Override
    public boolean isOver() {
        return timeLeft == GAME_OVER;
    }
    
}
