package it.unibo.bombardero.cell.powerup.impl;

import java.util.function.Consumer;

import it.unibo.bombardero.cell.powerup.api.PowerUpEffect;
import it.unibo.bombardero.character.Character;

/**
 * Implementation of {@link PowerUpEffect} for the Minus One Skate power-up.
 * <p>
 * This effect decreases the speed of the character.
 * </p>
 */
public final class MinusOneSkateEffect implements PowerUpEffect {

    /**
     * Returns a {@link Consumer} that applies the Minus One Skate effect to the character.
     * <p>
     * When applied, this effect reduces the character's speed.
     * </p>
     * 
     * @return a {@link Consumer} representing the effect of the Minus One Skate power-up on a {@link Character}
     */
    @Override
    public Consumer<Character> getEffect() {
        return character -> character.decreaseSpeed();
    }
}
