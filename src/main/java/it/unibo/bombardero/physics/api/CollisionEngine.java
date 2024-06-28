package it.unibo.bombardero.physics.api;

import it.unibo.bombardero.character.Character;

/**
 * this interface checks the collision of the game and solves them.
 */
public interface CollisionEngine {
 
    /**
     * Check if character is in the same cell of flames.
     * @param character
     */
    void checkFlameAndPowerUpCollision(Character character);

    /**
     * check collision for the character with the cells in front of him.
     * @param character 
     */
    void checkCharacterCollision(Character character);

}
