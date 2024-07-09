package it.unibo.bombardero.core.api;

import it.unibo.bombardero.map.api.GenPair;

/**
 * This interface models a slightly different game manager, 
 * intended to be used in the game's guide. 
 * @author Federico Bagattoni
*/
public interface GuideManager extends GameManager {

    /** 
     * The player's spawnpoint coordinates during the guide.
     */
    GenPair<Float, Float> PLAYER_GUIDE_SPAWNPOINT = new GenPair<>(4.5f, 6.5f);

    /**
     * The coordinate where the guide's crate have to be spawned.
     */
    GenPair<Integer, Integer> CRATE_GUIDE_SPAWNPOINT = new GenPair<>(8, 6);

    /** 
     * The coordinate where the dummy character have to be spawned.
     */
    GenPair<Float, Float> DUMMY_GUIDE_SPAWNPOINT = new GenPair<>(8.5f, 6.5f);

    /**
     * Spawns a dummy {@link Character} in the game. The
     * character must do nothing and serves as target for the
     * player. 
     */
    void spawnDummy();

}
