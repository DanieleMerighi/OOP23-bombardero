package it.unibo.bombardero.cell.powerup.impl;

import java.util.function.Consumer;

import it.unibo.bombardero.cell.powerup.api.PowerUpEffect;
import it.unibo.bombardero.character.Character;

/**
 * Implementation of {@link PowerUpEffect} for the Plus One Bomb power-up.
 * <p>
 * This effect increases the number of bombs a character can place.
 * </p>
 */
public final class PlusOneBombEffect implements PowerUpEffect {

    /**
     * Returns a {@link Consumer} that applies the Plus One Bomb effect to the character.
     * <p>
     * When applied, this effect increases the number of bombs the character can place at once.
     * </p>
     * 
     * @return a {@link Consumer} representing the effect of the Plus One Bomb power-up on a {@link Character}
     */
    @Override
    public Consumer<Character> getEffect() {
        return character -> character.increaseNumBomb();
    }
}
