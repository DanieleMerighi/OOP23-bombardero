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
    
    private final GameMap map;
    private final List<Character> enemies = new ArrayList<>();
    private final Character player;
    private final Controller controller;
    private final CollisionEngine ce;
    private final BombFactory bombFactory;
    private final BombarderoTimer gameTimer = new BombarderoTimerImpl();

    public BombarderoGameManager(final Controller ctrl){
        this.controller = ctrl;
        map = new GameMapImpl();
        ce = new BombarderoCollision(this);
        bombFactory = new BombFactoryImpl(this, ce);
        this.player = new Player(this, Utils.PLAYER_SPAWNPOINT, bombFactory);
        Utils.ENEMIES_SPAWNPOINT.forEach(enemyCoord -> enemies.add(new Enemy(this, enemyCoord, bombFactory)));
    }

    @Override
    public void updateGame() {
        map.update();
        if (player.isAlive()) {
            player.update();
        }
        enemies.forEach(enemy -> {
            if (enemy.isAlive()) {
                enemy.update();
            }
        });
        gameTimer.updateTimer();
    }

    @Override
    public void endGame() {
        controller.endGame();
    }

    @Override
    public void startTimer() {
        gameTimer.startTimer();
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
    
}
