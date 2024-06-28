package it.unibo.bombardero.cell;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Function;
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

    private boolean exploded;
    private final int range;
    private int countTick;
    private final Character character;
    private final GameManager mgr;
    private final Pair position;
    private final GameMap map;
    private final BombType bombType;

    public BasicBomb(final GameManager mgr, final Character character, final int range, final Pair pos) {
        super(CellType.BOMB , pos, true);
        this.mgr = mgr;
        this.position = pos;
        this.range = range;
        this.map = mgr.getGameMap();
        this.character = character;
        if(character.getBombType().isPresent()) {
            this.bombType = character.getBombType().get().toBombType();
        } else {
            this.bombType = BombType.BOMB_BASIC;
        }
    }

    @Override
    public BombType getBombType(){
        return this.bombType;
    }

    @Override
    public boolean isExploded() {
        return exploded;
    }

    @Override
    public void update(final boolean condition) {
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

    private void explode() {//TODO ritornare delle fiammeà o in update o in explode e provare a tenere la logica del character
        exploded = true;
        computeFlame(this);
        character.increaseNumBomb();
        character.removeBombFromDeque(this);//TODO cavare mgr e character 
    }

    @Override
    public int getRange(){
        return range;
    }
    
    @Override
    public Pair getPos(){
        return position;
    }

    public void computeFlame(final Bomb bomb) {
        final List<Direction> allDirection = List.of(Direction.LEFT, Direction.RIGHT, Direction.UP, Direction.DOWN);
        allDirection.stream()
            .map(dir -> checkDirection(dir, bomb.getRange(), bomb.getPos()))
            .forEach((set) -> set.forEach((e)->mgr.addFlame(e.getValue() , e.getKey())));
        mgr.addFlame(FlameType.FLAME_CROSS, bomb.getPos());
    }
    
    private Set<Entry<Pair , FlameType>> checkDirection(final Direction dir, final int range, final Pair pos) {
        return IntStream.iterate(1 , i->i <= range , i->i+1)
            .mapToObj(i->pos.sum(dir.getPair().multipy(i)))
            .takeWhile(p->stopFlamePropagation(p))
            .collect(Collectors.toMap(
                Function.identity() ,
                p -> p.equals(pos.sum(dir.getPair().multipy(range))) 
                    ? FlameType.getFlameEndType(dir) 
                    : FlameType.getFlameBodyType(dir)))
            .entrySet();
    }

    private boolean stopFlamePropagation(Pair pos) {
        return this.map.isEmpty(pos) || (isBomb(pos) && !map.isUnbreakableWall(pos) && !isBreckableWall(pos));
    }

    public boolean isBreckableWall(final Pair pos) {  
        if(map.isBreakableWall(pos)) {
            mgr.removeWall(pos);
            return true;
        }
        return false;
    }

    private boolean isBomb(final Pair pos) { 
        if(map.isBomb(pos)) {
            final Bomb bomb = mgr.getBomb(pos).get();
            if(!bomb.isExploded()) {
                bomb.update(true);
            }
        }
        return true;
    }
}
