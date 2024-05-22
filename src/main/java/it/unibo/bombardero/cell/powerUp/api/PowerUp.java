package it.unibo.bombardero.cell.powerUp.api;

import it.unibo.bombardero.character.Character;

public interface PowerUp {

    public PowerUpType getType();

    public void applyEffect(Character character);
}
