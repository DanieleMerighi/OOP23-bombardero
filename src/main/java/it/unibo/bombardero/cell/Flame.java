package it.unibo.bombardero.cell;

import it.unibo.bombardero.cell.FlameImpl.FlameType;
import it.unibo.bombardero.map.api.GenPair;

public interface Flame extends Cell{

    public void update(final long timeElapsed);

    public boolean isExpired();

    public GenPair<Integer, Integer> getPos();

    public FlameType getFlameType();

    public long getTimePassed();
}
