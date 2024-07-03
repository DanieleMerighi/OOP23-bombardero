package it.unibo.bombardero.cell;

import java.util.List;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.Set;

import it.unibo.bombardero.cell.Flame.FlameType;
import it.unibo.bombardero.character.Direction;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.physics.api.BoundingBox;

//TODO cercare di dividere logica del character e del manager es: ritornare entryset di fiamme da aggiungere e numbob nel character 
public abstract class BasicBomb extends AbstractCell implements Bomb{
    public final static long TIME_TO_EXPLODE=2000L;
    public final static int MAX_RANGE = 3; // TO-DO: decide the max bomb range

    private boolean exploded;
    private final int range;
    private int countTick;
    private final Pair position;
    private final BombType bombType;

    public BasicBomb(final BombType bombType, final int range, final Pair pos, BoundingBox bBox) {
        super(CellType.BOMB , pos, true, bBox);
        this.position = pos;
        this.range = range;
        this.bombType = bombType;
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
        if(countTick*16f>=TIME_TO_EXPLODE) {
            explode();
        }
    }

    private void explode() {//TODO ritornare delle fiammeà o in update o in explode e provare a tenere la logica del character
        exploded = true;
        //character.increaseNumBomb();
        //character.removeBombFromDeque(this);//TODO cavare mgr e character 
    }

    @Override
    public int getRange(){
        return range;
    }
    
    @Override
    public Pair getPos(){
        return position;
    }

    @Override
    public Set<Entry<Pair ,FlameType>> computeFlame(final GameMap map) {
        if(this.isExploded()) {
            Map<Pair, FlameType> temp = new HashMap<>();
            final List<Direction> allDirection = List.of(Direction.LEFT, Direction.RIGHT, Direction.UP, Direction.DOWN);
            allDirection.stream()
                .map(dir -> checkDirection(dir, map))
                .forEach((set) -> set.forEach((e)->temp.put(e.getKey(), e.getValue())));
            temp.put(this.getPos(), FlameType.FLAME_CROSS);
            return temp.entrySet();
        }
        return new HashMap<Pair, FlameType>().entrySet();
    }
    
    private Set<Entry<Pair ,FlameType>> checkDirection(final Direction dir, final GameMap map) {
        return IntStream.iterate(1 , i->i <= this.getRange() , i->i+1)
            .mapToObj(i -> getPos().sum(dir.getPair().multipy(i)))
            .takeWhile(p->stopFlamePropagation(p, map))
            .collect(Collectors.toMap(
                Function.identity() ,
                p -> p.equals(getPos().sum(dir.getPair().multipy(this.getRange()))) 
                    ? FlameType.getFlameEndType(dir) 
                    : FlameType.getFlameBodyType(dir)))
            .entrySet();
    }

    private boolean stopFlamePropagation(final Pair pos, final GameMap map) {
        return map.isEmpty(pos) || (isBomb(pos, map) && !map.isUnbreakableWall(pos) && !isBreckableWall(pos, map));
    }

    public boolean isBreckableWall(final Pair pos, GameMap map) {  
        if(map.isBreakableWall(pos)) {
            map.removeBreakableWall(pos);
            return true;
        }
        return false;
    }

    private boolean isBomb(final Pair pos, GameMap map) { 
        if(map.isBomb(pos)) {
            final Bomb bomb = (Bomb) map.getMap().get(pos);
            if(!bomb.isExploded()) {
                bomb.update(true);
            }
        }
        return true;
    }
}
