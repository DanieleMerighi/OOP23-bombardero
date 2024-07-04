package it.unibo.bombardero.cell.powerup.api;

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
     * @return a new instance of PowerUp with a specific type and effect
     */
    PowerUp createPowerUp();
}
