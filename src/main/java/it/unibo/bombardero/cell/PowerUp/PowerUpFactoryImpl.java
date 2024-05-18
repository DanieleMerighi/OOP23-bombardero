package it.unibo.bombardero.cell.PowerUp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;

import it.unibo.bombardero.character.Character;

public class PowerUpFactoryImpl implements PowerUpFactory {

    @Override
    public PowerUp createPowerUp() {
        PowerUpType powerUpType = extractPowerUp();
        Consumer<Character> effect = createEffect(powerUpType);
        return new PowerUp(powerUpType, effect);

    }

    // Extract a random weighted PowerUp
    private PowerUpType extractPowerUp() {
        return new EnumeratedDistribution<>(getWeightedPowerUpList()).sample();
    }

    // Generate a list of power-ups with their weights
    private List<Pair<PowerUpType, Double>> getWeightedPowerUpList() {
        return Arrays.stream(PowerUpType.values())
                .map(type -> new Pair<>(type, type.getWeight()))
                .collect(Collectors.toList());
    }

    public Consumer<Character> createEffect(final PowerUpType powerUpType) {
        Consumer<Character> lambda;
        switch (powerUpType) {
            case REMOTE_BOMB ->
                lambda = character -> character.setBombType(Optional.of(PowerUpType.REMOTE_BOMB));
            case PIERCING_BOMB ->
                lambda = character -> character.setBombType(Optional.of(PowerUpType.PIERCING_BOMB));
            case POWER_BOMB ->
                lambda = character -> character.setBombType(Optional.of(PowerUpType.POWER_BOMB));
            case PLUS_ONE_BOMB ->
                lambda = character -> character.increaseNumBomb();
            case MINUS_ONE_BOMB ->
                lambda = character -> character.decreaseNumBomb();
            case PLUS_ONE_FLAME_RANGE ->
                lambda = character -> character.increaseFlameRange();
            case MINUS_ONE_FLAME_RANGE ->
                lambda = character -> character.decreaseFlameRange();
            case MAX_FLAME_RANGE ->
                lambda = character -> character.setFlameRange(Character.MAX_FLAME_RANGE);
            case PLUS_ONE_SKATES ->
                lambda = character -> character.increaseSpeed();
            case MINUS_ONE_SKATES ->
                lambda = character -> character.decreaseSpeed();
            case LINE_BOMB ->
                lambda = character -> character.setLineBomb(true);
            case KICK ->
                lambda = character -> character.setKick(true);
            case SKULL -> {
                List<Consumer<Character>> skull = new ArrayList<>();
                skull.add(character -> {
                    float startingSpeed = character.getSpeed();
                    character.setSpeed(0);
                    // prendi actual time = timer
                    // if actual time - timer >= 10
                    character.setSpeed(startingSpeed);
                });
                skull.add(character -> character.setSpeed(10));
                skull.add(character -> character.setNumBomb(0));
                lambda = skull.get(0);
            }
            default ->
                throw new IllegalArgumentException("Unknown power-up type: " + powerUpType);
        }
        return lambda;

    }
}
