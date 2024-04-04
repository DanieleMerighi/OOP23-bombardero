package it.unibo.bombardero.core.impl;

import it.unibo.bombardero.core.BombarderoEngine;
import it.unibo.bombardero.core.api.Controller;
import it.unibo.bombardero.view.BombarderoGraphics;

public class BombarderoController implements Controller {

    private BombarderoGraphics graphics;

    public BombarderoController(BombarderoGraphics graphics) {
        this.graphics = graphics;
    }

    @Override
    public void startGame() {
        BombarderoEngine engine = new BombarderoEngine(this, this.graphics);
        engine.initGame();
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
    public void getMap() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMap'");
    }
    
}
