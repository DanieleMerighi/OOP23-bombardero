package it.unibo.bombardero.cell.powerUp;

import java.util.function.Consumer;

import it.unibo.bombardero.character.Character;
import it.unibo.bombardero.cell.Cell;

public class PowerUpImpl extends Cell implements PowerUp{
    private PowerUpType type;
    private Consumer<Character> effect;

    public PowerUpImpl(final PowerUpType type, final Consumer<Character> effect) {
        super(CellType.POWERUP);
        this.type = type;
        this.effect = effect;
    }

    @Override
    public PowerUpType getType() {
        return type;
    }

    @Override
    public void applyEffect(Character character) {
        effect.accept(character);
    }
    
}
