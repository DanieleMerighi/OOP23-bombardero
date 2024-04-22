package it.unibo.bombardero.cell;

import java.util.Optional;

import it.unibo.bombardero.cell.Cell.CellType;

public interface BombFactory {
    
    BasicBomb CreateBomb(Optional<CellType> powerUp);
}
