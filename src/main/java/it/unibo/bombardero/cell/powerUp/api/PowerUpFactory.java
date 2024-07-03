package it.unibo.bombardero.cell.powerup.api;

import it.unibo.bombardero.map.api.GenPair;

/**
 * Factory interface for creating instances of PowerUp.
 * <p>
 * Implementations of this interface are responsible for generating
 * PowerUp objects, each with a specific type and position on the game map.
 * </p>
 */
public interface PowerUpFactory {

    /**
     * Creates a new PowerUp instance at the specified position with a randomly selected type.
     * 
     * @param position the coordinates where the PowerUp will be placed on the map
     * @return a new instance of PowerUp with a specific type and effect
     */
    PowerUp createPowerUp(GenPair<Integer, Integer> position);
}
