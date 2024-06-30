package it.unibo.bombardero.cell;

import it.unibo.bombardero.map.api.Pair;
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
     */
    public Wall(CellType type, Pair pos) {
        super(type, pos, true);
    }

}
