package it.unibo.bombardero.cell;

import java.util.Optional;
import java.util.function.Predicate;

import edu.umd.cs.findbugs.annotations.OverrideMustInvoke;
import it.unibo.bombardero.cell.Bomb.BombType;
import it.unibo.bombardero.cell.Cell.CellType;
import it.unibo.bombardero.cell.powerup.api.PowerUpType;
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
            createBasicBomb(character);
        }
        switch (character.getBombType().get()) {
            case PIERCING_BOMB:
                return createPiercingBomb(character);
            case POWER_BOMB:
                return createRemoteBomb(character);
            case REMOTE_BOMB:
                return createPowerBomb(character);
            default :
                return null;
        }
    }
    
    private BasicBomb createBasicBomb(Character character) {
        return new BasicBomb(mgr,character) {
            
        };
    }

    private BasicBomb createPiercingBomb(Character character) {
        return new BasicBomb(mgr , character) {
            @Override
            protected Predicate<? super Pair> stopFlamePropagation() {
            return p-> !super.map.isBomb(p) && !super.map.isUnbreakableWall(p) && (super.map.isBreakableWall(p) && mgr.removeWall(p));
    }
        };
    }

    private BasicBomb createPowerBomb(Character character) {
        return new BasicBomb(mgr , character) {
            
        };
    }

    private BasicBomb createRemoteBomb(Character character) {
        return new BasicBomb(mgr , character ) {

            @Override
            public void update () {
                super.update(character.getHasToExplodeRemoteBomb());
            }

        };
    }

}
