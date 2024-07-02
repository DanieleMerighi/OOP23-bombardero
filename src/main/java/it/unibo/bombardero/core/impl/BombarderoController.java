package it.unibo.bombardero.core.impl;

import java.util.Map;
import java.util.List;
import java.util.Optional;

import org.jgrapht.generate.GnmRandomGraphGenerator;

import it.unibo.bombardero.cell.Cell;
import it.unibo.bombardero.core.BombarderoEngine;
import it.unibo.bombardero.core.api.Controller;
import it.unibo.bombardero.core.api.Engine;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.guide.impl.BombarderoGuideManager;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.view.BombarderoGraphics;
import it.unibo.bombardero.view.BombarderoViewMessages;
import it.unibo.bombardero.view.Graphics;
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
        graphics.showCard(Graphics.viewCards.GAME);
    }

    @Override
    public void endGame() {
        engine.endGameLoop();
        isGamePaused = true;
        isGameStarted = false;
        graphics.showCard(Graphics.viewCards.END);
    }

    @Override
    public void startGuide() {
        if (isGameStarted) {
            this.manager = new BombarderoGuideManager(this);
            isGamePaused = false;
            isGameStarted = true;
            graphics.showCard(Graphics.viewCards.GUIDE);
            toggleMessage(BombarderoViewMessages.EXPLAIN_MOVEMENT);
        }
    }

    @Override
    public void endGuide() {
        if (isGameStarted) {
            engine.endGameLoop();
            isGamePaused = true;
            isGameStarted = false;
            graphics.update(getMap(), List.of(), getEnemies(), getTimeLeft());
        }
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

    public void update(final long elapsed) {
        manager.updateGame(elapsed);
        graphics.update(getMap(), List.of(getMainPlayer()), getEnemies(), getTimeLeft());
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
