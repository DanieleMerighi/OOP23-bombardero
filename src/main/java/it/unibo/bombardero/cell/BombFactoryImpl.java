package it.unibo.bombardero.cell;

import java.util.Optional;
import java.util.function.Predicate;

import it.unibo.bombardero.cell.Bomb.BombType;
import it.unibo.bombardero.cell.PowerUp.PowerUpType;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.physics.api.CollisionEngine;

public class BombFactoryImpl implements BombFactory{
    private GameManager mgr;
    private CollisionEngine ce;

    public BombFactoryImpl(GameManager mgr , CollisionEngine ce) {
        this.mgr=mgr;
        this.ce=ce;
    }

    @Override
    public BasicBomb CreateBomb(Optional<PowerUpType> powerUp , Pair pos , int range) {
        if(powerUp.isEmpty()){
            return createBasicBomb(pos , range);
        }
        switch (powerUp.get()) {
            case PIERCING_BOMB:
                return createPiercingBomb(pos , range);
            case POWER_BOMB:
                return createRemoteBomb(pos , range);
            case REMOTE_BOMB:
                return createPowerBomb(pos , range);
            default :
                return null;
        }
    }
    
    private BasicBomb createBasicBomb(Pair pos , int range) {
        return new BasicBomb(mgr, pos, BombType.BOMB_BASIC , range , ce) {
            
        };
    }

    private BasicBomb createPiercingBomb(Pair pos , int range) {
        return new BasicBomb(mgr , pos , BombType.BOMB_PIERCING , range , ce) {
            @Override
            protected Predicate<? super Pair> stopFlamePropagation() {
            return p-> !super.map.isBomb(p) && !super.map.isUnbreakableWall(p) && (super.map.isBreakableWall(p) && mgr.removeWall(p));
    }
        };
    }

    private BasicBomb createPowerBomb(Pair pos , int range) {
        return new BasicBomb(mgr , pos , BombType.BOMB_POWER , BasicBomb.MAX_RANGE , ce) {
            
        };
    }

    private BasicBomb createRemoteBomb(Pair pos , int range) {
        return new BasicBomb(mgr , pos , BombType.BOMB_REMOTE , range , ce) {
            
            public void update(){
                if(true){
                    super.update(BasicBomb.TIME_TO_EXPLODE); //TODO find better option cosi non funziona
                }
            }
        };
    }

}
