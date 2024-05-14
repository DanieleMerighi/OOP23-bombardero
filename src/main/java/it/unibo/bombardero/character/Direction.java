package it.unibo.bombardero.character;

import it.unibo.bombardero.map.api.Pair;

public enum Direction {
    UP(-1, 0),
    DOWN(1, 0),
    LEFT(0, -1),
    RIGHT(0, 1),
    DEFAULT(0, 0);

    private final int dx;
    private final int dy;

    Direction(final int dx, final int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    public Pair getPair() {
        return new Pair(getDx(), getDy());
    }

}
