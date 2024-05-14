package it.unibo.bombardero.cell;

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

    private final CellType type;

    public Cell(final CellType type) {
        this.type = type;
    }

    public CellType getCellType() {
        return this.type;
    }
}   
