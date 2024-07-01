package it.unibo.bombardero.character.AI.impl;

import java.util.Optional;

import it.unibo.bombardero.character.Enemy;
import it.unibo.bombardero.character.AI.api.EnemyState;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.character.Character;

/**
 * In escape state, the enemy tries to move away from a detected danger zone.
 * It finds the nearest safe space outside the bomb's explosion radius
 * and sets it as the next move target.
 * - Transitions back to PATROL if it reaches a safe space.
 */
public class EscapeState implements EnemyState {

    /**
     * Executes the behavior associated with this enemy state.
     *
     * @param enemy the enemy character to execute the state behavior on
     * @param map   the game map where the enemy operates
     */
    @Override
    public void execute(final Enemy enemy, final GameMap map) {
        if (!enemy.getGraph().isInDangerZone(enemy.getIntCoordinate(), enemy.getFlameRange())) { // Safe now
            if (!enemy.getBombQueue().isEmpty()) {
                enemy.setState(new WaitingState());
            } else {
                if (enemy.isEnemyClose()) {
                    final Pair closeEnemy = enemy.getClosestEntity().get();
                    final Optional<Character> c = enemy.getClosestEntity(closeEnemy);
                    if (c.isPresent()) {
                        final Pair newdir = new Pair(-c.get().getFacingDirection().x(),
                                -c.get().getFacingDirection().y());
                        final Pair currPos = enemy.getIntCoordinate();
                        enemy.setNextMove(Optional.of(currPos.sum(newdir)));
                    }
                } else {
                    enemy.setState(new PatrolState());
                }
            }
        } else {
            enemy.setNextMove(new EscapeMovementStrategy().getNextMove(enemy, map));
            if (enemy.getNextMove().isEmpty()) {
                enemy.setState(new WaitingState());
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
        return otherState instanceof EscapeState;
    }

}
