package it.unibo.bombardero.cell.powerup.api;

import it.unibo.bombardero.cell.powerup.api.PowerUp.PowerUpType;

/**
 * Factory interface for creating instances of PowerUp.
 * <p>
 * Implementations of this interface are responsible for generating
 * PowerUp objects, each with a specific type and effect.
 * </p>
 */
public interface PowerUpFactory {

    /**
     * Creates a new PowerUp instance with a randomly selected type.
     * 
     * @return a new instance of PowerUp with a specific type and effect
     */
    PowerUp createPowerUp();

    /**
     * Creates a new PowerUp instance with a selected type passed as an argument.
     * 
     * @param powerUpType the type of PowerUp to create
     * 
     * @return a new instance of PowerUp with a specific type and effect
     */
    PowerUp createPowerUp(PowerUpType powerUpType);
}
