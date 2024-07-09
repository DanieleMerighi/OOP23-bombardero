package it.bombardero;

import it.unibo.bombardero.bomb.impl.BombFactoryImpl;
import it.unibo.bombardero.character.Character;
import it.unibo.bombardero.character.Character.CharacterType;
import it.unibo.bombardero.character.ai.api.EnemyState;
import it.unibo.bombardero.character.Enemy;
import it.unibo.bombardero.character.Player;
import it.unibo.bombardero.core.api.Controller;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.GenPair;
import it.unibo.bombardero.map.impl.GameMapImpl;
import it.unibo.bombardero.physics.api.CollisionEngine;
import it.unibo.bombardero.physics.impl.BombarderoCollision;
import it.unibo.bombardero.physics.impl.CollisionHandlerImpl;

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
    private final CollisionEngine cEngine;

    /**
     * Constructs a new {@link MyGameManager}.
     * Initializes the game map, player and enemy.
     */
    MyGameManager() {
        this.map = new GameMapImpl(false);
        this.enemy = new Enemy(new GenPair<Float, Float>(0f, 0f), new BombFactoryImpl());
        this.player = new Player(new GenPair<Float, Float>(0f, PLAYERY), new BombFactoryImpl());
        this.cEngine = new BombarderoCollision(new CollisionHandlerImpl());
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
     * add a power up to the map.
     * 
     * @param pos position
     */
    public void addPowerUp(final GenPair<Integer, Integer> pos) {
        map.addBreakableWall(pos);
        map.removeBreakableWall(pos);
    }

    /**
     * @return the CollisionEngine
     */
    public CollisionEngine getCollisionEngine() {
        return cEngine;
    }

    /**
     * Updates the game state.
     *
     * @param elapsed the elapsed time
     */
    @Override
    public void updateGame(final long elapsed, final Controller controller) {
        // 60 fps
        for (int i = 0; i < LENGHT; i++) {
            enemy.update(this, STANDARD_ELAPSED_TIME, CharacterType.ENEMY);
        }
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
        return map.getCopiedGameMap();
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

    /**
     * Gets the list of player.
     *
     * @return the list of player
     */
    @Override
    public List<Character> getPlayers() {
        return Arrays.asList(player);
    }

    /**
     * Sets the speed of the enemy.
     *
     * @param speed the speed to set for the enemy
     */
    public void setEnemySpeed(final float speed) {
        this.enemy.setSpeed(speed);
    }

    /**
     * Sets the number of bombs the enemy can place.
     *
     * @param num the number of bombs to set for the enemy
     */
    public void setEnemyBombs(final int num) {
        this.enemy.setNumBomb(num);
    }

    /**
     * Checks if the enemy's state is equal to another state.
     *
     * @param other the other state to compare with
     * @return true if the states are equal, false otherwise
     */
    public boolean isEnemyStateEqualTo(final EnemyState other) {
        return this.enemy.isStateEqualTo(other);
    }

    /**
     * Gets the integer coordinates of the enemy.
     *
     * @return the coordinates of the enemy as a GenPair
     */
    public GenPair<Integer, Integer> getEnemyCoord() {
        return this.enemy.getIntCoordinate();
    }

    /**
     * Gets the next move of the enemy.
     *
     * @return an Optional containing the next move of the enemy, or empty if there
     *         is no next move
     */
    public Optional<GenPair<Integer, Integer>> getEnemyNextMove() {
        return this.enemy.getNextMove();
    }

    /**
     * Updates the enemy state.
     */
    public void enemySingleUpdate() {
        this.enemy.update(this, STANDARD_ELAPSED_TIME, CharacterType.ENEMY);
    }

    /**
     * Gets the number of bombs the enemy can place.
     *
     * @return the number of bombs the enemy can place
     */
    public int getEnemyNumBombs() {
        return this.enemy.getNumBomb();
    }

    /**
     * Adds a breakable wall at the specified position.
     *
     * @param pos the position to add the breakable wall
     */
    public void addBreakableWall(final GenPair<Integer, Integer> pos) {
        this.map.addBreakableWall(pos);
    }
}
