package it.unibo.bombardero.character.ai.impl;

import java.util.Optional;
import java.util.List;

import it.unibo.bombardero.cell.powerup.api.PowerUpType;
import it.unibo.bombardero.character.Enemy;
import it.unibo.bombardero.character.ai.api.AbstractEnemyState;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.GenPair;

/**
 * Represents the EXPLORING state of the enemy where it seeks out the nearest
 * power-up
 * while avoiding danger zones.
 */
public class ExploringState extends AbstractEnemyState {

    /**
     * Executes the behavior associated with this enemy state.
     *
     * @param enemy   the enemy character to execute the state behavior on
     * @param manager the game manager
     */
    @Override
    public void execute(final Enemy enemy, final GameManager manager) {
        final GameMap map = manager.getGameMap();
        final Optional<GenPair<Integer, Integer>> powerUp = enemy.getGraph().findNearestPowerUp(enemy.getIntCoordinate());
        if (powerUp.isPresent()
                && !enemy.getGraph().isInDangerZone(enemy.getIntCoordinate(), enemy.getFlameRange())) {
            enemy.setNextMove(powerUp);
            final List<GenPair<Integer, Integer>> l = enemy.getGraph().findShortestPathToPlayer(
                    enemy.getIntCoordinate(),
                    enemy.getNextMove().get());
            if (l.stream().anyMatch(c -> enemy.getGraph().isInDangerZone(c, enemy.getFlameRange())
                    || map.isBreakableWall(c)
                    || map.whichPowerUpType(c).map(PowerUpType.SKULL::equals).orElse(false))) {
                enemy.setState(new WaitingState());
                enemy.setNextMove(Optional.empty());
            } else {
                enemy.setNextMove(new ShortestMovementStrategy().getNextMove(enemy, manager));
                enemy.setAttemptedPowerUp(false);
            }

        } else if (enemy.getGraph().isInDangerZone(enemy.getIntCoordinate(), enemy.getFlameRange())) {
            enemy.setState(new EscapeState());
        } else {
            enemy.setState(new PatrolState());
        }
    }
}
