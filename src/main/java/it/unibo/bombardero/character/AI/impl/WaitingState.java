package it.unibo.bombardero.character.ai.impl;

import java.util.Optional;

import it.unibo.bombardero.character.Enemy;
import it.unibo.bombardero.character.ai.api.AbstractEnemyState;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.GenPair;

/**
 * Represents the WAITING state of the enemy where it evaluates its surroundings
 * and decides the next course of action.
 */
public class WaitingState extends AbstractEnemyState {

    /**
     * Executes the behavior associated with this enemy state.
     *
     * @param enemy   the enemy character to execute the state behavior on
     * @param manager the game manager
     */
    @Override
    public void execute(final Enemy enemy, final GameManager manager) {
        enemy.setStationary(true);
        enemy.setNextMove(Optional.empty());

        final GenPair<Integer, Integer> currentCoord = enemy.getIntCoordinate();
        final int flameRange = enemy.getFlameRange();

        if (enemy.getGraph().isInDangerZone(manager.getGameMap(), currentCoord, flameRange)) {
            enemy.setState(new EscapeState());
        } else if (!enemy.isAttemptedPowerUp()
                && enemy.getGraph().findNearestPowerUp(manager.getGameMap(), currentCoord).isPresent()) {
            enemy.setAttemptedPowerUp(true);
            enemy.setState(new ExploringState());
        } else if (enemy.getQueuedBombs().isEmpty()) {
            enemy.setState(new PatrolState());
        }

        if (!enemy.isStateEqualTo(new WaitingState())) {
            enemy.setStationary(false);
        }
    }
}
