package it.unibo.bombardero.cell;

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

    boolean haveBoundingCollision();

    CellType getCellType();

    BoundingBox getBoundingBox();

}
