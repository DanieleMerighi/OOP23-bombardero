package it.unibo.bombardero.physics.impl;

import java.util.Map.Entry;

import it.unibo.bombardero.cell.Bomb;
import it.unibo.bombardero.cell.Flame;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.physics.api.CollisionEngine;

public class BombarderoCollision implements CollisionEngine{

    @Override
    public Entry<Pair, Flame> computeFlame(Bomb bomb) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'computeFlame'");
    }

    @Override
    public void checkFlameCollision() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'checkFlameCollision'");
    }
    
}
