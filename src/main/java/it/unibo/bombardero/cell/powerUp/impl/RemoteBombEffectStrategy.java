package it.unibo.bombardero.cell.powerUp.impl;

import it.unibo.bombardero.cell.powerUp.api.PowerUpEffectStrategy;
import it.unibo.bombardero.cell.powerUp.api.PowerUpType;
import it.unibo.bombardero.character.Character;
import java.util.Optional;
import java.util.function.Consumer;

public class RemoteBombEffectStrategy implements PowerUpEffectStrategy {

    @Override
    public Consumer<Character> getEffect() {
        return character -> character.setBombType(Optional.of(PowerUpType.REMOTE_BOMB));
    }
}
