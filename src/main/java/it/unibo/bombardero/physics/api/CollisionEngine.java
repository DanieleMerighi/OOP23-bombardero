package it.unibo.bombardero.physics.api;

import it.unibo.bombardero.character.Character;
import it.unibo.bombardero.map.api.GameMap;

/**
 * This interface checks the collision of the game and solves them.
 */
public interface CollisionEngine {
 
    /**
     * Check if character is in the same cell of flames.
     * @param character
     * @param gMap
     */
    void checkFlameAndPowerUpCollision(Character character, GameMap gMap);

    /**
     * Check collision for the character with the cells in front of him.
     * @param character 
     * @param gMap
     */
    void checkCharacterCollision(Character character, GameMap gMap);

}
