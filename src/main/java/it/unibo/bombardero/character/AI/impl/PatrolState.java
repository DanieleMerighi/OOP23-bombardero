package it.unibo.bombardero.character.AI.impl;

import it.unibo.bombardero.character.Enemy;
import it.unibo.bombardero.character.AI.api.EnemyState;
import it.unibo.bombardero.core.api.GameManager;

/**
 * In patrol state, the enemy moves randomly within the map.
 * It checks for dangers (bombs) and nearby players regularly.
 * - Transitions to ESCAPE if it detects a bomb within its flame range.
 * - Transitions to CHASE if it detects the player nearby.
 */
public class PatrolState implements EnemyState {

    /**
     * Executes the behavior associated with this enemy state.
     *
     * @param enemy the enemy character to execute the state behavior on
     * @param map   the game map where the enemy operates
     */
    @Override
    public void execute(final Enemy enemy, final GameManager manager) {
        if (enemy.getGraph().isInDangerZone(enemy.getIntCoordinate(), enemy.getFlameRange())) { // Detected bomb
            enemy.setState(new EscapeState());
        } else if (enemy.isEnemyClose(manager)) { // Detected player
            enemy.setState(new ChaseState());
        } else {
            enemy.setNextMove(new ChaseMovementStrategy().getNextMove(enemy, manager));
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
        return obj instanceof PatrolState;
    }
}
