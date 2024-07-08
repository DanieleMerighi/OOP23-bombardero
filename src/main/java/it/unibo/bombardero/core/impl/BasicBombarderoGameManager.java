package it.unibo.bombardero.core.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import it.unibo.bombardero.bomb.api.Bomb;
import it.unibo.bombardero.bomb.api.BombFactory;
import it.unibo.bombardero.bomb.impl.BombFactoryImpl;
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
import it.unibo.bombardero.view.api.GraphicsEngine.EndGameState;

/**
 * This class implements the concepts expressed in the
 * {@link GameManager} interface. This class is designed to
 * be extended, adding further capabilities such as time-keeping and
 * advanced game dynamics.
 */
public class BasicBombarderoGameManager implements GameManager {

    /**
     * The total length in time of one match: 2 minutes. 
     */
    public static final long TOTAL_GAME_TIME = 20_000L;
    public static final long GAME_OVER_TIME = 0L;

    private final GameMap map;
    private final Map<Bomb, Character> bombs = new HashMap<>();
    private final List<FlameImpl> flames = new ArrayList<>();
    private final List<Character> enemies = new ArrayList<>();
    private final List<Character> players;
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
            final boolean generateWalls,
            final CollisionEngine cEngine) {
        this.map = new GameMapImpl(generateWalls);
        this.ce = cEngine;
        this.bombFactory = new BombFactoryImpl();
        this.players = List.of(new Player(playerSpawnPoint, bombFactory));
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
    public void updateGame(final long elapsed, final Controller controller) {
        updatePlayer(elapsed);
        updateEnemies(elapsed);
        updateBombs(elapsed);
        updateFlames(elapsed);
        updateMap();
        if (!players.stream().allMatch(Character::isAlive)) {
            controller.displayEndScreen(EndGameState.LOSE);
        } else if ( !enemies.stream().allMatch(Character::isAlive)) {
            controller.displayEndScreen(EndGameState.WIN);
        }
    }

    @Override
    public final List<Character> getEnemies() {
        return List.copyOf(enemies);
    }

    @Override
    public final GameMap getGameMap() {
        return map.getCopiedGameMap();
    }

    @Override
    public final List<Character> getPlayers() {
        return List.copyOf(players);
    }

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

    protected final BombFactory getBombFactory() {
        return this.bombFactory;
    }

    protected final void triggetCollapse() {
        map.triggerCollapse();
    }

    private void updateFlames(final long elapsed) {
        if (!flames.isEmpty()) {
            flames.forEach(f -> f.update(elapsed));
            flames.stream()
                .filter(f -> f.isExpired())
                .forEach(f -> map.removeFlame(f.getPos()));
            flames.removeIf(f -> f.isExpired());
        }
    }

    private void updateBombs(final long elapsed) {
        if (!bombs.isEmpty()) {
            bombs.entrySet().forEach(entry -> entry.getKey().update(elapsed));
            placeBombExplosion();
        }
    }

    private void updateEnemies(final long elapsed) {
        enemies.forEach(enemy -> {
            if (enemy.isAlive()) {
                enemy.update(this, elapsed);
                addCharacterBombsToMap(enemy);
                ce.checkCharacterCollision(enemy, map);
                ce.checkFlameAndPowerUpCollision(enemy, map);
            }
        });
    }

    private void updatePlayer(final long elapsed) {
        players.stream()
        .filter(Character::isAlive)
        .forEach(player -> {
            player.update(this, elapsed);
            addCharacterBombsToMap(player);
            ce.checkCharacterCollision(player, map);
            ce.checkFlameAndPowerUpCollision(player, map);
        });
    }

    private void updateMap() {
        map.update();
    }

    private void addCharacterBombsToMap(final Character character) {
        character.getBombsToBePlaced()
            .stream()
            .filter(bomb -> map.addBomb(bomb, bomb.getPos()))
            .forEach(bomb -> bombs.put(bomb, character));
        character.resetBombsList();
    }

    private void placeBombExplosion() {
        Map.copyOf(bombs).entrySet().stream()
                .filter(entry -> entry.getKey().isExploded())
                .peek(entry -> bombs.remove(entry.getKey()))
                .peek(entry -> entry.getValue().removeBombFromDeque(entry.getKey()))
                .map(entry -> entry.getKey().computeFlame(map))
                .forEach(set -> set.stream()
                    .map(entry -> new FlameImpl(entry.getValue(), entry.getKey()))
                    .peek(flame -> flames.add(flame))
                    .forEach(flame -> map.addFlame(flame, flame.getPos())));
    }

}
