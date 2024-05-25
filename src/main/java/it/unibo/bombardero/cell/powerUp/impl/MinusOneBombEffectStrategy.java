package it.unibo.bombardero.cell.powerup.impl;

import it.unibo.bombardero.cell.powerup.api.PowerUpEffectStrategy;
import it.unibo.bombardero.character.Character;
import java.util.function.Consumer;

public class MinusOneBombEffectStrategy implements PowerUpEffectStrategy {

    @Override
    public Consumer<Character> getEffect() {
        return character -> character.decreaseNumBomb();
    }
}
