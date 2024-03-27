package it.unibo.bombardero.map.api;

/** 
 * This interface models the in-game countdown timer that triggers the arena's collapse
 * and is displayed on the top portion of the screen
 * @author Federico Bagattoni
 */

public interface BombarderoTimer {

    void startTimer();

    void updateTimer();

    String getTimeLeft();
    
}
