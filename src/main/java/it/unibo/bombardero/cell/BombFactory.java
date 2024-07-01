package it.unibo.bombardero.cell;

import java.util.Optional;

import it.unibo.bombardero.cell.powerup.api.PowerUpType;
import it.unibo.bombardero.map.api.Pair;

public interface BombFactory {

    /**
     * @param character
     * @param pos
     * @return a normal Bomb
     */
    Bomb createBasicBomb(final Optional<PowerUpType> BombType,final int range, final Pair pos);

    /**
     * 
     * @param character
     * @param pos
     * @return a PircingBomb that destroy every breackableWall in his range
     */
    Bomb createPiercingBomb(final Optional<PowerUpType> BombType,final int range, final Pair pos);

    /**
     * 
     * @param character
     * @param pos
     * @return a PowerBomb a bomb with max range
     */
    Bomb createPowerBomb(final Optional<PowerUpType> BombType, final Pair pos);

    /**
     * 
     * @param character
     * @param pos
     * @return a RemoteBomb that explode on comand
     */
    Bomb createRemoteBomb(final Optional<PowerUpType> BombType,final int range, final Pair pos);
}
