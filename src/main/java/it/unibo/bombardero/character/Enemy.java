package it.unibo.bombardero.character;

import java.util.Optional;
import java.util.List;
import java.util.LinkedList;
import java.util.Random;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import it.unibo.bombardero.character.AI.GraphBuilder;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.utils.Utils;

public class Enemy extends Character {

    private List<Pair> path = new LinkedList<>();
    Optional<Pair> nextMove = Optional.empty();
    private int[][] map = new int[Utils.MAP_ROWS][Utils.MAP_COLS];
    private State currentState = State.PATROL; // Initial state
    private int numBombs = Utils.ENEMY_STARTING_BOMBS; // Assuming starting number of bombs

    private final int SAFE_CELL_DISTANCE = 2;

    public Enemy(Pair coord, int width, int height) {
        super(coord, width, height);
    }

    private boolean isEnemyClose() {
        Pair enemyCoord = getCoord();
        Pair playerCoord = getPlayerCoord();
        int detectionRadius = Utils.ENEMY_DETECTION_RADIUS; // Assuming detection radius is defined in Utils

        // Calculate Manhattan distance between enemy and player
        int distance = Math.abs(enemyCoord.row() - playerCoord.row())
                + Math.abs(enemyCoord.col() - playerCoord.col());

        return distance <= detectionRadius; // Check if player is within detection radius
    }

    private boolean isInDangerZone() {
        Pair enemyCoord = getCoord();
        int explosionRadius = Utils.EXPLOSION_RADIUS; // Assuming explosion radius is defined in Utils

        // Check surrounding cells for explosions
        for (int row = enemyCoord.row() - explosionRadius; row <= enemyCoord.row() + explosionRadius; row++) {
            for (int col = enemyCoord.col() - explosionRadius; col <= enemyCoord.col() + explosionRadius; col++) {
                if (isValidCell(row, col) && map[row][col] == Utils.EXPLOSION) {
                    return true; // Explosion found within blast radius
                }
            }
        }

        return false; // No explosions found within blast radius
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

    // this is just for debug pourpose...
    private Pair getPlayerCoord() {
        // Iterate through all map cells
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                int cellValue = map[row][col];
                if (cellValue == Utils.PLAYER) {
                    return new Pair(row, col);
                }
            }
        }
        return new Pair(-1, -1);
    }

    private void placeBomb(Pair targetCell) {
        if (hasBombsLeft() && isValidCell(targetCell.row(), targetCell.col())
                && map[targetCell.row()][targetCell.col()] == Utils.GRASS) {
            map[targetCell.row()][targetCell.col()] = Utils.BOMB;
            numBombs--;
        }
    }

    private boolean hasBombsLeft() {
        return numBombs > 0;
    }

    // when the enemy doesn't know where to move he choose randomly
    protected void moveRandomly() {
        Pair currentCoord = getCoord();

        // Generate random direction
        Direction randomDirection = Direction.values()[new Random().nextInt(Direction.values().length)];

        int newRow = currentCoord.row() + randomDirection.getDx();
        int newCol = currentCoord.col() + randomDirection.getDy();

        // Check if the chosen direction leads to a valid and empty cell
        if (isValidCell(newRow, newCol) && map[newRow][newCol] == Utils.GRASS) {
            nextMove = Optional.of(new Pair(newRow, newCol));
        } else {
            // If the random move is invalid, try again recursively (up to a limit)
            int retryCount = 0;
            while (!isValidCell(newRow, newCol) || map[newRow][newCol] != Utils.GRASS) {
                randomDirection = Direction.values()[new Random().nextInt(Direction.values().length)];
                newRow = currentCoord.row() + randomDirection.getDx();
                newCol = currentCoord.col() + randomDirection.getDy();
                retryCount++;
                if (retryCount >= 4) { // Limit retries to avoid getting stuck
                    System.out.println("Enemy: Stuck! No valid random move found.");
                    break;
                }
            }
            if (retryCount < 4) { // Set nextMove if a valid random move was found
                nextMove = Optional.of(new Pair(newRow, newCol));
            }
        }
    }

    protected void findFarthestSafeSpace() {
        int farthestDistance = 0;
        Pair farthestSafeSpace = null;

        // Iterate through all map cells
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                int cellValue = map[row][col];

                // Check if cell is safe (grass or empty) and at least SAFE_CELL_DISTANCE away
                // from explosions
                if (cellValue == Utils.GRASS || cellValue == Utils.PLAYER || cellValue == Utils.ENEMY) {
                    int distance = findDistanceToNearestExplosion(row, col);
                    if (distance >= farthestDistance && distance >= SAFE_CELL_DISTANCE) {
                        farthestDistance = distance;
                        farthestSafeSpace = new Pair(row, col);
                    }
                }
            }
        }

        // If a safe space is found, set it as the next move
        if (farthestSafeSpace != null) {
            nextMove = Optional.of(farthestSafeSpace);
        } else {
            // Handle case where no safe space is found (may need additional logic)
            moveRandomly();
        }
    }

    // Helper method to find the distance to the nearest explosion
    private int findDistanceToNearestExplosion(int row, int col) {
        int distance = Integer.MAX_VALUE; // Initialize with a large value
        for (int i = row - SAFE_CELL_DISTANCE; i <= row + SAFE_CELL_DISTANCE; i++) {
            for (int j = col - SAFE_CELL_DISTANCE; j <= col + SAFE_CELL_DISTANCE; j++) {
                if (isValidCell(i, j) && map[i][j] == Utils.EXPLOSION) {
                    int currentDistance = Math.abs(i - row) + Math.abs(j - col); // Manhattan distance
                    distance = Math.min(distance, currentDistance);
                }
            }
        }
        return distance;
    }

    private static boolean isValidCell(int row, int col) {
        return row >= 0 && row < Utils.MAP_ROWS && col >= 0 && col < Utils.MAP_COLS;
    }

    /* base AI Heuristics */
    @Override
    public void update() {
        currentState.execute(this); // Delegate behavior to current state
        if (nextMove.isPresent() && isValidCell(nextMove.get().row(), nextMove.get().col())) {
            Pair nextCell = nextMove.get();
        
            // Check if nextCell is a destroyable wall
            if (map[nextCell.row()][nextCell.col()] == Utils.WALL) {
                // Enemy tries to destroy the wall
                placeBomb(coord);
            } else {
                // Move to the next cell if it's not a wall
                coord = nextCell;
            }
        } else {
            // Handle the case where nextMove is empty or invalid
            moveRandomly();
        }
        coord = nextMove.isPresent() ? nextMove.get() : coord;
    }

    public enum State {
        PATROL {
            @Override
            void execute(Enemy enemy) {
                if (enemy.isInDangerZone()) { // Detected player
                    enemy.currentState = State.ESCAPE;
                } else if (enemy.isEnemyClose()) { // Detected bomb
                    enemy.currentState = State.CHASE;
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
                    enemy.currentState = State.PATROL;
                } else {
                    enemy.findFarthestSafeSpace();
                }
            }
        };

        abstract void execute(Enemy enemy);
    }

    public List<Pair> getPath() {
        return new LinkedList<>(path);
    }

    public Optional<Pair> getNextMove() {
        return nextMove;
    }

    public State getState() {
        return currentState;
    }

    public int getNumBombs() {
        return numBombs;
    }

    public int[][] getMap() {
        return this.map.clone();
    }

    public void setMap(int[][] map) {
        this.map = map;
    }
}
