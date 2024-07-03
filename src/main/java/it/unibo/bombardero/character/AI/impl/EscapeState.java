package it.unibo.bombardero.character.ai.impl;

import java.util.Optional;

import it.unibo.bombardero.character.Enemy;
import it.unibo.bombardero.character.ai.api.AbstractEnemyState;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.Functions;
import it.unibo.bombardero.map.api.GenPair;
import it.unibo.bombardero.character.Character;

/**
 * In escape state, the enemy tries to move away from a detected danger zone.
 * It finds the nearest safe space outside the bomb's explosion radius
 * and sets it as the next move target.
 * - Transitions back to PATROL if it reaches a safe space.
 */
public class EscapeState extends AbstractEnemyState {

    /**
     * Executes the behavior associated with this enemy state.
     *
     * @param enemy the enemy character to execute the state behavior on
     * @param manager the game manager
     */
    @Override
    public void execute(final Enemy enemy, final GameManager manager) {
        if (!enemy.getGraph().isInDangerZone(enemy.getIntCoordinate(), enemy.getFlameRange())) { // Safe now
            if (!enemy.getBombQueue().isEmpty()) {
                enemy.setState(new WaitingState());
            } else {
                if (enemy.isEnemyClose(manager)) {
                    final GenPair<Integer, Integer> closeEnemy = enemy.getClosestEntity(manager).get();
                    final Optional<Character> c = enemy.getClosestEntity(manager, closeEnemy);
                    if (c.isPresent()) {
                        final GenPair<Integer, Integer> newdir = new GenPair<Integer, Integer>(-c.get().getFacingDirection().x(),
                                -c.get().getFacingDirection().y());
                        final GenPair<Integer, Integer> currPos = enemy.getIntCoordinate();
                        enemy.setNextMove(Optional.of(currPos.apply(Functions.sumInt(newdir))));
                    }
                } else {
                    enemy.setState(new PatrolState());
                }
            }
        } else {
            enemy.setNextMove(new EscapeMovementStrategy().getNextMove(enemy, manager));
            if (enemy.getNextMove().isEmpty()) {
                enemy.setState(new WaitingState());
            }
        }
    }
}
