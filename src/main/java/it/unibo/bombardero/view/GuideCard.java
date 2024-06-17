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

    public GuideCard(final JFrame parentFrame, final Controller controller, final BombarderoGraphics graphics, final ResourceGetter resourceGetter, final ResizingEngine resizingEngine) {
        super(graphics);
        this.resourceGetter = resourceGetter;
        this.controller = controller;
        this.graphicsEngine = graphics;
        this.resizingEngine = resizingEngine;
        this.parentFrame = parentFrame;
        
        player = controller.getMainPlayer();
        enemies = controller.getEnemies();

        // CHECKSTYLE: MagicNumbers OFF
        playerSprite = new GenericBombarderoSprite("character/main/walking", resourceGetter, Direction.DOWN, graphics.getResizingEngine()::getScaledCellImage);
        normalBombSprite = new GenericBombarderoSprite("bomb", resourceGetter, 4, graphics.getResizingEngine()::getScaledCellImage);
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
        super.paint(g);
    }

    public void showMessage(final BombarderoViewMessages message) {
        return;
    }
    
}
