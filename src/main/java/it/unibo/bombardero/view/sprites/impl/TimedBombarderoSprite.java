package it.unibo.bombardero.view.sprites.impl;

import java.awt.Image;

import java.util.function.Function;

import it.unibo.bombardero.view.ResourceGetter;

/** 
 * This class represents a Sprite with limited lifetime choosen
 * by the user. 
 * If the assigned lifetime is less than the frames in the asset
 * the sprite will stop before returning every frame. 
 */
public class TimedBombarderoSprite extends SimpleBombarderoSprite {
    
    private final int lifetime;
    private final int framesPerSprite;
    private long counter = 1;

    /**
     * Creates a new sprite with an assigned lifetime. 
     * @param asset
     * @param framesPerSprite
     * @param lifetime
     */
    public TimedBombarderoSprite(final Image[] asset, final int framesPerSprite, final int lifetime) {
        super(asset, framesPerSprite);
        this.lifetime = lifetime * FOUR_FRAMES_TPS;
        this.framesPerSprite = framesPerSprite * 7;
    }

    public TimedBombarderoSprite(
        final String resource,
        final ResourceGetter rg,
        final Function<Image, Image> imageResizer,
        final int framesPerSprite,
        final int lifetime) {
        super(resource, rg, imageResizer, framesPerSprite);
        this.lifetime = lifetime;
        this.framesPerSprite = framesPerSprite;
    }

    @Override
    public void update() {
        counter++;
        if(!isOver()) {
            super.update();
        }
    }

    public boolean isOver() {
        return counter >= lifetime || counter >= framesPerSprite;
    }
    
}
