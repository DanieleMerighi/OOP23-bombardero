package it.unibo.bombardero.cell.powerup.impl;

import java.util.function.Consumer;

import it.unibo.bombardero.cell.powerup.api.PowerUpEffect;
import it.unibo.bombardero.character.Character;

/**
 * Implementation of {@link PowerUpEffect} for the Minus One Flame power-up.
 * <p>
 * This effect decreases the flame range of the character's bombs by one.
 * </p>
 */
public final class MinusOneFlameEffect implements PowerUpEffect {

    /**
     * Returns a {@link Consumer} that applies the Minus One Flame effect to the character.
     * <p>
     * When applied, this effect reduces the character's flame range.
     * </p>
     * 
     * @return a {@link Consumer} representing the effect of the Minus One Flame power-up on a {@link Character}
     */
    @Override
    public Consumer<Character> getEffect() {
        return character -> character.decreaseFlameRange();
    }
}
