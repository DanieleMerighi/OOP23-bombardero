package it.unibo.bombardero.cell.powerup.impl;

import it.unibo.bombardero.cell.powerup.api.PowerUpEffect;
import it.unibo.bombardero.character.Character;
import java.util.function.Consumer;

/**
 * Implementation of {@link PowerUpEffect} for the Plus One Flame power-up.
 * <p>
 * This effect increases the range of flames produced by the character's bombs.
 * </p>
 */
public final class PlusOneFlameEffect implements PowerUpEffect {

    /**
     * Returns a {@link Consumer} that applies the Plus One Flame effect to the character.
     * <p>
     * When applied, this effect increases the character's flame range.
     * </p>
     * 
     * @return a {@link Consumer} representing the effect of the Plus One Flame power-up on a {@link Character}
     */
    @Override
    public Consumer<Character> getEffect() {
        return character -> character.increaseFlameRange();
    }
}
