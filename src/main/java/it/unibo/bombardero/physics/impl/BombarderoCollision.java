package it.unibo.bombardero.physics.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import it.unibo.bombardero.cell.Cell;
import it.unibo.bombardero.character.Direction;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.physics.api.BoundingBox;
import it.unibo.bombardero.physics.api.CollisionEngine;
import it.unibo.bombardero.character.Character;

public class BombarderoCollision implements CollisionEngine{
    private GameMap gMap;
    private Map<Pair,Cell> map;
    private List<Character> characters;
    private Map<Direction, Line2D.Float> MAP_OUTLINES = Map.of(
        Direction.UP , new Line2D.Float(new Point2D.Float(0, 0) , new Point2D.Float(13, 0)),
        Direction.DOWN , new Line2D.Float(new Point2D.Float(0, 13) , new Point2D.Float(13, 13)),
        Direction.LEFT , new Line2D.Float(new Point2D.Float(0, 0) , new Point2D.Float(0, 13)),
        Direction.RIGHT , new Line2D.Float(new Point2D.Float(13, 0) , new Point2D.Float(13, 13))
    );

    public BombarderoCollision(GameManager mgr){
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
        Optional<List<Pair>> collidingCell = getDirectionCell(character.getFacingDirection() , character.getIntCoordinate());
        if(collidingCell.isPresent()) {
            Optional<BoundingBox> collidigBox = collidingCell
                .stream().map( p -> map.get(p).getBoundingBox() )
                .filter(b->b.isColliding(character.getBoundingBox()))
                .findFirst();
                if(collidigBox.isPresent()){
                    character.setCharacterPosition(character.getCharacterPosition()
                        .sum(character.getBoundingBox()
                        .computeCollision(collidigBox.get(), character.getFacingDirection())));
                }
        } else {
            checkCoolisionWithMapWall(character);
        }

    }

    private void checkCoolisionWithMapWall(Character character) {
        BoundingBox bBox = character.getBoundingBox();
        Line2D.Float outerLine = MAP_OUTLINES.get(character.getFacingDirection());
        if(bBox.isColliding(outerLine)) {
            character.setCharacterPosition(bBox.computeCollision(outerLine, character.getFacingDirection()));
        }
    }

    private Optional<List<Pair>> getDirectionCell(Direction dir , Pair cDir) {
        Optional<List<Pair>> l = dir.getDiagonals(dir);
        if(l.isPresent()) {
            l.get().add(dir.getPair());
            return l;
        } else {
            return Optional.empty();
        }
    }
}