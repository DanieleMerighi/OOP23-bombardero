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
    public BasicBomb CreateBomb(Character character) {
        if(!character.getBombType().isPresent()){
            return createBasicBomb(character, character.getIntCoordinate());
        }
        switch (character.getBombType().get()) {
            case PIERCING_BOMB:
                return createPiercingBomb(character, character.getIntCoordinate());
            case POWER_BOMB:
                return createRemoteBomb(character, character.getIntCoordinate());
            case REMOTE_BOMB:
                return createPowerBomb(character, character.getIntCoordinate());
            default :
                return null;
        }
    }

    @Override
    public BasicBomb CreateBomb(Character character, Pair pos) {
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
    
    private BasicBomb createBasicBomb(Character character, Pair pos) {
        return new BasicBomb(mgr, character, character.getFlameRange(), pos) {};
    }

    private BasicBomb createPiercingBomb(Character character, Pair pos) {
        return new BasicBomb(mgr , character, character.getFlameRange(), pos) {
            public boolean isBreckableWall(Pair pos) {
                if(mgr.getGameMap().isBreakableWall(pos)) {
                    mgr.removeWall(pos);
                }
                return false;
            }
        };
    }

    private BasicBomb createPowerBomb(Character character, Pair pos) {
        return new BasicBomb(mgr , character, BasicBomb.MAX_RANGE, pos) {};
    }

    private BasicBomb createRemoteBomb(Character character, Pair pos) {
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
