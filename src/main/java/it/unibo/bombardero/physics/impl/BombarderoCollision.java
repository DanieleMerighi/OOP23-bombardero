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
    public void computeFlame(Bomb bomb) {
        List<Direction> allDirection = List.of(Direction.LEFT,Direction.RIGHT,Direction.UP,Direction.DOWN);
        allDirection.stream().map(dir->checkDirection(dir, bomb.getRange(), bomb.getPos() , bomb.getType()))
        .forEach((set)->set.forEach((e)->mgr.addFlame(e.getValue() , e.getKey() )));
        mgr.addFlame(CellType.FLAME_CROSS, bomb.getPos());
    }
    
    private Set<Entry<Pair , CellType>> checkDirection(Direction dir , int range , Pair pos , CellType type){
        int countBreakable=0,i=range;
        Pair p=pos;
        Set<Pair> flamePos = new HashSet<>();
        do{
            p=p.sum(dir.getPair());
            if(map.isBomb(p) || map.isUnbreakableWall(p)){
                break;
            } else if(map.isBreakableWall(p)){
                if(countBreakable>0){
                    break;
                }
                mgr.removeWall(p);
                flamePos.add(p);
                if(type.equals(CellType.BOMB_PIERCING)) {
                    countBreakable++;
                }
            } else {
                flamePos.add(p);
            }
        } while(i<range);
        return chooseType(dir, flamePos);
    }

    private Set<Entry<Pair , CellType>> chooseType(Direction dir , Set<Pair> flamePos){
        switch (dir) {
            case LEFT:
                return BuidSet(CellType.FLAME_BODY_HORIZONTAL , CellType.FLAME_END_LEFT , flamePos);
            case RIGHT:
                return BuidSet(CellType.FLAME_BODY_HORIZONTAL , CellType.FLAME_END_RIGHT , flamePos);
            case UP:
                return BuidSet(CellType.FLAME_BODY_VERTICAL , CellType.FLAME_END_TOP , flamePos);
            case DOWN:
                return BuidSet(CellType.FLAME_BODY_VERTICAL , CellType.FLAME_END_BOTTOM , flamePos);
            default:
                return null;
        }
    }

    private Set<Entry<Pair , CellType>> BuidSet(CellType bodyType, CellType endType , Set<Pair> flamePos) {
        var it = flamePos.iterator();
        Map<Pair , CellType > map = new HashMap<>();
        Pair p;
        while(it.hasNext()){
            p=it.next();
            if (!it.hasNext()) {
                map.put(p, endType);
            }
            map.put(p , bodyType); 
        }
        return map.entrySet();
    }

    @Override
    public void checkFlameCollision() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'checkFlameCollision'");
    }
    
}
