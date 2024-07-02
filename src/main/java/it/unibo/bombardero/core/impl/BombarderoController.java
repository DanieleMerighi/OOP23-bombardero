package it.unibo.bombardero.core.impl;

import java.util.Map;
import java.util.List;
import java.util.Optional;

import it.unibo.bombardero.cell.Cell;
import it.unibo.bombardero.core.BombarderoEngine;
import it.unibo.bombardero.core.api.Controller;
import it.unibo.bombardero.core.api.Engine;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.guide.impl.BombarderoGuideManager;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.view.BombarderoGraphics;
import it.unibo.bombardero.view.BombarderoViewMessages;
import it.unibo.bombardero.character.Character;

public class BombarderoController implements Controller {

    private final BombarderoGraphics graphics;
    private GameManager manager;
    private Engine engine;

    private boolean isGamePaused = true;
    private boolean isGameStarted = false;

    public BombarderoController() {
        this.graphics = new BombarderoGraphics(this);
    }

    @Override
    public void startGame() {
        this.manager = new FullBombarderoGameManager(this);
        isGamePaused = false;
        isGameStarted = true;
        graphics.initGameCard();
        graphics.showCard(BombarderoGraphics.GAME_CARD);
    }

    @Override
    public void endGame() {
        engine.endGameLoop();
        isGamePaused = true;
        isGameStarted = false;
        graphics.showCard(BombarderoGraphics.END_CARD);
    }

    @Override
    public void startGuide() {
        this.manager = new BombarderoGuideManager(this);
        isGamePaused = false;
        isGameStarted = true;
        graphics.showCard(BombarderoGraphics.GUIDE_CARD);
        toggleMessage(BombarderoViewMessages.EXPLAIN_MOVEMENT);
    }

    @Override
    public void endGuide() {
        engine.endGameLoop();
        isGamePaused = true;
        isGameStarted = false;
        graphics.update(getMap(), List.of(), getEnemies());
    }

    @Override
    public void displayEndGuide() {
        graphics.displayEndGuide();
    }
    

    @Override
    public void escape() {
        if (!isGamePaused) {
            graphics.setPausedView();
            isGamePaused = true;
        }
        else {
            isGamePaused = false;
            graphics.setUnpausedView();
        }
    }

    public void updateModel(final long elapsed) {
        manager.updateGame(elapsed);
        graphics.update(getMap(), List.of(getMainPlayer()), getEnemies());
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
    public Character getMainPlayer() {
        return manager.getPlayer();
    }

    @Override
    public List<Character> getEnemies() {
        return manager.getEnemies();
    }

    @Override
    public Map<Pair, Cell> getMap() {
        return manager.getGameMap().getMap();
    }

    @Override
    public Optional<Long> getTimeLeft() {
        return manager.getTimeLeft();
    }

}
