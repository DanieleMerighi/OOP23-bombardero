package it.unibo.bombardero.bomb.api;

import it.unibo.bombardero.map.api.GenPair;

/**
 * Rapresent a Factory that creates different types of Bomb(basic, power, remote, pircing).
 */
public interface BombFactory {

    /**
     * @param range
     * @param pos
     * @return a normal Bomb
     */
    Bomb createBasicBomb(int range, GenPair<Integer, Integer> pos);

    /**
     * 
     * @param range
     * @param pos
     * @return a PircingBomb that destroy every breackableWall in his range
     */
    Bomb createPiercingBomb(int range, GenPair<Integer, Integer> pos);

    /**
     * 
     * @param pos
     * @return a PowerBomb a bomb with max range
     */
    Bomb createPowerBomb(GenPair<Integer, Integer> pos);

    /**
     * 
     * @param range
     * @param pos
     * @return a RemoteBomb that explode on comand
     */
    Bomb createRemoteBomb(int range, GenPair<Integer, Integer> pos);
}
