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
        WALL_UNBREAKABLE("unbreakable"),
        WALL_BREAKABLE("breakable"),
        BOMB("bomb"),
        FLAME("flame"),
        POWERUP("powerup");
        // add power-up types here, along with their TypeString code (used to fetch images from resources)

        private String typeString;

        private CellType(final String typeString) {
            this.typeString = typeString;
        }

        public String getTypeString() {
            return this.typeString;
        }
    }

    private final CellType type;

    public Cell(final CellType type) {
        this.type = type;
    }

    public CellType getType() {
        return this.type;
    }
}   
