package it.unibo.bombardero.cell;

import java.util.Optional;

import it.unibo.bombardero.physics.api.BoundingBox;

/**
 * This interface models an ideal cell provided with a
 * bounding box, a type and a collision.
 * 
 */
public interface Cell {
    
    /** 
     * This enumerator defines the various types of
     * cells that exist in the game.
     */
    enum CellType {

        /**
         * A breakable wall ("crate").
         */
        WALL_BREAKABLE,

        /**
         * An unbreakable wall.
         */
        WALL_UNBREAKABLE,

        /**
         * A bomb.
         */
        BOMB,

        /**
         * A flame. 
         */
        FLAME,

        /** 
         * A powerup.
         */
        POWERUP;
    }

    /**
     * 
     * @return if this Cell needs the boundinding box if is colliding 
     */
    boolean haveBoundingCollision();

    /**
     * Returns the cell type of this instance of
     * {@link Cell}.
     * @return the {@link CellType} type linked to this cell.
     */
    CellType getCellType();

    /**
     * 
     * @return return the BoundingBox of this Cell 
     * */
    Optional<BoundingBox> getBoundingBox();

}
