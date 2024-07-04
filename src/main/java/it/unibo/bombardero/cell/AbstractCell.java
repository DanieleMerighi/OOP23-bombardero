package it.unibo.bombardero.cell;

import java.util.Optional;

import it.unibo.bombardero.physics.api.BoundingBox;

/**
 * A class that defines a generic "Cell", in this game a "Cell" is an entity
 * which occupies an entire squared tile and normally cannot move
 * 
 * @author Federico Bagattoni
 * @author Luca Venturini
 * @author Jacopo Turchi
 */
public abstract class AbstractCell implements Cell {

    private final Optional<BoundingBox> bBox;
    private final CellType type;
    private final boolean boundingCollision;

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
        return this.bBox;
    }
}
