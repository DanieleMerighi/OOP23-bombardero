package it.unibo.bombardero.physics.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.stream.Stream;
import java.util.Set;

import it.unibo.bombardero.cell.BasicBomb;
import it.unibo.bombardero.cell.Bomb;
import it.unibo.bombardero.cell.Cell;
import it.unibo.bombardero.cell.Flame;
import it.unibo.bombardero.cell.Cell.CellType;
import it.unibo.bombardero.character.Direction;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.physics.api.BoundingBox;
import it.unibo.bombardero.physics.api.CollisionEngine;
import it.unibo.bombardero.character.Character;

public class BombarderoCollision implements CollisionEngine{
    private GameManager mgr;
    private GameMap gMap;
    private Map<Pair,Cell> map;
    List<Character> characters;

    public BombarderoCollision(GameManager mgr){
        this.mgr=mgr;
        gMap=mgr.getGameMap();
        characters= new LinkedList<>( mgr.getEnemies()) ;
        characters.add(mgr.getPlayer());
        map=gMap.getMap();
    }

    @Override
    public void checkFlameCollision() {
        characters.stream().forEach( c ->
            { 
                if(gMap.isFlame(c.getIntCoordinate())) {
                    c.kill();
                }
            });
    }

    @Override
    public void checkCharacterCollision(Character character) {
        Optional<BoundingBox> collidigBox = getDirectionCell(character.getFacingDirection()).
            stream().map( p -> map.get(p).getBoundingBox() )
            .filter(b->b.isColliding(character.getBoundingBox()))
            .findFirst();
        if(collidigBox.isPresent()){
            character.setCharacterPosition(character.getCharacterPosition()
                .sum(character.getBoundingBox()
                .distanceOfCollision(collidigBox.get().getPhysicsBox(), character.getFacingDirection())));
        }

    }


    private List<Pair> getDirectionCell(Direction dir) {
        List<Pair> l = new LinkedList<Pair> (dir.getDiagonals(dir));
        l.add(dir.getPair());
        return l;
    }
}