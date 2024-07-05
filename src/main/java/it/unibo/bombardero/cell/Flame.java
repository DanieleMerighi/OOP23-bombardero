package it.unibo.bombardero.cell;

import it.unibo.bombardero.cell.FlameImpl.FlameType;
import it.unibo.bombardero.map.api.GenPair;

/**
 * Rapresnt a Flame so when a Charecter is over it he dies.
 */
public interface Flame extends Cell {

    /**
     * add timeElapsed to the timer.
     * 
     * @param timeElapsed
     */
    void update(final long timeElapsed);

    /**
     * Flames after 0.5 sec expires.
     * 
     * @return true if the timer is over 0.5 sec
     */
    boolean isExpired();

    /**
     * 
     * @return the position in the map
     */
    GenPair<Integer, Integer> getPos();

    /**
     * 
     * @return the flame type
     */
    FlameType getFlameType();

    /**
     * 
     * @return the time passed after it's creation
     */
    long getTimePassed();
}
