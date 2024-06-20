package it.unibo.bombardero.cell;

import java.util.Map;

import it.unibo.bombardero.character.Direction;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.Pair;

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

        private static final Map<Direction,FlameType> FLAME_BODY_TYPES_MAP = Map.of(
            Direction.LEFT, FlameType.FLAME_BODY_HORIZONTAL,
            Direction.RIGHT, FlameType.FLAME_BODY_HORIZONTAL,
            Direction.UP, FlameType.FLAME_BODY_VERTICAL,
            Direction.DOWN, FlameType.FLAME_BODY_VERTICAL
        );
        
        private static final Map<Direction, FlameType> FLAME_END_TYPES_MAP = Map.of(
            Direction.LEFT, FlameType.FLAME_END_LEFT,
            Direction.RIGHT, FlameType.FLAME_END_RIGHT,
            Direction.UP, FlameType.FLAME_END_TOP,
            Direction.DOWN, FlameType.FLAME_END_BOTTOM
        );

        private FlameType(final String typeString) {
            this.typeString = typeString;
        }

        public String getTypeString() {
            return this.typeString;
        }

        public static FlameType getFlameBodyType(Direction dir) {
            return FLAME_BODY_TYPES_MAP.get(dir);
        }

        public static FlameType getFlameEndType(Direction dir) {
            return FLAME_END_TYPES_MAP.get(dir);
        }

    }

    private boolean expired=false;
    private final GameManager mgr;
    private final FlameType specificFlameType;
    private final Pair pos;
    private float countTime=0;

    public Flame(CellType type, FlameType specfiFlameType, Pair pos, GameManager mgr) {
        super(type, pos, false);
        this.specificFlameType = specfiFlameType;
        this.mgr = mgr;
        this.pos = pos;
    }

    public void update(float timeElapsed) {
        this.countTime+=timeElapsed;
        if(countTime>500) {
            expired=true;
            mgr.removeFlame(pos);
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
}
