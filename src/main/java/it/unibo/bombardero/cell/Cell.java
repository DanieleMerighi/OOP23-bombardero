package it.unibo.bombardero.cell;

import java.awt.geom.Point2D;

import it.unibo.bombardero.map.api.Pair;
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

    private BoundingBox bBox=null;
    private final CellType type;

    public Cell(final CellType type , Pair pos) {
        this.type = type;
        this.bBox = new RectangleBoundingBox(new Point2D.Float(pos.x(), pos.y()), 1.0f , 1.0f);
    }

    public CellType getCellType() {
        return this.type;
    }

    public BoundingBox getBoundingBox() {
        return bBox;
    }
}   
