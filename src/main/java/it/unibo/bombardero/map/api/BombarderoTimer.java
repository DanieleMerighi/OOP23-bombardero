package it.unibo.bombardero.map.api;

/** 
 * This interface models the in-game countdown timer that triggers the arena's collapse
 * and is displayed on the top portion of the screen
 * @author Federico Bagattoni
 */

public interface BombarderoTimer {

    /**
     * Starts keeping the time
     */
    void startTimer();

    /**
     * Checks if the time is over, if such it alerts the @manager 
     * so the proper changes can be made to the map. 
     * This method is assumed to be called peiodically
     */
    void updateTimer();

    /**
     * Formats the time properly for displaying as such: "MM:SS"
     * @return the properly formatted time
     */
    String getTimeLeft();
    
}
