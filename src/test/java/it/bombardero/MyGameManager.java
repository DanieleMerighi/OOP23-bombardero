package it.bombardero;

import it.unibo.bombardero.cell.Bomb;
import it.unibo.bombardero.cell.BombFactoryImpl;
import it.unibo.bombardero.cell.Flame.FlameType;
import it.unibo.bombardero.character.Character;
import it.unibo.bombardero.character.Enemy;
import it.unibo.bombardero.character.Player;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.Coord;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.map.impl.GameMapImpl;
import java.util.List;
import java.util.Optional;
import java.util.Arrays;

public class MyGameManager implements GameManager {

    private static final int STANDARD_ELAPSED_TIME = 100;

    private Player player;
    private Enemy enemy;
    private GameMap map;

    MyGameManager() {
        this.map = new GameMapImpl(false);
        this.enemy = new Enemy(this, new Coord(0, 0), new BombFactoryImpl(this));
        this.player = new Player(this, new Coord(0, 12), new BombFactoryImpl(this));
    }

    public void setPlayerCoord(final int x, final int y) {
        player.setCharacterPosition(new Coord(x + 0.5f, y + 0.5f));
    }

    public void setEnemyCoord(final int x, final int y) {
        enemy.setCharacterPosition(new Coord(x + 0.5f, y + 0.5f));
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public void updateGame(final long elapsed) {
        // 60 fps
        for (int i = 0; i < 59; i++) {
            enemy.update(STANDARD_ELAPSED_TIME);
        }
        enemy.update(STANDARD_ELAPSED_TIME);
    }

    public Enemy getEnemy() {
        return this.enemy;
    }

    @Override
    public void endGame() {
        throw new UnsupportedOperationException("Unimplemented method 'endGame'");
    }

    @Override
    public List<Character> getEnemies() {
        return Arrays.asList(enemy);
    }

    @Override
    public GameMap getGameMap() {
        return map;
    }

    @Override
    public boolean addBomb(final Bomb b) {
        return map.addBomb(b, b.getPos());
    }

    @Override
    public void removeBomb(final Pair pos) {
        throw new UnsupportedOperationException("Unimplemented method 'removeBomb'");
    }

    @Override
    public void addFlame(final FlameType type, final Pair pos) {
        throw new UnsupportedOperationException("Unimplemented method 'addFlame'");
    }

    @Override
    public void removeFlame(final Pair pos) {
        throw new UnsupportedOperationException("Unimplemented method 'removeFlame'");
    }

    @Override
    public boolean removeWall(final Pair pos) {
        throw new UnsupportedOperationException("Unimplemented method 'removeWall'");
    }

    @Override
    public long getTimeLeft() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTimeLeft'");
    }

    @Override
    public void removePowerUp(final Pair pos) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removePowerUp'");
    }

    @Override
    public Optional<Bomb> getBomb(final Pair pos) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBomb'");
    }
}

