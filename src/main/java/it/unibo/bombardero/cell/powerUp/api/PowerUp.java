package it.unibo.bombardero.cell.powerup.api;

import it.unibo.bombardero.cell.Cell;
import it.unibo.bombardero.character.Character;

public interface PowerUp extends Cell {

    PowerUpType getType();

    void applyEffect(Character character);
}
