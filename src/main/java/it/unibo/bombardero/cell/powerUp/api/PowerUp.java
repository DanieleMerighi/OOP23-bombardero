package it.unibo.bombardero.cell.PowerUp.api;

import it.unibo.bombardero.character.Character;

public interface PowerUp {

    PowerUpType getType();

    void applyEffect(Character character);
}
