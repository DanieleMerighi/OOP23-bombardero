package it.unibo.bombardero.cell.powerup.impl;

import it.unibo.bombardero.cell.powerup.api.PowerUpEffect;
import it.unibo.bombardero.character.Character;
import java.util.function.Consumer;

/**
 * Implementation of {@link PowerUpEffect} for the Plus One Skate power-up.
 * <p>
 * This effect increases the speed of the character.
 * </p>
 */
public final class PlusOneSkateEffect implements PowerUpEffect {

    /**
     * Returns a {@link Consumer} that applies the Plus One Skate effect to the character.
     * <p>
     * When applied, this effect increases the character's speed.
     * </p>
     * 
     * @return a {@link Consumer} representing the effect of the Plus One Skate power-up on a {@link Character}
     */
    @Override
    public Consumer<Character> getEffect() {
        return character -> character.increaseSpeed();
    }
}
