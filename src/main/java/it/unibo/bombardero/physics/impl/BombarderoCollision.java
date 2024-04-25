package it.unibo.bombardero.physics.impl;

import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Stream;
import java.util.Set;

import it.unibo.bombardero.cell.BasicBomb;
import it.unibo.bombardero.cell.Bomb;
import it.unibo.bombardero.cell.Flame;
import it.unibo.bombardero.character.Direction;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.physics.api.CollisionEngine;

public class BombarderoCollision implements CollisionEngine{
    private GameMap map;
    private GameManager mgr;

    public BombarderoCollision(GameManager mgr, GameMap map){
        this.mgr=mgr;
        this.map=map;
    }

    @Override
    public Set<Entry<Pair, Flame>> computeFlame(Bomb bomb) {
        List<Direction> allDirection = List.of(Direction.LEFT,Direction.RIGHT,Direction.UP,Direction.DOWN);
        allDirection.stream().forEach(dir->checkDirection(dir, bomb.getRange(), bomb.getPos()));
        return null;
    }

    private void checkDirection(Direction dir , int range , Pair pos){
        Stream.generate(()->pos.sum(dir.getPair())).limit(range);
    }

    @Override
    public void checkFlameCollision() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'checkFlameCollision'");
    }
    
}
