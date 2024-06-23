package it.unibo.bombardero.view.sprites.impl;

import java.awt.Image;
import java.util.function.Function;
import java.util.Optional;

import it.unibo.bombardero.character.Direction;
import it.unibo.bombardero.view.ResourceGetter;
import it.unibo.bombardero.view.sprites.api.OrientedSprite;

public class BombarderoOrientedSprite extends SimpleBombarderoSprite implements OrientedSprite {

    private final Image standingAsset;
    private final String resource;
    private final ResourceGetter rg;
    private final Direction currentFacingDirection;
    private final Function<Image, Image> imageResizer;
    private int counter = 0;
    private int currentFrame = 0;

    /** 
     * Creates a new {@link SimpleBombarderoSprite} object, assigning it a {@link ResourceGetter} 
     * to use to fetch assets, a facing direction for the assets to get and the name of the resource to get.
     * The class will fetch the sprite animation in the direction requested and the corresponding standing image.
     * @param resource the path of the resource to get (e.g. <code>"character/main/walking"</code>).
     * The root where the asset will be fetched from is resources/it/unibo/bombardero
     * @param rg the {@link ResourceGetter} object to use for fetching the resource
     * @param facingDirection the direction that the assets will face (e.g. <code>Direction.UP</code> tells the constructor
     * to load the sprite facing the <code>UP</code> direction)
     */
    public BombarderoOrientedSprite(final String resource,
        final ResourceGetter rg,
        final Direction facingDirection,
        final Function<Image, Image> imageResizer) {

        super(importOrientedAssets(
            resource,
            rg,
            imageResizer,
            getFramesFromPosition(facingDirection), facingDirection),
            getFramesFromPosition(facingDirection)
        );
        
        this.resource = resource;
        this.rg = rg;
        this.currentFacingDirection = facingDirection;
        this.imageResizer = imageResizer;


        standingAsset = imageResizer.apply(
            rg.loadImage(
            resource
            + "/"
            + getStringFromDirection(facingDirection)
            + "/"
            + getStringFromDirection(facingDirection)
            + "_standing")
        );
    }

    @Override
    public OrientedSprite getNewSprite(Direction dir) {
        return new BombarderoOrientedSprite(resource, rg, dir, imageResizer);
    }

    @Override
    public Image getStandingImage() {
        return standingAsset;
    }

    @Override
    public Direction getCurrentFacingDirection() {
        return currentFacingDirection;
    }

    public static Image[] importOrientedAssets(
        final String resource,
        final ResourceGetter rg,
        final Function<Image, Image> imageResizer,
        final int framesPerSprite,
        final Direction facingDirection) {
        Image[] asset = new Image[framesPerSprite];
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
        return asset;
    }

    private static int getFramesFromPosition(final Direction facingDirection) {
        if (facingDirection.equals(Direction.UP) || facingDirection.equals(Direction.DOWN)) {
            return 2;
        }
        return 4;
    }

}
