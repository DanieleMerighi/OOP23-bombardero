package it.unibo.bombardero.core.impl;

import java.util.List;
import java.util.Set;

import it.unibo.bombardero.cell.Bomb;
import it.unibo.bombardero.core.api.Controller;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.physics.api.CollisionEngine;
import it.unibo.bombardero.character.Character;

public class BombarderoGameManager implements GameManager{
    
    private GameMap map;
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
    public Set<Character> getEnemies() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCharacter'");
    }

    @Override
    public GameMap getGameMap() {
        return this.map;
    }

    @Override
    public void explodeBomb(Bomb bomb) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'explodeVoid'");
    }

    @Override
    public Character getPlayer() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPlayer'");
    }
    
}
