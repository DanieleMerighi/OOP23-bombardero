package it.unibo.bombardero.character;

import it.unibo.bombardero.map.api.Pair;

public enum Direction {
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0),
    DEFAULT(0, 0);

    private final int x;
    private final int y;

    Direction(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public Pair getPair() {
        return new Pair(x, y);
    }

}
