package it.bombardero;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.bombardero.map.api.BombarderoTimer;
import it.unibo.bombardero.map.impl.BombarderoTimerImpl;

/** 
 * @author Federico Bagattoni
 */
public class TestTimer {

    private BombarderoTimer timer;
    private long startTimeForTest;

    @BeforeEach void init() {
        this.timer = new BombarderoTimerImpl();
        this.startTimeForTest = System.currentTimeMillis();
        timer.startTimer();
    }
    
    @Test void testGetTimeLeftAsLong() {
        this.timer.updateTimer();
        assertEquals(this.timer.getTimeLeft(), System.currentTimeMillis() - this.startTimeForTest);
    }

    /**
     * This method prints out the formatted result for visual verification:
     * the string has to be something on the line of "4:36"
     */
    @Test void testGetTimeLeftAsString() {
        this.timer.updateTimer();
        System.out.println(this.timer.getFormattedTimeLeft());
    }

}
