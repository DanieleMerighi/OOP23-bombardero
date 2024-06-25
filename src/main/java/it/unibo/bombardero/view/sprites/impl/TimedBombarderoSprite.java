package it.unibo.bombardero.view.sprites.impl;

import java.awt.Image;

import java.util.function.Function;

import it.unibo.bombardero.view.ResourceGetter;

public class TimedBombarderoSprite extends SimpleBombarderoSprite {

    public TimedBombarderoSprite(Image[] asset, int framesPerSprite) {
        super(asset, framesPerSprite);
    }

    public TimedBombarderoSprite(
        final String resource,
        final ResourceGetter rg,
        final Function<Image, Image> imageResizer,
        final int framesPerSprite) {
        super(resource, rg, imageResizer, framesPerSprite);
    }
    
}
