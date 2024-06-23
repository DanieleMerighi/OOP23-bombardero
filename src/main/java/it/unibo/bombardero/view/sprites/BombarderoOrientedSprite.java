package it.unibo.bombardero.view.sprites;

import java.awt.Image;

import java.lang.UnsupportedOperationException;

import org.apache.http.MethodNotSupportedException;

import it.unibo.bombardero.character.Direction;

/** 
 * This interface extends the {@link BombarderoSprite} interface
 * and models an immutable sprite which handles assets moving in a 
 * certain direction, or standing still but facing the same direction.
 * <p>
 * Through this interface a user can know which direction the asset is
 * pointing to (e.g. {@code Direction.UP}) and can request a new sprite
 * with the same category of asset that was requested in the constructor
 * of the current sprite, but facing a different Direction.
 * <p>
 */
public interface BombarderoOrientedSprite extends BombarderoSprite {
    
    /** 
     * Returns the image of the asset standing still, in the same
     * direction returned by {@link #getCurrentFacingDirection()}.
     * @return the image of the asset standing still
     */
    Image getStandingImage();

    /**
     * Returns a {@link BombarderoOrientedSprite} of the same resource of this object, but
     * in the direction passed as argument.
     * @param dir the direction the new asset will face
     * @return the new sprite, facing the requested direction
     */
    BombarderoOrientedSprite getNewSprite(Direction dir);

    /**
     * Returns the direction that the asset is pointing towards
     * @return the direction the asset is pointing to
     * @throws MethodNotSupportedException whenever this method is called on an object that does
     * not store the direction of an asset or the asset has no direction
     */
    /* TODO: CHANGE EXCEPTION, THIS IS FOR HTTP */
    Direction getCurrentFacingDirection() throws UnsupportedOperationException;

}
