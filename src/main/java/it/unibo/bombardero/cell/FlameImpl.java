package it.unibo.bombardero.cell;

import java.util.Map;

import it.unibo.bombardero.character.Direction;
import it.unibo.bombardero.map.api.GenPair;

/**
 * This is an actual flame that takes time to expire and kill characters that is
 * above it
 */
public final class FlameImpl extends AbstractCell implements Flame {

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
    private final GenPair<Integer, Integer> pos;
    private long countTime;

    /**
     * 
     * @param type            Flame
     * @param specfiFlameType if it's a CrossFlame, Orizzontal, Vertical
     * @param pos             position on the map
     */
    public FlameImpl(final CellType type, final FlameType specfiFlameType, final GenPair<Integer, Integer> pos) {
        super(type, false, null);
        this.specificFlameType = specfiFlameType;
        this.pos = pos;
    }

    @Override
    public void update(final long timeElapsed) {
        this.countTime += timeElapsed;
        if (countTime > 500) {
            expired = true;
        }
    }

    @Override
    public boolean isExpired() {
        return this.expired;
    }

    @Override
    public GenPair<Integer, Integer> getPos() {
        return this.pos;
    }

    @Override
    public FlameType getFlameType() {
        return specificFlameType;
    }

    @Override
    public long getTimePassed() {
        return countTime;
    }
}