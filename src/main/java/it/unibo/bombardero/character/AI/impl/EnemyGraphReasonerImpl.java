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

import it.unibo.bombardero.cell.powerup.api.PowerUp.PowerUpType;
import it.unibo.bombardero.character.Direction;
import it.unibo.bombardero.character.ai.api.EnemyGraphReasoner;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.GenPair;
import it.unibo.bombardero.utils.Utils;

/**
 * This class provides pathfinding and danger zone analysis functionalities for
 * enemies
 * within the game map. It utilizes the JGraphT library to represent the game
 * map as a graph
 * and performs graph traversals and shortest path calculations.
 */
public class EnemyGraphReasonerImpl implements EnemyGraphReasoner {

    /**
     * An internal JGraphT graph representation of the game map.
     * Vertices in this graph represent cells (Pairs of x and column coordinates),
     * and edges represent valid connections between cells.
     */
    private final Graph<GenPair<Integer, Integer>, DefaultWeightedEdge> graph;

    private List<GenPair<Integer, Integer>> oldWalls;

    /**
     * Constructs a new EnemyGraphReasoner instance for the given game map.
     * This constructor builds the internal graph representation based on the
     * provided map.
     *
     * @param map the game map object used for pathfinding and danger zone analysis
     */
    public EnemyGraphReasonerImpl(final GameMap map) {
        this.graph = GraphBuilderImpl.buildFromMap(map);
        oldWalls = map.getMap().keySet().stream()
                .filter(c -> map.isBreakableWall(c)).toList();
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
     * @param map        the game map
     * @param enemyCoord the enemy's current coordinates
     * @param explRadius the explosion radius of the danger zone
     * @return true if the enemy is within a danger zone, false otherwise
     */
    @Override
    public boolean isInDangerZone(final GameMap map, final GenPair<Integer, Integer> enemyCoord, final int explRadius) {
        final BreadthFirstIterator<GenPair<Integer, Integer>, DefaultWeightedEdge> bfsIterator = new BreadthFirstIterator<>(
                graph,
                enemyCoord);
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(bfsIterator, Spliterator.ORDERED), false)
                .takeWhile(cell -> bfsIterator.getDepth(cell) <= explRadius) // Limit traversal to explosion radius
                .anyMatch(cell -> (map.isBomb(cell) || map.isFlame(cell))
                        && (enemyCoord.x() == cell.x() || enemyCoord.y() == cell.y())
                        && !isPathBlockedByWalls(map, enemyCoord, cell));
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
     * @param map       the game map
     * @param startCell the starting cell of the path
     * @param endCell   the ending cell of the path
     * @return true if the path is blocked by walls, false otherwise
     */
    @Override
    public boolean isPathBlockedByWalls(final GameMap map, final GenPair<Integer, Integer> startCell,
            final GenPair<Integer, Integer> endCell) {
        if (!startCell.x().equals(endCell.x()) && !startCell.y().equals(endCell.y())) {
            return true; // Diagonal paths not supported
        }

        final int dx = endCell.x() - startCell.x();
        final int dy = endCell.y() - startCell.y();

        return IntStream.rangeClosed(0, Math.max(Math.abs(dx), Math.abs(dy)))
                .mapToObj(i -> {
                    final int x = startCell.x() + i * Integer.signum(dx);
                    final int y = startCell.y() + i * Integer.signum(dy);
                    return new GenPair<Integer, Integer>(x, y);
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
     * @param map         the game map
     * @param enemyCoord  the enemy's current coordinates
     * @param playerCoord the player's current coordinates
     * @return a list of cells representing the shortest path to the player
     *         (excluding starting position)
     *         or an empty list if no path exists
     */
    @Override
    public List<GenPair<Integer, Integer>> findShortestPathToPlayer(final GameMap map,
            final GenPair<Integer, Integer> enemyCoord,
            final GenPair<Integer, Integer> playerCoord) {
        if (enemyCoord.equals(playerCoord)) {
            return Collections.emptyList();
        }

        // Use Dijkstra's algorithm to find the shortest path to the player
        final DijkstraShortestPath<GenPair<Integer, Integer>, DefaultWeightedEdge> dijkstra = new DijkstraShortestPath<>(
                graph);
        final GraphPath<GenPair<Integer, Integer>, DefaultWeightedEdge> path = dijkstra.getPath(enemyCoord,
                playerCoord);
        return path == null ? Collections.emptyList()
                : path.getVertexList().subList(1, path.getVertexList().size());
    }

    /**
     * Finds the nearest safe cell for the enemy to move to, given its current
     * coordinates and explosion radius.
     *
     * @param map        the game map
     * @param enemyCoord the current coordinates of the enemy
     * @param explRad    the explosion radius to consider for safety
     * @return an {@code Optional<Pair>} containing the coordinates of the nearest
     *         safe cell if found, otherwise an empty {@code Optional}
     */
    @Override
    public Optional<GenPair<Integer, Integer>> findNearestSafeCell(final GameMap map,
            final GenPair<Integer, Integer> enemyCoord,
            final int explRad) {
        return findNearestSafeCellRecursive(map, enemyCoord, explRad, new HashSet<>());
    }

    private Optional<GenPair<Integer, Integer>> findNearestSafeCellRecursive(final GameMap map,
            final GenPair<Integer, Integer> enemyCoord,
            final int explRad,
            final Set<GenPair<Integer, Integer>> visited) {

        visited.add(enemyCoord);

        final List<GenPair<Integer, Integer>> adjacentCells = EnumSet.allOf(Direction.class)
                .stream()
                .map(d -> new GenPair<Integer, Integer>(enemyCoord.x() + d.x(), enemyCoord.y() + d.y()))
                .filter(cell -> isValidCell(cell) && !visited.contains(cell) && (map.isEmpty(cell)
                        || map.isPowerUp(cell)
                                && map.whichPowerUpType(cell).filter(type -> type != PowerUpType.SKULL).isPresent()))
                .collect(Collectors.toCollection(ArrayList::new));

        final Optional<GenPair<Integer, Integer>> safeCell = adjacentCells.stream()
                .filter(c -> !isInDangerZone(map, c, explRad))
                .min((cell1, cell2) -> Double.compare(calculateDistance(enemyCoord, cell1),
                        calculateDistance(enemyCoord, cell2)));

        if (safeCell.isPresent()) {
            return safeCell;
        }

        for (final GenPair<Integer, Integer> cell : adjacentCells) {
            if (!visited.contains(cell)) {
                final Optional<GenPair<Integer, Integer>> recursiveResult = findNearestSafeCellRecursive(map, cell,
                        explRad, visited);
                if (recursiveResult.isPresent()) {
                    return recursiveResult;
                }
            }
        }

        return Optional.empty();
    }

    private boolean isValidCell(final GenPair<Integer, Integer> cell) {
        return cell.x() >= 0 && cell.y() >= 0 && cell.x() < Utils.MAP_COLS && cell.y() < Utils.MAP_ROWS;
    }

    private double calculateDistance(final GenPair<Integer, Integer> p1, final GenPair<Integer, Integer> p2) {
        final int dx = p2.x() - p1.x();
        final int dy = p2.y() - p1.y();
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Finds the nearest power-up within a limited radius from the enemy's current
     * coordinates.
     *
     * @param map        the game map
     * @param enemyCoord the current coordinates of the enemy
     * @return an {@code Optional<Pair>} containing the coordinates of the nearest
     *         power-up if found, otherwise an empty {@code Optional}
     */
    @Override
    public Optional<GenPair<Integer, Integer>> findNearestPowerUp(final GameMap map,
            final GenPair<Integer, Integer> enemyCoord) {
        final BreadthFirstIterator<GenPair<Integer, Integer>, DefaultWeightedEdge> bfsIterator = new BreadthFirstIterator<>(
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
    @Override
    public void updateGraph(final GameMap newMap) {
        final List<GenPair<Integer, Integer>> newWalls = newMap.getMap().keySet().stream()
                .filter(c -> newMap.isBreakableWall(c)).toList();
        if (oldWalls.size() != newWalls.size()) {
            oldWalls.stream().filter(c -> !newWalls.contains(c)).forEach(c -> updateEdges(c));
            oldWalls = new ArrayList<>(newWalls);
        }
    }

    private void updateEdges(final GenPair<Integer, Integer> cell) {
        graph.edgesOf(cell).forEach(e -> graph.setEdgeWeight(e, 1));
    }
}
