package it.unibo.bombardero.view;

import javax.swing.JFrame;

import it.unibo.bombardero.guide.api.GuideManager;
import it.unibo.bombardero.map.api.GenPair;
import it.unibo.bombardero.utils.Utils;
import it.unibo.bombardero.view.api.GraphicsEngine;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Insets;
import java.awt.image.BufferedImage;
/*
 * NOTE: Due to the impressive quantity of references made to the sizes of the game's 
 * assets, the checkstyles have been suppressed in certain areas.
 */

/**
 * A class to compute the scale of the game relative to the window size and
 * display size.
 */
public final class ResizingEngine {

    private static final double DEFAULT_SCALE = 1.125;
    private static final int HIGH_RES_THRESHOLD = 200;
    private static final double MENU_LOGO_SCALE = 0.90;

    /* A mischevious padding no one knows its reason to exist: */
    private static final int MISCHIEVOUS_PADDING = 23;

    /* Constants for resources: */
    private static final int BUTTON_WIDTH = 346;
    private static final int BUTTON_HEIGHT = 92;
    private static final int MENU_BACKGROUND_WIDTH = 3840;
    private static final int MENU_BACKGROUND_HEIGHT = 2160;
    private static final int MENU_LOGO_WIDTH = 1522;
    private static final int MENU_LOGO_HEIGHT = 362;

    private double currentScale = DEFAULT_SCALE; /* default scale size for every device */
    private final int scaledCellSize;
    private final Dimension gameWindowSize;

    private final Insets frameInsets;

    private final Dimension mapPlacingPoint;
    private final Dimension entityPlacingPoint;
    private final Dimension imageClockPosition;
    private final Dimension timerPosition;
    private final Dimension wasdGuidePosition;
    private final Dimension spaceGuidePosition;

    private final Dimension buttonSize;
    private final Dimension menuLogoSize;

    public ResizingEngine(final GraphicsEngine graphics, final Insets insets) {
        final int resolution = Toolkit.getDefaultToolkit().getScreenResolution();
        if (resolution >= HIGH_RES_THRESHOLD) {
            currentScale = 1.25;
        }
        frameInsets = new Insets(insets.top, insets.left, insets.bottom, insets.right);
        scaledCellSize = (int) (currentScale * Utils.CELL_SIZE);

        gameWindowSize = initGameWindowSize(insets);
        mapPlacingPoint = initMapPlacingPoint();
        entityPlacingPoint = initEntityPlacingPoint();
        imageClockPosition = initImageClockPosition();
        timerPosition = initTimerPosition();
        wasdGuidePosition = initWASDPosition();
        spaceGuidePosition = initSpacePosition();
        buttonSize = initButtonSize();
        menuLogoSize = initLogoSize();
    }

    public ResizingEngine getNewEngine(final GraphicsEngine graphics) {
        return new ResizingEngine(graphics, frameInsets);
    }

    /* FRAME-RELATED METHODS */

    /*
     * The initial windows size is calculated scaling the map and adding some grass
     * on the sides, other than the insets
     */
    public Dimension getGameWindowSize(final JFrame frame) {
        return new Dimension(
                gameWindowSize.width,
                gameWindowSize.height);
    }

    public Dimension getMapSize() {
        return new Dimension(
                (int) (Utils.MAP_WIDTH * currentScale),
                (int) (Utils.MAP_HEIGHT * currentScale));
    }

