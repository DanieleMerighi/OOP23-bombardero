package it.unibo.bombardero.cell;

import java.awt.geom.Point2D;

import java.util.Optional;

import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.physics.api.BoundingBox;
import it.unibo.bombardero.physics.impl.RectangleBoundingBox;

/**
 * A class that defines a generic "Cell", in this game a "Cell" is an entity 
 * which occupies an entire squared tile and normally cannot move
 * @author Federico Bagattoni
 * @author Luca Venturini
 * @author Jacopo Turchi 
 */
public abstract class AbstractCell implements Cell {

    private BoundingBox bBox;
    private final CellType type;
    private final boolean boundingCollision;

    public AbstractCell(final CellType type , final Pair pos , final boolean boundingCollision) {
        this.type = type;
        this.boundingCollision=boundingCollision;
        if(this.haveBoundingCollision()) {
            this.bBox = new RectangleBoundingBox(new Point2D.Float(pos.x(), pos.y()), 1.0f , 1.0f);
        }
    }

    @Override
    public boolean haveBoundingCollision() {
        return boundingCollision;
    }

    @Override
    public CellType getCellType() {
        return this.type;
    }

    @Override
    public Optional<BoundingBox> getBoundingBox() {
        return Optional.of(bBox);
    }
}   
