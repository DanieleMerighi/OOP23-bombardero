package it.unibo.bombardero.physics.api;

import java.util.Map.Entry;
import java.util.Set;

import it.unibo.bombardero.cell.BasicBomb;
import it.unibo.bombardero.cell.Bomb;
import it.unibo.bombardero.cell.Flame;
import it.unibo.bombardero.map.api.Pair;

public interface CollisionEngine {
    
    void computeFlame(Bomb bomb);

    void checkFlameCollision();

}
