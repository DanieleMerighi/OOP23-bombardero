package it.unibo.bombardero.view.sprites.api;

import java.awt.Image;

/** This interface models a simple sprites, which can be updated and
 * from which you can get the current image from.
 * @author Federico Bagattoni
 */
public interface Sprite {

    /**
     * The advised TPS (ticks per second) for an asset with two frames.
     */
    int TWO_FRAMES_TPS = 16;

    /** 
     * The advised TPS (ticks per second) for an asset with four frames.
     */
    int FOUR_FRAMES_TPS = 7;

    /**
     * Updates the sprites, eventually changing the displayed frame.
     * The rate at which the frames change is implementation-dependant.
     */
    void update();

    /** 
     * Returns the current displayed image.
     * @return the current displayed image for the sprite.
     */
    Image getImage();
}
