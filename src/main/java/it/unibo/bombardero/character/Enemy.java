package it.unibo.bombardero.character;

import java.util.Optional;
import java.util.Stack;

import it.unibo.bombardero.map.api.Coord;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.utils.Utils;

public class Enemy extends Character {

    private Stack<Pair> path = new Stack<>();
    Optional<Pair> nextMove = Optional.empty();
    private int[][] map = new int[Utils.MAP_ROWS][Utils.MAP_COLS];
    private boolean isDestReachable = true;
    private boolean isBomb = false;

    private final int SAFE_CELL_DISTANCE = 2;

    public Enemy(Coord coord, int width, int height) {
        super(coord, width, height);
    }

    /*base AI Heuristics*/
    @Override
    protected void update() {
        // Detect danger zones (bomb range)
        detectDangerZones();

        if (isInDangerZone()) {
            findSafePlaces();
        } else {
            // Move towards a target (enemy or unblocked pathway)
            moveTowardsTarget();
        }
    }

    private void detectDangerZones() {
        // Implementation to detect bomb range and update danger zone status
    }
    
    private boolean isInDangerZone() {
        // Check if the current position is in a danger zone
        return false;
    }
    
    private void findSafePlaces() {
        // Implement logic to find safe places within the grid
    }
    
    private void moveTowardsTarget() {
        // Implement logic to move towards enemies or unblocked pathways
    }
    
    /* for the future... */
    private void placeBombsToUnblockPathways() {
        // Implement logic to place bombs strategically to unblock pathways
    }
    
    private void targetEnemies() {
        // Implement logic to target nearby enemies and place bombs to eliminate them
    }

    public int nextDir() {
        if (!isDestReachable) {
            return 0;
        }

        nextMove = Optional.empty();
        path.clear();
        if (isBomb) {

        }

        return 0;

    }

    // private void getMap() {
    // List<Cell> entities = MapImpl.getEnemies();
    // for (int i = 0; i < Utils.ROW; i++) {
    // for (int j = 0; j < Utils.COL; j++) {
    // if (entities.get(i*Utils.COL + j) instanceof UnbreakableWall) {
    // map[i][j] = 1;
    // } else if (entities.get(i*COL + j) instanceof Wall) map[i][j] = 2;
    // }
    // }
    // }

}
