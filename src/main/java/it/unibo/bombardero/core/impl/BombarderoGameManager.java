package it.unibo.bombardero.core.impl;

import java.util.List;
import java.util.Set;

import it.unibo.bombardero.cell.BasicBomb;
import it.unibo.bombardero.cell.Flame;
import it.unibo.bombardero.cell.Cell.CellType;
import it.unibo.bombardero.cell.BasicBomb;
import it.unibo.bombardero.core.api.Controller;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.physics.api.CollisionEngine;
import it.unibo.bombardero.character.Character;

public class BombarderoGameManager implements GameManager {
    
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
    public List<Character> getEnemies() {
        return List.copyOf(enemies);
    }

    @Override
    public GameMap getGameMap() {
        return this.map;
    }

    @Override
    public Character getPlayer() {
        return this.player;
    }

    @Override
    public void addBomb(BasicBomb bomb) {
        map.addBomb(bomb);
    }

    @Override
    public void removeBomb(Pair pos) {
        map.removeBomb(pos);
    }

    @Override
    public void addFlame(Flame.FlameType type, Pair pos) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addFlame'");
    }

    @Override
    public void removeFlame(Pair pos) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeFlame'");
    }

    @Override
    public boolean removeWall(Pair pos) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeWall'");
    }
    
}
