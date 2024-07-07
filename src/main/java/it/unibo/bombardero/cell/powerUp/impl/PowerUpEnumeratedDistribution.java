package it.unibo.bombardero.cell.powerup.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;

import it.unibo.bombardero.cell.powerup.api.PowerUpTypeExtractor;
import it.unibo.bombardero.cell.powerup.api.PowerUp.PowerUpType;

/**
 * Implementation of the PowerUpTypeExtractor interface.
 * <p>
 * This class is responsible for creating random PowerUp type instances based on their weights.
 * It uses a weighted random selection process to determine the type of PowerUp to extract.
 * </p>
 */
public class PowerUpEnumeratedDistribution implements PowerUpTypeExtractor {

    /**
     * Generate a list of power-ups with their weights (the probability mass function enumeration, pmf).
     */
    private static final List<Pair<PowerUpType, Double>> WEIGHTED_POWERUP_LIST = Arrays.stream(PowerUpType.values())
            .map(type -> new Pair<>(type, type.getWeight()))
            .collect(Collectors.toList());

    /** Create an enumerated distribution using the given pmf. */
    private static final EnumeratedDistribution<PowerUpType> WEIGHTED_POWERUP_DISTRIBUTION = new EnumeratedDistribution<>(
            WEIGHTED_POWERUP_LIST);

    /**
     * Creates a new PowerUp type using an enumerated distribution and returns it.
     * 
     * @return the PowerUp type
     */
    @Override
    public PowerUpType extractPowerUpType() {
        return WEIGHTED_POWERUP_DISTRIBUTION.sample();
    }
}
