package it.unibo.bombardero.cell;

import java.util.Optional;

import it.unibo.bombardero.cell.Cell.CellType;

public class BombFactoryImpl implements BombFactory{

    @Override
    public BasicBomb CreateBomb(Optional<CellType> powerUp) {
        switch (powerUp.get()) { //TODO: add powerup type instead bomb type
            case BOMB_PIERCING:
                
                break;
            case BOMB_REMOTE:
                
            break;
            case BOMB_POWER:
                
            break;
            case BOMB_PUNCH:
                
            break;
            default:
                break;
        }
        return null;
    }
    
    private Bomb createBasicBomb(){
        return null;
    }

    private Bomb createPircerBomb(){
        return null;
    }

    private Bomb createPowerBomb(){
        return null;
    }

    private Bomb createRemoteBomb(){
        return null;
    }

    private Bomb createPunchBomb(){
        return null;
    }
}
