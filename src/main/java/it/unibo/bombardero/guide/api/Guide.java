package it.unibo.bombardero.guide.api;

import it.unibo.bombardero.character.Direction;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.guide.impl.BombarderoGuideManager;
import it.unibo.bombardero.map.api.Coord;

public interface Guide extends GameManager {
    
    /**
     * Spawns a dummy {@link Character} in the game. The
     * character must do nothing and serves as target for the
     * player. 
     */
    void spawnDummy();

}
