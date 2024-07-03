package it.unibo.bombardero.view.sprites.impl;

import java.awt.Image;
import java.util.function.Function;

import it.unibo.bombardero.character.Direction;
import it.unibo.bombardero.view.ResourceGetter;
import it.unibo.bombardero.view.sprites.api.Sprite;

/**
 * This class represents an immutable single instance of a animated cell, that
 * requires being updated
 * and returns a frame when requested.
 * <p>
 * This class offers the possibility to pass the asset to the constructor
 * or import it.
 * 
 * @author Federico Bagattoni
 */
public class SimpleBombarderoSprite implements Sprite {

    private final Image[] asset;
    private int counter;
    private int ticksPerFrame;
    private int currentFrame;
    private int framesPerSprite;

    /**
     * Creates a new {@link SimpleBombarderoSprite} from the passed
     * asset and containing the specified number of frames.
     * 
     * @param asset           the asset that will be displayed
     * @param framesPerSprite the number of frames contained in the asset
     */
    public SimpleBombarderoSprite(final Image[] asset, final int framesPerSprite) {
        this.asset = new Image[framesPerSprite];
        System.arraycopy(asset, 0, this.asset, 0, framesPerSprite);
        this.framesPerSprite = framesPerSprite;
        ticksPerFrame = framesPerSprite == 2 ? TWO_FRAMES_TPS : FOUR_FRAMES_TPS;
    }

    /**
     * Creates a new Sprite, storing the passed asset that is already been imported
     * and eventually resized and setting the passed TPS without calculating it.
     * @param asset the asset to animate
     * @param framesPerSprite the frames contained in the asset
     * @param ticksPerFrame the ticks per frame by which the asset will be animated
     */
    public SimpleBombarderoSprite(final Image[] asset, final int framesPerSprite, final int ticksPerFrame) {
        this.asset = new Image[framesPerSprite];
        System.arraycopy(asset, 0, this.asset, 0, framesPerSprite);
        this.framesPerSprite = framesPerSprite;
        this.ticksPerFrame = ticksPerFrame;
    }

    /**
     * Creates a new {@link SimpleBombarderoSprite} importin the asset using
     * the objects and parameters passed as arugments.
     * 
     * @param resource        the name of the resource to import
     * @param rg              the {@link #ResourceGetter} that will be used to
     *                        import the assets
     * @param imageResizer    the function used to resize the assets
     * @param framesPerSprite the number of frames contained in the asset
     */
    public SimpleBombarderoSprite(
            final String resource,
            final ResourceGetter rg,
            final Function<Image, Image> imageResizer,
            final int framesPerSprite) {
        this(importAssets(resource, rg, imageResizer, framesPerSprite), framesPerSprite);
    }

    /**
     * Updates the sprite adding one tick to the internal counter and eventually
     * flipping to the next image in the asset.
     * <p>
     * This method is supposed to be called at each game clock's tick.
     */
    @Override
    public void update() {
        counter++;
        if (counter >= ticksPerFrame) {
            currentFrame = (currentFrame + 1) % framesPerSprite;
            counter = 0;
        }
    }

    @Override
    public final Image getImage() {
        return asset[currentFrame];
    }

    /**
     * Imports an asset immediately contained in a directory named
     * as the asset itself.
     * <p>
     * An "asset" is contained in a directory and is composed by files
     * named as the resource with an incremental number to the end, starting from
     * one (1).
     * <p>
     * {@code gun} will import assets {@code gun/gun1.png}, {@code gun/gun2.png}..
     * 
     * @param resource        the name of the asset.
     * @param rg              the ResourceGetter used to fetch the asset
     * @param imageResizer    the function used to resize the asset
     * @param framesPerSprite the number of frames the sprite is composed
     * @return the array of assets representing the sprite resource
     */
    public static Image[] importAssets(
            final String resource,
            final ResourceGetter rg,
            final Function<Image, Image> imageResizer,
            final int framesPerSprite) {
        Image[] asset = new Image[framesPerSprite];
        for (int i = 1; i <= framesPerSprite; i++) {
            asset[i - 1] = rg.loadImage(
                    resource
                            + "/"
                            + resource
                            + Integer.toString(i));
            asset[i - 1] = imageResizer.apply(asset[i - 1]);
        }
        return asset;
    }

    /**
     * Imports an asset contained in the directory specified by the
     * argument.
     * <p>
     * An "asset" is contained in a single directory and is composed by files
     * named as the resource with an incremental number to the end, starting from
     * one (1).
     * 
     * @param resource        the name of the resource to import
     * @param path            the path where the asset can be found
     * @param rg              the ResourceGetter used to fetch the asset
     * @param imageResizer    the function used to resize the asset
     * @param framesPerSprite the number of frames the sprite is composed
     * @return the array of assets representing the sprite resource
     */
    public static Image[] importAssets(
            final String resource,
            final String path,
            final ResourceGetter rg,
            final Function<Image, Image> imageResizer,
            final int framesPerSprite) {
        Image[] asset = new Image[framesPerSprite];
        for (int i = 1; i <= framesPerSprite; i++) {
            asset[i - 1] = rg.loadImage(
                    path
                            + "/"
                            + resource
                            + Integer.toString(i));
            asset[i - 1] = imageResizer.apply(asset[i - 1]);
        }
        return asset;
    }

    /**
     * Returns the string associated to the {@link Direction}
     * requested.
     * @param dir the direction requested
     * @return the string representing then {@link Direction}
     */
    public static String getStringFromDirection(final Direction dir) {
        return switch (dir) {
            case UP -> "up";
            case DOWN -> "down";
            case LEFT -> "left";
            case RIGHT -> "right";
            default -> throw new IllegalArgumentException();
        };
    }

}
