package it.unibo.bombardero.core.impl;

import java.util.Map;
import java.util.List;
import java.util.Optional;

import it.unibo.bombardero.cell.Cell;
import it.unibo.bombardero.core.api.Controller;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.GenPair;
import it.unibo.bombardero.physics.impl.BombarderoCollision;
import it.unibo.bombardero.physics.impl.CollisionHandlerImpl;
import it.unibo.bombardero.view.BombarderoGraphics;
import it.unibo.bombardero.view.BombarderoViewMessages;
import it.unibo.bombardero.view.api.GraphicsEngine;
import it.unibo.bombardero.view.api.GraphicsEngine.EndGameState;
import it.unibo.bombardero.view.api.GraphicsEngine.ViewCards;
import it.unibo.bombardero.character.Character;

/**
 * This class implements the idea of the game controller expressed by
 * the {@link Controller} interface.
 * <p>
 * By containing references to the game's {@link GameManager} and {@link GraphicsEngine}
 * this class is able to be updated by the engine and update the entire model and view,
 * while also serving as the Controller in the M.V.C. architecture of the software.
 */
public final class BombarderoController implements Controller {

    private final BombarderoGraphics graphics;
    private GameManager manager;

    private boolean isGamePaused = true;
    private boolean isGameStarted;
    private final boolean isGameOver;

    /**
     * Creates a new controller, creating and storing the {@GraphicsEngine} that will
     * display the model's elements. The {@link GraphicsEngine} creation results in 
     * the creation of the game's window and the main menu.
     * @see {@link GraphicsEngine} and {@link BombarderoGraphics} for the graphic's behaviour.
     */
    public BombarderoController() {
        this.graphics = new BombarderoGraphics(this);
        isGameOver = false;
    }

    @Override
    public void startGame() {
        this.manager = new FullBombarderoGameManager(new BombarderoCollision(new CollisionHandlerImpl()));
        isGamePaused = false;
        isGameStarted = true;
        graphics.showGameScreen(GraphicsEngine.ViewCards.GAME);
        graphics.clearView();
    }

    @Override
    public void endGame() {
        if (isGameStarted) {
            isGamePaused = true;
            isGameStarted = false;
        }
        graphics.clearView();
        graphics.showGameScreen(ViewCards.MENU);
    }

    @Override
    public void startGuide() {
        this.manager = new BombarderoGuideManager(new BombarderoCollision(new CollisionHandlerImpl()));
        isGamePaused = false;
        isGameStarted = true;
        graphics.showGameScreen(GraphicsEngine.ViewCards.GUIDE);
        toggleMessage(BombarderoViewMessages.EXPLAIN_MOVEMENT);
        graphics.update(getMap(), getPlayers(), getEnemies(), getTimeLeft());
    }

    @Override
    public void endGuide() {
        if (isGameStarted) {
            isGamePaused = true;
            isGameStarted = false;
            graphics.clearView();
        }
    }

    @Override
    public void displayEndScreen(final EndGameState endGameState) {
        graphics.showEndScreen(endGameState);
    }


    @Override
    public void escape() {
        if (!isGamePaused) {
            graphics.setPausedView();
            isGamePaused = true;
        } else {
            isGamePaused = false;
            graphics.setUnpausedView();
        }
    }

    @Override
    public void updateGame(final long elapsed) {
        manager.updateGame(elapsed, this);
    }

    @Override
    public void updateGraphics(final long elapsed) {
        graphics.update(getMap(), getPlayers(), getEnemies(), getTimeLeft());
    }


    @Override
    public void toggleMessage(final BombarderoViewMessages message) {
        graphics.setMessage(message);
    }

    @Override
    public boolean isGamePaused() {
        return isGamePaused;
    }

    @Override
    public boolean isGameStarted() {
        return isGameStarted;
    }

    @Override
    public boolean isGameOver() {
        return isGameOver;
    }

    @Override
    public List<Character> getPlayers() {
        return manager.getPlayers();
    }

    @Override
    public List<Character> getEnemies() {
        return manager.getEnemies();
    }

    @Override
    public Map<GenPair<Integer, Integer>, Cell> getMap() {
        return manager.getGameMap().getMap();
    }

    @Override
    public Optional<Long> getTimeLeft() {
        return manager.getTimeLeft();
    }

}
