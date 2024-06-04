package it.unibo.bombardero.character;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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

    public Optional<List<Pair>> getDiagonals(Direction dir) {
        Pair pd= dir.getPair();
        switch (dir) {
            case UP:
                return Optional.of(List.of(new Pair(pd.col()-1, pd.row()-1) , new Pair(pd.col()+1, pd.row()-1) ));
            case DOWN:
                return Optional.of(List.of(new Pair(pd.col()-1, pd.row()+1) , new Pair(pd.col()+1, pd.row()+1) ));
            case LEFT:
                return Optional.of(List.of(new Pair(pd.col()-1, pd.row()-1) , new Pair(pd.col()-1, pd.row()+1) ));
            case RIGHT:
                return Optional.of(List.of(new Pair(pd.col()+1, pd.row()-1) , new Pair(pd.col()+1, pd.row()+1) ));
            default:
                return null;
        }
        
    }

}
