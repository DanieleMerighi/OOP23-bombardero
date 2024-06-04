package it.unibo.bombardero.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;

import it.unibo.bombardero.cell.Cell;
import it.unibo.bombardero.cell.Cell.CellType;
import it.unibo.bombardero.cell.powerup.api.PowerUp;
import it.unibo.bombardero.core.KeyboardInput;
import it.unibo.bombardero.core.api.Controller;
import it.unibo.bombardero.utils.Utils;
import it.unibo.bombardero.view.sprites.BombarderoSprite;
import it.unibo.bombardero.view.sprites.BombarderoOrientedSprite;
import it.unibo.bombardero.view.sprites.GenericBombarderoSprite;
import it.unibo.bombardero.map.api.Coord;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.character.Character;
import it.unibo.bombardero.character.Direction;

/**
 * This class is the panel containing the game view.
 * @author Federico Bagattoni
 */
public final class GameCard extends GamePlayCard {

    /** 
     * For the first part of the class, the magic number checkstyle will be 
     * deactivated, as the resource are imported with their file name.
     */
    // CHECKSTYLE: MagicNumber OFF

    /* A mischevious padding no one knows its reason to exist: */
    private final static int MISCHIEVOUS_PADDING = 23;
    private final static SimpleDateFormat format = new SimpleDateFormat("mm:ss");

    /* Game resources: */
    private final ResourceGetter resourceGetter = new ResourceGetter();
    private final Image grass_bg_image = resourceGetter.loadImage("grass_background");
    private final Image map = resourceGetter.loadImage("map_square_nowalls");
    private Image obstacle = resourceGetter.loadImage("obstacles/cassa_prosp3");
    private Image unbreakable = resourceGetter.loadImage("obstacles/wall_prosp2");
    private Image clock = resourceGetter.loadImage("overlay/clock");
    private final Font font = resourceGetter.loadFont("clock_font");
    private Image bombLine = resourceGetter.loadImage("powerup/line_bomb");
    private Image bombPlusOne = resourceGetter.loadImage("powerup/bomb_minus_one");
    private Image bombMinusOne = resourceGetter.loadImage("powerup/bomb_plus_one");
    private Image bombPower = resourceGetter.loadImage("powerup/bomb_power");
    private Image bombRemote = resourceGetter.loadImage("powerup/bomb_remote");
    private Image bombPierce = resourceGetter.loadImage("powerup/bomb_pierce");
    private Image fireMax = resourceGetter.loadImage("powerup/fire_max");
    private Image firePlusOne = resourceGetter.loadImage("powerup/fire_plusone");
    private Image fireMinusOne = resourceGetter.loadImage("powerup/fire_minusone");
    private Image skatesPlusOne = resourceGetter.loadImage("powerup/skates_plus_one");
    private Image skateMinusOne = resourceGetter.loadImage("powerup/skates_minus_one");
    private Image skull = resourceGetter.loadImage("powerup/skull");
    /* TODO: KICK MANCANTE, DECIDERE SE FARLO */

    /* References to model components: */
    private final JFrame parentFrame;
    private final ResizingEngine resizingEngine;
    private Map<Pair, Cell> cells;
    private final Controller controller;
    private final Character player;
    //private final List<Character> enemies;

    /* Pause state buttons: */
    private final JButton resumeButton = new JButton("Resume");
    private final JButton quitButton = new JButton("Quit");
    
    /* Sprites: */
    private BombarderoOrientedSprite playerSprite;
    //private final BombarderoOrientedSprite[] enemySprite = new GenericBombarderoSprite[Utils.NUM_OF_ENEMIES];
    private final BombarderoSprite normalBomb = new GenericBombarderoSprite("bomb", resourceGetter, 4);
    private Image player_image;
    //private final Image[] enemiesImages = new Image[Utils.NUM_OF_ENEMIES];
    private Image bomb_image;

    /* Static positions for quicker access: */
    private Dimension mapPlacingPoint;
    private Dimension entityPlacingPoint; // the upper-left corner of the first cell, readily available
    private Dimension imageClockPosition;
    private Dimension timerPosition;
    private int overlayLevel;

