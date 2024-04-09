package it.unibo.bombardero.character;

import java.util.Optional;
import java.util.List;
import java.util.LinkedList;
import java.util.Random;
import java.util.stream.IntStream;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import it.unibo.bombardero.character.AI.GraphBuilder;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.utils.Utils;

public class Enemy extends Character {

    private List<Pair> path = new LinkedList<>();
    Optional<Pair> nextMove = Optional.empty();
    private State currentState = State.PATROL; // Initial state
    private int numBombs = Utils.ENEMY_STARTING_BOMBS; // Assuming starting number of bombs

    private final int SAFE_CELL_DISTANCE = 2;

    public Enemy(GameManager manager, Pair coord, int width, int height) {
        super(manager, coord, width, height);
    }

    private boolean isEnemyClose() {
        Pair enemyCoord = getCoord();
        Pair playerCoord = this.manager.getPlayer().getCoord();
        int detectionRadius = Utils.ENEMY_DETECTION_RADIUS; // Assuming detection radius is defined in Utils

        // Calculate Manhattan distance between enemy and player
        int distance = Math.abs(enemyCoord.row() - playerCoord.row())
                + Math.abs(enemyCoord.col() - playerCoord.col());

        return distance <= detectionRadius; // Check if player is within detection radius
    }

    private boolean isInDangerZone() {
        Pair enemyCoord = getCoord();
        int explosionRadius = Utils.EXPLOSION_RADIUS;

        return IntStream.rangeClosed(enemyCoord.row() - explosionRadius, enemyCoord.row() + explosionRadius)
                .boxed()
                .flatMap(row -> IntStream
                        .rangeClosed(enemyCoord.col() - explosionRadius, enemyCoord.col() + explosionRadius)
                        .mapToObj(col -> new Pair(row, col)))
                .filter(c -> isValidCell(c.row(), c.col()))
                .anyMatch(cell -> this.manager.getGameMap().isFlame(cell));
    }

    private void findShortestPathToPlayer() {
        Pair enemyCoord = getCoord(); // Get enemy's current coordinates
        Pair playerCoord = this.manager.getPlayer().getCoord();
        path = GraphBuilder.findShortestPath(buildMapGraph(this.manager.getGameMap()), enemyCoord, playerCoord)
                .orElse(new LinkedList<>());
        nextMove = path.isEmpty() ? Optional.empty() : Optional.of(path.remove(0));
    }

    private Graph<Pair, DefaultEdge> buildMapGraph(GameMap map) {
        return GraphBuilder.buildFromMap(map);
    }

    private void placeBomb(Pair targetCell) {
        if (hasBombsLeft() && isValidCell(targetCell.row(), targetCell.col())
                && this.manager.getGameMap().isEmpty(targetCell)) {
            // map.addBomb(null, targetCell);
            numBombs--;
        }
    }

    private boolean hasBombsLeft() {
        return numBombs > 0;
    }

    // when the enemy doesn't know where to move he choose randomly
    private void moveRandomly() {
        Pair currentCoord = getCoord();
        for (int retryCount = 0; retryCount < 4; retryCount++) {
            Direction randomDirection = Direction.values()[new Random().nextInt(Direction.values().length)];
            int newRow = currentCoord.row() + randomDirection.getDx();
            int newCol = currentCoord.col() + randomDirection.getDy();
            if (isValidCell(newRow, newCol) && manager.getGameMap().isEmpty(new Pair(newRow, newCol))) {
                nextMove = Optional.of(new Pair(newRow, newCol));
                break;
            }
        }
        if (nextMove.isEmpty()) {
            System.out.println("Enemy: Stuck! No valid random move found.");
        }
    }

    private void findFarthestSafeSpace() {
        int farthestDistance = 0;
        Pair farthestSafeSpace = null;

        for (Pair cell : manager.getGameMap().getMap().keySet()) {
            if (manager.getGameMap().isEmpty(cell)) {
                int distance = findDistanceToNearestExplosion(cell.row(), cell.col());
                if (distance >= farthestDistance && distance >= SAFE_CELL_DISTANCE) {
                    farthestDistance = distance;
                    farthestSafeSpace = cell;
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
                if (isValidCell(i, j) && this.manager.getGameMap().isFlame(new Pair(i, j))) {
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
        GameMap map = this.manager.getGameMap();
        currentState.execute(this); // Delegate behavior to current state
        if (nextMove.isPresent() && isValidCell(nextMove.get().row(), nextMove.get().col())) {
            Pair nextCell = nextMove.get();

            // Check if nextCell is a destroyable wall
            if (map.isBreakableWall(nextCell)) {
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
}
