package it.unibo.bombardero.core.impl;

import java.util.Map;

import it.unibo.bombardero.cell.Cell;
import it.unibo.bombardero.core.BombarderoEngine;
import it.unibo.bombardero.core.api.Controller;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.view.BombarderoGraphics;

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

    @Override
    public void getMainPlayer() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMainPlayer'");
    }

    @Override
    public void getEnemies() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEnemies'");
    }

    @Override
    public Map<Pair, Cell> getMap() {
        return manager.getGameMap().getMap();
    }
    
}
