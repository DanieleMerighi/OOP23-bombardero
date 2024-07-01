package it.unibo.bombardero.character.AI.impl;

import it.unibo.bombardero.character.Enemy;
import it.unibo.bombardero.character.AI.api.EnemyState;
import it.unibo.bombardero.map.api.GameMap;
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
    public void execute(final Enemy enemy, final GameMap map) {
        if (enemy.getGraph().isInDangerZone(enemy.getIntCoordinate(), enemy.getFlameRange())) { // Detected bomb
            enemy.setState(new EscapeState());
        } else if (!enemy.isEnemyClose()) { // Lost sight of player
            enemy.setState(new PatrolState());
        } else {
            final Pair closeEnemy = enemy.getClosestEntity().get();
            final Pair currPos = enemy.getIntCoordinate();
            if ((closeEnemy.x() == currPos.x() || closeEnemy.y() == currPos.y())) {
                if (enemy.calculateDistance(currPos, closeEnemy) <= enemy.getFlameRange()) {
                    enemy.placeBomb();
                } else {
                    enemy.setNextMove(new RandomMovementStrategy().getNextMove(enemy, map));
                }
            } else {
                enemy.setNextMove(new ChaseMovementStrategy().getNextMove(enemy, map));
            }

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
        return otherState instanceof ChaseState;
    }

}
