package it.unibo.bombardero.cell;

import it.unibo.bombardero.map.api.GenPair;

/**
 * This is an actual flame that takes time to expire and kill characters that is
 * above it.
 */
public final class FlameImpl extends AbstractCell implements Flame {

    private static final int TIME_TO_LIVE = 500;
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
        if (countTime > TIME_TO_LIVE) {
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
