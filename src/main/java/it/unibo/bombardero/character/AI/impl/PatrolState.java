package it.unibo.bombardero.character.ai.impl;

import it.unibo.bombardero.character.Enemy;
import it.unibo.bombardero.character.ai.api.AbstractEnemyState;
import it.unibo.bombardero.core.api.GameManager;

/**
 * In patrol state, the enemy moves randomly within the map.
 * It checks for dangers (bombs) and nearby players regularly.
 * - Transitions to ESCAPE if it detects a bomb within its flame range.
 * - Transitions to CHASE if it detects the player nearby.
 */
public class PatrolState extends AbstractEnemyState {

    /**
     * Executes the behavior associated with this enemy state.
     *
     * @param enemy   the enemy character to execute the state behavior on
     * @param manager the game manager
     */
    @Override
    public void execute(final Enemy enemy, final GameManager manager) {
        if (enemy.getGraph().isInDangerZone(manager.getGameMap(), enemy.getIntCoordinate(), enemy.getFlameRange())) { // Detected
                                                                                                                      // bomb
            enemy.setState(new EscapeState());
        } else if (enemy.isEnemyClose(manager)) { // Detected player
            enemy.setState(new ChaseState());
        } else {
            enemy.setNextMove(new ChaseMovementStrategy().getNextMove(enemy, manager));
            if(enemy.getNextMove().isEmpty()) {
                enemy.setNextMove(new RandomMovementStrategy().getNextMove(enemy, manager));
            }
        }
    }
}
