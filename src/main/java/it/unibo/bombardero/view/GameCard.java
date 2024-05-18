package it.unibo.bombardero.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.PageAttributes;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JPanel;

import it.unibo.bombardero.cell.Cell;
import it.unibo.bombardero.cell.Cell.CellType;
import it.unibo.bombardero.character.Player;
import it.unibo.bombardero.core.KeyboardInput;
import it.unibo.bombardero.core.api.Controller;
import it.unibo.bombardero.utils.Utils;
import it.unibo.bombardero.map.api.Coord;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.map.impl.GameMapImpl;
import it.unibo.bombardero.character.Character;
import it.unibo.bombardero.character.Direction;

public class GameCard extends JPanel {

    private final static int MISCHIEVOUS_PADDING = 23;

    private final ResourceGetter resourceGetter = new ResourceGetter();
    private final BufferedImage grass_bg_image = resourceGetter.loadImage("grass_background");
    private final BufferedImage map = resourceGetter.loadImage("map_square");
    private final BufferedImage obstacle = resourceGetter.loadImage("other_textures/crate");

    private final JFrame parentFrame;
    private final ResizingEngine resizingEngine;
    private final Map<Pair, Cell> cells;
    private final Controller controller;
    private final KeyboardInput keyInput;

    private final Character player;
    private final List<Character> enemies;
    
    private BombarderoSprite playerSprite;
    private final BombarderoSprite[] enemySprite = new BombarderoSprite[Utils.NUM_OF_ENEMIES];
    private Image player_image;
    private final Image[] enemiesImages = new Image[Utils.NUM_OF_ENEMIES];

    public GameCard(final JFrame parentFrame, final ResizingEngine resizingEngine, final Controller controller) {
        this.parentFrame = parentFrame;
        this.resizingEngine = resizingEngine;
        this.controller = controller;
        keyInput = new KeyboardInput(controller);
        parentFrame.addKeyListener(keyInput);

        this.setMinimumSize(resizingEngine.getMapSize());

        cells = controller.getMap(); 
        player = controller.getMainPlayer();
        enemies = controller.getEnemies();

        playerSprite = new BombarderoSprite("character/main/walking", resourceGetter, Direction.DOWN);
        player_image = playerSprite.getStandingImage();
        for (int i = 0; i < Utils.NUM_OF_ENEMIES; i++) {
            enemySprite[i] = new BombarderoSprite("character/main/walking", resourceGetter, Direction.DOWN);
            enemiesImages[i] = enemySprite[i].getStandingImage();
        }
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
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
        /* Drawing the breakable obstacles, the bombs and the power ups (not done yet)*/
        /* cells.entrySet().stream()
            .filter(entry -> entry.getValue().getCellType().equals(CellType.WALL_BREAKABLE))
            .forEach(entry -> {
                g2d.drawImage(
                    obstacle.getScaledInstance((int)(resizingEngine.getScaledCellSize()), (int)(resizingEngine.getScaledCellSize()), Image.SCALE_SMOOTH),
                    computeCellPlacingPoint(entry.getKey()).width,
                    computeCellPlacingPoint(entry.getKey()).height,
                    null
                );
            }); */
        /* Drawing the player and the enemies */
        /* TODO: FARE UNA SCALE PER CUI MOLTIPLICARE L'IMMAGINE DEL PLAYER PIANO PIANO CHE LA MAPPA DIVENTA GRANDE */
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
            computeMapPlacingPoint().width + (int)(resizingEngine.getScaledCellSize() * coordinate.row()) + resizingEngine.getScaledCellSize() + (int)(resizingEngine.getScale() * MISCHIEVOUS_PADDING),
            computeMapPlacingPoint().height + (int)(resizingEngine.getScaledCellSize() * coordinate.col()) + 2*resizingEngine.getScaledCellSize()
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
}