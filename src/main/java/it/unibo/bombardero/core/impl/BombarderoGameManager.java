package it.unibo.bombardero.core.impl;

import java.util.List;

import it.unibo.bombardero.cell.Bomb;
import it.unibo.bombardero.cell.Bomb.BombType;
import it.unibo.bombardero.core.api.Controller;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.MapManager;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.physics.api.CollisionEngine;

public class BombarderoGameManager implements GameManager{
    
    private MapManager map;
    private List<Character> enemies;
    private Character player;
    private Controller controller;
    private CollisionEngine ce;

    public BombarderoGameManager(Controller ctrl){
        this.controller=ctrl;
        //init game
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

    @Override
    public void explodeBomb(Bomb bomb) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'explodeVoid'");
    }
    
}
