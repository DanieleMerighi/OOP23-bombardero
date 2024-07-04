package it.unibo.bombardero.cell;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import it.unibo.bombardero.cell.Flame.FlameType;
import it.unibo.bombardero.character.Direction;
import it.unibo.bombardero.map.api.Functions;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.GenPair;
import it.unibo.bombardero.physics.impl.RectangleBoundingBox;

//TODO cercare di dividere logica del character e del manager es: ritornare entryset di fiamme da aggiungere e numbob nel character 
public abstract class BasicBomb extends AbstractCell implements Bomb {
    public final static long TIME_TO_EXPLODE = 2000L;
    public final static int MAX_RANGE = 3; // TO-DO: decide the max bomb range

    private boolean exploded;
    private final int range;
    private int countTick;
    private final GenPair<Integer, Integer> position;
    private final BombType bombType;

    public BasicBomb(final BombType bombType, final int range, final GenPair<Integer, Integer> pos) {
        super(CellType.BOMB, true, new RectangleBoundingBox(pos.x(), pos.y(), 1.0f, 1.0f));
        this.position = pos;
        this.range = range;
        this.bombType = bombType;
    }

    @Override
    public BombType getBombType() {
        return this.bombType;
    }

    @Override
    public boolean isExploded() {
        return exploded;
    }

    @Override
    public void update(final boolean condition) {
        if (condition) {
            this.explode();
        }
    }

    @Override
    public void update() {
        countTick++;
        if (countTick * 16f >= TIME_TO_EXPLODE) {
            explode();
        }
    }

    private void explode() {// TODO ritornare delle fiammeà o in update o in explode e provare a tenere la
                            // logica del character
        exploded = true;
        // character.increaseNumBomb();
        // character.removeBombFromDeque(this);//TODO cavare mgr e character
    }

    @Override
    public int getRange() {
        return range;
    }

    @Override
    public GenPair<Integer, Integer> getPos() {
        return position;
    }

    @Override
    public Set<Entry<GenPair<Integer, Integer>, FlameType>> computeFlame(final GameMap map) {
        if (this.isExploded()) {
            final Map<GenPair<Integer, Integer>, FlameType> temp = new HashMap<>();
            final List<Direction> allDirection = List.of(Direction.LEFT, Direction.RIGHT, Direction.UP, Direction.DOWN);
            allDirection.stream()
                    .map(dir -> checkDirection(dir, map))
                    .forEach((set) -> set.forEach((e) -> temp.put(e.getKey(), e.getValue())));
            temp.put(this.getPos(), FlameType.FLAME_CROSS);
            return temp.entrySet();
        }
        return new HashMap<GenPair<Integer, Integer>, FlameType>().entrySet();
    }

    private Set<Entry<GenPair<Integer, Integer>, FlameType>> checkDirection(final Direction dir, final GameMap map) {
        return IntStream.iterate(1, i -> i <= this.getRange(), i -> i + 1)
                .mapToObj(i -> getPos().apply(Functions.sumInt(dir.getPair().apply(Functions.multiplyInt(i)))))
                .takeWhile(p -> stopFlamePropagation(p, map))
                .collect(Collectors.toMap(
                        Function.identity(),
                        p -> p.equals(
                                getPos().apply(Functions.sumInt(position.apply(Functions.multiplyInt(getRange())))))
                                        ? FlameType.getFlameEndType(dir)
                                        : FlameType.getFlameBodyType(dir)))
                .entrySet();
    }

    private boolean stopFlamePropagation(final GenPair<Integer, Integer> pos, final GameMap map) {
        return map.isEmpty(pos) || isBomb(pos, map) && !map.isUnbreakableWall(pos) && !isBreckableWall(pos, map);
    }

    public boolean isBreckableWall(final GenPair<Integer, Integer> pos, final GameMap map) {
        if (map.isBreakableWall(pos)) {
            map.removeBreakableWall(pos);
            return true;
        }
        return false;
    }

    private boolean isBomb(final GenPair<Integer, Integer> pos, final GameMap map) {
        if (map.isBomb(pos)) {
            final Bomb bomb = (Bomb) map.getMap().get(pos);
            if (!bomb.isExploded()) {
                bomb.update(true);
            }
        }
        return true;
    }
}
