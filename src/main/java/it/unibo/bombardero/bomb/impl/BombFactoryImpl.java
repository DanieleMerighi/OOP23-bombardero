package it.unibo.bombardero.bomb.impl;

import it.unibo.bombardero.bomb.api.Bomb;
import it.unibo.bombardero.bomb.api.BombFactory;
import it.unibo.bombardero.bomb.api.Bomb.BombType;
import it.unibo.bombardero.character.Character;
import it.unibo.bombardero.map.api.Functions;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.GenPair;

/**
 * This is a Factory that creates different types of Bomb.
 */
public final class BombFactoryImpl implements BombFactory {

    @Override
    public Bomb createBasicBomb(final int range, final GenPair<Integer, Integer> pos) {
        return new BasicBomb(BombType.BOMB_BASIC, range, pos) { };
    }

    @Override
    public Bomb createPiercingBomb(final int range, final GenPair<Integer, Integer> pos) {
        return new BasicBomb(BombType.BOMB_PIERCING, range, pos) {

            @Override
            public boolean isBreckableWall(final GenPair<Integer, Integer> pos, final GameMap map) {
                if (map.isBreakableWall(pos) && isLastWall(pos)) {
                    map.removeBreakableWall(pos);
                    return true;
                }
                return false;
            }

            private boolean isLastWall(final GenPair<Integer, Integer> pos) {
                return pos.equals(
                        this.getPos().apply(Functions.sumInt(new GenPair<Integer, Integer>(this.getRange(), 0))))
                        || pos.equals(this.getPos()
                                .apply(Functions.sumInt(new GenPair<Integer, Integer>(0, this.getRange()))))
                        || pos.equals(this.getPos()
                                .apply(Functions.sumInt(new GenPair<Integer, Integer>(-this.getRange(), 0))))
                        || pos.equals(this.getPos()
                                .apply(Functions.sumInt(new GenPair<Integer, Integer>(0, -this.getRange()))));
            }
        };
    }

    @Override
    public Bomb createPowerBomb(final GenPair<Integer, Integer> pos) {
        return new BasicBomb(BombType.BOMB_POWER, Character.MAX_FLAME_RANGE, pos) { };
    }

    @Override
    public Bomb createRemoteBomb(final int range, final GenPair<Integer, Integer> pos) {
        return new BasicBomb(BombType.BOMB_REMOTE, range, pos) {

            @Override
            public void update(final long timeElapsed) {
            }
        };
    }

}
