package it.unibo.bombardero.character.AI;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.traverse.BreadthFirstIterator;

import java.util.*;
import java.util.stream.*;

import it.unibo.bombardero.character.Direction;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.utils.Utils;

public class EnemyGraphReasoner {

    private GameMap map;
    final private Graph<Pair, DefaultWeightedEdge> graph;

    public EnemyGraphReasoner(GameMap map) {
        this.map = map;
        this.graph = GraphBuilder.buildFromMap(map);
    }

    public boolean isInDangerZone(Pair enemyCoord, int explRadius) {
        final BreadthFirstIterator<Pair, DefaultWeightedEdge> bfsIterator = new BreadthFirstIterator<>(graph,
                enemyCoord);
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(bfsIterator, Spliterator.ORDERED), false)
                .takeWhile(cell -> bfsIterator.getDepth(cell) <= explRadius) // Limit traversal to explosion radius
                .anyMatch(cell -> map.isBomb(cell) && !isPathBlockedByWalls(enemyCoord, cell));
    }

    public boolean isPathBlockedByWalls(Pair startCell, Pair endCell) {
        if (startCell.row() != endCell.row() && startCell.col() != endCell.col()) {
            return false; // Diagonal paths not supported
        }

        int dx = endCell.row() - startCell.row();
        int dy = endCell.col() - startCell.col();

        return IntStream.rangeClosed(0, Math.max(Math.abs(dx), Math.abs(dy)))
                .mapToObj(i -> {
                    int x = startCell.row() + i * Integer.signum(dx);
                    int y = startCell.col() + i * Integer.signum(dy);
                    return new Pair(x, y);
                })
                .anyMatch(p -> map.isBreakableWall(p) || map.isUnbreakableWall(p));
    }

    public List<Pair> findShortestPathToPlayer(Pair enemyCoord, Pair playerCoord) {
        // Use Dijkstra's algorithm to find the shortest path to the player
        DijkstraShortestPath<Pair, DefaultWeightedEdge> dijkstra = new DijkstraShortestPath<>(graph);
        GraphPath<Pair, DefaultWeightedEdge> path = dijkstra.getPath(enemyCoord, playerCoord);
        return path == null ? Collections.emptyList() : path.getVertexList().subList(1, path.getVertexList().size());
    }

    public Optional<Pair> findNearestSafeSpace(Pair enemyCoord, int explRad) {
        Optional<Pair> nearestBomb = findNearestBomb(enemyCoord);
        if (nearestBomb.isEmpty()) {
            return Optional.empty();
        }

        return Arrays.stream(Direction.values())
                .filter(direction -> !direction.equals(getDirectionToCell(enemyCoord, nearestBomb.get())))
                .map(direction -> new Pair(enemyCoord.row() + direction.getDx(), enemyCoord.col() + direction.getDy()))
                .filter(potentialSafeCell -> isValidCell(potentialSafeCell)
                        && !isInDangerZone(potentialSafeCell, explRad))
                .findFirst();
    }

    public Optional<Pair> findNearestBomb(Pair enemyCoord) {
        final BreadthFirstIterator<Pair, DefaultWeightedEdge> bfsIterator = new BreadthFirstIterator<>(graph, enemyCoord);
    
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(bfsIterator, Spliterator.ORDERED), false)
                .filter(cell -> map.isBomb(cell) && !isPathBlockedByWalls(enemyCoord, cell)) // Find reachable bombs
                .findFirst();
    }

    private Direction getDirectionToCell(Pair fromCell, Pair toCell) {
        int dRow = toCell.row() - fromCell.row();
        int dCol = toCell.col() - fromCell.col();
        // Check for horizontal or vertical movement
        if (dRow != 0 && dCol == 0) {
            return dRow > 0 ? Direction.DOWN : Direction.UP;
        } else if (dRow == 0 && dCol != 0) {
            return dCol > 0 ? Direction.RIGHT : Direction.LEFT;
        }

        return null;
    }

    private boolean isValidCell(Pair cell) {
        return cell.row() >= 0 && cell.row() < Utils.MAP_ROWS && cell.col() >= 0 && cell.col() < Utils.MAP_COLS;
    }
}
