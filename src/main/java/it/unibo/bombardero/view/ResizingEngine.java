package it.unibo.bombardero.view;

import javax.swing.JFrame;

import it.unibo.bombardero.map.api.Coord;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.utils.Utils;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;

/** 
 * A class to compute the scale of the game relative to the window size and display size
 */
public class ResizingEngine {

    /* A mischevious padding no one knows its reason to exist: */
    private final static int MISCHIEVOUS_PADDING = 23;
    private final static double initialMenuScale = 0.75;

    private final BombarderoGraphics graphics;

    private double currentScale = 1.125; /* default scale size for every device */
    private final int scaledCellSize;
    private Dimension minimumFrameSize;
    private final Dimension gameWindowSize;

    private final Dimension mapPlacingPoint;
    private final Dimension entityPlacingPoint;
    private final Dimension imageClockPosition;
    private final Dimension timerPosition;
    private int overlayLevel;

    public ResizingEngine(final BombarderoGraphics graphics) {
        this.graphics = graphics;
        int resolution = Toolkit.getDefaultToolkit().getScreenResolution();
        if(resolution >= 200) {              
            currentScale = 1.25; 
        }

        scaledCellSize = (int)(currentScale * Utils.CELL_SIZE);

        gameWindowSize = initGameWindowSize(graphics.getParentFrame());
        mapPlacingPoint = initMapPlacingPoint();
        entityPlacingPoint = initEntityPlacingPoint();
        imageClockPosition = initImageClockPosition();
        timerPosition = initTimerPosition();
        overlayLevel = initOverlayLevel();
    }  

    /* FRAME-RELATED METHODS */

    /* Checks if the minimunFrameSize is satisfied */
    public Dimension getNewWindowSize(JFrame frame) {
        Dimension frameSize = frame.getSize();
        if(frameSize.height < minimumFrameSize.height || frameSize.width < minimumFrameSize.width) {
            return minimumFrameSize;
        }
        return frame.getSize();
    }
    
    public Dimension getInitialMenuSize() {
        return new Dimension(
            (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() * initialMenuScale), 
            (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight() * initialMenuScale)
        );
    }
    
    /* The initial windows size is calculated scaling the map and adding some grass on the sides, other than the insets */
    public Dimension getGameWindowSize(JFrame frame) {       
        return gameWindowSize;
    }

    public Dimension getMapSize() {
        return new Dimension(
            (int)(Utils.MAP_WIDTH * currentScale),
            (int)(Utils.MAP_HEIGHT * currentScale)
        );
    }
    
    public Dimension getBackgroundImageSize() {
        return new Dimension(
            (int)(Utils.BG_WIDTH * currentScale),
            (int)(Utils.BG_HEIGHT * currentScale)
        );
    }

    /* GENERAL UTILITY GETTERS */

    public double getScale() {
        return currentScale;
    }   

    public int getScaledCellSize() {
        return scaledCellSize;
    }
    
    public boolean hasScaleChanged() {
        return false;
    }
    
    /* SCALING-RELATED METHODS: */

    public Image getScaledCellImage(final Image cellImage) {
        return cellImage.getScaledInstance(getScaledCellSize(), (int)(getScaledCellSize() + 7 * getScale()), Image.SCALE_SMOOTH);
    }

    public Image getScaledCharacterImage(final Image characterImage) {
        return characterImage.getScaledInstance((int)Math.floor(35 * getScale()), (int)Math.floor(55 * getScale()), Image.SCALE_SMOOTH);
    }

    public Image getScaledBombImage(final Image bombImage) {
        /* TODO: scale appropriately */
        return bombImage;
    }

    public Image getScaledBackgroundImage(final Image backgroundImage) {
        return backgroundImage.getScaledInstance(getBackgroundImageSize().width, getBackgroundImageSize().height, Image.SCALE_SMOOTH);
    }

    public Image getScaledMapImage(final Image mapImage) {
        return mapImage.getScaledInstance(getMapSize().width, getMapSize().height, Image.SCALE_SMOOTH);
    }
    
