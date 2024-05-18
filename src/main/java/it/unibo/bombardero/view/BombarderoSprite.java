package it.unibo.bombardero.view;

import java.awt.Image;

import edu.umd.cs.findbugs.annotations.ReturnValuesAreNonnullByDefault;
import it.unibo.bombardero.character.Direction;

/** This class represents an immutable single instance of a animated cell, that requires being updated
 * and returns a frame when requested.
 * @author Federico Bagattoni
 */
public class BombarderoSprite {  

    private final static int TICKS_TO_NEXT_FRAME = 20;

    private final int frames_per_sprite;
    private final Image[] asset;
    private final Image standingAsset;
    private final String resource;
    private final ResourceGetter rg;
    private final Direction currentFacingDirection;
    private int counter = 0;
    private int currentFrame = 0;

    /** 
     * Creates a new BombarderSprite object, assigning it a ResourceGetter to use to fetch assets, a facing direction
     * for the assets to get and the name of the resource to get. The class will fetch the sprite animation in the
     * direction requested and the corresponding standing image.
     * @param resource the path of the resource to get (e.g. <code>"character/main/walking"</code>).
     * The root where the asset will be fetched from is resources/it/unibo/bombardero
     * @param rg the ResourceGetter object to be used for loading the assets
     * @param facingDirection the direction that the assets will face (e.g. <code>Direction.UP</code> tells the constructor
     * to load the sprite facing the <code>UP</code> direction)
     */
    public BombarderoSprite(final String resource, final ResourceGetter rg, final Direction facingDirection) {
        this.resource = resource;
        this.rg = rg;
        this.currentFacingDirection = facingDirection;

        frames_per_sprite = getFramesFromPosition(facingDirection);
        asset = new Image[frames_per_sprite];
        for (int i = 1; i <= frames_per_sprite; i++) {
            asset[i - 1] = rg.loadImage(
                resource
                + "/"
                + getStringFromDirection(facingDirection)
                + "/"
                + getStringFromDirection(facingDirection)
                + Integer.toString(i)
            );
        }
        standingAsset = rg.loadImage(
            resource
            + "/"
            + getStringFromDirection(facingDirection)
            + "/"
            + getStringFromDirection(facingDirection)
            + "_standing");
    }

    /**
     * Updates the sprite tick, eventually advancing to the next frame.
     */
    public void update() {
        counter++;
        if (counter > TICKS_TO_NEXT_FRAME) {
            currentFrame = (currentFrame + 1) % frames_per_sprite;
        }
    }

    /**
     * Returns the current frame of the sprite.
     * @return the current frame of the sprite
     */
    public Image getImage() {
        /* QUANDO ANDRA' LA VERSIONE NAIVE, PROVARE: (int)Math.ceil(counter / frames_per_sprite) */
        return asset[currentFrame];
    }

    /** 
     * Returns the standing frame of the sprite.
     * @return the standing frame of the sprite
     */
    public Image getStandingImage() {
        return standingAsset;
    }

    /** 
     * Returns a new Sprite for the same asset, but facing a different direction
     * @param dir the direction that the new asset will face 
     * @return the new sprite, facing the new direction
     */
    public BombarderoSprite getNewSprite(final Direction dir) {
        return new BombarderoSprite(resource, rg, dir);
    }

    /**
     * Returns the direction that the asset is pointing towards
     * @return the direction the asset is pointing to
     */
    public Direction getCurrentFacingDirection() {
        return currentFacingDirection;
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
