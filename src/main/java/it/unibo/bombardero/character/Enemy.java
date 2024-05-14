package it.unibo.bombardero.character;

import java.util.Optional;
import java.util.List;
import java.util.LinkedList;
import java.util.Random;

import it.unibo.bombardero.cell.Bomb;
import it.unibo.bombardero.cell.Cell.CellType;
import it.unibo.bombardero.character.AI.EnemyGraphReasoner;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.utils.Utils;

public class Enemy extends Character {

    private List<Pair> path = new LinkedList<>();
    Optional<Pair> nextMove = Optional.empty();
    private State currentState = State.PATROL; // Initial state
    private int numBombs = Utils.ENEMY_STARTING_BOMBS; // Assuming starting number of bombs
    private int movementTimer = 0; // Timer for movement updates
    private EnemyGraphReasoner graph;


    public Enemy(GameManager manager, float x, float y) {
        super(manager, x, y);
        graph = new EnemyGraphReasoner(manager.getGameMap());
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

    private void placeBomb(Pair targetCell) {
        if (hasBombsLeft() && isValidCell(targetCell)
                && this.manager.getGameMap().isEmpty(targetCell)) {
            this.manager.getGameMap().addBomb(new Bomb(manager, targetCell, CellType.BOMB_BASIC, 2), targetCell);
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
            if (isValidCell(new Pair(newRow, newCol)) && manager.getGameMap().isEmpty(new Pair(newRow, newCol))) {
                nextMove = Optional.of(new Pair(newRow, newCol));
                break;
            }
        }
        if (nextMove.isEmpty()) {
            System.out.println("Enemy: Stuck! No valid random move found.");
        }
    }

    private boolean isValidCell(Pair cell) {
        return cell.row() >= 0 && cell.row() < Utils.MAP_ROWS && cell.col() >= 0 && cell.col() < Utils.MAP_COLS;
    }

    private void computeNextDir() {
        GameMap map = this.manager.getGameMap();
        graph = new EnemyGraphReasoner(map);
        currentState.execute(this); // Delegate behavior to current state

        if(nextMove.isPresent() && isValidCell(nextMove.get()) && map.isBreakableWall(nextMove.get())) {
            placeBomb(getCoord());
            nextMove = Optional.empty();
            this.currentState = State.ESCAPE;
        }
    }

    /* base AI Heuristics */
    @Override
    public void update() {

        movementTimer += 1;
        // Every 60 frames (assuming 60 fps), call computeNextDir to get the next target
        if (movementTimer >= 60) {
            movementTimer = 0;
            computeNextDir();
        }

         // If a target exists, move towards it by a small increment
         if (nextMove.isPresent()) {
            Pair target = nextMove.get();
            float dx = target.row() - this.x;
            float dy = target.col() - this.y;

            // Adjust movement based on enemy speed 
            float movementSpeed = Utils.ENEMY_SPEED; 
            float movement = Math.min(Math.abs(dx) + Math.abs(dy), movementSpeed);

            this.x += (dx >= 0) ? movement : -movement;
            this.y += (dy >= 0) ? movement : -movement;

            // Check if reached the target cell
            if (Math.abs(dx) < movementSpeed && Math.abs(dy) < movementSpeed) {
                nextMove = Optional.empty(); // Clear target if reached
            }
        } else {
            computeNextDir();
        }
    }

    public enum State {
        PATROL {
            @Override
            void execute(Enemy enemy) {
                if (enemy.graph.isInDangerZone(enemy.getCoord(), Utils.EXPLOSION_RADIUS)) { // Detected player
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
                    enemy.path = enemy.graph.findShortestPathToPlayer(enemy.getCoord(), enemy.manager.getPlayer().getCoord());
                    enemy.nextMove = Optional.of(enemy.path.get(0));
                }
            }
        },
        ESCAPE {
            @Override
            void execute(Enemy enemy) {
                if (!enemy.graph.isInDangerZone(enemy.getCoord(), Utils.EXPLOSION_RADIUS)) { // Safe now
                    enemy.currentState = State.PATROL;
                } else {
                    enemy.nextMove = enemy.graph.findNearestSafeSpace(enemy.getCoord(), Utils.EXPLOSION_RADIUS);
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
