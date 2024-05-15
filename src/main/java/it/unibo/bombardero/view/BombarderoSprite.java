package it.unibo.bombardero.view;

import java.awt.Image;

import it.unibo.bombardero.character.Direction;

/** This class represents a single instance of a animated cell, that requires being updated
 * and returns a frame when requested.
 * @author Federico Bagattoni
 */
public class BombarderoSprite {  

    private final int frames_per_sprite;
    private final Image[] asset;
    private int counter = 0;
    private int currentFrame = 0;

    /** 
     * Creates a new BombarderSprite object, assigning it a ResourceGetter to use to fetch assets, a facing direction
     * for the assets to get and the name of the resource to get.
     * @param resource the path of the resource to get (e.g. <code>"character/main/walking"</code>).
     * The root where the asset will be fetched from is resources/it/unibo/bombardero
     * @param rg the ResourceGetter object to be used for loading the assets
     * @param facingDirection the direction that the assets will face (e.g. <code>Direction.UP</code> tells the constructor
     * to load the sprite facing the <code>UP</code> direction)
     */
    public BombarderoSprite(final String resource, final ResourceGetter rg, final Direction facingDirection) {
        frames_per_sprite = getFramesFromPosition(facingDirection);
        asset = new Image[frames_per_sprite];
        for (int i = 0; i < frames_per_sprite; i++) {
            asset[i] = rg.loadImage(resource + Integer.toString(i));
        }
    }

    /**
     * Updates the sprite tick, eventually advancing to the next frame.
     */
    public void update() {
        counter++;
        if (counter > 10) {
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

    private int getFramesFromPosition(final Direction facingDirection) {
        if (facingDirection.equals(Direction.UP) || facingDirection.equals(Direction.DOWN)) {
            return 2;
        }
        return 4;
    }

}
