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
     * Updates the timer
     * This method is assumed to be called peiodically and after the @startTimer method
     * has been called at least once. 
     */
    void updateTimer();

    /** 
     * Returns the time left for the logic to check, no formatting is done
     * @return the time left
    */
    long getTimeLeft();

    /**
     * Formats the time properly for displaying as such: "MM:SS" and 
     * returns a String type
     * @return the properly formatted time
     */
    String getFormattedTimeLeft();
    
}
