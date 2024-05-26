package it.unibo.bombardero.physics.impl;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import it.unibo.bombardero.character.Direction;
import it.unibo.bombardero.map.api.Coord;
import it.unibo.bombardero.physics.api.BoundingBox;


public class RectangleBoundingBox implements BoundingBox{
    private Rectangle2D fisicsBox;

    public RectangleBoundingBox(Point2D charPos, float withd, float height) {
        fisicsBox = new Rectangle2D.Float((float)charPos.getX(), (float)charPos.getY(), withd, height);
    }

    @Override
    public void move(Point2D newPos) {
        fisicsBox.setFrame(newPos.getX(), newPos.getY(), fisicsBox.getWidth(), fisicsBox.getHeight());
    }

    @Override
    public boolean isColliding(BoundingBox bBox) {
        return fisicsBox.intersects(bBox.getPhysicsBox());
        
    }

    @Override
    public Coord distanceOfCollision(Rectangle2D cellBox ,Direction dir) {
        switch (dir) {
            case UP:
                return new Coord((float)fisicsBox.createIntersection(cellBox).getHeight(),0);
            case DOWN:
                return new Coord(-(float)fisicsBox.createIntersection(cellBox).getHeight() , 0);
            case LEFT:
                return new Coord(0 , (float)fisicsBox.createIntersection(cellBox).getWidth());
            case RIGHT:
                return new Coord(0, -(float)fisicsBox.createIntersection(cellBox).getWidth());
            default:
                return new Coord(0, 0);//TODO not sure about that
        }
    }

    @Override
    public Rectangle2D getPhysicsBox() {
        return fisicsBox;
    }
}
