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
public final class TimedBombarderoSprite extends SimpleBombarderoSprite {

    private final int lifetime;
    private final int ticksPerSprite;
    private long counter = 1;

    /**
     * Creates a new sprite with an assigned lifetime and stores the passed asset
     * to animate.
     * 
     * @param asset the asset to animate.
     * @param framesPerSprite the total number of frames in the asset
     * @param lifetime the lifetime of the sprite, in game ticks.
     */
    public TimedBombarderoSprite(final Image[] asset, final int framesPerSprite, final int lifetime) {
        super(asset, framesPerSprite);
        this.lifetime = lifetime * FOUR_FRAMES_TPS;
        this.ticksPerSprite = framesPerSprite * FOUR_FRAMES_TPS;
    }

    /**
     * Creates a new sprite importing the resource requested before starting. The
     * resource is also resized using the passed lambda function.
     * @param resource the name of the resource to animate
     * @param rg the {@link ResourceGetter} to get the resource with
     * @param imageResizer the function that will be used to resize each image
     * @param framesPerSprite the total number of frames in the asset
     * @param lifetime the lifetime of the sprite, in game ticksthe total number of frames in the asset
     */
    public TimedBombarderoSprite(
            final String resource,
            final ResourceGetter rg,
            final Function<Image, Image> imageResizer,
            final int framesPerSprite,
            final int lifetime) {
        super(resource, rg, imageResizer, framesPerSprite);
        this.lifetime = lifetime;
        this.ticksPerSprite = framesPerSprite;
    }

    @Override
    public void update() {
        counter++;
        if (!isOver()) {
            super.update();
        }
    }

    /**
     * Returns wether the sprite is over or not.
     * @return true, if the sprites is over
     */
    public boolean isOver() {
        return counter >= lifetime || counter >= ticksPerSprite;
    }

}
