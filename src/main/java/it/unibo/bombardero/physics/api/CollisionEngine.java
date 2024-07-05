package it.unibo.bombardero.physics.api;

import it.unibo.bombardero.character.Character;
import it.unibo.bombardero.map.api.GameMap;

/**
 * this interface checks the collision of the game and solves them.
 */
public interface CollisionEngine {
 
    /**
     * Check if character is in the same cell of flames.
     * @param character
     * @param gMap neded to check cells
     */
    void checkFlameAndPowerUpCollision(Character character, GameMap gMap);

    /**
     * check collision for the character with the cells in front of him.
     * @param character 
     * @param gMap neded to check cells
     */
    void checkCharacterCollision(Character character, GameMap gMap);

}
