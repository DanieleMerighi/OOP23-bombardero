package it.unibo.bombardero.cell.powerup.impl;

import it.unibo.bombardero.cell.powerup.api.PowerUpEffect;
import it.unibo.bombardero.cell.powerup.api.PowerUpType;
import it.unibo.bombardero.character.Character;
import java.util.Optional;
import java.util.function.Consumer;

public final class PiercingBombEffect implements PowerUpEffect {

    @Override
    public Consumer<Character> getEffect() {
        return character -> character.setBombType(Optional.of(PowerUpType.PIERCING_BOMB));
    }
}
