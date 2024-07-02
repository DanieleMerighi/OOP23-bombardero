package it.unibo.bombardero.cell;

import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.cell.Bomb.BombType;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.physics.impl.RectangleBoundingBox;


//TODO dividere la logica fare una factory normale  
//chiedere a baga del menu fare un po di grafica rendere Pair generico e magari fare due classi specifiche
//in generale cercare di rendere il tutto piu generico
public class BombFactoryImpl implements BombFactory {

    @Override
    public Bomb createBasicBomb(final int range, final Pair pos) {
        return new BasicBomb(BombType.BOMB_BASIC, range, pos, new RectangleBoundingBox(pos.x(), pos.y(), 1.0f , 1.0f)) {};
    }

    @Override
    public Bomb createPiercingBomb(final int range, final Pair pos) {
        return new BasicBomb(BombType.BOMB_PIERCING, range, pos, new RectangleBoundingBox(pos.x(), pos.y(), 1.0f , 1.0f)) {

            @Override
            public boolean isBreckableWall(final Pair pos, GameMap map, GameManager mgr) {
                if (mgr.getGameMap().isBreakableWall(pos) && isLastWall(pos)) {
                    mgr.removeWall(pos);
                    return true;
                }
                return false;
            }

            private boolean isLastWall(final Pair pos) {
                return pos.equals(this.getPos().sum(new Pair(this.getRange(), 0)))
                        || pos.equals(this.getPos().sum(new Pair(0, this.getRange())))
                        || pos.equals(this.getPos().sum(new Pair(-this.getRange(), 0)))
                        || pos.equals(this.getPos().sum(new Pair(0, -this.getRange())));
            }
        };
    }

    @Override
    public Bomb createPowerBomb(final Pair pos) {
        return new BasicBomb(BombType.BOMB_POWER, BasicBomb.MAX_RANGE, pos, new RectangleBoundingBox(pos.x(), pos.y(), 1.0f , 1.0f)) {};
    }

    @Override
    public Bomb createRemoteBomb(final int range, final Pair pos) {
        return new BasicBomb(BombType.BOMB_REMOTE, range, pos, new RectangleBoundingBox(pos.x(), pos.y(), 1.0f , 1.0f)) {

            @Override
            public void update() {}

            @Override
            public void update(final boolean condition) {
                super.update(condition);
            }
        };
    }

}
