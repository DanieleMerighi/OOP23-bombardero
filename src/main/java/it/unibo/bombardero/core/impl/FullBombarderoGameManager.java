package it.unibo.bombardero.core.impl;

import java.util.Optional;

import it.unibo.bombardero.core.api.Controller;
import it.unibo.bombardero.utils.Utils;
/**
 * This class extends the {@link BasicBombarderoGameManager} class
 * and has the capability to keep time thus adding the possibility
 * of the {@link GameMap} collapse.
 */
public final class FullBombarderoGameManager extends BasicBombarderoGameManager {

    private long gameTime;

    /**
     * Creates a new {@link FullBombarderoGameManager} spawning the player, the enemies and 
     * generating the breakable walls in the map.
     * @param controller the game's controller
     */
    public FullBombarderoGameManager(final Controller controller) {
        super(controller, Utils.PLAYER_SPAWNPOINT, Utils.ENEMIES_SPAWNPOINT.subList(0, 1), true);
    }

    @Override
    public void updateGame(final long elapsed) {
        super.updateGame(elapsed);
        gameTime += elapsed;
    }

    @Override
    public Optional<Long> getTimeLeft() {
        return Optional.of(gameTime < TOTAL_GAME_TIME ? TOTAL_GAME_TIME - gameTime : GAME_OVER_TIME);
    }

}
