package it.unibo.bombardero.character.AI.impl;

import java.util.Optional;

import it.unibo.bombardero.character.Enemy;
import it.unibo.bombardero.character.AI.api.EnemyState;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.Pair;

/**
 * Represents the WAITING state of the enemy where it evaluates its surroundings
 * and decides the next course of action.
 */
public class WaitingState implements EnemyState {

    /**
     * Executes the behavior associated with this enemy state.
     *
     * @param enemy the enemy character to execute the state behavior on
     * @param map   the game map where the enemy operates
     */
    @Override
    public void execute(final Enemy enemy, final GameManager manager) {
        enemy.setStationary(true);
        enemy.setNextMove(Optional.empty());

        Pair currentCoord = enemy.getIntCoordinate();
        int flameRange = enemy.getFlameRange();

        if (enemy.getGraph().isInDangerZone(currentCoord, flameRange)) {
            enemy.setState(new EscapeState());
        } else if (!enemy.isAttemptedPowerUp() && enemy.getGraph().findNearestPowerUp(currentCoord).isPresent()) {
            enemy.setAttemptedPowerUp(true);
            enemy.setState(new ExploringState());
        } else if (enemy.getBombQueue().isEmpty()) {
            enemy.setState(new PatrolState());
        }

        if (!enemy.isStateEqualTo(new WaitingState())) {
            enemy.setStationary(false);
        }
    }

    /**
     * Checks if this enemy state is equal to another state.
     *
     * @param obj the other enemy state to compare with
     * @return true if this state is equal to the other state, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof WaitingState;
    }
}
