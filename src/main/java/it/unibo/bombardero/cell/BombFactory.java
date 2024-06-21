package it.unibo.bombardero.cell;

import java.util.Optional;

import it.unibo.bombardero.character.Character;
import it.unibo.bombardero.cell.Cell.CellType;
import it.unibo.bombardero.cell.powerup.api.PowerUpType;
import it.unibo.bombardero.map.api.Pair;

public interface BombFactory {

    BasicBomb CreateBomb(Character character, Pair pos);

    BasicBomb CreateBomb(Character character);
}
