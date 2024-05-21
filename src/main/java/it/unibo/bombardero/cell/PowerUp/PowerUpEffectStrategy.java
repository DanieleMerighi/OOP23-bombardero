package it.unibo.bombardero.cell.powerUp;

import it.unibo.bombardero.character.Character;
import java.util.function.Consumer;

public interface PowerUpEffectStrategy {

    public Consumer<Character> getEffect();
}
