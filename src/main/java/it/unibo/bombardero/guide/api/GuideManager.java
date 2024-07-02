package it.unibo.bombardero.guide.api;

import it.unibo.bombardero.core.api.Controller;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.Coord;
import it.unibo.bombardero.map.api.Pair;

/**
 * This interface models a slightly different game manager, 
 * intended to be used in the game's guide. 
 * @author Federico Bagattoni
*/
public interface GuideManager extends GameManager {

    /** 
     * The player's spawnpoint coordinates during the guide.
     */
    Coord PLAYER_GUIDE_SPAWNPOINT = new Coord(4.5f, 6.5f);

    /**
     * The coordinate where the guide's crate have to be spawned.
     */
    Pair CRATE_GUIDE_SPAWNPOINT = new Pair(8, 6);

    /** 
     * The coordinate where the dummy character have to be spawned.
     */
    Coord DUMMY_GUIDE_SPAWNPOINT = new Coord(8.5f, 6.5f);

    /**
     * Spawns a dummy {@link Character} in the game. The
     * character must do nothing and serves as target for the
     * player. 
     */
    void spawnDummy();

    void updateGame(long elapsed, Controller controller);
}
