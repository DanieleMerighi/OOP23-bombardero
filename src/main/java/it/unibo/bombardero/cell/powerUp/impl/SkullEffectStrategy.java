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

    @Override
    public Consumer<Character> getEffect() {
        List<Consumer<Character>> skull = new ArrayList<>();
        skull.add(character -> character.setSpeed(Character.STARTING_SPEED / 2)); // Slownes
        skull.add(character -> character.setSpeed(Character.MAX_SPEED * 2)); // Hyperactivity
        skull.add(character -> character.setConstipation(true)); // Constipation: unables to lay down bomb
        skull.add(character -> character.setButterfingers(true)); // Butterfinger: constantly lays down bomb in his cell
        skull.add(character -> character.setFlameRange(Character.STARTING_FLAME_RANGE)); // Weakness

        return character -> {
            // Save the previous stats
            float previousSpeed = character.getSpeed();
            int previousNumBombs = character.getNumBomb();
            int previousFlameRange = character.getFlameRange();

            // Apply a random effect
            skull.get(new Random().nextInt(5)).accept(character);

             // Set the effect duration and reset logic
             character.setEffectDuration(EFFECT_DURATION_IN_SECONDS * SECONDS_TO_MILLISECONDS); // 10 seconds in milliseconds
             character.setResetEffect(() -> { // Restores all stats modified by the skull
                 character.setSpeed(previousSpeed);
                 character.setNumBomb(previousNumBombs);
                 character.setFlameRange(previousFlameRange);
                 character.setConstipation(false);
                 character.setButterfingers(false);
             });
        };
    }
}
