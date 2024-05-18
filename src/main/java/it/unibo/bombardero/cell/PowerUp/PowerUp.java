package it.unibo.bombardero.cell.PowerUp;

import java.util.function.Consumer;

import it.unibo.bombardero.character.Character;
import it.unibo.bombardero.cell.Cell;

public class PowerUp extends Cell {
    private PowerUpType type;
    private Consumer<Character> effect;

    public PowerUp(final PowerUpType type, final Consumer<Character> effect) {
        super(CellType.POWERUP);
        this.type = type;
        this.effect = effect;
    }

    public PowerUpType getType() {
        return type;
    }

    public void applyEffect(Character character) {
        effect.accept(character);
    }

    
}
