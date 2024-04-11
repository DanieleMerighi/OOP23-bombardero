package it.unibo.bombardero.cell;

import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.Pair;

public class RemoteBomb extends BasicBomb{

    public RemoteBomb(GameManager mgr, Pair pos, BombType type, int range) {
        super(mgr, pos, type, range);
    }
    
    public void update(){
        
    }
}
