package it.unibo.bombardero.core.impl;

import java.util.List;

import it.unibo.bombardero.core.api.Controller;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.BombarderoTimer;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.MapManager;
import it.unibo.bombardero.map.impl.BombarderoTimerImpl;

public class BombarderoGameManager implements GameManager{
    
    private MapManager map;
    private List<Character> enemies;
    private Character player;
    private Controller controller;
    private BombarderoTimer gameTimer = new BombarderoTimerImpl();

    public BombarderoGameManager(Controller ctrl){
        this.controller=ctrl;
        //init game
    }

    @Override
    public void startGame() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'startGame'");
    }

    @Override
    public void updateGame() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateGame'");
    }

    @Override
    public void endGame() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'endGame'");
    }

    @Override
    public List<Character> getCharacter() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCharacter'");
    }

    @Override
    public GameMap getMap() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMap'");
    }
    
}
