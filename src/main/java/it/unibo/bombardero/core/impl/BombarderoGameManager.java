package it.unibo.bombardero.core.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import edu.umd.cs.findbugs.annotations.OverrideMustInvoke;
import it.unibo.bombardero.cell.BasicBomb;
import it.unibo.bombardero.cell.BombFactory;
import it.unibo.bombardero.cell.BombFactoryImpl;
import it.unibo.bombardero.cell.Flame;
import it.unibo.bombardero.core.api.Controller;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.guide.impl.BombarderoGuideManager;
import it.unibo.bombardero.map.api.BombarderoTimer;
import it.unibo.bombardero.map.api.Coord;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.map.impl.BombarderoTimerImpl;
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
    private final List<Character> enemies = new ArrayList<>();
    private final Character player;
    private final Controller controller;
    private final CollisionEngine ce;
    private final BombFactory bombFactory;
    private long gameTime = 0;

    public BombarderoGameManager(final Controller ctrl){
        this.controller = ctrl;
        map = new GameMapImpl();
        ce = new BombarderoCollision(this);
        bombFactory = new BombFactoryImpl(this, ce);
        this.player = new Player(this, Utils.PLAYER_SPAWNPOINT, bombFactory);
        Utils.ENEMIES_SPAWNPOINT.forEach(enemyCoord -> enemies.add(new Enemy(this, enemyCoord, bombFactory)));
    }

    public BombarderoGameManager(final Controller controller, final boolean guideMode) {
        this.controller = controller;
        map = new GameMapImpl(false);
        ce = new BombarderoCollision(this);
        bombFactory = new BombFactoryImpl(this, ce);
        /* TODO: CHANGE PLAYER SPAWNPOINT IN MIDDLE OF MAP */
        this.player = new Player(this, BombarderoGuideManager.PLAYER_GUIDE_SPAWNPOINT, bombFactory);
        this.map.addBreakableWall(BombarderoGuideManager.CRATE_GUIDE_SPAWNPOINT);
        /* TODO: enemies.add(new Player(this, , bombFactory)); */ 
    }

    @Override
    public void updateGame(final long elapsed) {
        gameTime += elapsed;
        /* TODO: CAPIRE COME FARE A FARE COLLASSO DELLA MAPPA IN GAME MA NON IN GUIDE */
        map.update();
        if (player.isAlive()) {
            player.update(elapsed);
        }
        // enemies.forEach(enemy -> {
        //     if (enemy.isAlive()) {
        //         enemy.update(elapsed);
        //     }
        // });
        if(enemies.get(0).isAlive()){
            System.out.println(enemies.get(0).getCharacterPosition());
            enemies.get(0).update(elapsed);
        }
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
    public boolean addBomb(final BasicBomb bomb) {
        return map.addBomb(bomb, bomb.getPos());
    }

    @Override
    public void removeBomb(final Pair pos) {
        map.removeBomb(pos);
    }

    @Override
    public void addFlame(final Flame.FlameType type, final Pair pos) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addFlame'");
    }

    @Override
    public void removeFlame(final Pair pos) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeFlame'");
    }

    @Override
    public boolean removeWall(final Pair pos) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeWall'");
    }

    @Override
    public long getTimeLeft() {
        return gameTime < TOTAL_GAME_TIME ? TOTAL_GAME_TIME - gameTime : GAME_OVER_TIME;
    }
    
}
