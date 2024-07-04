package it.unibo.bombardero.cell.powerup.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;

import it.unibo.bombardero.cell.powerup.api.PowerUp;
import it.unibo.bombardero.cell.powerup.api.PowerUpEffect;
import it.unibo.bombardero.cell.powerup.api.PowerUpFactory;
import it.unibo.bombardero.cell.powerup.api.PowerUpType;

/**
 * Implementation of the PowerUpFactory interface.
 * <p>
 * This class is responsible for creating PowerUp instances with random types based on their weights.
 * It uses a weighted random selection process to determine the type of PowerUp to create.
 * </p>
 */
public final class PowerUpFactoryImpl implements PowerUpFactory {

    /**
     * Generate a list of power-ups with their weights (the probability mass function enumeration, pmf).
     */
    private static final List<Pair<PowerUpType, Double>> WEIGHTED_POWERUP_LIST = Arrays.stream(PowerUpType.values())
            .map(type -> new Pair<>(type, type.getWeight()))
            .collect(Collectors.toList());

    /** Create an enumerated distribution using the given pmf. */
    private static final EnumeratedDistribution<PowerUpType> WEIGHTED_POWERUP_DISTRIBUTION = new EnumeratedDistribution<>(
            WEIGHTED_POWERUP_LIST);

    /** A map that stores the power-up types and their corresponding effect.*/
    private final Map<PowerUpType, Supplier<PowerUpEffect>> powerUpEffectMap;

    /**
     * Constructs a new PowerUpFactoryImpl and initializes the power-up effect map.
     */
    public PowerUpFactoryImpl() {
        this.powerUpEffectMap = new HashMap<>();
        initializePowerUps();
    }

    /**
     * Creates a new PowerUp instance with a randomly selected type.
     * <p>
     * This method uses a weighted random selection process to determine the type of the PowerUp
     * before instantiating it.
     * </p>
     * 
     * @return a new instance of PowerUp with a specific type and effect
     * @throws IllegalArgumentException if there is no effect for the selected PowerUp type
     */
    @Override
    public PowerUp createPowerUp() {
        final PowerUpType powerUpType = WEIGHTED_POWERUP_DISTRIBUTION.sample(); // Extract a random weighted PowerUp
        final Supplier<PowerUpEffect> supplier = powerUpEffectMap.get(powerUpType); // Get the power-Up effect
        if (supplier == null) { // Checks if the effect is null
            throw new IllegalArgumentException("Unknown power-up effect for: " + powerUpType);
        }
        return new PowerUpImpl(powerUpType, supplier.get());
    }

    /**
     * Initializes the map with power-up types and their corresponding effect.
     * This method is called in the constructor to populate the powerUpEffectMap.
     */
    private void initializePowerUps() {
        powerUpEffectMap.put(PowerUpType.REMOTE_BOMB, () -> new RemoteBombEffect());
        powerUpEffectMap.put(PowerUpType.PIERCING_BOMB, () -> new PiercingBombEffect());
        powerUpEffectMap.put(PowerUpType.POWER_BOMB, () -> new PowerBombEffect());
        powerUpEffectMap.put(PowerUpType.PLUS_ONE_BOMB, () -> new PlusOneBombEffect());
        powerUpEffectMap.put(PowerUpType.MINUS_ONE_BOMB, () -> new MinusOneBombEffect());
        powerUpEffectMap.put(PowerUpType.PLUS_ONE_FLAME_RANGE, () -> new PlusOneFlameEffect());
        powerUpEffectMap.put(PowerUpType.MINUS_ONE_FLAME_RANGE, () -> new MinusOneFlameEffect());
        powerUpEffectMap.put(PowerUpType.MAX_FLAME_RANGE, () -> new MaxFlameRangeEffect());
        powerUpEffectMap.put(PowerUpType.PLUS_ONE_SKATES, () -> new PlusOneSkateEffect());
        powerUpEffectMap.put(PowerUpType.MINUS_ONE_SKATES, () -> new MinusOneSkateEffect());
        powerUpEffectMap.put(PowerUpType.LINE_BOMB, () -> new LineBombEffect());
        powerUpEffectMap.put(PowerUpType.SKULL, () -> new SkullEffect());
    }
}
