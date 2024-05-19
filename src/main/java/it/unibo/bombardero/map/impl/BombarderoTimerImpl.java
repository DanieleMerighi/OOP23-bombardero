package it.unibo.bombardero.map.impl;

import it.unibo.bombardero.map.api.BombarderoTimer;

import java.text.SimpleDateFormat;
import java.util.Date;

/** 
 * This class is a simple timer that keeps track of the time left
 * and returns it in both milliseconds and formatted {@code mm:ss}
 * format.
 * <p> The game time is hard-coded into the class and is equal to two minutes.
 */
public final class BombarderoTimerImpl implements BombarderoTimer {

    private final static long GAME_TIME = 120000L;
    private final static SimpleDateFormat format = new SimpleDateFormat("mm:ss");

    private long startTime = 0L;
    private long timeLeft = 0L;

    @Override
    public void startTimer() {
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void updateTimer() {
        this.timeLeft = GAME_TIME - (System.currentTimeMillis() - startTime);
    }

    @Override
    public long getTimeLeft() {
        this.updateTimer();
        return this.timeLeft;
    }

    @Override
    public String getFormattedTimeLeft() {
        Date date = new Date(timeLeft);
        return format.format(date);
    }
    
}
