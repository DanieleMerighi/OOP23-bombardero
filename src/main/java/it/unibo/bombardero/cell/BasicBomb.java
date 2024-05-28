package it.unibo.bombardero.cell;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.Set;

import it.unibo.bombardero.cell.Flame.FlameType;
import it.unibo.bombardero.cell.powerup.api.PowerUpType;
import it.unibo.bombardero.character.Character;
import it.unibo.bombardero.character.Direction;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.Pair;


public abstract class BasicBomb extends Cell implements Bomb{
    public final static long TIME_TO_EXPLODE=2000L;
    public final static int MAX_RANGE = 3; // TO-DO: decide the max bomb range

    private final int range;
    private final Character character;
    private final GameManager mgr;
    private Pair pos;
    private long elapsedTime=0;
    protected GameMap map;
    private Optional<PowerUpType> bombType;

    public BasicBomb(GameManager mgr, Character character) {
        super(CellType.BOMB);
        this.mgr = mgr;
        this.pos = character.getIntCoordinate();
        this.range = character.getFlameRange();
        this.map=mgr.getGameMap();
        this.character = character;
        this.bombType = character.getBombType();
    }

    public BombType getBombType(){
        if(bombType.isPresent()){
            return bombType.get().toBombType();
        }
        return null;
    }

    @Override
    public CellType getCellType(){
        return super.getCellType();
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
        character.increaseNumBomb();
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
            .map(dir->checkDirection(dir, bomb.getRange(), bomb.getPos()))
            .forEach((set)->set.forEach((e)->mgr.addFlame(e.getValue() , e.getKey() )));
        mgr.addFlame(FlameType.FLAME_CROSS, bomb.getPos());
    }
    
    private Set<Entry<Pair , FlameType>> checkDirection(Direction dir , int range , Pair pos) {
        return IntStream.iterate(0 , i->i < range , i->i+1)
            .mapToObj(i->pos.sum(dir.getPair().multipy(i)))
            .takeWhile(stopFlamePropagation())
            .collect(Collectors.toMap(
                Function.identity() ,
                p -> p.equals(pos.sum(dir.getPair().multipy(range))) 
                    ? FlameType.getFlameEndType(dir) 
                    : FlameType.getFlameBodyType(dir)))
            .entrySet();
    }

    protected Predicate<? super Pair> stopFlamePropagation() {
        return p-> !map.isBomb(p) && !map.isUnbreakableWall(p) && (!map.isBreakableWall(p) && mgr.removeWall(p));
    }

}
