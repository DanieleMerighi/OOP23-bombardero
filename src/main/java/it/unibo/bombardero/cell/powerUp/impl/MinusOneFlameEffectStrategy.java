package it.unibo.bombardero.cell.powerUp.impl;

import it.unibo.bombardero.cell.powerUp.api.PowerUpEffectStrategy;
import it.unibo.bombardero.character.Character;
import java.util.function.Consumer;

public class MinusOneFlameEffectStrategy implements PowerUpEffectStrategy{

    @Override
    public Consumer<Character> getEffect() {
        return character -> character.decreaseFlameRange();
    }
    
}
