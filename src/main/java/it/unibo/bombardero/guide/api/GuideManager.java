package it.unibo.bombardero.guide.api;

import it.unibo.bombardero.character.Direction;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.guide.impl.BombarderoGuideManager;
import it.unibo.bombardero.map.api.Coord;

/**
 * This interface models a slightly different game manager, 
 * intended to be used in the game's guide. 
 * @author Federico Bagattoni
*/
public interface GuideManager extends GameManager {
    
    /**
     * Spawns a dummy {@link Character} in the game. The
     * character must do nothing and serves as target for the
     * player. 
     */
    void spawnDummy();

}
