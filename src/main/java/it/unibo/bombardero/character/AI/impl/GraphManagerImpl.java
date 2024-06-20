package it.unibo.bombardero.character.AI.impl;

import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.Pair;

public class GraphManagerImpl {
    private static EnemyGraphReasonerImpl graphReasoner;
    private static long lastMapUpdateTime = -1;

    private GraphManagerImpl() {
    }

    public static synchronized void initialize(GameMap gameMap) {
        if (graphReasoner == null) {
            graphReasoner = new EnemyGraphReasonerImpl(gameMap);
        }
    }

    public static EnemyGraphReasonerImpl getGraphReasoner(GameMap map, long elapsedTime) {
        if (graphReasoner == null) {
            throw new IllegalStateException("GraphManagerImpl is not initialized. Call initialize() first.");
        }
        if (elapsedTime != lastMapUpdateTime) {
            graphReasoner.updateGraph(map);
        }
        return graphReasoner;
    }
}

