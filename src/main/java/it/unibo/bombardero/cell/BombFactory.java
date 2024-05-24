package it.unibo.bombardero.cell;

import java.util.Optional;

import it.unibo.bombardero.cell.Cell.CellType;
import it.unibo.bombardero.cell.powerup.api.PowerUpType;
import it.unibo.bombardero.map.api.Pair;

public interface BombFactory {
    
    BasicBomb CreateBomb(Optional<PowerUpType> powerUp , Pair pos , int range);
}
