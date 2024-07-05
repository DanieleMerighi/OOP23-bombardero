package it.unibo.bombardero.bomb.api;

import java.util.Set;
import java.util.Map.Entry;

import it.unibo.bombardero.cell.Cell;
import it.unibo.bombardero.cell.Flame.FlameType;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.GenPair;

/**
 * This interface describe a bomb an object that explodes after a certain time or under determinated condition.
 */
public interface Bomb extends Cell {

    /**
     * This enumeration rapresent different tipe of bomb.
     */
    enum BombType {

        /**
         * normal bomb.
         */
        BOMB_BASIC("bomb/basic"),

        /**
         * bomb whith max range.
         */
        BOMB_POWER("bomb/power"),

        /**
         * bomb that pirces breckableWall.
         */
        BOMB_PIERCING("bomb/piercing"),

        /**
         * bomb that explodes when a character tells it.
         */
        BOMB_REMOTE("bomb/remote");

        private String typeString;

        BombType(final String typeString) {
            this.typeString = typeString;
        }

        /**
         * 
         * @return the equivalent String
         */
        public String getTypeString() {
            return this.typeString;
        }

    }

    /**
     * 
     * @return true if th ebomb is exploded
     */
    boolean isExploded();

    /**
     * the bomb explode if the condition is true.
     * @param condition
     */
    void update(boolean condition);

    /**
     * update the bomb.
     * @param timeElapsed time passed after last update
     */
    void update(long timeElapsed);

    /**
     * 
     * @param map
     * @return the EntrySet that contains the flames of the explosion
     */
    Set<Entry<GenPair<Integer, Integer>, FlameType>> computeFlame(GameMap map);

    /**
     * 
     * @return the BombType of this Bomb
     */
    BombType getBombType();

    /**
     * 
     * @return the range of this Bomb
     */
    int getRange();

    /**
     * 
     * @return the position of this Bomb
     */
    GenPair<Integer, Integer> getPos();
}
