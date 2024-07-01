package it.unibo.bombardero.character.AI.impl;

import java.util.Optional;
import java.util.List;

import it.unibo.bombardero.cell.powerup.api.PowerUpType;
import it.unibo.bombardero.character.Enemy;
import it.unibo.bombardero.character.AI.api.EnemyState;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.Pair;

/**
 * Represents the EXPLORING state of the enemy where it seeks out the nearest
 * power-up
 * while avoiding danger zones.
 */
public class ExploringState implements EnemyState {

    /**
     * Executes the behavior associated with this enemy state.
     *
     * @param enemy the enemy character to execute the state behavior on
     * @param map   the game map where the enemy operates
     */
    @Override
    public void execute(final Enemy enemy, final GameMap map) {
        final Optional<Pair> powerUp = enemy.getGraph().findNearestPowerUp(enemy.getIntCoordinate());
        if (powerUp.isPresent()
                && !enemy.getGraph().isInDangerZone(enemy.getIntCoordinate(), enemy.getFlameRange())) {
            enemy.setNextMove(powerUp);
            final List<Pair> l = enemy.getGraph().findShortestPathToPlayer(enemy.getIntCoordinate(),
                    enemy.getNextMove().get());
            if (l.stream().anyMatch(c -> enemy.getGraph().isInDangerZone(c, enemy.getFlameRange())
                    || map.isBreakableWall(c)
                    || map.whichPowerUpType(c).map(PowerUpType.SKULL::equals).orElse(false))) {
                enemy.setState(new WaitingState());
                enemy.setNextMove(Optional.empty());
            } else {
                enemy.setNextMove(new ShortestMovementStrategy().getNextMove(enemy, map));
                enemy.setAttemptedPowerUp(false);
            }

        } else if (enemy.getGraph().isInDangerZone(enemy.getIntCoordinate(), enemy.getFlameRange())) {
            enemy.setState(new EscapeState());
        } else {
            enemy.setState(new PatrolState());
        }
    }

    /**
     * Checks if this enemy state is equal to another state.
     *
     * @param otherState the other enemy state to compare with
     * @return true if this state is equal to the other state, false otherwise
     */
    @Override
    public boolean equals(final EnemyState otherState) {
        return otherState instanceof ExploringState;
    }

}
