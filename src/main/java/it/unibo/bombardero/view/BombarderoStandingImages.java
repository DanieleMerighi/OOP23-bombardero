package it.unibo.bombardero.view;

import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

import it.unibo.bombardero.character.Direction;

/** Holds the reference of an asset in multiple direction so it can be retrieved
 * by asking the direction
 * @author Federico Bagattoni
 */
public class BombarderoStandingImages {

    private final Map<Direction, Image> asset = new HashMap<>();
    
    public BombarderoStandingImages(final String resource, final ResourceGetter rg) {
        for (Direction dir : Direction.values()) {
            asset.put(dir, rg.loadImage(getStringFromDirection(dir)));
        }
    }

    /** Returns the image asset facing the requested direction
     * @param facingDirection the direction the returned asset should be facing
     * @return the asset facing the requested direction
     */
    public Image getImage(final Direction facingDirection) {
        return asset.get(facingDirection);
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
