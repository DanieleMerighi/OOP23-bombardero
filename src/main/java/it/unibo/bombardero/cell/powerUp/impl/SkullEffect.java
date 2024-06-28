package it.unibo.bombardero.cell.powerup.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import it.unibo.bombardero.cell.powerup.api.PowerUpEffect;
import it.unibo.bombardero.character.Character;

/**
 * Implementation of {@link PowerUpEffect} for the Skull power-up.
 * <p>
 * This effect randomly applies one of several debilitating effects to the character
 * for a fixed duration:
 * </p>
 * <ul>
 *   <li>Decreases the character's speed to a fraction of its starting value.</li>
 *   <li>Increases the character's speed to an exaggerated amount.</li>
 *   <li>Prevents the character from laying down bombs.</li>
 *   <li>Causes the character to constantly lay down bombs in their current cell.</li>
 *   <li>Reduces the character's flame range to its minimum value.</li>
 * </ul>
 * <p>
 * After applying an effect, it saves the character's previous speed and flame range.
 * When the effect duration ends, it restores these values and removes any applied effects.
 * </p>
 */
public final class SkullEffect implements PowerUpEffect {

    private static final int EFFECT_DURATION_IN_SECONDS = 10;
    private static final int SECONDS_TO_MILLISECONDS = 1000;
    private static final float DECREASE_SPEED_FACTOR = 2.5f;
    private static final float INCREASE_SPEED_FACTOR = 2f;

    /**
     * Returns a {@link Consumer} that applies a random effect of the Skull power-up to the character.
     * 
     * @return a {@link Consumer} representing the effect of the Skull power-up on a {@link Character}
     */
    @Override
    public Consumer<Character> getEffect() {
        final List<Consumer<Character>> skull = new ArrayList<>();
        skull.add(character -> character.setSpeed(Character.getStartingSpeed() / DECREASE_SPEED_FACTOR)); // Slownes
        skull.add(character -> character.setSpeed(Character.getMaxSpeed() * INCREASE_SPEED_FACTOR)); // Hyperactivity
        skull.add(character -> character.setConstipation(true)); // Constipation: unables to lay down bomb
        skull.add(character -> character.setButterfingers(true)); // Butterfinger: constantly lays down bombs in his cell
        skull.add(character -> character.setFlameRange(Character.getStartingFlameRange())); // Weakness

        return character -> {
            // Checks if the character already had a skeleton effect applied
            if (character.getResetEffect().isPresent()) { // Ends the previews effect and resets the player stats
                character.updateSkeleton(EFFECT_DURATION_IN_SECONDS * SECONDS_TO_MILLISECONDS);
            }
            // Saves the previous stats
            final float previousSpeed = character.getSpeed();
            final int previousFlameRange = character.getFlameRange();

            // Apply a random effect
            skull.get(new Random().nextInt(skull.size())).accept(character);

             // Set the effect duration and reset logic
             character.setSkeletonEffectDuration(EFFECT_DURATION_IN_SECONDS * SECONDS_TO_MILLISECONDS);
             character.setResetEffect(() -> { // Restores all stats modified by the skull
                 character.setSpeed(previousSpeed);
                 character.setFlameRange(previousFlameRange);
                 character.setConstipation(false);
                 character.setButterfingers(false);
             });
        };
    }
}
