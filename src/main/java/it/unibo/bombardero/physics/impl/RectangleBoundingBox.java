package it.unibo.bombardero.physics.impl;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import it.unibo.bombardero.character.Direction;
import it.unibo.bombardero.map.api.Coord;
import it.unibo.bombardero.physics.api.BoundingBox;


public class RectangleBoundingBox implements BoundingBox{
    private static final double MINIMUM_COLLISION_DISTANCE = 0.001;
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
        return fisicsBox.intersects(bBox.getPhysicsBox()) 
            && (float)fisicsBox.createIntersection(bBox.getPhysicsBox()).getWidth()>MINIMUM_COLLISION_DISTANCE 
            && (float)fisicsBox.createIntersection(bBox.getPhysicsBox()).getHeight()>MINIMUM_COLLISION_DISTANCE;
    }

    @Override
    public Coord computeCollision(BoundingBox bBox ,Direction dir) {
        switch (dir) {
            case LEFT:
                return new Coord((float)fisicsBox.createIntersection(bBox.getPhysicsBox()).getWidth(),0);
            case RIGHT:
                return new Coord((float)-fisicsBox.createIntersection(bBox.getPhysicsBox()).getWidth() , 0);
            case UP:
                return new Coord(0 , (float)fisicsBox.createIntersection(bBox.getPhysicsBox()).getHeight());
            case DOWN:
                return new Coord(0, -(float)fisicsBox.createIntersection(bBox.getPhysicsBox()).getHeight());
            default:
                return new Coord(0, 0);
        }
    }

    @Override
    public boolean isColliding(Line2D.Float mapOutline) {
        return fisicsBox.intersectsLine(mapOutline);
        
    }

    @Override
    public Coord computeCollision(Line2D.Float mapOutline ,Direction dir) {
        switch (dir) {
            case UP:
                return new Coord(0 , (float)(mapOutline.getY1()-fisicsBox.getMinY()));
            case DOWN:
                return new Coord(0 , (float)(mapOutline.getY1()-fisicsBox.getMaxY()));
            case LEFT:
                return new Coord((float)(mapOutline.getX1()-fisicsBox.getMinX()) , 0);
            case RIGHT:
                return new Coord((float)(mapOutline.getX1()-fisicsBox.getMaxX()) , 0);
            default:
                return new Coord(0, 0);
        }
    }

    @Override
    public Rectangle2D getPhysicsBox() {
        return fisicsBox;
    }
}
