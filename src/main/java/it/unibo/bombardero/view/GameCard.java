package it.unibo.bombardero.view;

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

import javax.swing.JFrame;
import javax.swing.JPanel;

import it.unibo.bombardero.cell.Cell;
import it.unibo.bombardero.cell.Cell.CellType;
import it.unibo.bombardero.cell.powerUp.api.PowerUp;
import it.unibo.bombardero.core.KeyboardInput;
import it.unibo.bombardero.core.api.Controller;
import it.unibo.bombardero.utils.Utils;
import it.unibo.bombardero.map.api.Coord;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.character.Character;
import it.unibo.bombardero.character.Direction;

public class GameCard extends JPanel {

    /* A mischevious padding no one knows its reason to exist: */
    private final static int MISCHIEVOUS_PADDING = 23;
    private final static SimpleDateFormat format = new SimpleDateFormat("mm:ss");

    /* Game resources: */
    private final ResourceGetter resourceGetter = new ResourceGetter();
    private final Image grass_bg_image = resourceGetter.loadImage("grass_background");
    private final Image map = resourceGetter.loadImage("map_square_nowalls");
    private Image obstacle = resourceGetter.loadImage("obstacles/cassa_prosp2");
    private Image unbreakable = resourceGetter.loadImage("obstacles/wall_prosp");
    private Image clock = resourceGetter.loadImage("overlay/clock");
    private final Font font = resourceGetter.loadFont("clock_font");
    /* TODO: import the rest of the static resources (first convert them to PNG) */

    /* References to model components: */
    private final JFrame parentFrame;
    private final ResizingEngine resizingEngine;
    private Map<Pair, Cell> cells;
    private final Controller controller;
    private final Character player;
    private final List<Character> enemies;
    
    /* Sprites: */
    private BombarderoSprite playerSprite;
    private final BombarderoSprite[] enemySprite = new BombarderoSprite[Utils.NUM_OF_ENEMIES];
    private Image player_image;
    private final Image[] enemiesImages = new Image[Utils.NUM_OF_ENEMIES];

    /* Static positions for quick access: */
    private Dimension mapPlacingPoint;
    private int overlayLevel;