    public Dimension getBackgroundImageSize() {
        return new Dimension(
                (int) (Utils.BG_WIDTH * currentScale),
                (int) (Utils.BG_HEIGHT * currentScale));
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
    // CHECKSTYLE: MagicNumber OFF
    public Image getScaledCellImage(final Image cellImage) {
        return cellImage.getScaledInstance(getScaledCellSize(), (int) (getScaledCellSize() + 7 * getScale()),
                Image.SCALE_SMOOTH);
    }

    public Image getScaledCharacterImage(final Image characterImage) {
        return characterImage.getScaledInstance((int) Math.floor(31 * getScale()), (int) Math.floor(49 * getScale()),
                Image.SCALE_SMOOTH);
    }
    // CHECKSTYLE: MagicNumber ON

    public Image getScaledBombImage(final Image bombImage) {
        return bombImage;
    }

    public Image getScaledBackgroundImage(final Image backgroundImage) {
        return backgroundImage.getScaledInstance(getBackgroundImageSize().width, getBackgroundImageSize().height,
                Image.SCALE_SMOOTH);
    }

    public Image getScaledMapImage(final Image mapImage) {
        return mapImage.getScaledInstance(getMapSize().width, getMapSize().height, Image.SCALE_SMOOTH);
    }

    public Image getScaledClockImage(final Image clockImage) {
        return clockImage.getScaledInstance(getScaledCellSize(), getScaledCellSize(), Image.SCALE_SMOOTH);
    }

    // CHECKSTYLE: MagicNumber OFF
    public Image getScaledWASDImage(final Image wasdImage) {
        return wasdImage.getScaledInstance((int) Math.floor(39 * 2.5 * getScale()),
                (int) Math.floor(24 * 2.5 * getScale()), Image.SCALE_SMOOTH);
    }

    public Image getScaledSpaceImage(final Image wasdImage) {
        return wasdImage.getScaledInstance((int) Math.floor(30 * 2 * getScale()), (int) Math.floor(12 * 2 * getScale()),
                Image.SCALE_SMOOTH);
    }
    // CHECKSTYLE: MagicNumber ON

    public Image getScaledButtonImage(final Image buttonImage) {
        return buttonImage.getScaledInstance(buttonSize.width, buttonSize.height, Image.SCALE_SMOOTH);
    }

    public Image getScaledMenuLogoImage(final Image menuLogoImage) {
        return menuLogoImage.getScaledInstance(menuLogoSize.width, menuLogoSize.height, Image.SCALE_SMOOTH);
    }

    public Image getSubImageFromBackground(final BufferedImage menuBackgroundImage) {
        final Dimension cropSize = gameWindowSize;
        return menuBackgroundImage.getSubimage(
                MENU_BACKGROUND_WIDTH - cropSize.width,
                MENU_BACKGROUND_HEIGHT - cropSize.height,
                cropSize.width,
                cropSize.height);
    }

    /* GAME-RELATED METHODS: */

    public Dimension getMapPlacingPoint() {
        return new Dimension(
                mapPlacingPoint.width,
                mapPlacingPoint.height);
    }

    /**
     * Returns the corner of the north-eastern cell of the map
     */
    public Dimension getEntityPlacingPoint() {
        return new Dimension(
                entityPlacingPoint.width,
                entityPlacingPoint.height);
    }

    /**
     * Returns the upper-left corner of the cell at {@link #cooordinate}
     */
    public Dimension getCellPlacingPoint(final GenPair<Integer, Integer> coordinate) {
        return new Dimension(
                entityPlacingPoint.width + (int) (getScaledCellSize() * coordinate.x()),
                entityPlacingPoint.height + (int) (getScaledCellSize() * coordinate.y()));
    }

    public Dimension getBombPlacingPoint(final GenPair<Integer, Integer> coordinate) {
        final Dimension placingPoint = getCellPlacingPoint(coordinate);
        return new Dimension(
                placingPoint.width + (int) (getScaledCellSize() / 2),
                placingPoint.height + (int) (getScaledCellSize() / 2));
    }

    // CHECKSTYLE: MagicNumber OFF
    public Dimension getCharacterPlacingPoint(final GenPair<Float, Float> playerPosition) {
        return new Dimension(
                (int) Math.floor(
                        entityPlacingPoint.width
                                + playerPosition.x() * getScaledCellSize()
                                - (int) Math.floor((31 * getScale()) / 2)),
                (int) Math.floor(
                        entityPlacingPoint.height
                                + playerPosition.y() * getScaledCellSize()
                                - (int) Math.floor(getScaledCellSize() - (49 * getScale() * (3 / 4)))));
    }
    // CHECKSTYLE: MagicNumber ON

    public Dimension getImageClockPosition() {
        return new Dimension(
                imageClockPosition.width,
                imageClockPosition.height);
    }

    public Dimension getTimerPosition() {
        return new Dimension(
                timerPosition.width,
                timerPosition.height);
    }

    public Dimension getWasdGuidePosition() {
        return new Dimension(
                wasdGuidePosition.width,
                wasdGuidePosition.height);
    }

    public Dimension getSpaceGuidePosition() {
        return new Dimension(
                spaceGuidePosition.width,
                spaceGuidePosition.height);
    }

    public Dimension getMenuLogoSize() {
        return new Dimension(
                menuLogoSize.width,
                menuLogoSize.height);
    }

    public Dimension initGameWindowSize(final Insets frameInsets) {
        return new Dimension(
                frameInsets.left + frameInsets.right
                        + (int) (Utils.MAP_WIDTH * currentScale + Utils.GRASS_PADDING_RATIO * Utils.MAP_WIDTH),
                frameInsets.top + frameInsets.bottom
                        + (int) (Utils.MAP_HEIGHT * currentScale + Utils.GRASS_PADDING_RATIO * Utils.MAP_HEIGHT));
    }

    private Dimension initMapPlacingPoint() {
        return new Dimension(
                gameWindowSize.width / 2 - getMapSize().width / 2 - frameInsets.right + frameInsets.left,
                gameWindowSize.height / 2 - getMapSize().height / 2 - frameInsets.top + frameInsets.bottom);
    }

    /**
     * Returns the corner of the north-eastern cell of the map
     */
    // CHECKSTYLE: MagicNumber OFF
    private Dimension initEntityPlacingPoint() {
        return new Dimension(
                getMapPlacingPoint().width + getScaledCellSize() + (int) (getScale() * MISCHIEVOUS_PADDING),
                getMapPlacingPoint().height + 2 * getScaledCellSize() - (int) (7 * getScale()));
    }
    // CHECKSTYLE: MagicNumber ON

    private Dimension initImageClockPosition() {
        return new Dimension(
                (int) Math.floor(getMapPlacingPoint().width + getScaledCellSize() * 6.8),
                getMapPlacingPoint().height + getScaledCellSize() / 2);
    }

    private Dimension initTimerPosition() {
        final Dimension clockPos = getImageClockPosition();
        return new Dimension(
                (int) Math.floor(clockPos.width + getScaledCellSize() * 1.5),
                (int) Math.floor(getMapPlacingPoint().height + getScaledCellSize() * 1.2));
    }

    // CHECKSTYLE: MagicNumber OFF
    private Dimension initWASDPosition() {
        final Dimension cell = getCharacterPlacingPoint(GuideManager.PLAYER_GUIDE_SPAWNPOINT);
        return new Dimension(
                cell.width - 39,
                cell.height + 2 * getScaledCellSize());
    }
    // CHECKSTYLE: MagicNumber ON

    private Dimension initSpacePosition() {
        final Dimension cell = getCellPlacingPoint(GuideManager.CRATE_GUIDE_SPAWNPOINT);
        return new Dimension(
                cell.width - getScaledCellSize(),
                (int) Math.floor(cell.height + 1.5 * getScaledCellSize()));
    }

    private Dimension initButtonSize() {
        return new Dimension(
                (int) Math.floorDiv(BUTTON_WIDTH, 2),
                (int) Math.floorDiv(BUTTON_HEIGHT, 2));
    }

    private Dimension initLogoSize() {
        final float scale = (float) MENU_LOGO_HEIGHT / (float) MENU_LOGO_WIDTH;
        return new Dimension(
                (int) Math.floor(gameWindowSize.width * MENU_LOGO_SCALE),
                (int) Math.floor(gameWindowSize.width * MENU_LOGO_SCALE * scale));
    }

}
