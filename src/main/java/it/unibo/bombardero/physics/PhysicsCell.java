package it.unibo.bombardero.physics;

import java.awt.geom.Point2D;

import it.unibo.bombardero.cell.Cell;
import it.unibo.bombardero.utils.Utils;

public class PhysicsCell extends Cell{
    private final BoundingBox bBox;
    //private boolean collision;

    public PhysicsCell(CellType type, Point2D pos ) {
        super(type);
        this.bBox = new RectangleBoundingBox(pos, (float)Utils.CELL_SIZE, (float)Utils.CELL_SIZE);
        //this.collision=collision;
    }

    public BoundingBox getBoundingBox() {
        return bBox;
    }
    
}
