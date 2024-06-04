package it.unibo.bombardero.physics.api;

import it.unibo.bombardero.character.Character;

public interface CollisionEngine {
    
    /**
     * Check if character are in the same cell of flames
     */
    void checkFlameCollision();

    /**
     * check collision for the character with the cells in front of him
     * @param character 
     */
    void checkCharacterCollision(Character character);

}
