package it.unibo.bombardero.character;

import java.util.Optional;
import java.util.List;
import java.util.LinkedList;
import java.util.Random;

import it.unibo.bombardero.cell.BombFactory;
import it.unibo.bombardero.character.AI.EnemyGraphReasoner;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.Coord;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.utils.Utils;

import java.util.EnumSet;
import java.util.stream.Collectors;

public class Enemy extends Character {

    Optional<Pair> nextMove = Optional.empty();
    private State currentState = State.PATROL; // Initial state
    private int movementTimer = 0; // Timer for movement updates
    private EnemyGraphReasoner graph;

    public Enemy(GameManager manager, Coord coord, BombFactory bombFactory) {
        super(manager, coord, bombFactory);
        graph = new EnemyGraphReasoner(manager.getGameMap());
    }

    private boolean isEnemyClose() {
        Pair enemyCoord = getIntCoordinate();
        Pair playerCoord = this.getManager().getPlayer().getIntCoordinate();
        int detectionRadius = Utils.ENEMY_DETECTION_RADIUS; // Assuming detection radius is defined in Utils

        // Calculate Manhattan distance between enemy and player
        int distance = Math.abs(enemyCoord.row() - playerCoord.row())
                + Math.abs(enemyCoord.col() - playerCoord.col());

        return distance <= detectionRadius; // Check if player is within detection radius
    }

    private void placeBomb(Pair targetCell) {
        if (hasBombsLeft() && isValidCell(targetCell)
                && this.getManager().getGameMap().isEmpty(targetCell)) {
            // map.addBomb(null, targetCell);
            // numBombs--;
        }
    }

    // when the enemy doesn't know where to move he choose randomly
    private void moveRandomly() {
        Pair currentCoord = getIntCoordinate();
        List<Direction> dirs = EnumSet.allOf(Direction.class)
                .stream()
                .filter(d -> d != Direction.DEFAULT)
                .collect(Collectors.toList());
        while (!dirs.isEmpty()) {
            Direction randomDirection = dirs.remove(new Random().nextInt(dirs.size()));
            Pair p = currentCoord.sum(new Pair(randomDirection.getDx(), randomDirection.getDy()));
            if (isValidCell(p) && getManager().getGameMap().isEmpty(p)) {
                nextMove = Optional.of(p);
                break;
            }
        }
        if (nextMove.isEmpty()) {
            System.out.println("Enemy: Stuck! No valid random move found.");
        }
    }

    private boolean isValidCell(Pair cell) {
        return cell.row() >= 0 && cell.col() >= 0 && cell.row() <= Utils.MAP_ROWS && cell.col() <= Utils.MAP_COLS;
    }

    private void computeNextDir() {
        graph = new EnemyGraphReasoner(this.getManager().getGameMap());
        currentState.execute(this); // Delegate behavior to current state
        nextMove.ifPresent(cell -> {
            if (this.getManager().getGameMap().isBreakableWall(cell)) {
                placeBomb(getIntCoordinate());
                // change the state of the enemy?
            }
        });
    }

    /* base AI Heuristics */
    @Override
    public void update() {
        movementTimer += 1;
        // Every 60 frames (assuming 60 fps), call computeNextDir to get the next target
        if (movementTimer >= 60 || nextMove.isEmpty()) {
            movementTimer = movementTimer >= 60 ? 0 : movementTimer;
            computeNextDir();
        }

        if (nextMove.isPresent()) {
            Pair target = nextMove.get();
            Coord direction = getCharacterPosition().subtract(target); // Calculate direction vector

            // Restrict movement to 4 directions (up, down, left, right)
            direction = restrictToGridDirections(direction);
            setCharacterPosition(getCharacterPosition().sum(direction.multiply(getSpeed())));

            // Check if reached the target cell using a small tolerance
            if (Math.abs(target.row() - getCharacterPosition().row()) < getSpeed() &&
                    Math.abs(target.col() - getCharacterPosition().col()) < getSpeed()) {
                nextMove = Optional.empty(); // Clear target if reached
            }
        }
    }

    // Helper method to restrict movement to 4 directions (up, down, left, right)
    private Coord restrictToGridDirections(Coord direction) {
        // Check if the direction is already aligned with one of the 4 grid directions
        if (Math.abs(direction.row()) > Math.abs(direction.col())) {
            // Vertical movement
            direction = new Coord(Math.signum(direction.row()) * 1, 0);
        } else {
            // Horizontal movement
            direction = new Coord(0, Math.signum(direction.col()) * 1);
        }
        return direction;
    }

    public enum State {
        PATROL {
            @Override
            void execute(Enemy enemy) {
                if (enemy.graph.isInDangerZone(enemy.getIntCoordinate(), enemy.getFlameRange())) { // Detected bomb
                    enemy.currentState = State.ESCAPE;
                } else if (enemy.isEnemyClose()) { // Detected player
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
                    List<Pair> path = enemy.graph.findShortestPathToPlayer(enemy.getIntCoordinate(),
                            enemy.getManager().getPlayer().getIntCoordinate());
                    enemy.nextMove = Optional.of(path.get(0));
                }
            }
        },
        ESCAPE {
            @Override
            void execute(Enemy enemy) {
                if (!enemy.graph.isInDangerZone(enemy.getIntCoordinate(), Utils.EXPLOSION_RADIUS)) { // Safe now
                    enemy.currentState = State.PATROL;
                } else {
                    enemy.nextMove = enemy.graph.findNearestSafeSpace(enemy.getIntCoordinate(), enemy.getFlameRange());
                }
            }
        };

        abstract void execute(Enemy enemy);
    }

    public Optional<Pair> getNextMove() {
        return nextMove;
    }

    public State getState() {
        return currentState;
    }

    @Override
    public void placeBomb() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'placeBomb'");
    }
}
