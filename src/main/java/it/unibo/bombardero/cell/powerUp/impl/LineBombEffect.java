package it.unibo.bombardero.cell.powerup.impl;

import it.unibo.bombardero.cell.powerup.api.PowerUpEffect;
import it.unibo.bombardero.character.Character;
import java.util.function.Consumer;

/**
 * Implementation of {@link PowerUpEffect} for the Line Bomb power-up.
 * <p>
 * This effect enables the character to place a line of bombs.
 * </p>
 */
public final class LineBombEffect implements PowerUpEffect {

    /**
     * Returns a {@link Consumer} that applies the Line Bomb effect to the character.
     * <p>
     * When applied, this effect enables the character to place a line of bombs.
     * </p>
     * 
     * @return a {@link Consumer} representing the effect of the Line Bomb power-up on a {@link Character}
     */
    @Override
    public Consumer<Character> getEffect() {
        return character -> character.setLineBomb(true);
    }
}
