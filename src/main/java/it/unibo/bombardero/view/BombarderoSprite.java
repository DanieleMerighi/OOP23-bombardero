package it.unibo.bombardero.view;

import java.awt.Image;

/* This class represents a single instance of a animated cell, that updates itself 
 * and returns an image when requested
 */
public class BombarderoSprite {  

    private final int FRAMES_PER_SPRITE;
    private final Image[] asset;
    private int counter = 0;
    private int currentFrame = 0;

    public BombarderoSprite(final String resource, final ResourceGetter rg, final int frames) {
        FRAMES_PER_SPRITE = frames;
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

}