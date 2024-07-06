package it.unibo.bombardero.cell;

import java.util.Optional;

import it.unibo.bombardero.physics.api.BoundingBox;

/**
 * A class that defines a generic "Cell", in this game a "Cell" is an entity
 * which occupies an entire squared tile and normally cannot move.
 * 
 * @author Federico Bagattoni
 * @author Luca Venturini
 * @author Jacopo Turchi
 */
public abstract class AbstractCell implements Cell {

    private final Optional<BoundingBox> bBox;
    private final CellType type;
    private final boolean boundingCollision;

    /**
     * Set the type the bounding collision if it has it and if needed the BoundingBox.
     * @param type
     * @param boundingCollision
     * @param bBox
     */
    public AbstractCell(
            final CellType type,
            final boolean boundingCollision,
            final BoundingBox bBox) {
        this.type = type;
        this.boundingCollision = boundingCollision;
        if (boundingCollision) {
            this.bBox = Optional.of(bBox);
        } else {
            this.bBox = Optional.empty();
        }
    }

    /**
     * 
     * @return true if this cell have Bounding collision
     */
    @Override
    public boolean haveBoundingCollision() {
        return boundingCollision;
    }

    /**
     * 
     * @ruturn the CellType
     */
    @Override
    public CellType getCellType() {
        return this.type;
    }

    /**
     * 
     * @return an Optional of the BoundingBox
     */
    @Override
    public Optional<BoundingBox> getBoundingBox() {
        return this.bBox;
    }
}
