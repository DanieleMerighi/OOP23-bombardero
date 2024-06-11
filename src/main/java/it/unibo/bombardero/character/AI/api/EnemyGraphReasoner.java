package it.unibo.bombardero.character.AI.api;

import it.unibo.bombardero.map.api.Pair;
import java.util.List;
import java.util.Optional;

/**
 * This interface defines the functionalities for pathfinding and danger zone analysis for enemies
 * within the game map. It utilizes the JGraphT library to represent the game map as a graph
 * and performs graph traversals and shortest path calculations.
 */
public interface EnemyGraphReasoner {
    boolean isInDangerZone(Pair enemyCoord, int explRadius);

    boolean isPathBlockedByWalls(Pair startCell, Pair endCell);

    List<Pair> findShortestPathToPlayer(Pair enemyCoord, Pair playerCoord);

    Optional<Pair> findNearestSafeCell(Pair enemyCoord, int explRadius);

    Optional<Pair> findNearestBomb(Pair enemyCoord);
}

