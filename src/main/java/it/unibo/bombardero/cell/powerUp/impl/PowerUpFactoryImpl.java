package it.unibo.bombardero.cell.powerup.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import it.unibo.bombardero.cell.powerup.api.PowerUp;
import it.unibo.bombardero.cell.powerup.api.PowerUp.PowerUpType;
import it.unibo.bombardero.cell.powerup.api.PowerUpEffect;
import it.unibo.bombardero.cell.powerup.api.PowerUpFactory;
import it.unibo.bombardero.cell.powerup.api.PowerUpTypeExtractor;

/**
 * Implementation of the PowerUpFactory interface.
 * <p>
 * This class is responsible for creating PowerUp instances with random types based on their weights.
 * It uses a weighted random selection process to determine the type of PowerUp to create.
 * </p>
 */
public final class PowerUpFactoryImpl implements PowerUpFactory {

    /** A map that stores the power-up types and their corresponding effect.*/
    private final Map<PowerUpType, Supplier<PowerUpEffect>> powerUpEffectMap;

    private final PowerUpTypeExtractor extractor;

    /**
     * Constructs a new PowerUpFactoryImpl and initializes the power-up effect map.
     */
    public PowerUpFactoryImpl() {
        this.powerUpEffectMap = new HashMap<>();
        this.extractor = new PowerUpEnumeratedDistribution();
        initializePowerUpEffectMap();
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
        final PowerUpType powerUpType = extractor.extractPowerUpType(); // Extract a random weighted PowerUp
        return createPowerUpImpl(powerUpType);
    }

    /**
     * Creates a new PowerUp instance with a selected type passed as an argument.
     * 
     * @return a new instance of PowerUp with a specific type and effect
     * @throws IllegalArgumentException if there is no effect for the selected PowerUp type
     */
    @Override
    public PowerUp createPowerUp(final PowerUpType powerUpType) {
        return createPowerUpImpl(powerUpType);
    }

    private PowerUp createPowerUpImpl(final PowerUpType powerUpType) {
        final Supplier<PowerUpEffect> effectSupplier = powerUpEffectMap.get(powerUpType); // Get the power-Up effect
        if (effectSupplier == null) { // Checks if the effect is null
            throw new IllegalArgumentException("Unknown power-up effect for: " + powerUpType);
        }
        return new PowerUpImpl(powerUpType, effectSupplier.get());
    }

    /**
     * Initializes the map with power-up types and their corresponding effect.
     * This method is called in the constructor to populate the powerUpEffectMap.
     */
    private void initializePowerUpEffectMap() {
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
