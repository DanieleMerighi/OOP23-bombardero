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
        BOMB_BASIC("basic"),
        BOMB_POWER("power"),
        BOMB_PUNCH("punch"),
        BOMB_PIERCING("piescing"),
        BOMB_REMOTE("remote"),
        FLAME_CROSS("cross"),
        FLAME_BODY_VERTICAL("vertical"),
        FLAME_BODY_HORIZONTAL("horizontal"),
        FLAME_END_TOP("end_top"),
        FLAME_END_BOTTOM("end_bottom"),
        FLAME_END_RIGHT("end_right"),
        FLAME_END_LEFT("end_left");
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
