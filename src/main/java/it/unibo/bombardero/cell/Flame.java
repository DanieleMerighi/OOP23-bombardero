package it.unibo.bombardero.cell;

public class Flame extends Cell {

    public enum FlameType {

        FLAME_CROSS("cross"),
        FLAME_BODY_VERTICAL("vertical"),
        FLAME_BODY_HORIZONTAL("horizontal"),
        FLAME_END_TOP("end_top"),
        FLAME_END_BOTTOM("end_bottom"),
        FLAME_END_RIGHT("end_right"),
        FLAME_END_LEFT("end_left");

        private String typeString;

        private FlameType(final String typeString) {
            this.typeString = typeString;
        }

        public String getTypeString() {
            return this.typeString;
        }

    }

    public Flame(CellType type) {
        super(type);
    }
}
