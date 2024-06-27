package it.unibo.bombardero.cell.powerup.api;

import it.unibo.bombardero.cell.Cell;
import it.unibo.bombardero.character.Character;

/**
 * Represents a power-up in the game. A power-up is a special item that can be collected
 * by characters to gain various effects.
 */
public interface PowerUp extends Cell {

    /**
     * Gets the type of the PowerUp.
     * 
     * @return the type of the PowerUp
     */
    PowerUpType getType();

     /**
     * Applies the effect of the PowerUp to the specified character.
     * 
     * @param character the character to apply the effect to
     */
    void applyEffect(Character character);
}
