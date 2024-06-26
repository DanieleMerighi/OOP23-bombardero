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
import it.unibo.bombardero.cell.powerup.api.PowerUpEffectStrategy;
import it.unibo.bombardero.cell.powerup.api.PowerUpFactory;
import it.unibo.bombardero.cell.powerup.api.PowerUpType;


public class PowerUpFactoryImpl implements PowerUpFactory {

    /* A map that stores the powerUp implementation as the value */
    private final Map<PowerUpType, Supplier<PowerUpEffectStrategy>> powerUpEffectMap;
    /*
     * Generate a list of power-ups with their weights
     * (the probability mass function enumeration, pmf)
     */
    private static final List<Pair<PowerUpType, Double>> WEIGHTED_POWERUP_LIST = Arrays.stream(PowerUpType.values())
            .map(type -> new Pair<>(type, type.getWeight()))
            .collect(Collectors.toList());
    // Create an enumerated distribution using the given pmf
    private static final EnumeratedDistribution<PowerUpType> WEIGHTED_POWERUP_DISTRIBUTION = new EnumeratedDistribution<>(WEIGHTED_POWERUP_LIST);

    public PowerUpFactoryImpl() {
        this.powerUpEffectMap = new HashMap<>();
        initializePowerUps();
    }

    // Initializes the map 
    private void initializePowerUps() {
        powerUpEffectMap.put(PowerUpType.REMOTE_BOMB, () -> new RemoteBombEffectStrategy());
        powerUpEffectMap.put(PowerUpType.PIERCING_BOMB, () -> new PiercingBombEffectStrategy());
        powerUpEffectMap.put(PowerUpType.POWER_BOMB, () -> new PowerBombEffectStrategy());
        powerUpEffectMap.put(PowerUpType.PLUS_ONE_BOMB, () -> new PlusOneBombEffectStrategy());
        powerUpEffectMap.put(PowerUpType.MINUS_ONE_BOMB, () -> new MinusOneBombEffectStrategy());
        powerUpEffectMap.put(PowerUpType.PLUS_ONE_FLAME_RANGE, () -> new PlusOneFlameEffectStrategy());
        powerUpEffectMap.put(PowerUpType.MINUS_ONE_FLAME_RANGE, () -> new MinusOneFlameEffectStrategy());
        powerUpEffectMap.put(PowerUpType.MAX_FLAME_RANGE, () -> new MaxFlameRangeEffectStrategy());
        powerUpEffectMap.put(PowerUpType.PLUS_ONE_SKATES, () -> new PlusOneSkateEffectStrategy());
        powerUpEffectMap.put(PowerUpType.MINUS_ONE_SKATES, () -> new MinusOneSkateEffectStrategy());
        powerUpEffectMap.put(PowerUpType.LINE_BOMB, () -> new LineBombEffectStrategy());
        powerUpEffectMap.put(PowerUpType.SKULL, () -> new SkullEffectStrategy());
    }

    @Override
    public PowerUp createPowerUp(it.unibo.bombardero.map.api.Pair pos) {
        final PowerUpType powerUpType = WEIGHTED_POWERUP_DISTRIBUTION.sample(); // Extract a random weighted PowerUp
        final Supplier<PowerUpEffectStrategy> supplier = powerUpEffectMap.get(powerUpType); // Get the power-Up effect
        if (supplier == null) { // Checks if the effect is null
            throw new IllegalArgumentException("Unknown power-up effect for: " + powerUpType);
        }
        return new PowerUpImpl(powerUpType, pos, supplier.get());
    }
}
