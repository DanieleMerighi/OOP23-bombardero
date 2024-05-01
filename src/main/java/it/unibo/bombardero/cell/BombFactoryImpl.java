package it.unibo.bombardero.cell;

import java.util.Optional;

import it.unibo.bombardero.cell.Bomb.BombType;
import it.unibo.bombardero.cell.Cell.CellType;
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
    public Bomb CreateBomb(Optional<PowerUpType> powerUp , Pair pos , int range) {
        if(powerUp.isEmpty()){
            return createBasicBomb(pos , range);
        }
        switch (powerUp.get()) { //TODO: add powerup type instead bomb type
            case PIERCING_BOMB:
                return createPiercingBomb(pos , range);
            case HYPER_BOMB:
                return createRemoteBomb(pos , range);
            case REMOTE_BOMB:
                return createPowerBomb(pos , range);
        }
        return null;
    }
    
    private Bomb createBasicBomb(Pair pos , int range) {
        return new BasicBomb(mgr, pos, BombType.BOMB_BASIC , range , ce) {
            
        };
    }

    private Bomb createPiercingBomb(Pair pos , int range) {
        return null;
    }

    private Bomb createPowerBomb(Pair pos , int range) {
        return null;
    }

    private Bomb createRemoteBomb(Pair pos , int range) {
        return null;
    }

    private Bomb createPunchBomb(Pair pos , int range) {
        return null;
    }
}
