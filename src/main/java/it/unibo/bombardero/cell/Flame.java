package it.unibo.bombardero.cell;

import it.unibo.bombardero.cell.FlameImpl.FlameType;
import it.unibo.bombardero.map.api.GenPair;

public interface Flame extends Cell{

    void update(long timeElapsed);

    boolean isExpired();

    GenPair<Integer, Integer> getPos();

    FlameType getFlameType();

    long getTimePassed();
}
