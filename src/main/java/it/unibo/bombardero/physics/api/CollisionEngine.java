package it.unibo.bombardero.physics.api;

import java.util.Map.Entry;
import java.util.Set;

import it.unibo.bombardero.cell.BasicBomb;
import it.unibo.bombardero.cell.Bomb;
import it.unibo.bombardero.cell.Flame;
import it.unibo.bombardero.map.api.Pair;
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
