package it.unibo.bombardero.cell;

public class Flame extends Cell {
    private FlameType flameType;
    
    public enum FlameType {

        CROSS("cross"),
        BODY_VERTICAL("vertical"),
        BODY_HORIZONTAL("horizontal"),
        END_TOP("end_top"),
        END_BOTTOM("end_bottom"),
        END_RIGHT("end_right"),
        END_LEFT("end_left");

        private String type;

        private FlameType(final String type) {
            this.type = type;
        }

        public String getTypeName() {
            return this.type;
        }
    }

    public Flame(FlameType type) {
        this.flameType = type;
    }
}
