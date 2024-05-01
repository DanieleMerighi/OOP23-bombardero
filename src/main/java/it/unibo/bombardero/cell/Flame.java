package it.unibo.bombardero.cell;

public class Flame extends Cell {

    public enum FlameType {

        FLAME_CROSS("explosion/cross"),
        FLAME_BODY_VERTICAL("explosion/body/vert"),
        FLAME_BODY_HORIZONTAL("explosion/body/hor"),
        FLAME_END_TOP("explosion/end/up"),
        FLAME_END_BOTTOM("explosion/end/down"),
        FLAME_END_RIGHT("explosion/end/right"),
        FLAME_END_LEFT("explosion/end/left");

        private String typeString;

        private FlameType(final String typeString) {
            this.typeString = typeString;
        }

        public String getTypeString() {
            return this.typeString;
        }

    }

    private FlameType specificFlameType;

    public Flame(CellType type, FlameType specfiFlameType) {
        super(type);
        this.specificFlameType = specfiFlameType;
    }

    public FlameType getFlameType() {
        return specificFlameType;
    }
}
