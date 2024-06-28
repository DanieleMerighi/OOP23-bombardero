package it.unibo.bombardero.character;

import java.util.ArrayList;
import java.util.List;

import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.utils.Utils;

/**
 * Enumeration representing the different direction in the game.
 */
public enum Direction {
    /**
     * Upward direction.
     */
    UP(0, -1),
    /**
     * Downward direction.
     */
    DOWN(0, 1),
    /**
     * Leftward direction.
     */
    LEFT(-1, 0),
    /**
     * Rightward direction.
     */
    RIGHT(1, 0);

    private final int x;
    private final int y;

    /**
     * Constructor for Direction.
     * 
     * @param x The x-coordinate change for this direction.
     * @param y The y-coordinate change for this direction.
     */
    Direction(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets the x-coordinate change for this direction.
     * 
     * @return The x-coordinate change.
     */
    public int x() {
        return x;
    }

    /**
     * Gets the y-coordinate change for this direction.
     * 
     * @return The y-coordinate change.
     */
    public int y() {
        return y;
    }

    /**
     * Gets the pair representing the direction.
     * 
     * @return A Pair representing the direction.
     */
    public Pair getPair() {
        return new Pair(x, y);
    }

    /**
     * Gets the diagonal cells relative to the current direction and character position.
     * 
     * @param dir The direction to consider.
     * @param characterPos The current position of the character.
     * @return A list of diagonal cells relative to the direction and character position.
     */
    public List<Pair> getDiagonals(final Direction dir, final Pair characterPos) {
        List<Pair> cells;
        List<Pair> aCell = new ArrayList<>();
        switch (dir) {
            case UP:
                cells = List.of(new Pair(characterPos.x() - 1, characterPos.y() - 1),
                        new Pair(characterPos.x() + 1, characterPos.y() - 1));
                break;
            case DOWN:
                cells = List.of(new Pair(characterPos.x() - 1, characterPos.y() + 1),
                        new Pair(characterPos.x() + 1, characterPos.y() + 1));
                break;
            case LEFT:
                cells = List.of(new Pair(characterPos.x() - 1, characterPos.y() - 1),
                        new Pair(characterPos.x() - 1, characterPos.y() + 1));
                break;
            case RIGHT:
                cells = List.of(new Pair(characterPos.x() + 1, characterPos.y() - 1),
                        new Pair(characterPos.x() + 1, characterPos.y() + 1));
                break;
            default:
                return null;
        }
        aCell = cells.stream().filter(c -> c.x() >= 0 && c.y() >= 0 && c.x() < Utils.MAP_COLS && c.y() < Utils.MAP_ROWS).toList();
        return aCell;

    }

}
