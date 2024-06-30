package it.unibo.bombardero.core.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import it.unibo.bombardero.cell.Bomb;
import it.unibo.bombardero.cell.BombFactory;
import it.unibo.bombardero.cell.BombFactoryImpl;
import it.unibo.bombardero.cell.Cell.CellType;
import it.unibo.bombardero.cell.Flame;
import it.unibo.bombardero.core.api.Controller;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.Coord;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.map.impl.GameMapImpl;
import it.unibo.bombardero.physics.api.CollisionEngine;
import it.unibo.bombardero.physics.impl.BombarderoCollision;
import it.unibo.bombardero.character.Character;
import it.unibo.bombardero.character.Enemy;
import it.unibo.bombardero.character.Player;

/**
 * This class implements the concepts expressed in the 
 * {@link GameManager} interface. It serves as the main
 * class of the model, coordinating everything that happens
 * within the model such as the end of the game and the end of
 * the time. 
 */
public class BasicBombarderoGameManager implements GameManager {

    public final static long TOTAL_GAME_TIME = 120_000L;
    public final static long GAME_OVER_TIME = 0L;
    
    private final GameMap map;
    private final List<Bomb> boombs = new ArrayList<>();
    private final List<Flame> flames = new ArrayList<>();
    private final List<Character> enemies = new ArrayList<>();
    private final Character player;
    private final Controller controller;
    private final CollisionEngine ce;
    private final BombFactory bombFactory;

    public BasicBombarderoGameManager(
            final Controller controller,
            final Coord playerSpawnPoint, 
            final List<Coord> enemiesSpawnpoint,
            final boolean generateWalls) {
        this.controller = controller;
        map = new GameMapImpl(generateWalls);
        ce = new BombarderoCollision(this);
        bombFactory = new BombFactoryImpl(this);
        this.player = new Player(this, playerSpawnPoint, bombFactory);
        enemiesSpawnpoint.forEach(spawnpoint -> enemies.add(new Enemy(this, spawnpoint, bombFactory)));
    }

    /**
     * Updates the synchronous and asynchronous objects of the game, namely:
     * <ul>
     *  <li> The map
     *  <li> The main character
     *  <li> The bombs
     *  <li> The flames
     *  <li> The enemies 
     * </ul> 
     * The argument passed can be passed to the synchronous entities
     * to synchronize them to the game's time.
     * @param elapsed the time passed since the last update. 
     */
    @Override
    public void updateGame(final long elapsed) {
        //map.update(getTimeLeft());
        if (player.isAlive()) {
            player.update(elapsed);
            ce.checkCharacterCollision(player);
            ce.checkFlameAndPowerUpCollision(player);
        }
        if (!boombs.isEmpty()) {
            boombs.forEach(b -> b.update());
            boombs.removeIf(b -> b.isExploded());
        }
        if (!flames.isEmpty()) {
            flames.forEach(f -> f.update(elapsed));
            flames.removeIf(f -> f.isExpired());
        }
        enemies.forEach(enemy -> {
             if (enemy.isAlive()) {
                 enemy.update(elapsed);
                 ce.checkCharacterCollision(enemy);
                 ce.checkFlameAndPowerUpCollision(enemy);
             }
        });
    }

    @Override
    public void endGame() {
        controller.endGame();
    }

    @Override
    public List<Character> getEnemies() {
        return List.copyOf(enemies);
    }

    @Override
    public GameMap getGameMap() {
        return this.map;
    }

    @Override
    public Character getPlayer() {
        return this.player;
    }

    @Override
    public boolean addBomb(final Bomb bomb) {
        if (map.addBomb(bomb, bomb.getPos())) { // If the bomb is added to the map
            boombs.add(bomb); // The bomb is added to the list
            return true;
        }
        return false;
    }

    @Override
    public void removeBomb(final Pair pos) {
        map.removeBomb(pos);
    }

    @Override
    public Optional<Bomb> getBomb(final Pair pos) {
        return boombs.stream().filter(b -> b.getPos().equals(pos)).findAny();
    }

    @Override
    public void removePowerUp(final Pair pos) {
        map.removePowerUp(pos);
    }

    @Override
    public void addFlame(final Flame.FlameType type, final Pair pos) {
        final Flame f = new Flame(CellType.FLAME, type, pos , this);
        flames.add(f);
        map.addFlame(f, pos);
    }

    @Override
    public void removeFlame(final Pair pos) {
        map.removeFlame(pos);
    }

    @Override
    public boolean removeWall(final Pair pos) {
        if (map.isBreakableWall(pos)) {
            map.removeBreakableWall(pos);
            return true;
        }
        return false;
    }

    /**
     * If the time is being kept it returns the time passed. 
     */
    @Override
    public Optional<Long> getTimeLeft() {
        return Optional.empty();
    }

    protected void addEnemy(final Character enemy) {
        this.enemies.add(enemy);
    }

    protected BombFactory getBombFactory() {
        return this.bombFactory;
    }

    protected Controller getController() {
        return this.controller;
    }
    
}
