package it.unibo.bombardero.physics.api;

import it.unibo.bombardero.character.Character;
import it.unibo.bombardero.map.api.GameMap;

/**
 * This interface Take care of the collisions in the Game.
 */
public interface CollisionEngine {
 
    /**
     * Check if character is in the same cell of flames.
     * @param character
     * @param gMap nededed to check cells
     */
    void checkFlameAndPowerUpCollision(Character character, GameMap gMap);

    /**
     * Check collision for the character with the cells in front of him.
     * @param character 
     * @param gMap nededed to check cells
     */
    void checkCharacterCollision(Character character, GameMap gMap);


    /**
     * check if the character is over an UnbreckableWall.
     * @param character
     * @param map
     */
    void checkMapCollapseCollision(Character character, GameMap map);
}
