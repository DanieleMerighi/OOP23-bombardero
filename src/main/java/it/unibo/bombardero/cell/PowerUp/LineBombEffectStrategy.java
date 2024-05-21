package it.unibo.bombardero.cell.powerUp;

import it.unibo.bombardero.character.Character;
import java.util.function.Consumer;

public class LineBombEffectStrategy implements PowerUpEffectStrategy{

    @Override
    public Consumer<Character> getEffect() {
        return character -> character.setLineBomb(true);
    }
    
}
