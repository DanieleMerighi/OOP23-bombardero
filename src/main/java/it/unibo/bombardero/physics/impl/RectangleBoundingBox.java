package it.unibo.bombardero.physics.impl;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import it.unibo.bombardero.character.Direction;
import it.unibo.bombardero.map.api.Coord;
import it.unibo.bombardero.physics.api.BoundingBox;


public class RectangleBoundingBox implements BoundingBox{
    private static final double MINIMUM_COLLISION_DISTANCE = 0.001;
    private final Rectangle2D fisicsBox;

    public RectangleBoundingBox(final Point2D charPos, final float withd, final float height) {
        fisicsBox = new Rectangle2D.Float((float)charPos.getX(), (float)charPos.getY(), withd, height);
    }

    @Override
    public void move(final Point2D newPos) {
        this.fisicsBox.setFrame(newPos.getX(), newPos.getY(), fisicsBox.getWidth(), fisicsBox.getHeight());
    }

    @Override
    public boolean isColliding(final BoundingBox bBox) {
        return this.fisicsBox.intersects(bBox.getPhysicsBox()) 
            && (float)fisicsBox.createIntersection(bBox.getPhysicsBox()).getWidth()>MINIMUM_COLLISION_DISTANCE 
            && (float)fisicsBox.createIntersection(bBox.getPhysicsBox()).getHeight()>MINIMUM_COLLISION_DISTANCE;
    }

    @Override
    public Coord computeCollision(final BoundingBox bBox, final Direction dir) {
        switch (dir) {
            case LEFT:
                return new Coord((float)this.fisicsBox.createIntersection(bBox.getPhysicsBox()).getWidth(),0);
            case RIGHT:
                return new Coord((float)-this.fisicsBox.createIntersection(bBox.getPhysicsBox()).getWidth() , 0);
            case UP:
                return new Coord(0 , (float)this.fisicsBox.createIntersection(bBox.getPhysicsBox()).getHeight());
            case DOWN:
                return new Coord(0, -(float)this.fisicsBox.createIntersection(bBox.getPhysicsBox()).getHeight());
            default:
                return new Coord(0, 0);
        }
    }

    @Override
    public boolean isColliding(final Line2D.Float mapOutline) {
        return this.fisicsBox.intersectsLine(mapOutline);
        
    }

    @Override
    public Coord computeCollision(final Line2D.Float mapOutline ,final Direction dir) {
        switch (dir) {
            case UP:
                return new Coord(0 , (float)(mapOutline.getY1()-this.fisicsBox.getMinY()));
            case DOWN:
                return new Coord(0 , (float)(mapOutline.getY1()-this.fisicsBox.getMaxY()));
            case LEFT:
                return new Coord((float)(mapOutline.getX1()-this.fisicsBox.getMinX()) , 0);
            case RIGHT:
                return new Coord((float)(mapOutline.getX1()-this.fisicsBox.getMaxX()) , 0);
            default:
                return new Coord(0, 0);
        }
    }

    @Override
    public Rectangle2D getPhysicsBox() {
        return this.fisicsBox;
    }
}
