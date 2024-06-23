package it.unibo.bombardero.view.sprites.impl;

import java.awt.Image;

import java.util.Optional;
import java.util.function.Function;

import org.apache.http.MethodNotSupportedException;

import it.unibo.bombardero.character.Direction;
import it.unibo.bombardero.view.ResourceGetter;
import it.unibo.bombardero.view.sprites.api.BombarderoOrientedSprite;
import it.unibo.bombardero.view.sprites.api.BombarderoSprite;

/** This class represents an immutable single instance of a animated cell, that requires being updated
 * and returns a frame when requested.
 * @author Federico Bagattoni
 */
public class GenericBombarderoSprite implements BombarderoOrientedSprite {  


    private final int ticksPerFrame;
    private final int framesPerSprite;
    private final Image[] asset;
    private final Optional<Image> standingAsset;
    private final String resource;
    private final ResourceGetter rg;
    private final Optional<Direction> currentFacingDirection;
    private final Function<Image, Image> imageResizer;
    private int counter = 0;
    private int currentFrame = 0;

    /** 
     * Creates a new {@link GenericBombarderoSprite} object, assigning it a {@link ResourceGetter} 
     * to use to fetch assets, a facing direction for the assets to get and the name of the resource to get.
     * The class will fetch the sprite animation in the direction requested and the corresponding standing image.
     * @param resource the path of the resource to get (e.g. <code>"character/main/walking"</code>).
     * The root where the asset will be fetched from is resources/it/unibo/bombardero
     * @param rg the {@link ResourceGetter} object to use for fetching the resource
     * @param facingDirection the direction that the assets will face (e.g. <code>Direction.UP</code> tells the constructor
     * to load the sprite facing the <code>UP</code> direction)
     */
    public GenericBombarderoSprite(final String resource,
        final ResourceGetter rg,
        final Direction facingDirection,
        final Function<Image, Image> imageResizer) {
        this.resource = resource;
        this.rg = rg;
        this.currentFacingDirection = Optional.of(facingDirection);
        this.imageResizer = imageResizer;
        framesPerSprite = getFramesFromPosition(facingDirection);
        ticksPerFrame = (framesPerSprite == 2 ? 16 : 7);

        asset = new Image[framesPerSprite];
        for (int i = 1; i <= framesPerSprite; i++) {
            asset[i - 1] = rg.loadImage(
                resource
                + "/"
                + getStringFromDirection(facingDirection)
                + "/"
                + getStringFromDirection(facingDirection)
                + Integer.toString(i)
            );
            asset[i - 1] = imageResizer.apply(asset[i - 1]);
        }

        standingAsset = Optional.of(
            imageResizer.apply(
                rg.loadImage(
                resource
                + "/"
                + getStringFromDirection(facingDirection)
                + "/"
                + getStringFromDirection(facingDirection)
                + "_standing")
            )
        );
    }

    /** 
     * Creates a new {@link GenericBombarderoSprite} object, assigning it a {@link ResourceGetter} 
     * to use to fetch assets and a number of frames to import. This constructor is intended to be used 
     * for simple and static sprites, as modeled by the {@link BombarderoSprite} interface.
     * @param resource the name of the resource to get
     * @param rg the {@link ResourceGetter} object to use for fetching the resource
     * @param framesPerSprite how many frames to get. In the directory have to be present at least {@link #framesPerSprite}
     * resources present
    */
    public GenericBombarderoSprite(final String resource,
        final ResourceGetter rg,
        final int framesPerSprite,
        final Function<Image, Image> imageResizer) {
        this.resource = resource;
        this.rg = rg;
        this.framesPerSprite = framesPerSprite;
        this.imageResizer = imageResizer;
        ticksPerFrame = 16;

        asset = new Image[framesPerSprite];
        for (int i = 1; i <= framesPerSprite; i++) {
            asset[i - 1] = rg.loadImage(
                resource
                + "/"
                + resource
                + Integer.toString(i)
            );
            asset[i - 1] = imageResizer.apply(asset[i - 1]);
        }

        standingAsset = Optional.empty();
        this.currentFacingDirection = Optional.empty();
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

    @Override
    public Image getStandingImage() {
        if (standingAsset.isPresent()) {
            return standingAsset.get();
        }
        return asset[currentFrame];
    }
    
    @Override
    public BombarderoOrientedSprite getNewSprite(final Direction dir) {
        return new GenericBombarderoSprite(resource, rg, dir, imageResizer);
    }

    @Override
    public Direction getCurrentFacingDirection() throws UnsupportedOperationException {
        if (currentFacingDirection.isPresent()) {
            return currentFacingDirection.get();
        }
        throw new UnsupportedOperationException("The sprite has no direction");
    }

    private int getFramesFromPosition(final Direction facingDirection) {
        if (facingDirection.equals(Direction.UP) || facingDirection.equals(Direction.DOWN)) {
            return 2;
        }
        return 4;
    }

    private String getStringFromDirection(final Direction dir) {
        return switch (dir) {
            case UP -> "up";
            case DOWN -> "down";
            case LEFT -> "left";
            case RIGHT -> "right";
            default -> throw new IllegalArgumentException();
        };
    }

}
