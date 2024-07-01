package it.unibo.bombardero.cell;

import java.util.Optional;

import it.unibo.bombardero.physics.api.BoundingBox;

/**
 * This interface models an ideal cell provided with a
 * bounding box, a type and a collision
 * 
 */
public interface Cell {

    enum CellType {
        WALL_BREAKABLE,
        WALL_UNBREAKABLE,
        BOMB,
        FLAME,
        POWERUP;
    }

    /**
     * 
     * @return if this Cell needs the boundinding box if is colliding 
     */
    boolean haveBoundingCollision();

    /**
     * 
     * @return the CellType of this Cell
     */
    CellType getCellType();

    /**
     * 
     * @return return the BoundingBox of this Cell 
     * */
    Optional<BoundingBox> getBoundingBox();

}
