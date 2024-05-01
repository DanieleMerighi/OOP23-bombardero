package it.unibo.bombardero.cell;

import java.util.Optional;

import it.unibo.bombardero.cell.Cell.CellType;
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
    public Bomb CreateBomb(Optional<CellType> powerUp , Pair pos , int range) {
        if(powerUp.isEmpty()){
            return createBasicBomb(pos , range);
        }
        switch (powerUp.get()) { //TODO: add powerup type instead bomb type
            case BOMB_PIERCING:
                return createPircerBomb(pos , range);
            case BOMB_REMOTE:
                return createRemoteBomb(pos , range);
            case BOMB_POWER:
                return createPowerBomb(pos , range);
            case BOMB_PUNCH:
                return createPunchBomb(pos , range);
        }
        return null;
    }
    
    private Bomb createBasicBomb(Pair pos , int range) {
        return new BasicBomb(mgr, pos, CellType.BOMB_BASIC , range , ce) {
            
        };
    }

    private Bomb createPircerBomb(Pair pos , int range) {
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