    /* GAME-RELATED METHODS: */
    
    public Dimension getMapPlacingPoint() {
        return mapPlacingPoint;
    }

    /** 
     * Returns the corner of the north-eastern cell of the map
     */
    public Dimension getEntityPlacingPoint() {
        return entityPlacingPoint;
    }

    /** 
     * Returns the upper-left corner of the cell at {@link #cooordinate}
     */
    public Dimension getCellPlacingPoint(Pair coordinate) {
        return new Dimension(
            entityPlacingPoint.width + (int)(getScaledCellSize() * coordinate.x()),
            entityPlacingPoint.height + (int)(getScaledCellSize() * coordinate.y())
        );
    }

    public Dimension getBombPlacingPoint(Pair coordinate) {
        Dimension placingPoint = getCellPlacingPoint(coordinate);
        return new Dimension(
            placingPoint.width + (int)(getScaledCellSize() / 2),
            placingPoint.height + (int)(getScaledCellSize() / 2)
        );
    }

    public Dimension getCharacterPlacingPoint(final Coord playerPosition) {
        Dimension cellCorner = getCellPlacingPoint(new Pair((int)Math.floor(playerPosition.x()), (int)Math.floor(playerPosition.y())));
        return new Dimension(
            cellCorner.width
            + (int)Math.floorDiv((int)Math.floor(getScaledCellSize() - (35 * getScale())), 2),
            cellCorner.height 
            + (int)((int)Math.floor(getScaledCellSize() - (55 * getScale())))
        );
    }

    public Dimension getImageClockPosition() {
        return imageClockPosition;
    }

    public Dimension getTimerPosition() {
        return timerPosition;
    }

    public int getOverlayLevel() {
        return overlayLevel;
    }

    private Dimension initMapPlacingPoint() {
        return new Dimension(
            gameWindowSize.width/2 - getMapSize().width/2 - (graphics.getParentFrame().getInsets().right + graphics.getParentFrame().getInsets().left),
            gameWindowSize.height/2 - getMapSize().height/2 - (graphics.getParentFrame().getInsets().top + graphics.getParentFrame().getInsets().bottom)
        );
    }

    public Dimension initGameWindowSize(JFrame frame) {       
        minimumFrameSize = new Dimension(
            frame.getInsets().left + frame.getInsets().right + (int)(Utils.MAP_WIDTH * currentScale), 
            frame.getInsets().top + frame.getInsets().bottom + (int)(Utils.MAP_HEIGHT * currentScale)
        ); 
        return new Dimension(
            frame.getInsets().left + frame.getInsets().right + (int)(Utils.MAP_WIDTH * currentScale + Utils.GRASS_PADDING_RATIO * Utils.MAP_WIDTH), 
            frame.getInsets().top + frame.getInsets().bottom + (int)(Utils.MAP_HEIGHT * currentScale + Utils.GRASS_PADDING_RATIO * Utils.MAP_HEIGHT)
        );
    }

    /** 
     * Returns the corner of the north-eastern cell of the map
     */
    private Dimension initEntityPlacingPoint() {
        return new Dimension(
            getMapPlacingPoint().width + getScaledCellSize() + (int)(getScale() * MISCHIEVOUS_PADDING),
            getMapPlacingPoint().height + 2 * getScaledCellSize() - (int)(7 * getScale())
        );
    }

    private Dimension initImageClockPosition() {
        return new Dimension(
            getMapPlacingPoint().width + Utils.MAP_WIDTH / 2 - getScaledCellSize() / 2,
            getOverlayLevel() + getScaledCellSize() / 2
        );
    }

    private Dimension initTimerPosition() {
        Dimension clockPos = getImageClockPosition();
        return new Dimension(
            (int)Math.floor(clockPos.width + getScaledCellSize() * 1.5),
            overlayLevel + getScaledCellSize()
        );
    }

    private int initOverlayLevel() {
        return getMapPlacingPoint().height;
    }
    
}
