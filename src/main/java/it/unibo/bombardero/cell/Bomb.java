package it.unibo.bombardero.cell;

import java.util.Set;
import java.util.Map.Entry;

import it.unibo.bombardero.cell.Flame.FlameType;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.GenPair;

public interface Bomb extends Cell {

    enum BombType {

        BOMB_BASIC("bomb/basic"),
        BOMB_POWER("bomb/power"),
        BOMB_PUNCH("bomb/punch"),
        BOMB_PIERCING("bomb/piercing"),
        BOMB_REMOTE("bomb/remote");

        private String typeString;

        BombType(final String typeString) {
            this.typeString = typeString;
        }

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
     * update the bomb
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
