package it.unibo.bombardero.character.ai.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.traverse.BreadthFirstIterator;

import it.unibo.bombardero.cell.powerup.api.PowerUpType;
import it.unibo.bombardero.character.Direction;
import it.unibo.bombardero.character.ai.api.EnemyGraphReasoner;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.utils.Utils;

/**
 * This class provides pathfinding and danger zone analysis functionalities for
 * enemies
 * within the game map. It utilizes the JGraphT library to represent the game
 * map as a graph
 * and performs graph traversals and shortest path calculations.
 */
public class EnemyGraphReasonerImpl implements EnemyGraphReasoner {

    private GameMap map;
    /**
     * An internal JGraphT graph representation of the game map.
     * Vertices in this graph represent cells (Pairs of x and column coordinates),
     * and edges represent valid connections between cells.
     */
    private Graph<Pair, DefaultWeightedEdge> graph;

    /**
     * Constructs a new EnemyGraphReasoner instance for the given game map.
     * This constructor builds the internal graph representation based on the
     * provided map.
     *
     * @param map the game map object used for pathfinding and danger zone analysis
     */
    public EnemyGraphReasonerImpl(final GameMap map) {
        this.map = map;
        this.graph = GraphBuilderImpl.buildFromMap(map);
    }

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
    public boolean isInDangerZone(final Pair enemyCoord, final int explRadius) {
        final BreadthFirstIterator<Pair, DefaultWeightedEdge> bfsIterator = new BreadthFirstIterator<>(
                graph,
                enemyCoord);
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(bfsIterator, Spliterator.ORDERED), false)
                .takeWhile(cell -> bfsIterator.getDepth(cell) <= explRadius) // Limit traversal to explosion radius
                .anyMatch(cell -> (map.isBomb(cell) || map.isFlame(cell))
                        && (enemyCoord.x() == cell.x() || enemyCoord.y() == cell.y())
                        && !isPathBlockedByWalls(enemyCoord, cell));
    }

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
    public boolean isPathBlockedByWalls(final Pair startCell, final Pair endCell) {
        if (startCell.x() != endCell.x() && startCell.y() != endCell.y()) {
            return true; // Diagonal paths not supported
        }

        int dx = endCell.x() - startCell.x();
        int dy = endCell.y() - startCell.y();

        return IntStream.rangeClosed(0, Math.max(Math.abs(dx), Math.abs(dy)))
                .mapToObj(i -> {
                    int x = startCell.x() + i * Integer.signum(dx);
                    int y = startCell.y() + i * Integer.signum(dy);
                    return new Pair(x, y);
                })
                .anyMatch(p -> map.isBreakableWall(p) || map.isUnbreakableWall(p));
    }

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
    public List<Pair> findShortestPathToPlayer(final Pair enemyCoord, final Pair playerCoord) {
        if (enemyCoord.equals(playerCoord)) {
            return Collections.emptyList();
        }

        // Use Dijkstra's algorithm to find the shortest path to the player
        DijkstraShortestPath<Pair, DefaultWeightedEdge> dijkstra = new DijkstraShortestPath<>(
                graph);
        GraphPath<Pair, DefaultWeightedEdge> path = null;
        try {
            path = dijkstra.getPath(enemyCoord, playerCoord);
        } catch (Exception e) {
            System.out.println(e);
        }

        return path == null ? Collections.emptyList()
                : path.getVertexList().subList(1, path.getVertexList().size());
    }

    /**
     * Finds the nearest safe cell for the enemy to move to, given its current
     * coordinates and explosion radius.
     *
     * @param enemyCoord the current coordinates of the enemy
     * @param explRad    the explosion radius to consider for safety
     * @return an {@code Optional<Pair>} containing the coordinates of the nearest
     *         safe cell if found, otherwise an empty {@code Optional}
     */
    public Optional<Pair> findNearestSafeCell(final Pair enemyCoord, final int explRad) {
        return findNearestSafeCellRecursive(enemyCoord, explRad, new HashSet<>());
    }

    private Optional<Pair> findNearestSafeCellRecursive(final Pair enemyCoord, final int explRad,
            final Set<Pair> visited) {
        List<Pair> adjacentCells = EnumSet.allOf(Direction.class)
                .stream()
                .map(d -> new Pair(enemyCoord.x() + d.x(), enemyCoord.y() + d.y()))
                .filter(cell -> isValidCell(cell) && (map.isEmpty(cell)
                        || (map.isPowerUp(cell)
                                && map.whichPowerUpType(cell).filter(type -> type != PowerUpType.SKULL).isPresent()))
                        && !visited.contains(cell))
                .collect(Collectors.toCollection(ArrayList::new));

        Optional<Pair> safeCell = adjacentCells.stream()
                .filter(c -> !isInDangerZone(c, explRad))
                .min((cell1, cell2) -> Double.compare(calculateDistance(enemyCoord, cell1),
                        calculateDistance(enemyCoord, cell2)));

        if (safeCell.isPresent()) {
            return safeCell;
        } else {
            visited.addAll(adjacentCells);
            for (Pair cell : adjacentCells) {
                Optional<Pair> recursiveResult = findNearestSafeCellRecursive(cell, explRad, visited);
                if (recursiveResult.isPresent()) {
                    return recursiveResult;
                }
            }
        }
        return Optional.empty();
    }

    private boolean isValidCell(final Pair cell) {
        return cell.x() >= 0 && cell.y() >= 0 && cell.x() < Utils.MAP_COLS && cell.y() < Utils.MAP_ROWS;
    }

    private double calculateDistance(final Pair p1, final Pair p2) {
        int dx = p2.x() - p1.x();
        int dy = p2.y() - p1.y();
        return Math.sqrt(dx * dx + dy * dy);
    }

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
    public Optional<Pair> findNearestBomb(final Pair enemyCoord) {
        final BreadthFirstIterator<Pair, DefaultWeightedEdge> bfsIterator = new BreadthFirstIterator<>(
                graph,
                enemyCoord);

        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(bfsIterator, Spliterator.ORDERED), false)
                .filter(cell -> map.isBomb(cell)) // Find reachable bombs
                .findFirst();
    }

    /**
     * Finds the nearest power-up within a limited radius from the enemy's current
     * coordinates.
     *
     * @param enemyCoord the current coordinates of the enemy
     * @return an {@code Optional<Pair>} containing the coordinates of the nearest
     *         power-up if found, otherwise an empty {@code Optional}
     */
    public Optional<Pair> findNearestPowerUp(final Pair enemyCoord) {
        final BreadthFirstIterator<Pair, DefaultWeightedEdge> bfsIterator = new BreadthFirstIterator<>(
                graph,
                enemyCoord);

        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(bfsIterator, Spliterator.ORDERED), false)
                .takeWhile(cell -> bfsIterator.getDepth(cell) <= 3) // Limit traversal to explosion radius
                .filter(cell -> map.isPowerUp(cell))
                .min((cell1, cell2) -> Double.compare(calculateDistance(enemyCoord, cell1),
                        calculateDistance(enemyCoord, cell2)));
    }

    /**
     * Updates the internal graph representation of the game map with a new map.
     *
     * @param newMap the new game map to update to
     */
    public void updateGraph(final GameMap newMap) {
        List<Pair> oldWalls = map.getMap().keySet().stream().filter(c -> map.isBreakableWall(c)).toList();
        List<Pair> newWalls = newMap.getMap().keySet().stream().filter(c -> newMap.isBreakableWall(c)).toList();
        if (oldWalls.size() != newWalls.size()) {
            oldWalls.stream().filter(c -> !newWalls.contains(c)).forEach(c -> updateEdges(c));
        }
        this.map = newMap;
    }

    private void updateEdges(final Pair cell) {
        graph.edgesOf(cell).forEach(e -> graph.setEdgeWeight(e, 1));
    }
}