    public GameCard(final JFrame parentFrame, final ResizingEngine resizingEngine, final Controller controller) {
        this.parentFrame = parentFrame;
        this.resizingEngine = resizingEngine;
        this.controller = controller;
        this.setMinimumSize(resizingEngine.getMapSize());
        this.setLayout(new BorderLayout());

        scaleEverything();

        cells = controller.getMap(); 
        player = controller.getMainPlayer();
        //enemies = controller.getEnemies();

        playerSprite = new GenericBombarderoSprite("character/main/walking", resourceGetter, Direction.DOWN);
        player_image = playerSprite.getStandingImage();
        /*for (int i = 0; i < Utils.NUM_OF_ENEMIES; i++) {
            enemySprite[i] = new GenericBombarderoSprite("character/main/walking", resourceGetter, Direction.DOWN);
            enemiesImages[i] = enemySprite[i].getStandingImage();
        }*/
        bomb_image = normalBomb.getImage();

        this.setFont(font);
    }

    // CHECKSTYLE: MagicNumber OFF

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        /* If the scale changes, then scale again the images */
        if(resizingEngine.hasScaleChanged()) {
            scaleEverything();
            /* TODO: scale the rest of the resources */
        }
        /* Drawing the Map and the Background */
        Dimension actualMapSize = resizingEngine.getMapSize();
        Dimension actualBgSize = resizingEngine.getBackgroundImageSize();
        g2d.drawImage(
            grass_bg_image.getScaledInstance(actualBgSize.width, actualBgSize.height, Image.SCALE_SMOOTH),
            0, 0,
            null);
        g2d.drawImage(
            map.getScaledInstance(actualMapSize.width, actualMapSize.height, Image.SCALE_SMOOTH),
            computeMapPlacingPoint().width,
            computeMapPlacingPoint().height,
            null
        );
        /* Load the game overlay, including: the clock logo, the time left and the players icons with the "alive" status */
        g2d.setFont(font);
        g2d.drawString(getFormattedTime(), timerPosition.width, timerPosition.height);
        g2d.drawImage(clock,imageClockPosition.width, imageClockPosition.height, null);
        /* Drawing the breakable obstacles, the bombs and the power ups (TODO: not done yet) */
        for (int i = 0; i < Utils.MAP_ROWS; i++) {
            for (int j = 0; j < Utils.MAP_COLS; j++) {
                if (cells.containsKey(new Pair(i, j))) {
                    Cell entry = cells.get(new Pair(i ,j));
                    Image img = unbreakable;
                    Dimension placingPoint;
                    switch (entry.getCellType()) {
                        case WALL_BREAKABLE:
                            img = obstacle;
                            placingPoint = computeCellPlacingPoint(new Pair(i, j));
                            break;
                        case WALL_UNBREAKABLE:
                            img = unbreakable;
                            placingPoint = computeCellPlacingPoint(new Pair(i, j));
                            break;
                        case BOMB: 
                            img = bomb_image;
                            placingPoint = computeBombPlacingPoint(new Pair(i, j));
                            break;
                        case POWERUP:
                            PowerUp pu = (PowerUp)entry;
                            img = switch (pu.getType()) {
                                case REMOTE_BOMB -> bombRemote;
                                case PIERCING_BOMB -> bombPierce;
                                case POWER_BOMB -> bombPower;
                                case LINE_BOMB -> bombLine;
                                case PLUS_ONE_BOMB -> bombPlusOne;
                                case MINUS_ONE_BOMB -> bombMinusOne;
                                case PLUS_ONE_FLAME_RANGE -> firePlusOne;
                                case MINUS_ONE_FLAME_RANGE -> fireMinusOne;
                                case MAX_FLAME_RANGE -> fireMax;
                                case PLUS_ONE_SKATES -> skatesPlusOne;
                                case MINUS_ONE_SKATES -> skateMinusOne;
                                case KICK -> unbreakable;
                                case SKULL -> skull;
                                default -> throw new IllegalArgumentException("texture not present for \"" + pu.getType() + "\"");
                            };
                            placingPoint = computeCellPlacingPoint(new Pair(i, j));
                            break;
                        default:
                            img = unbreakable;
                            placingPoint = computeCellPlacingPoint(new Pair(i, j));
                            break;
                    }
                    g2d.drawImage(
                        img,
                        placingPoint.width,
                        placingPoint.height,
                        null
                    );
                }
            
            }
        }
        /* Drawing the player and the enemies */
        Dimension playerPosition = computeCharacterPlacingPoint(controller.getMainPlayer().getCharacterPosition());
        g2d.drawImage(player_image.getScaledInstance(35, 55, Image.SCALE_SMOOTH),
            playerPosition.width, playerPosition.height,
            null
        );
        /*for(int i = 0; i < Utils.NUM_OF_ENEMIES; i++) {
            Dimension enemyPos = computeCharacterPlacingPoint(controller.getEnemies().get(i).getCharacterPosition());
            g2d.drawImage(enemiesImages[i], enemyPos.width, enemyPos.height, null);
        } */
    }

    public void updateMap() {
        cells = controller.getMap();
        updateSprites();
    }

    public void updateSprites() {
        /* Update player sprites: */
        playerSprite.update();
        if (player.isStationary()) {
            player_image = playerSprite.getStandingImage();
        }
        else if (playerSprite.getCurrentFacingDirection().equals(player.getFacingDirection())) {
            player_image = playerSprite.getImage();
        }
        else {
            playerSprite = playerSprite.getNewSprite(player.getFacingDirection());
            player_image = playerSprite.getImage();
        }
        /* Update enemy sprites: */
        /*for (int i = 0; i < Utils.NUM_OF_ENEMIES; i++) {
            enemySprite[i].update();
            if(enemies.get(i).getFacingDirection().equals(Direction.DEFAULT)) {
                enemiesImages[i] = enemySprite[i].getStandingImage();
            }
            else {
                enemySprite[i] = enemySprite[i].getNewSprite(enemies.get(i).getFacingDirection());
                enemiesImages[i] = enemySprite[i].getStandingImage();
            }
        } */
        /* Update bomb sprites: */
        normalBomb.update();
        bomb_image = normalBomb.getImage();
    }

    public void setPausedView() {
        this.add(quitButton, BorderLayout.CENTER);
        this.add(resumeButton, BorderLayout.CENTER);
        this.repaint(0);
    }

    public void setUnpausedView() {
        this.remove(quitButton);
        this.remove(resumeButton);
        this.repaint(0);
    }

    private Dimension computeMapPlacingPoint() {
        return new Dimension(
            parentFrame.getSize().width/2 - resizingEngine.getMapSize().width/2 - (parentFrame.getInsets().right + parentFrame.getInsets().left),
            parentFrame.getSize().height/2 - resizingEngine.getMapSize().height/2 - (parentFrame.getInsets().top + parentFrame.getInsets().bottom)
        );
    }

    private Dimension computeEntityPlacingPoint() {
        return new Dimension(
            mapPlacingPoint.width + resizingEngine.getScaledCellSize() + (int)(resizingEngine.getScale() * MISCHIEVOUS_PADDING),
            mapPlacingPoint.height + 2 * resizingEngine.getScaledCellSize() - (int)(7 * resizingEngine.getScale())
        );
    }

    private Dimension computeCellPlacingPoint(Pair coordinate) {
        return new Dimension(
            entityPlacingPoint.width + (int)(resizingEngine.getScaledCellSize() * coordinate.row()),
            entityPlacingPoint.height + (int)(resizingEngine.getScaledCellSize() * coordinate.col())
        );
    }

    private Dimension computeBombPlacingPoint(Pair coordinate) {
        Dimension placingPoint = computeCellPlacingPoint(coordinate);
        return new Dimension(
            placingPoint.width + (int)(resizingEngine.getScaledCellSize() / 2),
            placingPoint.height + (int)(resizingEngine.getScaledCellSize() / 2)
        );
    }

    private Dimension computeImageClockPosition() {
        return new Dimension(
            mapPlacingPoint.width + Utils.MAP_WIDTH / 2 - resizingEngine.getScaledCellSize() / 2,
            overlayLevel + resizingEngine.getScaledCellSize() / 2
        );
    }

    private Dimension computeTimerPosition() {
        Dimension clockPos = computeImageClockPosition();
        return new Dimension(
            (int)Math.floor(clockPos.width + resizingEngine.getScaledCellSize() * 1.5),
            overlayLevel + resizingEngine.getScaledCellSize()
        );
    }

    /* The Character object's position contains a float coordinate representing the center of the charater therefore we
     * have to compute the placing point for the character's image
     * SE NON VA PROVA AD INVERTIRE row E col
     */
    private Dimension computeCharacterPlacingPoint(final Coord playerPosition) {

        return new Dimension(
            mapPlacingPoint.width
            + (int)(Math.floor(playerPosition.row() * resizingEngine.getScaledCellSize())
                - Math.floorDiv(Utils.PLAYER_WIDTH, 2)
                + resizingEngine.getScaledCellSize()
                + resizingEngine.getScale() * MISCHIEVOUS_PADDING),
            mapPlacingPoint.height 
            + (int)(Math.floor(playerPosition.col() * resizingEngine.getScaledCellSize())
                - Utils.PLAYER_HEIGHT * 0.85
                + 2 * resizingEngine.getScaledCellSize())
        );
    }

    private int computeOverlayLevel() {
        return computeMapPlacingPoint().height;
    }

    private String getFormattedTime() {
        return format.format(new Date(controller.getTimeLeft()));
    }   

    private void scaleEverything() {
        mapPlacingPoint = computeMapPlacingPoint();
        entityPlacingPoint = computeEntityPlacingPoint();
        overlayLevel = computeOverlayLevel();
        imageClockPosition = computeImageClockPosition();
        timerPosition = computeTimerPosition();

        unbreakable = unbreakable.getScaledInstance((int)(resizingEngine.getScale() * 32), (int)resizingEngine.getScale() * 39, Image.SCALE_SMOOTH);
        obstacle = obstacle.getScaledInstance((int)(resizingEngine.getScale() * 32), (int)(resizingEngine.getScale() * 39), Image.SCALE_SMOOTH);
        clock = clock.getScaledInstance(resizingEngine.getScaledCellSize(), resizingEngine.getScaledCellSize(), Image.SCALE_SMOOTH);
        
        bombLine = bombLine.getScaledInstance((int)resizingEngine.getScale() * 32, (int)(resizingEngine.getScale() * 39), Image.SCALE_SMOOTH);
        bombPlusOne = bombPlusOne.getScaledInstance((int)resizingEngine.getScale() * 32, (int)(resizingEngine.getScale() * 39), Image.SCALE_SMOOTH);
        bombMinusOne = bombMinusOne.getScaledInstance((int)resizingEngine.getScale() * 32, (int)(resizingEngine.getScale() * 39), Image.SCALE_SMOOTH);
        bombPower = bombPower.getScaledInstance((int)resizingEngine.getScale() * 32, (int)(resizingEngine.getScale() * 39), Image.SCALE_SMOOTH);
        bombRemote = bombRemote.getScaledInstance((int)resizingEngine.getScale() * 32, (int)(resizingEngine.getScale() * 39), Image.SCALE_SMOOTH);
        bombPierce = bombPierce.getScaledInstance((int)resizingEngine.getScale() * 32, (int)(resizingEngine.getScale() * 39), Image.SCALE_SMOOTH);
        fireMax = fireMax.getScaledInstance((int)resizingEngine.getScale() * 32, (int)(resizingEngine.getScale() * 39), Image.SCALE_SMOOTH);
        firePlusOne = firePlusOne.getScaledInstance((int)resizingEngine.getScale() * 32, (int)(resizingEngine.getScale() * 39), Image.SCALE_SMOOTH);
        fireMinusOne = fireMinusOne.getScaledInstance((int)resizingEngine.getScale() * 32, (int)(resizingEngine.getScale() * 39), Image.SCALE_SMOOTH);
        skatesPlusOne = skatesPlusOne.getScaledInstance((int)resizingEngine.getScale() * 32, (int)(resizingEngine.getScale() * 39), Image.SCALE_SMOOTH);
        skateMinusOne = skateMinusOne.getScaledInstance((int)resizingEngine.getScale() * 32, (int)(resizingEngine.getScale() * 39), Image.SCALE_SMOOTH);
        skull = skull.getScaledInstance((int)resizingEngine.getScale() * 32, (int)(resizingEngine.getScale() * 39), Image.SCALE_SMOOTH);
    }
}