package it.unibo.bombardero.cell;

import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.Pair;

public class Bomb extends Cell {
    private final long TIME_TO_EXPLODE=2000L;

    public enum BombType{
        
        BASIC("basic"),
        PIERCING("piescing"),
        REMOTE("remote");

        private String type;

        private BombType(String type){
            this.type=type;
        }

        public String getType(){
            return type;
        }
    }

    private final int range;
    private final static int MAX_RANGE = 3; // TO-DO: decide the max bomb range
    private final GameManager mgr;
    private Pair pos;
    private final BombType type;
    private long elapsedTime=0;

    public Bomb(GameManager mgr, Pair pos , BombType type, int range){
        this.mgr = mgr;
        this.pos = pos;
        this.type = type;
        this.range=range;
    }

    void update(long time){
        elapsedTime += time;
        if(elapsedTime>TIME_TO_EXPLODE){
            explode();
        }
    }

    private void explode(){
        mgr.explodeBomb(this);
    }

}
