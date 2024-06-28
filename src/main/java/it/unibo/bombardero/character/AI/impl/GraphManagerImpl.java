package it.unibo.bombardero.character.AI.impl;

import it.unibo.bombardero.map.api.GameMap;

/**
 * This class manages the enemy graph reasoner, which provides reasoning based on a game map.
 * It ensures that the graph reasoner is initialized and provides access to it with synchronization.
 */
public final class GraphManagerImpl {
    private static EnemyGraphReasonerImpl graphReasoner;
    private static long lastMapUpdateTime = -1;

    private GraphManagerImpl() {
    }

    /**
     * Initializes the graph reasoner with the provided game map if it has not been initialized already.
     * This method should be called once before accessing the graph reasoner.
     *
     * @param gameMap the game map to initialize the graph reasoner with
     */
    public static synchronized void initialize(final GameMap gameMap) {
        if (graphReasoner == null) {
            graphReasoner = new EnemyGraphReasonerImpl(gameMap);
        }
    }

    /**
     * Retrieves the initialized enemy graph reasoner.
     * Throws an {@link IllegalStateException} if the graph reasoner has not been initialized.
     * Updates the graph reasoner with the provided game map if the elapsed time since the last update
     * differs from the provided elapsed time.
     *
     * @param map        the game map used to update the graph reasoner
     * @param elapsedTime the elapsed time since the last update
     * @return the initialized enemy graph reasoner
     * @throws IllegalStateException if the graph reasoner is not initialized
     */
    public static EnemyGraphReasonerImpl getGraphReasoner(final GameMap map, final long elapsedTime) {
        if (graphReasoner == null) {
            throw new IllegalStateException("GraphManagerImpl is not initialized. Call initialize() first.");
        }
        if (elapsedTime != lastMapUpdateTime) {
            graphReasoner.updateGraph(map);
        }
        return graphReasoner;
    }
}

