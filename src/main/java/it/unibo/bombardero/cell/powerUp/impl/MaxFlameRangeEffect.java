package it.unibo.bombardero.cell.powerup.impl;

import it.unibo.bombardero.cell.powerup.api.PowerUpEffect;
import it.unibo.bombardero.character.Character;
import java.util.function.Consumer;

public final class MaxFlameRangeEffect implements PowerUpEffect {

    @Override
    public Consumer<Character> getEffect() {
        return character -> character.setFlameRange(Character.getMaxFlameRange());
    }
}
