package it.unibo.bombardero.character;

import java.util.LinkedList;
import java.util.List;

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

    public List<Pair> getDiagonals(Direction dir) {
        Pair pd= dir.getPair();
        switch (dir) {
            case UP:
                return List.of(new Pair(pd.x()-1, pd.y()-1) , new Pair(pd.x()+1, pd.y()-1) );
            case DOWN:
                return List.of(new Pair(pd.x()-1, pd.y()+1) , new Pair(pd.x()+1, pd.y()+1) );
            case LEFT:
                return List.of(new Pair(pd.x()-1, pd.y()-1) , new Pair(pd.x()-1, pd.y()+1) );
            case RIGHT:
                return List.of(new Pair(pd.x()+1, pd.y()-1) , new Pair(pd.x()+1, pd.y()+1) );
            default:
                return null;
        }
        
    }

}
