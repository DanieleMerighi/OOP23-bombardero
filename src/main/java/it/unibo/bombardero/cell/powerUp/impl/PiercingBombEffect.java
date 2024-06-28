package it.unibo.bombardero.cell.powerup.impl;

import it.unibo.bombardero.cell.powerup.api.PowerUpEffect;
import it.unibo.bombardero.cell.powerup.api.PowerUpType;
import it.unibo.bombardero.character.Character;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Implementation of {@link PowerUpEffect} for the Piercing Bomb power-up.
 * <p>
 * This effect sets the bomb type of the character to {@link PowerUpType#PIERCING_BOMB},
 * allowing the character to place Piercing Bombs.
 * </p>
 */
public final class PiercingBombEffect implements PowerUpEffect {

    /**
     * Returns a {@link Consumer} that applies the Piercing Bomb effect to the character.
     * <p>
     * When applied, this effect sets the bomb type of the character to {@link PowerUpType#PIERCING_BOMB}.
     * </p>
     * @return a {@link Consumer} representing the effect of the Piercing Bomb power-up on a {@link Character}
     */
    @Override
    public Consumer<Character> getEffect() {
        return character -> character.setBombType(Optional.of(PowerUpType.PIERCING_BOMB));
    }
}
