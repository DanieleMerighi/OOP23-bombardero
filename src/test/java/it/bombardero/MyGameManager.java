package it.bombardero;

import it.unibo.bombardero.cell.Bomb;
import it.unibo.bombardero.cell.BombFactoryImpl;
import it.unibo.bombardero.cell.Flame.FlameType;
import it.unibo.bombardero.character.Character;
import it.unibo.bombardero.character.Enemy;
import it.unibo.bombardero.character.Player;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.Coordinates;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.IntPair;
import it.unibo.bombardero.map.impl.GameMapImpl;
import it.unibo.bombardero.physics.impl.RectangleBoundingBox;

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
    private static final int PLAYERY = 12;

    private Player player;
    private Enemy enemy;
    private GameMap map;

    /**
     * Constructs a new {@link MyGameManager}.
     * Initializes the game map, player and enemy.
     */
    MyGameManager() {
        this.map = new GameMapImpl(false);
        this.enemy = new Enemy(new Coordinates(0, 0), new BombFactoryImpl(), new RectangleBoundingBox(0, 0, Character.BOUNDING_BOX_WIDTH, Character.BOUNDING_BOX_HEIGHT));
        this.player = new Player(new Coordinates(0, PLAYERY), new BombFactoryImpl(), new RectangleBoundingBox(0, 0, Character.BOUNDING_BOX_WIDTH, Character.BOUNDING_BOX_HEIGHT));
    }

    /**
     * Sets the player's coordinates.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public void setPlayerCoord(final int x, final int y) {
        player.setCharacterPosition(new Coordinates(x + CENTER, y + CENTER));
    }

    /**
     * Sets the enemy's coordinates.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public void setEnemyCoord(final int x, final int y) {
        enemy.setCharacterPosition(new Coordinates(x + CENTER, y + CENTER));
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
            enemy.update(STANDARD_ELAPSED_TIME, this);
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
     * Adds a bomb to the game map.
     *
     * @param b the bomb
     * @return true if the bomb was added, false otherwise
     */
    @Override
    public boolean addBomb(final Bomb b, final Character character) {
        return map.addBomb(b, b.getPos());
    }

    /**
     * Removes a bomb from the game map.
     *
     * @param pos the position of the bomb
     */
    @Override
    public void removeBomb(final IntPair pos) {
        throw new UnsupportedOperationException("Unimplemented method 'removeBomb'");
    }

    /**
     * Adds a flame to the game map.
     *
     * @param type the type of flame
     * @param pos the position of the flame
     */
    @Override
    public void addFlame(final FlameType type, final IntPair pos) {
        throw new UnsupportedOperationException("Unimplemented method 'addFlame'");
    }

    /**
     * Removes a flame from the game map.
     *
     * @param pos the position of the flame
     */
    @Override
    public void removeFlame(final IntPair pos) {
        throw new UnsupportedOperationException("Unimplemented method 'removeFlame'");
    }

    /**
     * Removes a wall from the game map.
     *
     * @param pos the position of the wall
     * @return true if the wall was removed, false otherwise
     */
    @Override
    public boolean removeWall(final IntPair pos) {
        throw new UnsupportedOperationException("Unimplemented method 'removeWall'");
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
     * Removes a power-up from the game map.
     *
     * @param pos the position of the power-up
     */
    @Override
    public void removePowerUp(final IntPair pos) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removePowerUp'");
    }

    /**
     * Gets a bomb from the game map.
     *
     * @param pos the position of the bomb
     * @return an optional containing the bomb, or an empty optional if no bomb is found
     */
    @Override
    public Optional<Bomb> getBomb(final IntPair pos) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBomb'");
    }
}
