package it.unibo.bombardero.view.sprites.impl;

import java.awt.Image;
import java.util.function.Function;

import it.unibo.bombardero.character.Direction;
import it.unibo.bombardero.view.ResourceGetter;
import it.unibo.bombardero.view.sprites.api.Sprite;

/** This class represents an immutable single instance of a animated cell, that requires being updated
 * and returns a frame when requested.
 * @author Federico Bagattoni
 */
public class SimpleBombarderoSprite implements Sprite {  

    private final Image[] asset;
    private int counter = 0;
    private int ticksPerFrame;
    private int currentFrame;
    private int framesPerSprite;

    public SimpleBombarderoSprite(final Image[] asset, final int framesPerSprite) {
        this.asset = asset;
        this.framesPerSprite = framesPerSprite;
        ticksPerFrame = (framesPerSprite == 2 ? 16 : 7);
    }

    public SimpleBombarderoSprite(
        final String resource,
        final ResourceGetter rg,
        final Function<Image, Image> imageResizer,
        final int framesPerSprite) {
            this(importAssets(resource, rg, imageResizer, framesPerSprite), framesPerSprite);
        }

    @Override
    public void update() {
        counter++;
        if (counter >= ticksPerFrame) {
            currentFrame = (currentFrame + 1) % framesPerSprite;
            counter = 0;
        }
    }

    @Override
    public Image getImage() {
        return asset[currentFrame];
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

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
                + Integer.toString(i)
            );
            asset[i - 1] = imageResizer.apply(asset[i - 1]);
        }
        return asset;
    }

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
