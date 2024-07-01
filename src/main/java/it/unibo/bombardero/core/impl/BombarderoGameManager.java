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
import it.unibo.bombardero.guide.api.GuideManager;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.map.impl.GameMapImpl;
import it.unibo.bombardero.physics.api.CollisionEngine;
import it.unibo.bombardero.physics.impl.BombarderoCollision;
import it.unibo.bombardero.utils.Utils;
import it.unibo.bombardero.character.Character;
import it.unibo.bombardero.character.Enemy;
import it.unibo.bombardero.character.Player;

public class BombarderoGameManager implements GameManager {

    public final static long TOTAL_GAME_TIME = 120000L;
    public final static long GAME_OVER_TIME = 0L;
    
    private final GameMap map;
    private List<Bomb> boombs = new ArrayList<>();
    private List<Flame> flames = new ArrayList<>();
    private final List<Character> enemies = new ArrayList<>();
    private final Character player;
    private final Controller controller;
    private final CollisionEngine ce;
    private final BombFactory bombFactory;
    private long gameTime = 0;

    public BombarderoGameManager(final Controller ctrl){
        this.controller = ctrl;
        map = new GameMapImpl();
        ce = new BombarderoCollision();
        bombFactory = new BombFactoryImpl(this);
        this.player = new Player(this, Utils.PLAYER_SPAWNPOINT, bombFactory);
        enemies.add(new Enemy(this, Utils.ENEMIES_SPAWNPOINT.get(0), bombFactory));
        //Utils.ENEMIES_SPAWNPOINT.forEach(enemyCoord -> enemies.add(new Enemy(this, enemyCoord, bombFactory)));
    }

    public BombarderoGameManager(final Controller controller, final boolean guideMode) {
        this.controller = controller;
        map = new GameMapImpl(false);
        ce = new BombarderoCollision();
        bombFactory = new BombFactoryImpl(this);
        this.player = new Player(this, GuideManager.PLAYER_GUIDE_SPAWNPOINT, bombFactory);
        this.map.addBreakableWall(GuideManager.CRATE_GUIDE_SPAWNPOINT);
    }

    @Override
    public void updateGame(final long elapsed) {
        gameTime += elapsed;
        map.update(getTimeLeft());
        if (player.isAlive()) {
            player.update(elapsed);
            ce.checkCharacterCollision(player, this);
            ce.checkFlameAndPowerUpCollision(player, this);
        }
        if(!boombs.isEmpty()) {
            boombs.forEach(b->b.update());
            boombs.removeIf(b->b.isExploded());
        }
        if(!flames.isEmpty()) {
            flames.forEach(f->f.update(elapsed));
            flames.removeIf(f->f.isExpired());
        }
        enemies.forEach(enemy -> {
             if (enemy.isAlive()) {
                 enemy.update(elapsed);
                 ce.checkCharacterCollision(enemy, this);
                 ce.checkCharacterCollision(enemy, this);
             }
        });
        /*if(enemies.get(0).isAlive()){
            enemies.get(0).update(elapsed);
        }*/
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
    public Optional<Bomb> getBomb(Pair pos) {
        return boombs.stream().filter(b-> b.getPos().equals(pos)).findAny();
    }

    @Override
    public void removePowerUp(final Pair pos) {
        map.removePowerUp(pos);
    }

    @Override
    public void addFlame(final Flame.FlameType type, final Pair pos) {
        Flame f = new Flame(CellType.FLAME, type, pos , this);
        flames.add(f);
        map.addFlame(f, pos);
    }

    @Override
    public void removeFlame(final Pair pos) {
        map.removeFlame(pos);
    }

    @Override
    public boolean removeWall(final Pair pos) {
        if(map.isBreakableWall(pos)) {
            map.removeBreakableWall(pos);
            return true;
        }
        return false;
    }

    @Override
    public long getTimeLeft() {
        return gameTime < TOTAL_GAME_TIME ? TOTAL_GAME_TIME - gameTime : GAME_OVER_TIME;
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
