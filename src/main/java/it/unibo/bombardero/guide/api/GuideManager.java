package it.unibo.bombardero.guide.api;

import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.Coord;
import it.unibo.bombardero.map.api.Pair;

/**
 * This interface models a slightly different game manager, 
 * intended to be used in the game's guide. 
 * @author Federico Bagattoni
*/
public interface GuideManager extends GameManager {

    Coord PLAYER_GUIDE_SPAWNPOINT = new Coord(4.5f, 6.5f);
    Pair CRATE_GUIDE_SPAWNPOINT = new Pair(8, 6);
    Coord DUMMY_GUIDE_SPAWNPOINT = new Coord(8.5f, 6.5f);
    
    /**
     * Spawns a dummy {@link Character} in the game. The
     * character must do nothing and serves as target for the
     * player. 
     */
    void spawnDummy();

}
