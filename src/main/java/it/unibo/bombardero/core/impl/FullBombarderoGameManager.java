package it.unibo.bombardero.core.impl;

import java.util.Optional;

import it.unibo.bombardero.core.api.Controller;
import it.unibo.bombardero.utils.Utils;

public class FullBombarderoGameManager extends BasicBombarderoGameManager {

    private long gameTime;

    public FullBombarderoGameManager(final Controller controller) {
        super(controller, Utils.PLAYER_SPAWNPOINT, Utils.ENEMIES_SPAWNPOINT.subList(0, 1), true);
    }

    @Override
    public void updateGame(long elapsed) {
        super.updateGame(elapsed);
        gameTime += elapsed;
    }

    @Override
    public Optional<Long> getTimeLeft() {
        return Optional.of(gameTime < TOTAL_GAME_TIME ? TOTAL_GAME_TIME - gameTime : GAME_OVER_TIME);
    }
    
}
