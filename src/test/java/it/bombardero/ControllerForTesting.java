package it.bombardero;

import java.util.Arrays;
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
import it.unibo.bombardero.view.api.GraphicsEngine.EndGameState;

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
    public void displayEndScreen(final EndGameState endGameState) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'displayEndScreen'");
    }

    @Override
    public void updateGame(final long elapsed) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateGame'");
    }

    @Override
    public void updateGraphics(final long elapsed) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateGraphics'");
    }

    @Override
    public void escape() {
        this.escapeCalled = true;
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
    public List<Character> getPlayers() {
        return List.of(player);
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

    @Override
    public boolean isGameOver() {
        throw new UnsupportedOperationException("Unimplemented method 'isGameOver'");
    }

}
