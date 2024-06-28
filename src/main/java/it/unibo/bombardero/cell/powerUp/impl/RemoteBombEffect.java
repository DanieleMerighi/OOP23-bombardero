package it.unibo.bombardero.cell.powerup.impl;

import java.util.Optional;
import java.util.function.Consumer;

import it.unibo.bombardero.cell.powerup.api.PowerUpEffect;
import it.unibo.bombardero.cell.powerup.api.PowerUpType;
import it.unibo.bombardero.character.Character;

/**
 * Implementation of {@link PowerUpEffect} for the Remote Bomb power-up.
 * <p>
 * This effect sets the bomb type of the character to {@link PowerUpType#REMOTE_BOMB},
 * allowing the character to place remote-controlled bombs.
 * </p>
 */
public final class RemoteBombEffect implements PowerUpEffect {

    /**
     * Returns a {@link Consumer} that applies the Remote Bomb effect to the character.
     * <p>
     * When applied, this effect sets the bomb type of the character to {@link PowerUpType#REMOTE_BOMB}.
     * </p>
     * @return a {@link Consumer} representing the effect of the Remote Bomb power-up on a {@link Character}
     */
    @Override
    public Consumer<Character> getEffect() {
        return character -> character.setBombType(Optional.of(PowerUpType.REMOTE_BOMB));
    }
}
