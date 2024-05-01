package it.unibo.bombardero.cell;

import java.util.Map.Entry;

import it.unibo.bombardero.cell.Cell.CellType;
import it.unibo.bombardero.map.api.Pair;

public interface Bomb{

    void update(long time);

    CellType getType();

    int getRange();

    Pair getPos();
}
