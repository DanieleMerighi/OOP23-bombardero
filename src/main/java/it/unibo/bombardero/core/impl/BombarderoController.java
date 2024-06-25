package it.unibo.bombardero.core.impl;

import java.util.Map;
import java.util.List;

import it.unibo.bombardero.cell.AbstractCell;
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

    public BombarderoController(final BombarderoGraphics graphics) {
        this.graphics = graphics;
    }

    @Override
    public void startGame() {
        this.manager = new BombarderoGameManager(this);
        engine = new BombarderoEngine(this, this.graphics, this.manager);
        graphics.initGameCard();
        graphics.showCard(BombarderoGraphics.GAME_CARD);
        engine.startGameLoop();
    }

    @Override
    public void endGame() {
        engine.endGameLoop();
        graphics.showCard(BombarderoGraphics.END_CARD);
    }

    @Override
    public void startGuide() {
        this.manager = new BombarderoGuideManager(this);
        engine = new BombarderoEngine(this, this.graphics, this.manager);
        graphics.initGuideCard();
        graphics.showCard(BombarderoGraphics.GUIDE_CARD);
        engine.startGameLoop();
        toggleMessage(BombarderoViewMessages.EXPLAIN_MOVEMENT);
    }

    @Override
    public void endGuide() {
        graphics.displayEndGuide();
        engine.endGameLoop();
        graphics.update();
    }

    @Override
    public void escape() {
        if (!engine.isInterrupted()) {
            graphics.setPausedView();
            engine.pauseGameLoop();
        }
        else {
            engine.resumeGameLoop();
            graphics.setUnpausedView();
        }
    }

    @Override
    public void toggleMessage(final BombarderoViewMessages message) {
        graphics.setMessage(message);
    }
    
    @Override
    public boolean isGamePaused() {
        return engine.isInterrupted();
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
    public long getTimeLeft() {
        return manager.getTimeLeft();
    }
    
}
