package it.unibo.bombardero.character.AI.api;

import it.unibo.bombardero.character.Enemy;
import it.unibo.bombardero.map.api.GameMap;

/**
 * Represents the different behavioral states of an enemy in the game.
 * Each state defines specific actions the enemy takes based on its current
 * situation (e.g., danger zone, player proximity).
 */
public interface EnemyState {

    /**
     * Executes the behavior associated with this enemy state.
     *
     * @param enemy the enemy character to execute the state behavior on
     * @param map   the game map where the enemy operates
     */
    void execute(Enemy enemy, GameMap map);

    /**
     * Checks if this enemy state is equal to another state.
     *
     * @param otherState the other enemy state to compare with
     * @return true if this state is equal to the other state, false otherwise
     */
    boolean equals(EnemyState otherState);
}
