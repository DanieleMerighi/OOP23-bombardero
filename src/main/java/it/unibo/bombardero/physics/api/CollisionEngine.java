package it.unibo.bombardero.physics.api;

import java.util.Map.Entry;

import it.unibo.bombardero.cell.Bomb;
import it.unibo.bombardero.cell.Flame;
import it.unibo.bombardero.map.api.Pair;

public interface CollisionEngine {
    
    Entry<Pair,Flame> computeFlame(Bomb bomb);

    void checkFlameCollision();

}
