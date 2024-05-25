package it.unibo.bombardero.cell;

import it.unibo.bombardero.physics.api.BoundingBox;
import it.unibo.bombardero.physics.impl.RectangleBoundingBox;
import it.unibo.bombardero.utils.Utils;

/**
 * A class that defines a generic "Cell", in this game a "Cell" is an entity 
 * which occupies an entire squared tile and normally cannot move
 * @author Federico Bagattoni
 * @author Luca Venturini
 * @author Jacopo Turchi 
 */
public abstract class Cell {
    
    public enum CellType {
        WALL_BREAKABLE,
        WALL_UNBREAKABLE,
        BOMB,
        FLAME,
        POWERUP;
    }

    private final BoundingBox bBox=null;
    private final CellType type;

    public Cell(final CellType type) {
        this.type = type;
        //this.bBox = new RectangleBoundingBox(pos, (float)Utils.CELL_SIZE, (float)Utils.CELL_SIZE);
    }

    public CellType getCellType() {
        return this.type;
    }

    public BoundingBox getBoundingBox() {
        return bBox;
    }
}   
