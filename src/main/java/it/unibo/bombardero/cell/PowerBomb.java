package it.unibo.bombardero.cell;

import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.utils.Utils;

public class PowerBomb extends BasicBomb{

    public PowerBomb(GameManager mgr, Pair pos, BombType type) {
        super(mgr, pos, type, Utils.MAX_RANGE_BOMB);
    }
}
