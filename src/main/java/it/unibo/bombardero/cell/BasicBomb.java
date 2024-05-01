package it.unibo.bombardero.cell;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import it.unibo.bombardero.cell.Flame.FlameType;
import it.unibo.bombardero.character.Direction;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.physics.api.CollisionEngine;

public abstract class BasicBomb extends Cell implements Bomb{
    private final long TIME_TO_EXPLODE=2000L;

    private final int range;
    private final static int MAX_RANGE = 3; // TO-DO: decide the max bomb range
    private final GameManager mgr;
    private Pair pos;
    private long elapsedTime=0;
    private CollisionEngine ce;
    private GameMap map;

    public BasicBomb(GameManager mgr, Pair pos , CellType type, int range, CollisionEngine ce) {
        super(type);
        this.mgr = mgr;
        this.pos = pos;
        this.range=range;
        this.ce=ce;
        this.map=mgr.getGameMap();
    }

    public CellType getType(){
        return super.getType();
    }

    @Override
    public void update(long time) {
        elapsedTime += time;
        if(elapsedTime>=TIME_TO_EXPLODE) {
            explode();
        }
    }

    private void explode() {
        computeFlame(this);
    }

    public int getRange(){
        return range;
    }
    
    public Pair getPos(){
        return pos;
    }

    public void computeFlame(Bomb bomb) {
        List<Direction> allDirection = List.of(Direction.LEFT,Direction.RIGHT,Direction.UP,Direction.DOWN);
        allDirection.stream()
            .map(dir->checkDirection(dir, bomb.getRange(), bomb.getPos() , bomb.getType()))
            .forEach((set)->set.forEach((e)->mgr.addFlame(e.getValue() , e.getKey() )));
        mgr.addFlame(FlameType.FLAME_CROSS, bomb.getPos());
    }
    
    private Set<Entry<Pair , CellType>> checkDirection(Direction dir , int range , Pair pos , CellType type){
        int i=range;
        Pair p=pos;
        Set<Pair> flamePos = new HashSet<>();
        do{
            p=p.sum(dir.getPair());
            if(map.isBomb(p) || map.isUnbreakableWall(p)){
                break;
            } else if(map.isBreakableWall(p)){
                mgr.removeWall(p);
                flamePos.add(p);
                break;
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

}
