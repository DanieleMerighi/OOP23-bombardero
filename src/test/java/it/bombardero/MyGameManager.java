package it.bombardero;

import it.unibo.bombardero.cell.BombFactoryImpl;
import it.unibo.bombardero.character.Character;
import it.unibo.bombardero.character.Enemy;
import it.unibo.bombardero.character.Player;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.GenPair;
import it.unibo.bombardero.map.impl.GameMapImpl;

import java.util.List;
import java.util.Optional;
import java.util.Arrays;

/**
 * A Modified manager for testing purpose.
 */
public class MyGameManager implements GameManager {

    private static final int STANDARD_ELAPSED_TIME = 100;
    private static final float CENTER = 0.5f;
    private static final int LENGHT = 60;
    private static final float PLAYERY = 12f;

    private final Player player;
    private final Enemy enemy;
    private final GameMap map;

    /**
     * Constructs a new {@link MyGameManager}.
     * Initializes the game map, player and enemy.
     */
    MyGameManager() {
        this.map = new GameMapImpl(false);
        this.enemy = new Enemy(new GenPair<Float, Float>(0f, 0f), new BombFactoryImpl());
        this.player = new Player(new GenPair<Float, Float>(0f, PLAYERY), new BombFactoryImpl());
    }

    /**
     * Sets the player's coordinates.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public void setPlayerCoord(final int x, final int y) {
        player.setCharacterPosition(new GenPair<Float, Float>(x + CENTER, y + CENTER));
    }

    /**
     * Sets the enemy's coordinates.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public void setEnemyCoord(final int x, final int y) {
        enemy.setCharacterPosition(new GenPair<Float, Float>(x + CENTER, y + CENTER));
    }

    /**
     * Add a bomb in the map.
     * 
     * @param pos the position of the bomb
     */
    public void addBomb(final GenPair<Integer, Integer> pos) {
        this.map.addBomb(new MyBomb(pos), pos);
    }

    /**
     * Gets the player.
     *
     * @return the player
     */
    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * Updates the game state.
     *
     * @param elapsed the elapsed time
     */
    @Override
    public void updateGame(final long elapsed) {
        // 60 fps
        for (int i = 0; i < LENGHT; i++) {
            enemy.update(this, STANDARD_ELAPSED_TIME);
        }
    }

    /**
     * Gets the enemy.
     *
     * @return the enemy
     */
    public Enemy getEnemy() {
        return this.enemy;
    }

    /**
     * Ends the game.
     */
    @Override
    public void endGame() {
        throw new UnsupportedOperationException("Unimplemented method 'endGame'");
    }

    /**
     * Gets the list of enemies.
     *
     * @return the list of enemies
     */
    @Override
    public List<Character> getEnemies() {
        return Arrays.asList(enemy);
    }

    /**
     * Gets the game map.
     *
     * @return the game map
     */
    @Override
    public GameMap getGameMap() {
        return map;
    }

    /**
     * Gets the time left in the game.
     *
     * @return the time left
     */
    @Override
    public Optional<Long> getTimeLeft() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTimeLeft'");
    }
}
