package it.bombardero;

import it.unibo.bombardero.cell.Bomb;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.physics.api.BoundingBox;

public class MyBomb implements Bomb {

    private Pair pos;

    MyBomb(final Pair pos) {
        this.pos = pos;
    }

    @Override
    public boolean getBoundingCollision() {
        throw new UnsupportedOperationException("Unimplemented method 'getBoundingCollision'");
    }

    @Override
    public CellType getCellType() {
        return CellType.BOMB;
    }

    @Override
    public BoundingBox getBoundingBox() {
        throw new UnsupportedOperationException("Unimplemented method 'getBoundingBox'");
    }

    @Override
    public boolean isExploded() {
        throw new UnsupportedOperationException("Unimplemented method 'isExploded'");
    }

    @Override
    public void update(final boolean condition) {
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void update() {
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public BombType getBombType() {
        throw new UnsupportedOperationException("Unimplemented method 'getBombType'");
    }

    @Override
    public int getRange() {
        throw new UnsupportedOperationException("Unimplemented method 'getRange'");
    }

    @Override
    public Pair getPos() {
        return this.pos;
    }
}