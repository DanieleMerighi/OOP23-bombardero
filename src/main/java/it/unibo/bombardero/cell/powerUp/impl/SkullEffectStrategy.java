package it.unibo.bombardero.cell.powerup.impl;

import it.unibo.bombardero.cell.powerup.api.PowerUpEffectStrategy;
import it.unibo.bombardero.character.Character;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


public class SkullEffectStrategy implements PowerUpEffectStrategy {

    @Override
    public Consumer<Character> getEffect() {
        List<Consumer<Character>> skull = new ArrayList<>();
                skull.add(character -> {
                    float startingSpeed = character.getSpeed();
                    character.setSpeed(0);
                    // prendi actual time = timer
                    // if actual time - timer >= 10
                    character.setSpeed(startingSpeed);
                });
                skull.add(character -> character.setSpeed(10));
                skull.add(character -> character.setNumBomb(0));
                return skull.get(0);
    }
}
