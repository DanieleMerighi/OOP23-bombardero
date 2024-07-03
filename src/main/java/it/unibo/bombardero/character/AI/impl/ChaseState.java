package it.unibo.bombardero.character.ai.impl;

import it.unibo.bombardero.character.Enemy;
import it.unibo.bombardero.character.Character.CharacterType;
import it.unibo.bombardero.character.ai.api.AbstractEnemyState;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.GenPair;

/**
 * In chase state, the enemy actively seeks the player.
 * It finds the shortest path to the player's current position and sets it
 * as the next move target.
 * - Transitions back to PATROL if it loses sight of the player.
 */
public class ChaseState extends AbstractEnemyState {

    /**
     * Executes the behavior associated with this enemy state.
     *
     * @param enemy the enemy character to execute the state behavior on
     * @param manager the game manager
     */
    @Override
    public void execute(final Enemy enemy, final GameManager manager) {
        if (enemy.getGraph().isInDangerZone(enemy.getIntCoordinate(), enemy.getFlameRange())) { // Detected bomb
            enemy.setState(new EscapeState());
        } else if (!enemy.isEnemyClose(manager)) { // Lost sight of player
            enemy.setState(new PatrolState());
        } else {
            final GenPair<Integer, Integer> closeEnemy = enemy.getClosestEntity(manager).get();
            final GenPair<Integer, Integer> currPos = enemy.getIntCoordinate();
            if ((closeEnemy.x() == currPos.x() || closeEnemy.y() == currPos.y())) {
                if (enemy.calculateDistance(currPos, closeEnemy) <= enemy.getFlameRange()) {
                    enemy.placeBomb(manager, CharacterType.ENEMY);
                } else {
                    enemy.setNextMove(new RandomMovementStrategy().getNextMove(enemy, manager));
                }
            } else {
                enemy.setNextMove(new ChaseMovementStrategy().getNextMove(enemy, manager));
            }

        }
    }
}
