package it.unibo.bombardero.view;

import javax.swing.JFrame;

import it.unibo.bombardero.guide.api.GuideManager;
import it.unibo.bombardero.map.api.Coord;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.utils.Utils;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Insets;

/** 
 * A class to compute the scale of the game relative to the window size and display size
 */
public final class ResizingEngine {

    /* A mischevious padding no one knows its reason to exist: */
    private final static int MISCHIEVOUS_PADDING = 23;
    private final static double initialMenuScale = 0.75;
    private final static int BUTTON_WIDTH = 346;
    private final static int BUTTON_HEIGHT = 92; 

    private double currentScale = 1.125; /* default scale size for every device */
    private final int scaledCellSize;
    private Dimension minimumFrameSize;
    private final Dimension gameWindowSize;

    private final Insets frameInsets;

    private final Dimension mapPlacingPoint;
    private final Dimension entityPlacingPoint;
    private final Dimension imageClockPosition;
    private final Dimension timerPosition;
    private final Dimension wasdGuidePosition;
    private final Dimension spaceGuidePosition;

    private final Dimension buttonSize; 

    public ResizingEngine(final BombarderoGraphics graphics) {
        int resolution = Toolkit.getDefaultToolkit().getScreenResolution();
        if(resolution >= 200) {              
            currentScale = 1.25; 
        }
        frameInsets = graphics.getParentFrame().getInsets();
        scaledCellSize = (int)(currentScale * Utils.CELL_SIZE);

        gameWindowSize = initGameWindowSize(graphics.getParentFrame());
        mapPlacingPoint = initMapPlacingPoint();
        entityPlacingPoint = initEntityPlacingPoint();
        imageClockPosition = initImageClockPosition();
        timerPosition = initTimerPosition();
        wasdGuidePosition = initWASDPosition();
        spaceGuidePosition = initSpacePosition();
        buttonSize = initButtonSize();
    }  

    /* FRAME-RELATED METHODS */

    /* Checks if the minimunFrameSize is satisfied */
    public Dimension getNewWindowSize(JFrame frame) {
        Dimension frameSize = frame.getSize();
        if(frameSize.height < minimumFrameSize.height || frameSize.width < minimumFrameSize.width) {
            return new Dimension(
                minimumFrameSize.width,
                minimumFrameSize.height
            );
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
        return new Dimension(
            gameWindowSize.width,
            gameWindowSize.height
        );
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
        return characterImage.getScaledInstance((int)Math.floor(31 * getScale()), (int)Math.floor(49 * getScale()), Image.SCALE_SMOOTH);
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

    public Image getScaledClockImage(final Image clockImage) {
        return clockImage.getScaledInstance(getScaledCellSize(), getScaledCellSize(), Image.SCALE_SMOOTH);
    }

    public Image getScaledWASDImage(final Image wasdImage) {
        return wasdImage.getScaledInstance((int)Math.floor(39 * 2.5 * getScale()), (int)Math.floor(24 * 2.5 * getScale()), Image.SCALE_SMOOTH);
    }

    public Image getScaledSpaceImage(final Image wasdImage) {
        return wasdImage.getScaledInstance((int)Math.floor(30 * 2 * getScale()), (int)Math.floor(12 * 2 * getScale()), Image.SCALE_SMOOTH);
    }

    public Image getScaledButtonImage(final Image buttonImage) {
        return buttonImage.getScaledInstance(buttonSize.width, buttonSize.height, Image.SCALE_SMOOTH);
    }
    
    /* GAME-RELATED METHODS: */
    
    public Dimension getMapPlacingPoint() {
        return new Dimension(
            mapPlacingPoint.width,
            mapPlacingPoint.height
        );
    }

    /** 
     * Returns the corner of the north-eastern cell of the map
     */
    public Dimension getEntityPlacingPoint() {
        return new Dimension(
            entityPlacingPoint.width,
            entityPlacingPoint.height
        );
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
        return new Dimension(
            (int)Math.floor(
                entityPlacingPoint.width
                + playerPosition.x() * getScaledCellSize()
                - (int)Math.floor((31 * getScale()) / 2)),
            (int)Math.floor(
                entityPlacingPoint.height
                + playerPosition.y() * getScaledCellSize()
                - (int)Math.floor(getScaledCellSize() - (49 * getScale() * (3/4))))
        );
    }

    public Dimension getImageClockPosition() {
        return new Dimension(
            imageClockPosition.width,
            imageClockPosition.height
        );
    }

    public Dimension getTimerPosition() {
        return new Dimension(
            timerPosition.width,
            timerPosition.height
        );
    }

    public Dimension getWasdGuidePosition() {
        return new Dimension(
            wasdGuidePosition.width,
            wasdGuidePosition.height
        );
    }

    public Dimension getSpaceGuidePosition() {
        return new Dimension(
            spaceGuidePosition.width,
            spaceGuidePosition.height
        );
    }

    private Dimension initMapPlacingPoint() {
        return new Dimension(
            gameWindowSize.width/2 - getMapSize().width/2 - (frameInsets.right + frameInsets.left),
            gameWindowSize.height/2 - getMapSize().height/2 - (frameInsets.top + frameInsets.bottom)
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
            (int)Math.floor(getMapPlacingPoint().width + getScaledCellSize() * 6.8),
            getMapPlacingPoint().height + getScaledCellSize() / 2
        );
    }

    private Dimension initTimerPosition() {
        Dimension clockPos = getImageClockPosition();
        return new Dimension(
            (int)Math.floor(clockPos.width + getScaledCellSize() * 1.5),
            (int)Math.floor(getMapPlacingPoint().height + getScaledCellSize() * 1.2)
        );
    }

    private Dimension initWASDPosition() {
        Dimension cell = getCharacterPlacingPoint(GuideManager.PLAYER_GUIDE_SPAWNPOINT);
        return new Dimension(
            cell.width - 39,
            cell.height + 2 * getScaledCellSize()
        );
    }

    private Dimension initSpacePosition() {
        Dimension cell = getCellPlacingPoint(GuideManager.CRATE_GUIDE_SPAWNPOINT);
        return new Dimension(
            cell.width - getScaledCellSize(),
            (int)Math.floor(cell.height + 1.5 * getScaledCellSize())
        );
    }

    private Dimension initButtonSize() {
        return new Dimension(
            (int)Math.floor(BUTTON_WIDTH / 2),
            (int)Math.floor(BUTTON_HEIGHT / 2)
        );
    }
    
}
