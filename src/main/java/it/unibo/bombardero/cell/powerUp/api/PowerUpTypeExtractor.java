package it.unibo.bombardero.cell.powerup.api;

import it.unibo.bombardero.cell.powerup.api.PowerUp.PowerUpType;

/**
 * PowerUp type extractor interface.
 * <p>
 * Implementations of this interface are responsible for generating
 * PowerUpType objects.
 * </p>
 */
public interface PowerUpTypeExtractor {

    /**
     * Creates a new PowerUp type and returns it.
     * 
     * @return the PowerUp type
     */
    PowerUpType extractPowerUpType();
}
