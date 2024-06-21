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
    private final Map<PowerUpType, Supplier<PowerUpEffectStrategy>> powerUpMap;
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
        this.powerUpMap = new HashMap<>();
        initializePowerUps();
    }

    // Initializes the map 
    private void initializePowerUps() {
        powerUpMap.put(PowerUpType.REMOTE_BOMB, () -> new RemoteBombEffectStrategy());
        powerUpMap.put(PowerUpType.PIERCING_BOMB, () -> new PiercingBombEffectStrategy());
        powerUpMap.put(PowerUpType.POWER_BOMB, () -> new PowerBombEffectStrategy());
        powerUpMap.put(PowerUpType.PLUS_ONE_BOMB, () -> new PlusOneBombEffectStrategy());
        powerUpMap.put(PowerUpType.MINUS_ONE_BOMB, () -> new MinusOneBombEffectStrategy());
        powerUpMap.put(PowerUpType.PLUS_ONE_FLAME_RANGE, () -> new PlusOneFlameEffectStrategy());
        powerUpMap.put(PowerUpType.MINUS_ONE_FLAME_RANGE, () -> new MinusOneFlameEffectStrategy());
        powerUpMap.put(PowerUpType.MAX_FLAME_RANGE, () -> new MaxFlameRangeEffectStrategy());
        powerUpMap.put(PowerUpType.PLUS_ONE_SKATES, () -> new PlusOneSkateEffectStrategy());
        powerUpMap.put(PowerUpType.MINUS_ONE_SKATES, () -> new MinusOneSkateEffectStrategy());
        powerUpMap.put(PowerUpType.LINE_BOMB, () -> new LineBombEffectStrategy());
        powerUpMap.put(PowerUpType.KICK, () -> new KickEffectStrategy());
        powerUpMap.put(PowerUpType.SKULL, () -> new SkullEffectStrategy());
    }

    @Override
    public PowerUp createPowerUp(it.unibo.bombardero.map.api.Pair pos) {
        final PowerUpType powerUpType = WEIGHTED_POWERUP_DISTRIBUTION.sample(); // Extract a random weighted PowerUp
        final Supplier<PowerUpEffectStrategy> supplier = powerUpMap.get(powerUpType); // Get the power-Up effect
        if (supplier == null) { // Checks if the effect is null
            throw new IllegalArgumentException("Unknown power-up effect for: " + powerUpType);
        }
        return new PowerUpImpl(powerUpType, pos, supplier.get());
    }
}
