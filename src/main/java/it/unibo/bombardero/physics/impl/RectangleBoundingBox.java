package it.unibo.bombardero.physics.impl;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import it.unibo.bombardero.character.Direction;
import it.unibo.bombardero.map.api.GenPair;
import it.unibo.bombardero.physics.api.BoundingBox;

/**
 * This class rappresent a rectangular BoundingBox so how it can move and how it collide.
 */
public final class RectangleBoundingBox implements BoundingBox {
    private static final double MINIMUM_COLLISION_DISTANCE = 0.001;
    private final Rectangle2D physicsBox;

    /**
     * Set the physicsBox making new Rectangle2D.
     * @param x
     * @param y
     * @param withd
     * @param height
     */
    public RectangleBoundingBox(final float x, final float y, final float withd, final float height) {
        physicsBox = new Rectangle2D.Float(x, y, withd, height);
    }

    @Override
    public void move(final Point2D newPos) {
        this.physicsBox.setFrame(newPos.getX(), newPos.getY(), physicsBox.getWidth(), physicsBox.getHeight());
    }

    @Override
    public boolean isColliding(final BoundingBox bBox) {
        return this.physicsBox.intersects(bBox.getPhysicsBox()) 
            && (float) physicsBox.createIntersection(bBox.getPhysicsBox()).getWidth() > MINIMUM_COLLISION_DISTANCE 
            && (float) physicsBox.createIntersection(bBox.getPhysicsBox()).getHeight() > MINIMUM_COLLISION_DISTANCE;
    }

    @Override
    public GenPair<Float, Float> computeCollision(final BoundingBox bBox, final Direction dir) {
        switch (dir) {
            case LEFT:
                return new GenPair<Float, Float>(
                        (float) this.physicsBox.createIntersection(bBox.getPhysicsBox()).getWidth(), 0f);
            case RIGHT:
                return new GenPair<Float, Float>(
                        (float) -this.physicsBox.createIntersection(bBox.getPhysicsBox()).getWidth(), 0f);
            case UP:
                return new GenPair<Float, Float>(0f,
                        (float) this.physicsBox.createIntersection(bBox.getPhysicsBox()).getHeight());
            case DOWN:
                return new GenPair<Float, Float>(0f,
                        -(float) this.physicsBox.createIntersection(bBox.getPhysicsBox()).getHeight());
            default:
                return new GenPair<Float, Float>(0f, 0f);
        }
    }

    @Override
    public boolean isColliding(final Line2D.Float mapOutline) {
        return this.physicsBox.intersectsLine(mapOutline);
    }

    @Override
    public GenPair<Float, Float> computeCollision(final Line2D.Float mapOutline, final Direction dir) {
        switch (dir) {
            case UP:
                return new GenPair<Float, Float>(0f, (float) (mapOutline.getY1() - this.physicsBox.getMinY()));
            case DOWN:
                return new GenPair<Float, Float>(0f, (float) (mapOutline.getY1() - this.physicsBox.getMaxY()));
            case LEFT:
                return new GenPair<Float, Float>((float) (mapOutline.getX1() - this.physicsBox.getMinX()), 0f);
            case RIGHT:
                return new GenPair<Float, Float>((float) (mapOutline.getX1() - this.physicsBox.getMaxX()), 0f);
            default:
                return new GenPair<Float, Float>(0f, 0f);
        }
    }

    @Override
    public Rectangle2D getPhysicsBox() {
        return this.physicsBox.getBounds2D();
    }
}