    public GameCard(final JFrame parentFrame, final ResizingEngine resizingEngine, final Controller controller) {
        this.parentFrame = parentFrame;
        this.resizingEngine = resizingEngine;
        this.controller = controller;
        this.setMinimumSize(resizingEngine.getMapSize());
        mapPlacingPoint = computeMapPlacingPoint();
        overlayLevel = computeOverlayLevel();

        obstacle = obstacle.getScaledInstance((int)(resizingEngine.getScale() * 32), (int)(resizingEngine.getScale() * 39), Image.SCALE_SMOOTH);
        unbreakable = unbreakable.getScaledInstance((int)(resizingEngine.getScale() * 32), (int)(resizingEngine.getScale() * 39), Image.SCALE_SMOOTH);
        clock = clock.getScaledInstance(resizingEngine.getScaledCellSize(), resizingEngine.getScaledCellSize(), Image.SCALE_SMOOTH);

        cells = controller.getMap(); 
        player = controller.getMainPlayer();
        enemies = controller.getEnemies();

        playerSprite = new BombarderoSprite("character/main/walking", resourceGetter, Direction.DOWN);
        player_image = playerSprite.getStandingImage();
        for (int i = 0; i < Utils.NUM_OF_ENEMIES; i++) {
            enemySprite[i] = new BombarderoSprite("character/main/walking", resourceGetter, Direction.DOWN);
            enemiesImages[i] = enemySprite[i].getStandingImage();
        }

        this.setFont(font);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        /* If the scale changes, then scale again the images */
        if(resizingEngine.hasScaleChanged()) {
            obstacle = obstacle.getScaledInstance((int)(resizingEngine.getScale() * 32), (int)(resizingEngine.getScale() * 39), Image.SCALE_SMOOTH);
            mapPlacingPoint = computeMapPlacingPoint();
            overlayLevel = computeOverlayLevel();
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
        g2d.drawString(getFormattedTime(), 100, overlayLevel);
        /* Drawing the breakable obstacles, the bombs and the power ups (TODO: not done yet) */
        for (int i = 0; i < Utils.MAP_ROWS; i++) {
            for (int j = 0; j < Utils.MAP_COLS; j++) {
                if (cells.containsKey(new Pair(i, j))) {
                    Cell entry = cells.get(new Pair(i ,j));
                    Image img = unbreakable;
                    switch (entry.getCellType()) {
                        case WALL_BREAKABLE:
                            img = obstacle;
                            break;
                        case WALL_UNBREAKABLE:
                            img = unbreakable;
                            break;
                        default:
                            img = unbreakable;
                            break;
                    }
                    g2d.drawImage(
                        img,
                        computeCellPlacingPoint(new Pair(i, j)).width,
                        computeCellPlacingPoint(new Pair(i, j)).height,
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
        
        for(int i = 0; i < Utils.NUM_OF_ENEMIES; i++) {
            Dimension enemyPos = computeCharacterPlacingPoint(controller.getEnemies().get(i).getCharacterPosition());
            g2d.drawImage(enemiesImages[i], enemyPos.width, enemyPos.height, null);
        }
    }

    public void updateMap() {
        cells = controller.getMap();
        updateSprites();
    }

    public void updateSprites() {
        playerSprite.update();
        if (player.getFacingDirection().equals(Direction.DEFAULT)) {
            player_image = playerSprite.getStandingImage();
        }
        else if (playerSprite.getCurrentFacingDirection().equals(player.getFacingDirection())) {
            player_image = playerSprite.getImage();
        }
        else {
            playerSprite = playerSprite.getNewSprite(player.getFacingDirection());
            player_image = playerSprite.getImage();
        }
        for (int i = 0; i < Utils.NUM_OF_ENEMIES; i++) {
            enemySprite[i].update();
            if(enemies.get(i).getFacingDirection().equals(Direction.DEFAULT)) {
                enemiesImages[i] = enemySprite[i].getStandingImage();
            }
            else {
                enemySprite[i] = enemySprite[i].getNewSprite(enemies.get(i).getFacingDirection());
                enemiesImages[i] = enemySprite[i].getStandingImage();
            }
        }
    }

    private Dimension computeMapPlacingPoint() {
        return new Dimension(
            parentFrame.getSize().width/2 - resizingEngine.getMapSize().width/2 - (parentFrame.getInsets().right + parentFrame.getInsets().left),
            parentFrame.getSize().height/2 - resizingEngine.getMapSize().height/2 - (parentFrame.getInsets().top + parentFrame.getInsets().bottom)
        );
    }

    private Dimension computeCellPlacingPoint(Pair coordinate) {
        return new Dimension(
            mapPlacingPoint.width + (int)(resizingEngine.getScaledCellSize() * coordinate.row()) + resizingEngine.getScaledCellSize() + (int)(resizingEngine.getScale() * MISCHIEVOUS_PADDING),
            mapPlacingPoint.height + (int)(resizingEngine.getScaledCellSize() * coordinate.col()) + 2*resizingEngine.getScaledCellSize() - (int)(7 * resizingEngine.getScale())
        );
    }

    /* The Character object's position contains a float coordinate representing the center of the charater therefore we
     * have to compute the placing point for the character's image
     */
    private Dimension computeCharacterPlacingPoint(final Coord playerPosition) {
        return new Dimension(
            (int)(Math.floor(playerPosition.row() * resizingEngine.getScaledCellSize()) - Math.floorDiv(Utils.PLAYER_WIDTH, 2)),
            (int)(Math.floor(playerPosition.col() * resizingEngine.getScaledCellSize()) - Math.floorDiv(Utils.PLAYER_HEIGHT, 2))
        );
    }

    private int computeOverlayLevel() {
        return computeMapPlacingPoint().height;
    }

    private String getFormattedTime() {
        return format.format(new Date(controller.getTimeLeft()));
    }   
}