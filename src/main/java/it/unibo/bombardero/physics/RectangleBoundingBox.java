package it.unibo.bombardero.physics;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import it.unibo.bombardero.character.Direction;


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
    public boolean isColliding(Rectangle2D cellBox) {
        return fisicsBox.intersects(cellBox);
    }

    @Override
    public float distanceOfCollision(Rectangle2D cellBox ,Direction dir) {
        switch (dir) {
            case UP:
                return -(float)fisicsBox.createIntersection(cellBox).getHeight();
            case DOWN:
                return (float)fisicsBox.createIntersection(cellBox).getHeight();
            case LEFT:
                return -(float)fisicsBox.createIntersection(cellBox).getWidth();
            case RIGHT:
                return (float)fisicsBox.createIntersection(cellBox).getWidth();
            default:
                return 0;//TODO not sure about that
        }
    }
}
