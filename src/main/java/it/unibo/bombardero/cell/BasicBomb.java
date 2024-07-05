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

/**
 * This rapresent a Bomb an object that after placed start a timer and after 3 sec explode.
 */
public abstract class BasicBomb extends AbstractCell implements Bomb {
    public final static long TIME_TO_EXPLODE = 2000L;
    public final static int MAX_RANGE = 3; // TO-DO: decide the max bomb range

    private boolean exploded;
    private final int range;
    private long timer;
    private final GenPair<Integer, Integer> position;
    private final BombType bombType;

    /**
     * Set bombTypo the position and the range of the bomb.
     * @param bombType
     * @param range
     * @param pos
     */
    public BasicBomb(final BombType bombType, final int range, final GenPair<Integer, Integer> pos) {
        super(CellType.BOMB, true, new RectangleBoundingBox(pos.x(), pos.y(), 1.0f, 1.0f));
        this.position = pos;
        this.range = range;
        this.bombType = bombType;
    }

    /**
     * 
     * @return the BombType of this Bomb
     */
    @Override
    public BombType getBombType() {
        return this.bombType;
    }

    /**
     * 
     * @return true if th ebomb is exploded
     */
    @Override
    public boolean isExploded() {
        return exploded;
    }

    /**
     * the bomb explode if the condition is true.
     * @param condition
     */
    @Override
    public void update(final boolean condition) {
        if (condition) {
            this.explode();
        }
    }

    /**
     * update the bomb
     * @param timeElapsed time passed after last update
     */
    @Override
    public void update(long timeElapsed) {
        timer += timeElapsed;
        if (timer >= TIME_TO_EXPLODE) {
            explode();
        }
    }

    private void explode() {
        exploded = true;
    }

    /**
     * 
     * @return the range of this Bomb
     */
    @Override
    public int getRange() {
        return range;
    }

    /**
     * 
     * @return the position of this Bomb
     */
    @Override
    public GenPair<Integer, Integer> getPos() {
        return position;
    }

    /**
     * 
     * @param map
     * @return the EntrySet that contains the flames of the explosion
     */
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
                .filter(p -> p.x() >= GameMap.MIN_NUM_CELL && p.x() <= GameMap.MAX_NUM_CELL
                    && p.y() >= GameMap.MIN_NUM_CELL && p.y() <= GameMap.MAX_NUM_CELL)
                .takeWhile(p -> stopFlamePropagation(p, map))
                .collect(Collectors.toMap(
                        Function.identity(),
                        p -> p.equals(
                                getPos().apply(Functions.sumInt(dir.getPair().apply(Functions.multiplyInt(getRange())))))
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
