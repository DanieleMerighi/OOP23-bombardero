package it.unibo.bombardero.map.impl;

import it.unibo.bombardero.map.api.BombarderoTimer;
import it.unibo.bombardero.map.api.MapManager;
import it.unibo.bombardero.utils.Utils;

public class BombarderoTimerImpl implements BombarderoTimer {

    /* Magic number for formatting the time properly */
    private final static int SECONDS_TO_MILLIS = 1000;
    private final static int MINUTES_TO_SECONDS = 60;
    private final static String TIME_SEPARATOR = ":";
    
    private long startTime = 0l;
    private long timeLeft = 0l;

    @Override
    public void startTimer() {
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void updateTimer() {
        this.timeLeft = System.currentTimeMillis() - startTime;
    }

    public long getTimeLeft() {
        updateTimer();
        return this.timeLeft;
    }

    @Override
    public String getFormattedTimeLeft() {
        double secondsLeft = (System.currentTimeMillis() - this.startTime)/SECONDS_TO_MILLIS;
        return Double.toString(Math.ceil(secondsLeft/BombarderoTimerImpl.MINUTES_TO_SECONDS)) + 
            BombarderoTimerImpl.TIME_SEPARATOR + 
            Double.toString(secondsLeft%BombarderoTimerImpl.MINUTES_TO_SECONDS);
    }
    
}
