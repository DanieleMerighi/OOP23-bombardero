package it.unibo.bombardero.character;

import java.util.Optional;
import java.util.Stack;


import it.unibo.bombardero.map.api.Coord;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.utils.Utils;

public class Enemy extends Character {

    private Stack<Pair> path = new Stack<>();
    Optional<Pair> nextMove = Optional.empty();
    private int[][] map = new int[Utils.ROW][Utils.COL];
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

    // private void getMap() {
    //     List<Cell> entities = MapImpl.getEnemies();
    //     for (int i = 0; i < Utils.ROW; i++) {
    //         for (int j = 0; j < Utils.COL; j++) {
    //             if (entities.get(i*Utils.COL + j) instanceof UnbreakableWall) {
    //                 map[i][j] = 1;
    //             } else if (entities.get(i*COL + j) instanceof Wall) map[i][j] = 2;
    //         }
    //     }
    // }
   
}
