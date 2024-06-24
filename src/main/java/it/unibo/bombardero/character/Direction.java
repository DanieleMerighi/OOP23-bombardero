package it.unibo.bombardero.character;

import java.util.ArrayList;
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

    public List<Pair> getDiagonals(Direction dir , Pair characterPos) { 
        List<Pair> cells;
        List<Pair> aCell = new ArrayList<>();
        switch (dir) {
            case UP:
                cells = List.of(new Pair(characterPos.x()-1, characterPos.y()-1) , new Pair(characterPos.x()+1, characterPos.y()-1) );
                break;
            case DOWN:
                cells = List.of(new Pair(characterPos.x()-1, characterPos.y()+1) , new Pair(characterPos.x()+1, characterPos.y()+1) );
                break;
            case LEFT:
                cells = List.of(new Pair(characterPos.x()-1, characterPos.y()-1) , new Pair(characterPos.x()-1, characterPos.y()+1) );
                break;
            case RIGHT:
                cells = List.of(new Pair(characterPos.x()+1, characterPos.y()-1) , new Pair(characterPos.x()+1, characterPos.y()+1) );
                break;
            default :
                return null;
        }
        aCell = cells.stream().filter(c -> c.x() >= 0 && c.y() >= 0 && c.x() < 13 && c.y() < 13).toList();
        return aCell;
        
        
    }

}
