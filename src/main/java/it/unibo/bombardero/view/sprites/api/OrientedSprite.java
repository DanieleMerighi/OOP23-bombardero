package it.unibo.bombardero.view.sprites.api;

import java.awt.Image;

import it.unibo.bombardero.character.Direction;

/** 
 * This interface extends the {@link Sprite} interface
 * and models an immutable sprite which handles assets moving in a 
 * certain direction, or standing still but facing the same direction.
 * <p>
 * Through this interface a user can know which direction the asset is
 * pointing to (e.g. {@code Direction.UP}) and can request a new sprite
 * with the same category of asset that was requested in the constructor
 * of the current sprite, but facing a different Direction.
 * <p>
 */
public interface OrientedSprite extends Sprite {
    
    /** 
     * Returns the image of the asset standing still, in the same
     * direction returned by {@link #getCurrentFacingDirection()}.
     * @return the image of the asset standing still
     */
    Image getStandingImage();

    /**
     * Returns a {@link OrientedSprite} of the same resource of this object, but
     * in the direction passed as argument.
     * @param dir the direction the new asset will face
     * @return the new sprite, facing the requested direction
     */
    OrientedSprite getNewSprite(Direction dir);

    /**
     * Returns the direction that the asset is pointing towards
     * @return the direction the asset is pointing to
     */
    Direction getCurrentFacingDirection();

}
