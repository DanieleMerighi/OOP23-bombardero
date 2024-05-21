package it.unibo.bombardero.cell.powerUp;

import it.unibo.bombardero.character.Character;
import java.util.Optional;
import java.util.function.Consumer;

public class PiercingBombEffectStrategy implements PowerUpEffectStrategy {

    @Override
    public Consumer<Character> getEffect() {
        return character -> character.setBombType(Optional.of(PowerUpType.PIERCING_BOMB));
    }

}
