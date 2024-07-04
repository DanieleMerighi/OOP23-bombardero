package it.unibo.bombardero.view.sprites.api;

import java.awt.Image;

import it.unibo.bombardero.cell.FlameImpl;

/**
 * This interface models a specific sprite designed
 * for holding correctly the flames in the game.
 * <p>
 * Due to the nature of the flames it is not optimal to
 * hold a sprite for each one, that would mean updating each sprite
 * independentely.
 * By implementing this interface the game only needs to instantiate
 * a single class and ask for each cell containing a flame the right
 * image to display.
 * 
 * @author Federico Bagattoni
 */
public interface FlameSprite {

    /**
     * Return the image associated to the {@link #Flame.FlameType} passed
     * and that represents the life moment of such flame.
     * <p>
     * For example if the flame is about to expire then the method will
     * return an image different from the one it would return if the flame
     * was in the middle of its life.
     * 
     * @param elapsed   the time passed since the flame spawned
     * @param flameType the shape of flame that has to be returned
     * @return the image of the flame in the specific form and life moment
     */
    Image getImage(long elapsed, FlameImpl.FlameType flameType);

}
