package it.unibo.bombardero.view;

import java.awt.Image;

/* This class represents a single instance of a animated cell, that updates itself 
 * and returns an image when requested
 */
public class BombermanSprite {  

    private static final int FRAMES_PER_SPRITE = 3;
    private final Image[] asset = new Image[FRAMES_PER_SPRITE];
    private int counter = 0;
    private int currentFrame = 0;

    public BombermanSprite(final String resource, final ResourceGetter rg) {
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