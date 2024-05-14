package it.unibo.bombardero.core.impl;

import java.util.Map;
import java.util.List;

import it.unibo.bombardero.cell.Cell;
import it.unibo.bombardero.core.BombarderoEngine;
import it.unibo.bombardero.core.api.Controller;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.view.BombarderoGraphics;
import it.unibo.bombardero.character.Character;

public class BombarderoController implements Controller {

    private final BombarderoGraphics graphics;
    private GameManager manager;

    public BombarderoController(final BombarderoGraphics graphics) {
        this.graphics = graphics;
    }

    @Override
    public void startGame() {
        BombarderoEngine engine = new BombarderoEngine(this, this.graphics);
        this.manager = engine.initGameManager();
        engine.mainLoop();
    }

    /* TODO: endGame method */

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
    
}
