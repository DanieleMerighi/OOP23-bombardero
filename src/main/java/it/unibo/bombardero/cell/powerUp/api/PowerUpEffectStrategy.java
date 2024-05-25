package it.unibo.bombardero.cell.powerup.api;

import it.unibo.bombardero.character.Character;
import java.util.function.Consumer;

public interface PowerUpEffectStrategy {

    Consumer<Character> getEffect();
}
