package it.unibo.bombardero.cell;

import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.character.Character;
import it.unibo.bombardero.map.api.Pair;

public class BombFactoryImpl implements BombFactory{
    private GameManager mgr;

    public BombFactoryImpl(GameManager mgr) {
        this.mgr=mgr;
    }

    @Override
    public Bomb CreateBomb(Character character) {
        if(!character.getBombType().isPresent()){
            return createBasicBomb(character, character.getIntCoordinate());
        }
        switch (character.getBombType().get()) {
            case PIERCING_BOMB:
                return createPiercingBomb(character, character.getIntCoordinate());
            case REMOTE_BOMB:
                return createRemoteBomb(character, character.getIntCoordinate());
            case POWER_BOMB:
                return createPowerBomb(character, character.getIntCoordinate());
            default :
                return null;
        }
    }

    @Override
    public Bomb CreateBomb(Character character, Pair pos) {
        if(!character.getBombType().isPresent()){
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
    
    private Bomb createBasicBomb(Character character, Pair pos) {
        return new BasicBomb(mgr, character, character.getFlameRange(), pos) {};
    }

    private Bomb createPiercingBomb(Character character, Pair pos) {
        return new BasicBomb(mgr , character, character.getFlameRange(), pos) {
            public boolean isBreckableWall(Pair pos) {
                if(mgr.getGameMap().isBreakableWall(pos)) {
                    if(isLastWall(pos)) {
                        mgr.removeWall(pos);
                        return true;
                    }
                }
                return false;
            }

            private boolean isLastWall(Pair pos) {
                return pos.equals(this.getPos().sum(new Pair(this.getRange(),0))) 
                    || pos.equals(this.getPos().sum(new Pair(0,this.getRange())))
                    || pos.equals(this.getPos().sum(new Pair(-this.getRange(),0))) 
                    || pos.equals(this.getPos().sum(new Pair(0,-this.getRange())));
            }
        };
    }

    private Bomb createPowerBomb(Character character, Pair pos) {
        return new BasicBomb(mgr , character, BasicBomb.MAX_RANGE, pos) {};
    }

    private Bomb createRemoteBomb(Character character, Pair pos) {
        return new BasicBomb(mgr, character, character.getFlameRange(), pos) {

            @Override
            public void update() {} 
            
            @Override
            public void update(boolean condition) {
                super.update(condition);
                character.removeBombFromDeque(this);
            }
        };
    }

}
