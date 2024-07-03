package it.bombardero;

import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import it.unibo.bombardero.cell.Bomb;
import it.unibo.bombardero.cell.Flame.FlameType;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.IntPair;
import it.unibo.bombardero.physics.api.BoundingBox;

/**
 * A Modified bomb for testing purpose.
 */
public class MyBomb implements Bomb{

    private IntPair pos;

    /**
     * Constructs a new {@link MyBomb}.
     * Initializes the position of the bomb.
     * 
     * @param pos the initial position of the bomb
     */
    MyBomb(final IntPair pos) {
        this.pos = pos;
    }

    /**
     * Returns the type of this cell.
     *
     * @return the cell type
     */
    @Override
    public CellType getCellType() {
        return CellType.BOMB;
    }

    /**
     * {@inheritDoc}
     * @throws UnsupportedOperationException as this method is not implemented
     */
    @Override
    public Optional<BoundingBox> getBoundingBox() {
        throw new UnsupportedOperationException("Unimplemented method 'getBoundingBox'");
    }

    /**
     * {@inheritDoc}
     * @throws UnsupportedOperationException as this method is not implemented
     */
    @Override
    public boolean isExploded() {
        throw new UnsupportedOperationException("Unimplemented method 'isExploded'");
    }

    /**
     * {@inheritDoc}
     * @throws UnsupportedOperationException as this method is not implemented
     */
    @Override
    public void update(final boolean condition) {
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    /**
     * {@inheritDoc}
     * @throws UnsupportedOperationException as this method is not implemented
     */
    @Override
    public void update() {
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    /**
     * {@inheritDoc}
     * @throws UnsupportedOperationException as this method is not implemented
     */
    @Override
    public BombType getBombType() {
        throw new UnsupportedOperationException("Unimplemented method 'getBombType'");
    }

    /**
     * {@inheritDoc}
     * @throws UnsupportedOperationException as this method is not implemented
     */
    @Override
    public int getRange() {
        throw new UnsupportedOperationException("Unimplemented method 'getRange'");
    }

    /**
     * Gets the position of this bomb.
     *
     * @return the position of the bomb
     */
    @Override
    public IntPair getPos() {
        return this.pos;
    }

    @Override
    public boolean haveBoundingCollision() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'haveBoundingCollision'");
    }

    @Override
    public Set<Entry<IntPair, FlameType>> computeFlame(GameMap map) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'computeFlame'");
    }
}
