package it.unibo.bombardero.cell;

import java.util.Map.Entry;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.physics.api.CollisionEngine;

public abstract class BasicBomb extends Cell implements Bomb{
    private final long TIME_TO_EXPLODE=2000L;

    private final int range;
    private final static int MAX_RANGE = 3; // TO-DO: decide the max bomb range
    private final GameManager mgr;
    private Pair pos;
    private long elapsedTime=0;
    private CollisionEngine ce;

    public BasicBomb(GameManager mgr, Pair pos , CellType type, int range, CollisionEngine ce) {
        super(type);
        this.mgr = mgr;
        this.pos = pos;
        this.range=range;
        this.ce=ce;
    }

    public CellType getType(){
        return super.getType();
    }

    @Override
    public void update(long time) {
        elapsedTime += time;
        if(elapsedTime>=TIME_TO_EXPLODE) {
            explode();
        }
    }

    private Entry<Pair,Flame> explode() {
        ce.computeFlame(this);
        return null;
    }

    public int getRange(){
        return range;
    }
    
    public Pair getPos(){
        return pos;
    }
}
