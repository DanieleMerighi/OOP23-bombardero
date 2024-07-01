package it.unibo.bombardero.view.sprites.impl;

import java.awt.Image;

import java.util.Map;
import java.util.HashMap;
import java.util.function.Function;

import it.unibo.bombardero.cell.Flame;
import it.unibo.bombardero.view.ResourceGetter;
import it.unibo.bombardero.view.sprites.api.FlameSprite;

/**
 * A class that holds the game's flames sprites in every direction 
 * and returns the appropriate image.
 * <p>
 * The class divides the flame lifetime in as many equal parts as
 * the frames in the sprite. Once an image is requested the class
 * determines which "phase of life" the flame is in and returns the
 * appropriate image. 
 * @author Federico Bagattoni
 */
public final class BombarderoFlameSprite implements FlameSprite {

    private final Map<Flame.FlameType, Image[]> assets = new HashMap<>();
    private final int coefficient;
    private final int framesPerSprite;

    /**
     * Instantiates a new BombarderoFlameSprite object. Imports the resources,
     * and saves the passed lifetime for internal use. 
     * @param imageResizer function used to resize appropriately the assets
     * @param lifetime the total flame lifetime
     * @param framesPerSprite the number of frames contained in the sprite
     * @param rg the ResourceGetter that will be used to fetch the resources
     */
    public BombarderoFlameSprite(
        final long lifetime,
        final int framesPerSprite,
        final Function<Image, Image> imageResizer,
        final ResourceGetter rg
        ){
        for (final Flame.FlameType type : Flame.FlameType.values()) {
            assets.put(type, SimpleBombarderoSprite.importAssets(getStringFromType(type), type.getTypeString(), rg, imageResizer, framesPerSprite));
        }
        this.framesPerSprite = framesPerSprite;
        coefficient = (int)Math.floorDiv(lifetime, framesPerSprite);
    }

    @Override
    public Image getImage(final long elapsed, final Flame.FlameType flameType) {
        final int frameToDisplay = (int)(elapsed / coefficient);
        return assets.get(flameType)[frameToDisplay == framesPerSprite ? framesPerSprite - 1 : frameToDisplay];
    }

    public static String getStringFromType(final Flame.FlameType type) {
        return switch(type) {
            case FLAME_BODY_HORIZONTAL -> "hor";
            case FLAME_BODY_VERTICAL -> "vert";
            case FLAME_CROSS -> "cross";
            case FLAME_END_BOTTOM -> "down";
            case FLAME_END_LEFT -> "left";
            case FLAME_END_RIGHT -> "right";
            case FLAME_END_TOP -> "up";
        };
    }

}
