package it.unibo.bombardero.view;

import java.awt.Image;

import it.unibo.bombardero.character.Direction;

/* This class represents a single instance of a animated cell, that updates itself 
 * and returns an image when requested
 */
public class BombarderoSprite {  

    private final int FRAMES_PER_SPRITE;
    private final Image[] asset;
    private int counter = 0;
    private int currentFrame = 0;

    public BombarderoSprite(final String resource, final ResourceGetter rg, final Direction facingDirection) {
        FRAMES_PER_SPRITE = getFramesFromPosition(facingDirection);
        asset = new Image[FRAMES_PER_SPRITE];
        for(int i = 0; i < FRAMES_PER_SPRITE; i++) {
            asset[i] = rg.loadImage(resource + Integer.toString(i));
        }
    }

    public void update() {
        counter++;
        if(counter > 10) {
            currentFrame = (currentFrame + 1)%FRAMES_PER_SPRITE;
        }
    }

    public Image getImage() {
        /* QUANDO ANDRA' LA VERSIONE NAIVE, PROVARE: (int)Math.ceil(counter / FRAMES_PER_SPRITE) */
        return asset[currentFrame];
    }

    private int getFramesFromPosition(Direction facingDirection) {
        if(facingDirection.equals(Direction.UP) || facingDirection.equals(Direction.DOWN)) {
            return 2;
        }
        return 4;
    }

}