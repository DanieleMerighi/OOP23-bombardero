package it.unibo.bombardero.cell;

import it.unibo.bombardero.character.Character;
import it.unibo.bombardero.map.api.Pair;

public interface BombFactory {

    /**
     * @param character
     * @param pos
     * @return a Bomb in the given position with character's caratteristics 
     */
    Bomb createBomb(Character character, Pair pos);

    /**
     * @param character
     * @param pos
     * @return a Bomb in the character's position with character's caratteristics 
     */
    Bomb createBomb(Character character);
}
