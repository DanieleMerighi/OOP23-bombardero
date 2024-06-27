package it.unibo.bombardero.cell;

import it.unibo.bombardero.character.Character;
import it.unibo.bombardero.map.api.Pair;

public interface BombFactory {

    Bomb CreateBomb(Character character, Pair pos);

    Bomb CreateBomb(Character character);
}
