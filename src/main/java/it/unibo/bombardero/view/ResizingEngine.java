package it.unibo.bombardero.view;

import it.unibo.bombardero.guide.api.GuideManager;
import it.unibo.bombardero.map.api.GenPair;
import it.unibo.bombardero.utils.Utils;
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

    private static final double SPACE_DISTANCE_FROM_CELL = 1.5;
    private static final double DEFAULT_SCALE = 1.125;
    private static final int HIGH_RES_THRESHOLD = 200;
    private static final double HIGH_RES_SCALE = 1.25;
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

    /**
     * The class that manages the dynamic resizing of the game, choosing and setting 
     * a {@link #currentScale} that uses to proportionally enlarge every item in the game.
     * <p>
     * The scale is choosen by checking the display's resolution.
     * @param insets the insets of the frame, used to compute the total window size
     */
    public ResizingEngine(final Insets insets) {
        final int resolution = Toolkit.getDefaultToolkit().getScreenResolution();
        if (resolution >= HIGH_RES_THRESHOLD) {
            currentScale = HIGH_RES_SCALE;
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

    /**
     * Returns a cloned version of the current instance with the same values.
     * @return a new cloned {@link ResizingEngine}
     */
    public ResizingEngine getNewEngine() {
        return new ResizingEngine(frameInsets);
    }

    /* FRAME-RELATED METHODS */

    /**
     * Returns the size of the whole window, taking into account insets,
     * map's size and a little bit of grass.
     * @return the width and height of the game's window
     */
    public Dimension getGameWindowSize() {
        return new Dimension(
                gameWindowSize.width,
                gameWindowSize.height);
    }

    /**
     * Returns the scaled map's image size, in pixels.
     * @return the width and height of the image, in pixels
     */
    public Dimension getMapSize() {
        return new Dimension(
                (int) (Utils.MAP_WIDTH * currentScale),
                (int) (Utils.MAP_HEIGHT * currentScale));
    }

    /**
     * Returns the scaled background image size, in pixels.
     * @return the width and height of the image, in pixels
    */
    public Dimension getBackgroundImageSize() {
        return new Dimension(
                (int) (Utils.BG_WIDTH * currentScale),
                (int) (Utils.BG_HEIGHT * currentScale));
    }

    /* GENERAL UTILITY GETTERS */

    /**
     * Returns the scale by which everything is scaled.
     * @return the scale of the view
     */
    public double getScale() {
        return currentScale;
    }

    /**
     * Returns the Cell's size, scaled by the current scale.
     * @return the scaled cell size
     */
    public int getScaledCellSize() {
        return scaledCellSize;
    }

    /* SCALING-RELATED METHODS: */
    // CHECKSTYLE: MagicNumber OFF
    /**
     * Scales a passed image using the view's scale.
     * @param cellImage the image to scale
     * @return the scaledc cell image
     */
    public Image getScaledCellImage(final Image cellImage) {
        return cellImage.getScaledInstance(getScaledCellSize(), (int) (getScaledCellSize() + 7 * getScale()),
                Image.SCALE_SMOOTH);
    }

    /**
     * Scales a Character's image using the view's scale.
     * @param characterImage the image to scale
     * @return the scaled character image
     */
    public Image getScaledCharacterImage(final Image characterImage) {
        return characterImage.getScaledInstance((int) Math.floor(31 * getScale()), (int) Math.floor(49 * getScale()),
                Image.SCALE_SMOOTH);
    }
    // CHECKSTYLE: MagicNumber ON

    /**
     * Scales a bomb's image using the view's scale.
     * @param bombImage the image to scale
     * @return the scaled bomb's image
     */
    public Image getScaledBombImage(final Image bombImage) {
        return bombImage;
    }

    /**
     * Scales the background image using the view's scale.
     * @param backgroundImage the image to scale
     * @return the scaled bomb's image
     */
    public Image getScaledBackgroundImage(final Image backgroundImage) {
        return backgroundImage.getScaledInstance(getBackgroundImageSize().width, getBackgroundImageSize().height,
                Image.SCALE_SMOOTH);
    }

    /**
     * Scales the map's image using the view's scale.
     * @param mapImage the image to scale
     * @return the scaled map's image
     */
    public Image getScaledMapImage(final Image mapImage) {
        return mapImage.getScaledInstance(getMapSize().width, getMapSize().height, Image.SCALE_SMOOTH);
    }

    /**
     * Scales the clock's image using the view's scale.
     * @param clockImage the image to scale
     * @return the scaled map's image
     */
    public Image getScaledClockImage(final Image clockImage) {
        return clockImage.getScaledInstance(getScaledCellSize(), getScaledCellSize(), Image.SCALE_SMOOTH);
    }

    // CHECKSTYLE: MagicNumber OFF
    /**
     * Scales the sprite's image using the view's scale.
     * @param wasdImage the image to scale
     * @return the sprite's scaled image
     */
    public Image getScaledWASDImage(final Image wasdImage) {
        return wasdImage.getScaledInstance((int) Math.floor(39 * 2.5 * getScale()),
                (int) Math.floor(24 * 2.5 * getScale()), Image.SCALE_SMOOTH);
    }

    /** 
     * Scales the sprite's image using the view's scale.
     * @param spaceBarImage the image to scale
     * @return the sprite's scaled image
     */
    public Image getScaledSpaceImage(final Image spaceBarImage) {
        return spaceBarImage.getScaledInstance((int) Math.floor(30 * 2 * getScale()), (int) Math.floor(12 * 2 * getScale()),
                Image.SCALE_SMOOTH);
    }
    // CHECKSTYLE: MagicNumber ON
    /** 
     * Scales the button's image using the view's scale.
     * @param buttonImage the image to scale
     * @return the sprite's scaled image
     */
    public Image getScaledButtonImage(final Image buttonImage) {
        return buttonImage.getScaledInstance(buttonSize.width, buttonSize.height, Image.SCALE_SMOOTH);
    }

    /** 
     * Scales the menu's image using the view's scale.
     * @param menuLogoImage the image to scale
     * @return the sprite's scaled image
     */
    public Image getScaledMenuLogoImage(final Image menuLogoImage) {
        return menuLogoImage.getScaledInstance(menuLogoSize.width, menuLogoSize.height, Image.SCALE_SMOOTH);
    }

    /** 
     * Crops the background image passed.
     * @param menuBackgroundImage the image to scale
     * @return the sprite's scaled image
     */
    public Image getSubImageFromBackground(final BufferedImage menuBackgroundImage) {
        final Dimension cropSize = gameWindowSize;
        return menuBackgroundImage.getSubimage(
                MENU_BACKGROUND_WIDTH - cropSize.width,
                MENU_BACKGROUND_HEIGHT - cropSize.height,
                cropSize.width,
                cropSize.height);
    }

    /* GAME-RELATED METHODS: */

    /** 
     * Returns the coordinates where the map has to be rendered.
     * @return the top-left corner of where the map will be placed
     */
    public Dimension getMapPlacingPoint() {
        return new Dimension(
                mapPlacingPoint.width,
                mapPlacingPoint.height);
    }

    /**
     * Returns the corner of the top-left cell of the map.
     * @return the coordinate of the top-left corner of the top-left cell
     * of the map
     */
    public Dimension getEntityPlacingPoint() {
        return new Dimension(
                entityPlacingPoint.width,
                entityPlacingPoint.height);
    }

    /**
     * Returns the upper-left corner of the cell at the {@link #cooordinate} of the map.
     * @param coordinate the position of the cell on the map
     * @return the coordinate of the top-left corner of the requested cell.
     */
    public Dimension getCellPlacingPoint(final GenPair<Integer, Integer> coordinate) {
        return new Dimension(
                entityPlacingPoint.width + (int) (getScaledCellSize() * coordinate.x()),
                entityPlacingPoint.height + (int) (getScaledCellSize() * coordinate.y()));
    }

    /**
     * Returns the point where the bomb has to be drawed.
     * @param coordinate the cell where the bomb sits
     * @return the coordinate of the top-left corner of the bomb's image
     */
    public Dimension getBombPlacingPoint(final GenPair<Integer, Integer> coordinate) {
        final Dimension placingPoint = getCellPlacingPoint(coordinate);
        return new Dimension(
                placingPoint.width + (int) (getScaledCellSize() / 2),
                placingPoint.height + (int) (getScaledCellSize() / 2));
    }

    // CHECKSTYLE: MagicNumber OFF
    /**
     * Returns the character's placing point.
     * @param playerPosition the position of the character in the map, as a couple of floats.
     * @return the upper-left corner of the character's image where the image will be drawed
     */
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

    /**
     * Returns the position where the clock will be drawed.
     * @return the upper-left corner of the clock's image
     */
    public Dimension getImageClockPosition() {
        return new Dimension(
                imageClockPosition.width,
                imageClockPosition.height);
    }

    /**
     * Returns the position where the timer will be drawed.
     * @return the position where the first character of the timer will be drawed
     */
    public Dimension getTimerPosition() {
        return new Dimension(
                timerPosition.width,
                timerPosition.height);
    }

    /** 
     * Returns the position where the WASD sprite will be drawed.
     * @return the position of the upper-left corner of the sprite 
     */
    public Dimension getWasdGuidePosition() {
        return new Dimension(
                wasdGuidePosition.width,
                wasdGuidePosition.height);
    }

    /** 
     * Returns the position where the SPACEBAR sprite will be drawed.
     * @return the position of the upper-left corner of the sprite 
     */
    public Dimension getSpaceGuidePosition() {
        return new Dimension(
                spaceGuidePosition.width,
                spaceGuidePosition.height);
    }

    /** 
     * Returns the position where the menu's logo will be drawed.
     * @return the position of the upper-left corner of the image 
     */
    public Dimension getMenuLogoSize() {
        return new Dimension(
                menuLogoSize.width,
                menuLogoSize.height);
    }

    private Dimension initGameWindowSize(final Insets frameInsets) {
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

    // CHECKSTYLE: MagicNumber OFF
    private Dimension initEntityPlacingPoint() {
        return new Dimension(
                getMapPlacingPoint().width + getScaledCellSize() + (int) (getScale() * MISCHIEVOUS_PADDING),
                getMapPlacingPoint().height + 2 * getScaledCellSize() - (int) (7 * getScale()));
    }

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
                (int) Math.floor(cell.height + SPACE_DISTANCE_FROM_CELL * getScaledCellSize()));
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
