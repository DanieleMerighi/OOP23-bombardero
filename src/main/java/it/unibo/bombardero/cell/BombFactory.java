package it.unibo.bombardero.cell;

import it.unibo.bombardero.map.api.GenPair;

public interface BombFactory {

    /**
     * @param character
     * @param pos
     * @return a normal Bomb
     */
    Bomb createBasicBomb(final int range, final GenPair<Integer, Integer> pos);

    /**
     * 
     * @param character
     * @param pos
     * @return a PircingBomb that destroy every breackableWall in his range
     */
    Bomb createPiercingBomb(final int range, final GenPair<Integer, Integer> pos);

    /**
     * 
     * @param character
     * @param pos
     * @return a PowerBomb a bomb with max range
     */
    Bomb createPowerBomb(final GenPair<Integer, Integer> pos);

    /**
     * 
     * @param character
     * @param pos
     * @return a RemoteBomb that explode on comand
     */
    Bomb createRemoteBomb(final int range, final GenPair<Integer, Integer> pos);
}
