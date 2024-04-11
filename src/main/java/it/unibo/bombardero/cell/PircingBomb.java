package it.unibo.bombardero.cell;

import java.util.Map.Entry;
import java.util.Set;

import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.Pair;

public class PircingBomb extends BasicBomb{

    public PircingBomb(GameManager mgr, Pair pos, BombType type, int range) {
        super(mgr, pos, type, range);
    }

    public Set<Entry<Pair,Flame>> explodeBomb() {
        
        return null;
    }
    
}
