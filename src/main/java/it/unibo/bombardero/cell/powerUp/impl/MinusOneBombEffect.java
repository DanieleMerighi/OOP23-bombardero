package it.unibo.bombardero.cell.powerup.impl;

import it.unibo.bombardero.cell.powerup.api.PowerUpEffect;
import it.unibo.bombardero.character.Character;
import java.util.function.Consumer;

/**
 * Implementation of {@link PowerUpEffect} for the Minus One Bomb power-up.
 * <p>
 * This effect decreases the number of bombs that the character can place by one.
 * </p>
 */
public final class MinusOneBombEffect implements PowerUpEffect {

    /**
     * Returns a {@link Consumer} that applies the Minus One Bomb effect to the character.
     * <p>
     * When applied, this effect reduces the character's bomb capacity by one, limiting the number of bombs
     * they can place at a time.
     * </p>
     * 
     * @return a {@link Consumer} representing the effect of the Minus One Bomb power-up on a {@link Character}
     */
    @Override
    public Consumer<Character> getEffect() {
        return character -> character.decreaseNumBomb();
    }
}
