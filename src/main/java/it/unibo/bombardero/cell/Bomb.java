package it.unibo.bombardero.cell;

import it.unibo.bombardero.map.api.Pair;

public interface Bomb extends Cell {

    public enum BombType {

        BOMB_BASIC("bomb/basic"),
        BOMB_POWER("bomb/power"),
        BOMB_PUNCH("bomb/punch"),
        BOMB_PIERCING("bomb/piercing"),
        BOMB_REMOTE("bomb/remote");

        private String typeString;

        private BombType(final String typeString) {
            this.typeString = typeString;
        }

        public String getTypeString() {
            return this.typeString;
        }

    }

    boolean isExploded();

    void update(boolean condition);

    void update();

    BombType getBombType();

    int getRange();

    Pair getPos();
}
