package it.unibo.bombardero.map.impl;

import it.unibo.bombardero.map.api.BombarderoTimer;
import it.unibo.bombardero.map.api.MapManager;
import it.unibo.bombardero.utils.Utils;

public class BombarderoTimerImpl implements BombarderoTimer {
    
    private long startTime = 0l;
    private final MapManager manager;
    private final static int SECONDS_TO_MILLIS = 1000;
    private final static int MINUTES_TO_SECONDS = 60;
    private final static String TIME_SEPARATOR = ":";

    public BombarderoTimerImpl(MapManager manager) {
        this.manager = manager;
    }

    @Override
    public void startTimer() {
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void updateTimer() {
        if(System.currentTimeMillis() - startTime >= Utils.GAME_TIME) {
            manager.triggerArenaCollpse();
        }
    }

    @Override
    public String getTimeLeft() {
        double secondsLeft = (System.currentTimeMillis() - this.startTime)/SECONDS_TO_MILLIS;
        return Double.toString(Math.ceil(secondsLeft/BombarderoTimerImpl.MINUTES_TO_SECONDS)) + 
            BombarderoTimerImpl.TIME_SEPARATOR + 
            Double.toString(secondsLeft%BombarderoTimerImpl.MINUTES_TO_SECONDS);
    }
    
}
