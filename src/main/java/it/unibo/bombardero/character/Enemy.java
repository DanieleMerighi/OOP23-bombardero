package it.unibo.bombardero.character;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;


import it.unibo.bombardero.cell.Cell;
import it.unibo.bombardero.map.api.Coord;
import it.unibo.bombardero.map.api.Pair;

public class Enemy extends Character {

    private Stack<Pair> path = new Stack<>();
    Optional<Pair> nextMove = Optional.empty();
    protected Map<Pair, Cell> map = new HashMap<>();
    private boolean isDestReachable = true;
    private boolean isBomb = false;

    private final int SAFE_CELL_DISTANCE = 2;

    public Enemy(Coord coord, int width, int height) {
        super(coord, width, height);
    }

    @Override
    protected void update() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    public int nextDir() {
        if(!isDestReachable) {
            return 0;
        }

        nextMove = Optional.empty();
        path.clear();
        if(isBomb) {

        }

        return 0;

    }
   
}
