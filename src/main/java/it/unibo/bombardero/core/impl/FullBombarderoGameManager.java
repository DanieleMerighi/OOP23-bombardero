package it.unibo.bombardero.core.impl;

import java.util.Optional;
import java.util.stream.Stream;

import it.unibo.bombardero.core.api.Controller;
import it.unibo.bombardero.physics.api.CollisionEngine;
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
     * @param cEngine the collision engine related to this instance of the game.
     */
    public FullBombarderoGameManager(final CollisionEngine cEngine) {
        super(Utils.PLAYER_SPAWNPOINT,
            Utils.ENEMIES_SPAWNPOINT,
            true,
            cEngine);
    }

    @Override
    public void updateGame(final long elapsed, final Controller controller) {
        gameTime += elapsed;
        super.updateGame(elapsed, controller);
        if (gameTime >= TOTAL_GAME_TIME) {
            super.triggerCollapse();
            Stream.of(getEnemies(), getPlayers())
            .flatMap(list -> list.stream())
            .forEach(character -> {
                getCollisionEngine().checkMapCollapseCollision(character, getGameMap());
            });
        }
    }

    @Override
    public Optional<Long> getTimeLeft() {
        return Optional.of(gameTime < TOTAL_GAME_TIME ? TOTAL_GAME_TIME - gameTime : 0L);
    }

}
