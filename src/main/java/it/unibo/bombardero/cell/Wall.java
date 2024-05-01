package it.unibo.bombardero.cell;

public class Wall extends Cell {

    public enum WallType {

        WALL_UNBREAKABLE("unbreakable"),
        WALL_BREAKABLE("breakable");

        private String typeString;

        private WallType(final String typeString) {
            this.typeString = typeString;
        }

        public String getTypeString() {
            return this.typeString;
        }
    }

    private final WallType specificType;

    public Wall(CellType type, WallType specificType) {
        super(type);
        this.specificType = specificType;
    }

    public WallType getWallType() {
        return this.specificType;
    }

}
