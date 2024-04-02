package it.unibo.bombardero.character;

import java.util.Optional;
import java.util.List;
import java.util.LinkedList;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import it.unibo.bombardero.character.AI.GraphBuilder;
import it.unibo.bombardero.map.api.Coord;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.utils.Utils;

public class Enemy extends Character {

    private List<Pair> path = new LinkedList<>();
    Optional<Pair> nextMove = Optional.empty();
    private int[][] map = new int[Utils.MAP_ROWS][Utils.MAP_COLS];
    private boolean isDestReachable = true;
    private boolean isBomb = false;
    private State currentState = State.PATROL; // Initial state

    private final int SAFE_CELL_DISTANCE = 2;

    public Enemy(Pair coord, int width, int height) {
        super(coord, width, height);
    }

    /* base AI Heuristics */
    @Override
    protected void update() {
        currentState.execute(this); // Delegate behavior to current state
    }

    private boolean isEnemyClose() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isEnemyClose'");
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

    private void findShortestPathToPlayer() {
        Pair enemyCoord = getCoord(); // Get enemy's current coordinates
        Pair playerCoord = getPlayerCoord(); // Get player's coordinates (assuming this method exists)
        path = GraphBuilder.findShortestPath(buildMapGraph(map), enemyCoord, playerCoord).orElse(new LinkedList<>());
        nextMove = path.isEmpty() ? Optional.empty() : Optional.of(path.remove(0));
    }

    private Graph<Pair, DefaultEdge> buildMapGraph(int[][] map) {
        return GraphBuilder.buildFromMap(map);
    }

    private Pair getPlayerCoord() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPlayerCoord'");
    }

    protected void moveRandomly() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'moveRandomly'");
    }

    protected void findFarthestSafeSpace() {
        // Implement logic to find the farthest cell that's at least SAFE_CELL_DISTANCE away from explosions
    }

    private enum State {
        PATROL {
            @Override
            void execute(Enemy enemy) {
                if (enemy.isEnemyClose()) { // Detected player
                    enemy.currentState = State.CHASE;
                } else if (enemy.isInDangerZone()) { // Detected bomb
                    enemy.isBomb = true;
                    enemy.currentState = State.ESCAPE;
                } else {
                    enemy.moveRandomly(); // Keep patrolling
                }
            }
        },
        CHASE {
            @Override
            void execute(Enemy enemy) {
                if (!enemy.isEnemyClose()) { // Lost sight of player
                    enemy.currentState = State.PATROL;
                } else {
                    enemy.findShortestPathToPlayer();
                }
            }
        },
        ESCAPE {
            @Override
            void execute(Enemy enemy) {
                if (!enemy.isInDangerZone()) { // Safe now
                    enemy.isBomb = false;
                    enemy.currentState = State.PATROL;
                } else {
                    enemy.findFarthestSafeSpace();
                }
            }
        };

        abstract void execute(Enemy enemy);
    }
}

