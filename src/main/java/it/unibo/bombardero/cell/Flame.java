package it.unibo.bombardero.cell;

import java.util.Map;

import it.unibo.bombardero.character.Direction;
import it.unibo.bombardero.map.api.Pair;

public class Flame extends AbstractCell {

    public enum FlameType {

        FLAME_CROSS("explosion/cross"),
        FLAME_BODY_VERTICAL("explosion/body/vert"),
        FLAME_BODY_HORIZONTAL("explosion/body/hor"),
        FLAME_END_TOP("explosion/end/up"),
        FLAME_END_BOTTOM("explosion/end/down"),
        FLAME_END_RIGHT("explosion/end/right"),
        FLAME_END_LEFT("explosion/end/left");

        private final String typeString;

        private static final Map<Direction, FlameType> FLAME_BODY_TYPES_MAP = Map.of(
                Direction.LEFT, FlameType.FLAME_BODY_HORIZONTAL,
                Direction.RIGHT, FlameType.FLAME_BODY_HORIZONTAL,
                Direction.UP, FlameType.FLAME_BODY_VERTICAL,
                Direction.DOWN, FlameType.FLAME_BODY_VERTICAL);

        private static final Map<Direction, FlameType> FLAME_END_TYPES_MAP = Map.of(
                Direction.LEFT, FlameType.FLAME_END_LEFT,
                Direction.RIGHT, FlameType.FLAME_END_RIGHT,
                Direction.UP, FlameType.FLAME_END_TOP,
                Direction.DOWN, FlameType.FLAME_END_BOTTOM);

        FlameType(final String typeString) {
            this.typeString = typeString;
        }

        public String getTypeString() {
            return this.typeString;
        }

        public static FlameType getFlameBodyType(final Direction dir) {
            return FLAME_BODY_TYPES_MAP.get(dir);
        }

        public static FlameType getFlameEndType(final Direction dir) {
            return FLAME_END_TYPES_MAP.get(dir);
        }

    }

    private boolean expired;
    private final FlameType specificFlameType;
    private final Pair pos;
    private long countTime;

    public Flame(final CellType type, final FlameType specfiFlameType, final Pair pos) {
        super(type, pos, false, null);
        this.specificFlameType = specfiFlameType;
        this.pos = pos;
    }

    public void update(long timeElapsed) {
        this.countTime += timeElapsed;
        if (countTime > 500) {
            expired = true;
        }
    }

    public boolean isExpired() {
        return this.expired;
    }

    public Pair getPos() {
        return this.pos;
    }

    public FlameType getFlameType() {
        return specificFlameType;
    }

    public long getTimePassed() {
        return countTime;
    }
}
