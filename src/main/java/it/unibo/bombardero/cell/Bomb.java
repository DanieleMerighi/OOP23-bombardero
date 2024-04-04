package it.unibo.bombardero.cell;

import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.Pair;

public class Bomb extends Cell {

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

    private int range;
    private final static int MAX_RANGE = 3; // TO-DO: decide the max bomb range
    private GameManager mgr;
    private Pair pos;
    private 

    public Bomb(GameManager mgr, Pair pos){
        this.mgr = mgr;
        this.pos = pos;
    }

    void update(){}

    private void explode(){
        mgr.explodeBomb(pos);
    }

}
