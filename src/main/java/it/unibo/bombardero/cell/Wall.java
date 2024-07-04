package it.unibo.bombardero.cell;

import it.unibo.bombardero.map.api.GenPair;
import it.unibo.bombardero.physics.api.BoundingBox;
import it.unibo.bombardero.physics.impl.RectangleBoundingBox;
/**
 * This class further expands the concept of a cell,
 * creating a Wall.
 */
public class Wall extends AbstractCell {

    /**
     * Creates a new wall assigning it a type and a
     * position inside the map. 
     * @param type the type of the wall
     * @param bBox the {@link BoundingBox} representing the hitbox of this class
     */
    public Wall(final CellType type, final GenPair<Integer, Integer> pos) {
        super(type, true, new RectangleBoundingBox(pos.x(), pos.y(), 1.0f, 1.0f));
    }

}
