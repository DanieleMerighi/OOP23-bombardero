package it.unibo.bombardero.cell.powerup.api;

import java.util.function.Consumer;

import it.unibo.bombardero.character.Character;

/**
 * Interface representing the effect of a PowerUp in the game.
 * <p>
 * This interface defines a method to retrieve the effect of a PowerUp, 
 * which is represented as a {@link Consumer} that performs an action on a {@link Character}.
 * </p>
 */
public interface PowerUpEffect {

    /**
     * Retrieves the effect of the PowerUp.
     * <p>
     * The effect is defined as a {@link Consumer} that takes a {@link Character} as an argument 
     * and performs some modifications to the character.
     * </p>
     * 
     * @return a {@link Consumer} representing the effect of the PowerUp on a {@link Character}
     */
    Consumer<Character> getEffect();
}
