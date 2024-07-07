package it.unibo.bombardero.cell.powerup.impl;

import java.util.Optional;
import java.util.function.Consumer;

import it.unibo.bombardero.cell.powerup.api.PowerUp.PowerUpType;
import it.unibo.bombardero.cell.powerup.api.PowerUpEffect;
import it.unibo.bombardero.character.Character;

/**
 * Implementation of {@link PowerUpEffect} for the Power Bomb power-up.
 * <p>
 * This effect sets the character's bomb type to {@link PowerUpType#POWER_BOMB},
 * allowing the character to place POWER bombs.
 * </p>
 */
public final class PowerBombEffect implements PowerUpEffect {

    /**
     * Returns a {@link Consumer} that applies the Power Bomb effect to the character.
     * <p>
     * When applied, this effect sets the bomb type of the character to {@link PowerUpType#POWER_BOMB}.
     * </p>
     * @return a {@link Consumer} representing the effect of the Power Bomb power-up on a {@link Character}
     */
    @Override
    public Consumer<Character> getEffect() {
        return character -> character.setBombType(Optional.of(PowerUpType.POWER_BOMB));
    }
}
