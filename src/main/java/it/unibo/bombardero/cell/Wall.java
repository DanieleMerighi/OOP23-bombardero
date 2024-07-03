package it.unibo.bombardero.cell;

import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.physics.api.BoundingBox;
/**
 * This class further expands the concept of a cell,
 * creating a Wall.
 */
public class Wall extends AbstractCell {

    /**
     * Creates a new wall assigning it a type and a
     * position inside the map. 
     * @param type the type of the wall
     * @param pos the position of the wall
     * @param bBox the {@link BoundingBox} representing the hitbox of this class
     */
    public Wall(final CellType type, final Pair pos, final BoundingBox bBox) {
        super(type, pos, true, bBox);
    }

}
