package it.unibo.bombardero.cell;

import java.util.Map.Entry;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.Pair;

public class BasicBomb extends Cell implements Bomb{
    private final long TIME_TO_EXPLODE=2000L;

    private final int range;
    private final static int MAX_RANGE = 3; // TO-DO: decide the max bomb range
    private final GameManager mgr;
    private Pair pos;
    private long elapsedTime=0;

    public BasicBomb(GameManager mgr, Pair pos , CellType type, int range) {
        super(type);
        this.mgr = mgr;
        this.pos = pos;
        this.range=range;
    }

    @Override
    public void update(long time) {
        elapsedTime += time;
        if(elapsedTime>=TIME_TO_EXPLODE) {
            explode();
        }
    }

    private Entry<Pair,Flame> explode() {
        mgr.explodeBomb(this);
        mgr.
        return null;
    }

}
