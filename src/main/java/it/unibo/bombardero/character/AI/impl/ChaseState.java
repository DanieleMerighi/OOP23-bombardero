package it.unibo.bombardero.character.AI.impl;

import it.unibo.bombardero.character.Enemy;
import it.unibo.bombardero.character.AI.api.EnemyState;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.Pair;

/**
 * In chase state, the enemy actively seeks the player.
 * It finds the shortest path to the player's current position and sets it
 * as the next move target.
 * - Transitions back to PATROL if it loses sight of the player.
 */
public class ChaseState implements EnemyState {

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
        } else if (!enemy.isEnemyClose(manager)) { // Lost sight of player
            enemy.setState(new PatrolState());
        } else {
            final Pair closeEnemy = enemy.getClosestEntity(manager).get();
            final Pair currPos = enemy.getIntCoordinate();
            if ((closeEnemy.x() == currPos.x() || closeEnemy.y() == currPos.y())) {
                if (enemy.calculateDistance(currPos, closeEnemy) <= enemy.getFlameRange()) {
                    enemy.placeBomb(manager);
                } else {
                    enemy.setNextMove(new RandomMovementStrategy().getNextMove(enemy, manager));
                }
            } else {
                enemy.setNextMove(new ChaseMovementStrategy().getNextMove(enemy, manager));
            }

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
        return obj instanceof ChaseState;
    }

}
