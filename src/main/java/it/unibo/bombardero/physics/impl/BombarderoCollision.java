package it.unibo.bombardero.physics.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import it.unibo.bombardero.cell.Cell;
import it.unibo.bombardero.cell.Cell.CellType;
import it.unibo.bombardero.cell.powerup.api.PowerUp;
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
                if( c!=null && gMap.isFlame(c.getIntCoordinate())) {
                    c.kill();
                }
            });
    }

    @Override
    public void checkCharacterCollision(Character character) {
        Optional<List<Pair>> Cells = getDirectionCell(character.getFacingDirection() , character.getIntCoordinate());
        System.out.println(character.getIntCoordinate() + "initial");
        if(Cells.isPresent()) {
            Optional<Cell> collidingCell= Cells.get()
                .stream().filter(p -> map.containsKey(p)).peek(p->System.out.println(p + "colliding cell"))
                .map( p -> map.get(p))
                .filter(c-> c.getBoundingBox().isColliding(character.getBoundingBox()))
                .findFirst();
            CellCollision(character, collidingCell);
        } else {
            checkCoolisionWithMapWall(character);
        }
        System.out.println(character.getIntCoordinate() + "final");
    }

    private void SolidCellCollision(Character character, Cell collidingCell) {
        character.setCharacterPosition(character.getCharacterPosition()
            .sum(character.getBoundingBox()
                .computeCollision(collidingCell.getBoundingBox(), character.getFacingDirection())));
    }

    private void pickUpCollision(Character character, Cell collidingCell) {
        ((PowerUp)collidingCell).applyEffect(character);
    }

    private void CellCollision(Character character , Optional<Cell> collidingCell) {
        if(collidingCell.isPresent()) {
            switch (collidingCell.get().getCellType()) {
                case WALL_BREAKABLE:
                case WALL_UNBREAKABLE:
                case BOMB:
                    SolidCellCollision(character, collidingCell.get());
                    return;
                case POWERUP:
                    pickUpCollision(character, collidingCell.get());
                    return;   
                default:
                    return;
            }
        }
    }

    private void checkCoolisionWithMapWall(Character character) {
        BoundingBox bBox = character.getBoundingBox();
        Line2D.Float outerLine = MAP_OUTLINES.get(character.getFacingDirection());
        System.out.println(character.getIntCoordinate() + "initial map");
        if(bBox.isColliding(outerLine)) {
            character.setCharacterPosition(bBox.computeCollision(outerLine, character.getFacingDirection()));
        }
        System.out.println(character.getIntCoordinate() + "final map");
    }

    private Optional<List<Pair>> getDirectionCell(Direction dir , Pair cDir) {
        Optional<List<Pair>> l = dir.getDiagonals(dir);
        if(l.isPresent()) {
            List<Pair> l2 = new LinkedList <> (l.get());
            l2.add(dir.getPair());
            return l;
        } else {
            return Optional.empty();
        }
    }
}