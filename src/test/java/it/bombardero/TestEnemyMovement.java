package it.bombardero;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.utils.Utils;
import it.unibo.bombardero.character.Enemy;

public class TestEnemyMovement {

    private int[][] baseMap;
    private Enemy enemy;

    @BeforeEach 
    void initBaseMap() {
        baseMap = new int[][]{
            {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
            {2, 4, 1, 1, 1, 5, 1, 1, 1, 3, 1, 1, 2},
            {2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2},
            {2, 3, 3, 3, 1, 1, 1, 1, 1, 3, 1, 1, 2},
            {2, 3, 2, 1, 5, 3, 2, 1, 2, 1, 2, 1, 2},
            {2, 1, 1, 3, 1, 3, 1, 1, 1, 1, 1, 3, 2},
            {2, 1, 2, 3, 2, 1, 2, 1, 2, 1, 2, 1, 2},
            {2, 1, 1, 3, 1, 1, 1, 1, 1, 3, 3, 1, 2},
            {2, 1, 2, 3, 2, 1, 2, 1, 2, 1, 2, 1, 2},
            {2, 1, 3, 3, 3, 1, 1, 3, 1, 1, 1, 1, 2},
            {2, 1, 2, 1, 2, 1, 2, 3, 2, 1, 2, 1, 2},
            {2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
            {2, 3, 2, 1, 2, 1, 2, 3, 2, 1, 2, 1, 2},
            {2, 3, 1, 3, 1, 1, 3, 3, 3, 1, 3, 3, 2},
            {2, 1, 2, 1, 2, 1, 2, 3, 2, 3, 2, 3, 2},
            {2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
            {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2}
        };
        enemy = new Enemy(new Pair(1, 5), 15, 15); // Example starting position
        enemy.setMap(baseMap);
    }

    @Test
    public void testEnemyChasesPlayer() {
        enemy.update(); // Trigger enemy update
        assertEquals(Enemy.State.CHASE, enemy.getState(), "Enemy should enter CHASE state");
        enemy.update(); // Trigger enemy update
        assertTrue(enemy.getPath().size() > 0, "Enemy should calculate a path to the player");
        assertEquals(new Pair(1, 4), enemy.getNextMove().get(), "Enemy should get closer to the player");
        enemy.update(); // Trigger enemy update
        assertEquals(new Pair(1, 3), enemy.getNextMove().get(), "Enemy should get closer to the player");
    }

    @Test
    public void testEnemyPlacesBombWhenBlocked() {
        // Set up enemy and player positions within blocks
        baseMap[1][3] = Utils.WALL;
        enemy.setMap(baseMap);
        enemy.update(); // Trigger enemy update

        Pair bombPlacementCell = null;
        // Search for a bomb placed
        

        assertNotNull(bombPlacementCell, "Enemy should place a bomb to clear a path");
    }
    
}
