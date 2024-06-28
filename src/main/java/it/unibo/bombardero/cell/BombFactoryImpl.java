package it.unibo.bombardero.cell;

import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.character.Character;
import it.unibo.bombardero.map.api.Pair;

public class BombFactoryImpl implements BombFactory {
    private final GameManager mgr;

    public BombFactoryImpl(final GameManager mgr) {
        this.mgr = mgr;
    }

    @Override
    public Bomb createBomb(final Character character) {
        if (!character.getBombType().isPresent()) {
            return createBasicBomb(character, character.getIntCoordinate());
        }
        switch (character.getBombType().get()) {
            case PIERCING_BOMB:
                return createPiercingBomb(character, character.getIntCoordinate());
            case REMOTE_BOMB:
                return createRemoteBomb(character, character.getIntCoordinate());
            case POWER_BOMB:
                return createPowerBomb(character, character.getIntCoordinate());
            default:
                return null;
        }
    }

    @Override
    public Bomb createBomb(final Character character, final Pair pos) {
        if (!character.getBombType().isPresent()) {
            return createBasicBomb(character, pos);
        }
        switch (character.getBombType().get()) {
            case PIERCING_BOMB:
                return createPiercingBomb(character, pos);
            case REMOTE_BOMB:
                return createRemoteBomb(character, pos);
            case POWER_BOMB:
                return createPowerBomb(character, pos);
            default:
                return null;
        }
    }

    private Bomb createBasicBomb(final Character character, final Pair pos) {
        return new BasicBomb(mgr, character, character.getFlameRange(), pos) {
        };
    }

    private Bomb createPiercingBomb(final Character character, final Pair pos) {
        return new BasicBomb(mgr, character, character.getFlameRange(), pos) {

            @Override
            public boolean isBreckableWall(final Pair pos) {
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

    private Bomb createPowerBomb(final Character character, final Pair pos) {
        return new BasicBomb(mgr, character, BasicBomb.MAX_RANGE, pos) {
        };
    }

    private Bomb createRemoteBomb(final Character character, final Pair pos) {
        return new BasicBomb(mgr, character, character.getFlameRange(), pos) {

            @Override
            public void update() {
            }

            @Override
            public void update(final boolean condition) {
                super.update(condition);
                character.removeBombFromDeque(this);
            }
        };
    }

}
