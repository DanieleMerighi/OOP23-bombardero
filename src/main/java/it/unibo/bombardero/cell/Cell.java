package it.unibo.bombardero.cell;

import it.unibo.bombardero.physics.api.BoundingBox;

/**
 * This interface models an ideal cell provided with a
 * bounding box, a type and a collision
 * @author Federico Bagattoni
 * @author Luca Venturini
 * @author Jacopo Turchi 
 */
public interface Cell {
    
    public enum CellType {
        WALL_BREAKABLE,
        WALL_UNBREAKABLE,
        BOMB,
        FLAME,
        POWERUP;
    }

    boolean getBoundingCollision();

    CellType getCellType();

    BoundingBox getBoundingBox();

}
