package it.unibo.bombardero.physics.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import it.unibo.bombardero.cell.Cell;
import it.unibo.bombardero.cell.powerup.api.PowerUp;
import it.unibo.bombardero.character.Direction;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.physics.api.BoundingBox;
import it.unibo.bombardero.physics.api.CollisionEngine;
import it.unibo.bombardero.character.Character;

public class BombarderoCollision implements CollisionEngine{
    private final GameManager mgr;
    private GameMap gMap;
    private Map<Pair,Cell> map;
    private final Map<Direction, Line2D.Float> MAP_OUTLINES = Map.of(
        Direction.UP , new Line2D.Float(new Point2D.Float(0, 0) , new Point2D.Float(13, 0)),
        Direction.DOWN , new Line2D.Float(new Point2D.Float(0, 13) , new Point2D.Float(13, 13)),
        Direction.LEFT , new Line2D.Float(new Point2D.Float(0, 0) , new Point2D.Float(0, 13)),
        Direction.RIGHT , new Line2D.Float(new Point2D.Float(13, 0) , new Point2D.Float(13, 13))
    );

    public BombarderoCollision(GameManager mgr){
        this.mgr=mgr;
    }

    @Override
    public void checkFlameAndPowerUpCollision(Character character) {
        if(gMap.isFlame(character.getIntCoordinate())) {
            character.kill();
        } else if(gMap.isPowerUp(character.getIntCoordinate())) {
            PowerUp p = (PowerUp)gMap.getMap().get(character.getIntCoordinate());
            p.applyEffect(character);
            mgr.removePowerUp(character.getIntCoordinate());
        }
    }

    @Override
    public void checkCharacterCollision(Character character) {
        gMap=mgr.getGameMap();
        map=gMap.getMap();
        List<Pair> cells = getDirectionCell(character.getFacingDirection() , character.getIntCoordinate());
        if(!cells.isEmpty()) {
            Optional<Cell> collidingCell= cells.stream()
                .filter(p -> map.containsKey(p))
                .map( p -> map.get(p))
                .filter(c-> c.getBoundingBox().isColliding(character.getBoundingBox()) && c.getBoundingCollision())
                .findFirst();
            SolidCellCollision(character, collidingCell);
        } else {
            checkCoolisionWithMapWall(character);
        }
    }

    private void SolidCellCollision(Character character, Optional<Cell> collidingCell) {
        if(collidingCell.isPresent()) {
            character.setCharacterPosition(character.getCharacterPosition()
                .sum(character.getBoundingBox()
                    .computeCollision(collidingCell.get().getBoundingBox(), character.getFacingDirection())));
        }
    }

    private void checkCoolisionWithMapWall(Character character) {
        BoundingBox bBox = character.getBoundingBox();
        Line2D.Float outerLine = MAP_OUTLINES.get(character.getFacingDirection());
        if(bBox.isColliding(outerLine)) {
            character.setCharacterPosition(
                character.getCharacterPosition()
                .sum(bBox.computeCollision(outerLine, character.getFacingDirection())));
        }
    }

    private List<Pair> getDirectionCell(Direction dir , Pair cPos) {//TODO brutto
        List<Pair> l = dir.getDiagonals(dir,cPos);
        List<Pair> l2 = new LinkedList <> (l);
        if(cPos.sum(dir.getPair()).x() >= 0 && cPos.sum(dir.getPair()).y() >= 0 && cPos.sum(dir.getPair()).x() < 13 && cPos.sum(dir.getPair()).y() < 13) {
            l2.add(cPos.sum(dir.getPair()));
        } 
        return l2;
    }
}