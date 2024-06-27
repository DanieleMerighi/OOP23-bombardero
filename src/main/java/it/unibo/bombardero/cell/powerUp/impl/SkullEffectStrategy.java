package it.unibo.bombardero.cell.powerup.impl;

import it.unibo.bombardero.cell.powerup.api.PowerUpEffectStrategy;
import it.unibo.bombardero.character.Character;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class SkullEffectStrategy implements PowerUpEffectStrategy {

    private static final int EFFECT_DURATION_IN_SECONDS = 10;
    private static final int SECONDS_TO_MILLISECONDS = 1000;
    private static final float DECREASE_SPEED_FACTOR = 2.5f;
    private static final float INCREASE_SPEED_FACTOR = 2f;

    @Override
    public Consumer<Character> getEffect() {
        List<Consumer<Character>> skull = new ArrayList<>();
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
            // Save the previous stats
            float previousSpeed = character.getSpeed();
            int previousFlameRange = character.getFlameRange();

            // Apply a random effect
            skull.get(new Random().nextInt(5)).accept(character);

             // Set the effect duration and reset logic
             character.setSkeletonEffectDuration(EFFECT_DURATION_IN_SECONDS * SECONDS_TO_MILLISECONDS); // 10 seconds in milliseconds
             character.setResetEffect(() -> { // Restores all stats modified by the skull
                 character.setSpeed(previousSpeed);
                 character.setFlameRange(previousFlameRange);
                 character.setConstipation(false);
                 character.setButterfingers(false);
             });
        };
    }
}
