package it.unibo.bombardero.physics.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;
import java.util.Set;

import it.unibo.bombardero.cell.BasicBomb;
import it.unibo.bombardero.cell.Bomb;
import it.unibo.bombardero.cell.Flame;
import it.unibo.bombardero.cell.Cell.CellType;
import it.unibo.bombardero.character.Direction;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.physics.api.CollisionEngine;

public class BombarderoCollision implements CollisionEngine{
    private GameManager mgr;
    private GameMap map=mgr.getGameMap();

    public BombarderoCollision(GameManager mgr){
        this.mgr=mgr;
    }

    @Override
    public void checkFlameCollision() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'checkFlameCollision'");
    }
    
}
