package it.unibo.bombardero.character.ai.api;

import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.Pair;
import java.util.List;
import java.util.Optional;

/**
 * This interface defines the functionalities for pathfinding and danger zone analysis for enemies
 * within the game map. It utilizes the JGraphT library to represent the game map as a graph
 * and performs graph traversals and shortest path calculations.
 */
public interface EnemyGraphReasoner {
    /**
     * Checks if a specific cell is within the enemy's danger zone based on the
     * explosion radius.
     * This method performs a Breadth-First Search (BFS) traversal on the graph
     * starting
     * from the enemy's coordinates. It explores cells within the specified
     * explosion radius
     * and checks if any of them contain a reachable bomb (not blocked by walls).
     *
     * @param enemyCoord the enemy's current coordinates
     * @param explRadius the explosion radius of the danger zone
     * @return true if the enemy is within a danger zone, false otherwise
     */
    boolean isInDangerZone(Pair enemyCoord, int explRadius);

    /**
     * Checks if a path between two cells is blocked by walls (breakable or
     * unbreakable).
     * This method iterates over the line connecting the two cells and checks if any
     * cell
     * along the line is a wall, effectively blocking the path. Diagonal movements
     * are not
     * supported.
     *
     * @param startCell the starting cell of the path
     * @param endCell   the ending cell of the path
     * @return true if the path is blocked by walls, false otherwise
     */
    boolean isPathBlockedByWalls(Pair startCell, Pair endCell);

    /**
     * Finds the shortest path from the enemy's current location to the player's
     * position.
     * This method utilizes Dijkstra's algorithm to find the shortest path on the
     * internal graph.
     * It returns a list of cells representing the path from the enemy to the
     * player,
     * excluding the starting enemy position (first element). If no path exists, an
     * empty list
     * is returned.
     *
     * @param enemyCoord  the enemy's current coordinates
     * @param playerCoord the player's current coordinates
     * @return a list of cells representing the shortest path to the player
     *         (excluding starting position)
     *         or an empty list if no path exists
     */
    List<Pair> findShortestPathToPlayer(Pair enemyCoord, Pair playerCoord);

    /**
     * Finds the nearest safe cell for the enemy to move to, given its current
     * coordinates and explosion radius.
     *
     * @param enemyCoord the current coordinates of the enemy
     * @param explRad    the explosion radius to consider for safety
     * @return an {@code Optional<Pair>} containing the coordinates of the nearest
     *         safe cell if found, otherwise an empty {@code Optional}
     */
    Optional<Pair> findNearestSafeCell(Pair enemyCoord, int explRad);

    /**
     * Finds the nearest reachable bomb from the enemy's current location.
     * This method performs a Breadth-First Search (BFS) traversal on the graph
     * starting
     * from the enemy's coordinates. It explores cells within the map and checks if
     * any
     * of them contain a reachable bomb (not blocked by walls). If a reachable bomb
     * is found, its coordinates are returned as an Optional object. Otherwise, an
     * empty Optional is returned.
     *
     * @param enemyCoord the enemy's current coordinates
     * @return an Optional containing the nearest reachable bomb (Pair) or empty if
     *         none found
     */
    Optional<Pair> findNearestBomb(Pair enemyCoord);

    /**
     * Updates the internal graph representation of the game map with a new map.
     *
     * @param newMap the new game map to update to
     */
    void updateGraph(GameMap newMap);

    /**
     * Finds the nearest power-up within a limited radius from the enemy's current
     * coordinates.
     *
     * @param enemyCoord the current coordinates of the enemy
     * @return an {@code Optional<Pair>} containing the coordinates of the nearest
     *         power-up if found, otherwise an empty {@code Optional}
     */
    Optional<Pair> findNearestPowerUp(Pair enemyCoord);
}
