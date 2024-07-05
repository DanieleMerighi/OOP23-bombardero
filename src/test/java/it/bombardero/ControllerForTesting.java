package it.bombardero;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import it.unibo.bombardero.bomb.impl.BombFactoryImpl;
import it.unibo.bombardero.cell.Cell;
import it.unibo.bombardero.character.Character;
import it.unibo.bombardero.character.Enemy;
import it.unibo.bombardero.character.Player;
import it.unibo.bombardero.core.api.Controller;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.GenPair;
import it.unibo.bombardero.view.BombarderoViewMessages;

import java.util.Arrays;

/**
 * A simulation of an implementation of the {@link #Controller} interface
 * for testing purposes only.
 */
public final class ControllerForTesting implements Controller {
    private final GameManager manager = new MyGameManager();
    private final Player player = new Player(new GenPair<Float, Float>(0f, 0f), new BombFactoryImpl());
    private final Enemy enemy = new Enemy(new GenPair<Float, Float>(0f, 0f), new BombFactoryImpl());
    private boolean escapeCalled;

    @Override
    public void startGame() {
        throw new UnsupportedOperationException("Unimplemented method 'startGame'");
    }

    @Override
    public void endGame() {
        throw new UnsupportedOperationException("Unimplemented method 'endGame'");
    }

    @Override
    public void startGuide() {
        throw new UnsupportedOperationException("Unimplemented method 'startGuide'");
    }

    @Override
    public void endGuide() {
        throw new UnsupportedOperationException("Unimplemented method 'endGuide'");
    }

    @Override
    public void displayEndGuide() {
        throw new UnsupportedOperationException("Unimplemented method 'displayEndGuide'");
    }

    @Override
    public void escape() {
        this.escapeCalled = true;
    }

    @Override
    public void update(final long elapsed) {
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void toggleMessage(final BombarderoViewMessages message) {
        throw new UnsupportedOperationException("Unimplemented method 'toggleMessage'");
    }

    @Override
    public boolean isGamePaused() {
        return this.escapeCalled;
    }

    @Override
    public boolean isGameStarted() {
        throw new UnsupportedOperationException("Unimplemented method 'isGameStarted'");
    }

    @Override
    public Character getMainPlayer() {
        return this.player;
    }

    @Override
    public List<Character> getEnemies() {
        return Arrays.asList(enemy);
    }

    @Override
    public Map<GenPair<Integer, Integer>, Cell> getMap() {
        return this.manager.getGameMap().getMap();
    }

    @Override
    public Optional<Long> getTimeLeft() {
        throw new UnsupportedOperationException("Unimplemented method 'getTimeLeft'");
    }

}
