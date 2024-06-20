package it.unibo.bombardero.cell;

import it.unibo.bombardero.map.api.Pair;

public class Wall extends AbstractCell {

    public Wall(CellType type, Pair pos) {
        super(type, pos, true);
    }

}
