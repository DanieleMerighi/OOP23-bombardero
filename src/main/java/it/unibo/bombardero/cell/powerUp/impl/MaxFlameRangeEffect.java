package it.unibo.bombardero.cell.powerup.impl;

import java.util.function.Consumer;

import it.unibo.bombardero.cell.powerup.api.PowerUpEffect;
import it.unibo.bombardero.character.Character;

/**
 * Implementation of {@link PowerUpEffect} for the Max Flame Range power-up.
 * <p>
 * This effect sets the character's flame range to the maximum possible value.
 * </p>
 */
public final class MaxFlameRangeEffect implements PowerUpEffect {

    /**
     * Returns a {@link Consumer} that applies the Max Flame Range effect to the character.
     * <p>
     * When applied, this effect increases the character's flame range to its maximum value.
     * </p>
     * 
     * @return a {@link Consumer} representing the effect of the Max Flame Range power-up on a {@link Character}
     */
    @Override
    public Consumer<Character> getEffect() {
        return character -> character.setFlameRange(Character.MAX_FLAME_RANGE);
    }
}
