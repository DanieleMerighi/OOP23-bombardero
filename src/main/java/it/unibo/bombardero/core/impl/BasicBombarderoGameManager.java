package it.unibo.bombardero.core.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import it.unibo.bombardero.cell.Bomb;
import it.unibo.bombardero.cell.BombFactory;
import it.unibo.bombardero.cell.BombFactoryImpl;
import it.unibo.bombardero.cell.Cell;
import it.unibo.bombardero.cell.FlameImpl;
import it.unibo.bombardero.character.Character;
import it.unibo.bombardero.character.Enemy;
import it.unibo.bombardero.character.Player;
import it.unibo.bombardero.core.api.Controller;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.GenPair;
import it.unibo.bombardero.map.impl.GameMapImpl;
import it.unibo.bombardero.physics.api.CollisionEngine;
import it.unibo.bombardero.physics.impl.BombarderoCollision;

/**
 * This class implements the concepts expressed in the
 * {@link GameManager} interface. This class is designed to
 * be extended, adding further capabilities such as time-keeping and
 * advanced game dynamics.
 */
public class BasicBombarderoGameManager implements GameManager {

    public static final long TOTAL_GAME_TIME = 120_000L;
    public static final long GAME_OVER_TIME = 0L;

    private final GameMap map;
    private final Map<Bomb, Character> boombs = new HashMap<>();
    private final List<FlameImpl> flames = new ArrayList<>();
    private final List<Character> enemies = new ArrayList<>();
    private final Character player;
    private final Controller controller;
    private final CollisionEngine ce;
    private final BombFactory bombFactory;

    /**
     * Creates a new Game Manager, creating all the model's entities. Spawning
     * the player and the enemies in the request positions.
     * <p>
     * A enemy for each element of the list will be spawned.
     * 
     * @param controller        the game's controller
     * @param playerSpawnPoint  the main player's spawnpoint
     * @param enemiesSpawnpoint a list of the enemies spawnpoints
     * @param generateWalls     wether the breakable walls have to be generated or not
     */
    public BasicBombarderoGameManager(
            final Controller controller,
            final GenPair<Float, Float> playerSpawnPoint,
            final List<GenPair<Float, Float>> enemiesSpawnpoint,
            final boolean generateWalls) {
        this.controller = controller;
        map = new GameMapImpl(generateWalls);
        ce = new BombarderoCollision();
        bombFactory = new BombFactoryImpl();
        this.player = new Player(playerSpawnPoint, bombFactory);
        enemiesSpawnpoint.forEach(spawnpoint -> enemies.add(new Enemy(spawnpoint, bombFactory)));
    }

    /**
     * Updates the synchronous and asynchronous objects of the game, namely:
     * <ul>
     * <li>The map
     * <li>The main character
     * <li>The enemies
     * <li>The bombs
     * <li>The flames
     * </ul>
     * The argument passed can be passed to the synchronous entities
     * to synchronize them to the game's time.
     * 
     * @param elapsed the time passed since the last update.
     */
    @Override
    public void updateGame(final long elapsed) {
        if (player.isAlive()) {
            player.update(this, elapsed);
            addCharacterBombsToMap(player);
            ce.checkCharacterCollision(player, this.getGameMap());
            ce.checkFlameAndPowerUpCollision(player, this.getGameMap());
        }
        enemies.forEach(enemy -> {
            if (enemy.isAlive()) {
                enemy.update(this, elapsed);
                addCharacterBombsToMap(enemy);
                ce.checkCharacterCollision(enemy, this.getGameMap());
                ce.checkFlameAndPowerUpCollision(enemy, this.getGameMap());
            }
        });
        if (!boombs.isEmpty()) {
            boombs.entrySet().forEach(entry -> entry.getKey().update());
            placeBombExplosion();
        }
        if (!flames.isEmpty()) {
            flames.forEach(f -> f.update(elapsed));
            List.copyOf(flames)
                .stream()
                .filter(f -> f.isExpired())
                .peek(f -> flames.remove(f))
                .forEach(f -> map.removeFlame(f.getPos()));
        }
    }

    private void addCharacterBombsToMap(final Character character) {
        character.getBombsToBePlaced()
            .stream()
            .filter(bomb -> map.addBomb(bomb, bomb.getPos()))
            .forEach(bomb -> boombs.put(bomb, character));
        character.resetBombsList();
    }

    @Override
    public final void endGame() {
        controller.endGame();
    }

    @Override
    public final List<Character> getEnemies() {
        return List.copyOf(enemies);
    }

    @Override
    public final GameMap getGameMap() {
        return this.map;
    }

    @Override
    public final Character getPlayer() {
        return this.player;
    }
    /*
     * @Override
     * public boolean addBomb(final Bomb bomb, final Character character) {
     * if (map.addBomb(bomb, bomb.getPos())) { // If the bomb is added to the map
     * boombs.put(bomb, character); // The bomb is added to the Map bombs
     * return true;
     * }
     * return false;
     * }
     * 
     * @Override
     * public void removeBomb(final GenPair<Integer, Integer> pos) {
     * map.removeBomb(pos);
     * }
     * 
     * @Override
     * public Optional<Bomb> getBomb(final GenPair<Integer, Integer> pos) {
     * return boombs.entrySet().stream().map(entry -> entry.getKey()).filter(b ->
     * b.getPos().equals(pos)).findAny();
     * }
     * 
     * @Override
     * public void removePowerUp(final GenPair<Integer, Integer> pos) {
     * map.removePowerUp(pos);
     * }
     * 
     * @Override
     * public void addFlame(final FlameImpl.FlameType type, final GenPair<Integer,
     * Integer> pos) {
     * final FlameImpl f = new FlameImpl(CellType.FLAME, type, pos);
     * flames.add(f);
     * map.addFlame(f, pos);
     * }
     * 
     * @Override
     * public void removeFlame(final GenPair<Integer, Integer> pos) {
     * map.removeFlame(pos);
     * }
     * 
     * @Override
     * public boolean removeWall(final GenPair<Integer, Integer> pos) {
     * if (map.isBreakableWall(pos)) {
     * map.removeBreakableWall(pos);
     * return true;
     * }
     * return false;
     * }
     */

    /**
     * If the time is being kept it returns the time passed. This implementation
     * does
     * not keep the game time, therefore returns an empty {@link Optional}.
     */
    @Override
    public Optional<Long> getTimeLeft() {
        return Optional.empty();
    }

    protected final void addEnemy(final Character enemy) {
        this.enemies.add(enemy);
    }

    protected BombFactory getBombFactory() {
        return this.bombFactory;
    }

    private void placeBombExplosion() {
        Map.copyOf(boombs).entrySet().stream()
                .filter(entry -> entry.getKey().isExploded())
                .peek(entry -> boombs.remove(entry.getKey()))
                .peek(entry -> entry.getValue().removeBombFromDeque(entry.getKey()))
                .map(entry -> entry.getKey().computeFlame(this.getGameMap()))
                .forEach(set -> set.forEach(entry -> map.addFlame(
                        new FlameImpl(Cell.CellType.FLAME, entry.getValue(), entry.getKey()), entry.getKey())));
    }

}
