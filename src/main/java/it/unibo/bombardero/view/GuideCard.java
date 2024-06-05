package it.unibo.bombardero.view;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JFrame;

import it.unibo.bombardero.cell.Cell;
import it.unibo.bombardero.cell.powerup.api.PowerUp;
import it.unibo.bombardero.character.Direction;
import it.unibo.bombardero.character.Character;
import it.unibo.bombardero.core.api.Controller;
import it.unibo.bombardero.map.api.Coord;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.utils.Utils;
import it.unibo.bombardero.view.sprites.BombarderoOrientedSprite;
import it.unibo.bombardero.view.sprites.BombarderoSprite;
import it.unibo.bombardero.view.sprites.GenericBombarderoSprite;

/**
 * This class contains the panel for the Guide of
 * the game. It essentially works the same as the {@link GameCard}
 * with minor adjustments; such as removal of the timer and the Graphics
 * and adding instruction for the player.
 * <p>
 * Instructions on what to display are given by the {@link Controller}
 * through appropriate methods.
 * @author Federico Bagattoni
 */
public final class GuideCard extends GamePlayCard {

    private final static int MISCHIEVOUS_PADDING = 23;

    private final ResourceGetter resourceGetter;
    private final ResizingEngine resizingEngine;
    private final BombarderoGraphics graphicsEngine;
    private final Controller controller;
    private final JFrame parentFrame;

    private Map<Pair, Cell> cells;
    private final Character player;
    private final List<Character> enemies;
    
    private final Image grass_bg_image;
    private final Image map;
    private Image obstacle;
    private Image unbreakable;

    private BombarderoOrientedSprite playerSprite;
    private final BombarderoSprite normalBombSprite;
    
    private Image playerImage;
    private Image enemyImage;
    private Image bombImage;
    private Image flamePowerUpImage;

    /* Static positions for quicker access: */
    private Dimension mapPlacingPoint;
    private Dimension entityPlacingPoint; // the upper-left corner of the first cell, readily available
    private Dimension imageClockPosition;
    private Dimension timerPosition;
    private int overlayLevel;

    public GuideCard(final JFrame parentFrame, final Controller controller, final BombarderoGraphics graphicsEngine, final ResourceGetter resourceGetter, final ResizingEngine resizingEngine) {
        this.resourceGetter = resourceGetter;
        this.controller = controller;
        this.graphicsEngine = graphicsEngine;
        this.resizingEngine = resizingEngine;
        this.parentFrame = parentFrame;

        mapPlacingPoint = computeMapPlacingPoint();
        entityPlacingPoint = computeEntityPlacingPoint();
        
        player = controller.getMainPlayer();
        enemies = controller.getEnemies();

        // CHECKSTYLE: MagicNumbers OFF
        playerSprite = new GenericBombarderoSprite("character/main/walking", resourceGetter, Direction.DOWN);
        normalBombSprite = new GenericBombarderoSprite("bomb", resourceGetter, 4);
        enemyImage = resourceGetter.loadImage("character/main/walking/down/down_standing");
        grass_bg_image = resourceGetter.loadImage("grass_background");
        map = resourceGetter.loadImage("map_square_nowalls");
        obstacle = resourceGetter.loadImage("obstacles/cassa_prosp3");
        unbreakable = resourceGetter.loadImage("obstacles/wall_prosp2");
        // CHECKSTYLE: MagicNumbers ON

        playerImage = playerSprite.getImage();
        bombImage = normalBombSprite.getImage();
        /*
        JButton back = new JButton("back");
        JButton start = new JButton("start");

        this.add(back);
        this.add(start);
        
        back.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                graphicsEngine.showCard(BombarderoGraphics.MENU_CARD);
            }
            
        });

        start.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                controller.startGame();
            }
            
        });
        */
    }

    @Override 
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        //super.paintComponent(g);
        /* If the scale changes, then scale again the images */
        if(resizingEngine.hasScaleChanged()) {
            // scaleEverything();
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
        /* Drawing the player */
        Dimension playerPosition = computeCharacterPlacingPoint(controller.getMainPlayer().getCharacterPosition());
        g2d.drawImage(playerImage.getScaledInstance(35, 55, Image.SCALE_SMOOTH),
            playerPosition.width, playerPosition.height,
            null
        );

        for (int i = 0; i < Utils.MAP_ROWS; i++) {
            for (int j = 0; j < Utils.MAP_COLS; j++) {
                Pair position = new Pair(i, j);
                if (cells.containsKey(position)) {
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
                            img = bombImage;
                            placingPoint = computeBombPlacingPoint(new Pair(i, j));
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
    }

    public void updateMap() {
        cells = controller.getMap();
        updateSprites();
    }

    public void updateSprites() {
        /* Update player sprites: */
        playerSprite.update();
        if (player.isStationary()) {
            playerImage = playerSprite.getStandingImage();
        }
        else if (playerSprite.getCurrentFacingDirection().equals(player.getFacingDirection())) {
            playerImage = playerSprite.getImage();
        }
        else {
            playerSprite = playerSprite.getNewSprite(player.getFacingDirection());
            playerImage = playerSprite.getImage();
        }
        /* Update enemy sprites: */
        /* 
        for (int i = 0; i < Utils.NUM_OF_ENEMIES; i++) {
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
        normalBombSprite.update();
        bombImage = normalBombSprite.getImage();
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
            entityPlacingPoint.width + (int)(resizingEngine.getScaledCellSize() * coordinate.x()),
            entityPlacingPoint.height + (int)(resizingEngine.getScaledCellSize() * coordinate.y())
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
     * SE NON VA PROVA AD INVERTIRE x E y
     */
    private Dimension computeCharacterPlacingPoint(final Coord playerPosition) {

        return new Dimension(
            mapPlacingPoint.width
            + (int)(Math.floor(playerPosition.x() * resizingEngine.getScaledCellSize())
                - Math.floorDiv(Utils.PLAYER_WIDTH, 2)
                + resizingEngine.getScaledCellSize()
                + resizingEngine.getScale() * MISCHIEVOUS_PADDING),
            mapPlacingPoint.height 
            + (int)(Math.floor(playerPosition.y() * resizingEngine.getScaledCellSize())
                - Utils.PLAYER_HEIGHT * 0.85
                + 2 * resizingEngine.getScaledCellSize())
        );
    }

    private int computeOverlayLevel() {
        return computeMapPlacingPoint().height;
    }
    
}
