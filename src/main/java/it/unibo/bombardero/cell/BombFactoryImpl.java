package it.unibo.bombardero.cell;

import java.util.Optional;

import it.unibo.bombardero.cell.Cell.CellType;

public class BombFactoryImpl implements BombFactory{

    @Override
    public BasicBomb CreateBomb(Optional<CellType> powerUp) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'CreateBomb'");
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
