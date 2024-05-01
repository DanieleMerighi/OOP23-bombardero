package it.unibo.bombardero.cell;

import java.util.Optional;

import it.unibo.bombardero.cell.Cell.CellType;
import it.unibo.bombardero.map.api.Pair;

public interface BombFactory {
    
    Bomb CreateBomb(Optional<CellType> powerUp , Pair pos , int range);
}
