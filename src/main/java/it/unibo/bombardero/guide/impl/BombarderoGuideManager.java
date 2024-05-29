package it.unibo.bombardero.guide.impl;

import it.unibo.bombardero.core.api.Controller;
import it.unibo.bombardero.core.impl.BombarderoGameManager;
import it.unibo.bombardero.guide.api.Guide;
import it.unibo.bombardero.map.api.Coord;
import it.unibo.bombardero.map.api.Pair;

public final class BombarderoGuideManager extends BombarderoGameManager implements Guide {

    public final static Coord PLAYER_GUIDE_SPAWNPOINT = new Coord(4.5f, 6.5f);
    public final static Pair CRATE_GUIDE_SPAWNPOINT = new Pair(8, 6);
    public final static Coord DUMMY_GUIDE_SPAWNPOIT = new Coord(7.5f, 5.5f);

    public BombarderoGuideManager(final Controller controller) {
        super(controller, true);
    }
    
}
