package it.unibo.bombardero.cell;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.Set;

import it.unibo.bombardero.cell.Flame.FlameType;
import it.unibo.bombardero.character.Character;
import it.unibo.bombardero.character.Direction;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.Pair;


public abstract class BasicBomb extends AbstractCell implements Bomb{
    public final static long TIME_TO_EXPLODE=2000L;
    public final static int MAX_RANGE = 3; // TO-DO: decide the max bomb range

    private boolean exploded = false;
    private final int range;
    private int countTick=0;
    private final Character character;
    private final GameManager mgr;
    private Pair position;
    private GameMap map;
    private final BombType bombType;

    public BasicBomb(GameManager mgr, Character character, int range, Pair pos) {
        super(CellType.BOMB , pos, true);
        this.mgr = mgr;
        this.position = pos;
        this.range = character.getFlameRange();
        this.map=mgr.getGameMap();
        this.character = character;
        if(character.getBombType().isPresent()) {
            this.bombType = character.getBombType().get().toBombType();
        } else {
            this.bombType = BombType.BOMB_BASIC;
        }
    }

    public BombType getBombType(){
        return this.bombType;
    }

    @Override
    public boolean isExploded() {
        return exploded;
    }

    @Override
    public CellType getCellType(){
        return super.getCellType();
    }

    public void update(boolean condition) {
        if(condition){
            this.explode(); 
        }
    }

    @Override
    public void update() {
        countTick++;
        if(countTick*16>=TIME_TO_EXPLODE) {
            explode();
        }
    }

    private void explode() {
        exploded = true;
        computeFlame(this);
        character.increaseNumBomb();
        character.removeBombFromDeque(this);
        mgr.removeBomb(position);
    }

    public int getRange(){
        return range;
    }
    
    public Pair getPos(){
        return position;
    }

    public void computeFlame(Bomb bomb) {
        List<Direction> allDirection = List.of(Direction.LEFT,Direction.RIGHT,Direction.UP,Direction.DOWN);
        allDirection.stream()
            .map(dir->checkDirection(dir, bomb.getRange(), bomb.getPos()))
            .forEach((set)->set.forEach((e)->mgr.addFlame(e.getValue() , e.getKey() )));
        mgr.addFlame(FlameType.FLAME_CROSS, bomb.getPos());
    }
    
    private Set<Entry<Pair , FlameType>> checkDirection(Direction dir , int range , Pair pos) {
        return IntStream.iterate(1 , i->i <= range , i->i+1)
            .mapToObj(i->pos.sum(dir.getPair().multipy(i)))
            .takeWhile(stopFlamePropagation())
            .collect(Collectors.toMap(
                Function.identity() ,
                p -> p.equals(pos.sum(dir.getPair().multipy(range))) 
                    ? FlameType.getFlameEndType(dir) 
                    : FlameType.getFlameBodyType(dir)))
            .entrySet();
    }

    private Predicate<? super Pair> stopFlamePropagation() {
        return p-> map.isEmpty(p) || (isBomb(p) && !map.isUnbreakableWall(p) && !isBreckableWall(p));
    }

    public boolean isBreckableWall(Pair pos) {//TODO check 
        if(map.isBreakableWall(pos)) {
            mgr.removeWall(pos);
            return true;
        }
        return false;
    }

    private boolean isBomb(Pair pos) { 
        if(map.isBomb(pos)) {
            Bomb bomb = mgr.getBomb(pos).get();
            if(!bomb.isExploded()) {
                bomb.update(true);
            }
        }
        return true;
    }
}
