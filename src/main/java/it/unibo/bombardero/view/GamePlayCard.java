package it.unibo.bombardero.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Map;
import java.util.HashMap;

import javax.swing.JPanel;

import it.unibo.bombardero.cell.Cell;
import it.unibo.bombardero.cell.powerup.api.PowerUp;
import it.unibo.bombardero.utils.Utils;
import it.unibo.bombardero.view.sprites.BombarderoSprite;
import it.unibo.bombardero.view.sprites.BombarderoOrientedSprite;
import it.unibo.bombardero.view.sprites.GenericBombarderoSprite;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.character.Character;
import it.unibo.bombardero.character.Direction;

/**
 * This class is the panel containing the game view.
 * @author Federico Bagattoni
 */
public class GamePlayCard extends JPanel {

    /** 
     * For the first part of the class, the magic number checkstyle will be 
     * deactivated, as the resource are imported with their file name.
     */
    // CHECKSTYLE: MagicNumber OFF

    /* Game resources: */
    private final ResourceGetter resourceGetter = new ResourceGetter();
    private Image grass_bg_image = resourceGetter.loadImage("grass_background");
    private Image mapImage = resourceGetter.loadImage("map_square_nowalls");
    private Image obstacle = resourceGetter.loadImage("obstacles/cassa_prosp3");
    private Image unbreakable = resourceGetter.loadImage("obstacles/wall_prosp2");
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
    private final BombarderoGraphics graphics;
    private Map<Pair, Cell> cells;
    private final Character player;
    private final Map<Character, EnemyImage> enemies = new HashMap<>(); // every enemy is linked to its own sprite
    
    /* Sprites and images: */
    private BombarderoOrientedSprite playerSprite;
    private Image playerImage;
    private final BombarderoSprite normalBomb;
    private Image bomb_image;

    /* Static positions for quicker access: */
    private final Dimension mapPlacingPoint;
    private final Dimension entityPlacingPoint;


    public GamePlayCard(final BombarderoGraphics graphics) {
        this.graphics = graphics;
        this.setMinimumSize(graphics.getResizingEngine().getMapSize());
        this.setLayout(new BorderLayout());

        mapPlacingPoint = graphics.getResizingEngine().getMapPlacingPoint();
        entityPlacingPoint = graphics.getResizingEngine().getEntityPlacingPoint();

        cells = graphics.getController().getMap(); 
        player = graphics.getController().getMainPlayer();
        graphics.getController().getEnemies().forEach(enemy -> {
            BombarderoOrientedSprite sprite = new GenericBombarderoSprite("character/main/walking", resourceGetter, Direction.DOWN, graphics.getResizingEngine()::getScaledCharacterImage);
            enemies.put(
                enemy,
                new EnemyImage(
                    sprite,
                    sprite.getStandingImage()
                )
            );
        });

        playerSprite = new GenericBombarderoSprite("character/main/walking", resourceGetter, Direction.DOWN, graphics.getResizingEngine()::getScaledCharacterImage);
        playerImage = playerSprite.getStandingImage();
        normalBomb = new GenericBombarderoSprite("bomb", resourceGetter, 4, graphics.getResizingEngine()::getScaledBombImage);
        bomb_image = normalBomb.getImage();

        scaleEverything();
    }

    // CHECKSTYLE: MagicNumber ON

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        /* Drawing the Map and the Background */
        // CHECKSTYLE: MagicNumber OFF
        g2d.drawImage(
            grass_bg_image,
            0, 0,
            null);
        // CHECKSTYLE: MagicNumber ON
        g2d.drawImage(
            mapImage,
            mapPlacingPoint.width,
            mapPlacingPoint.height,
            null
        );
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
                            placingPoint = graphics.getResizingEngine().getCellPlacingPoint(new Pair(i, j));
                            break;
                        case WALL_UNBREAKABLE:
                            img = unbreakable;
                            placingPoint = graphics.getResizingEngine().getCellPlacingPoint(new Pair(i, j));
                            break;
                        case BOMB: 
                            img = bomb_image;
                            placingPoint = graphics.getResizingEngine().getBombPlacingPoint(new Pair(i, j));
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
                            placingPoint = graphics.getResizingEngine().getCellPlacingPoint(new Pair(i, j));
                            break;
                        default:
                            img = unbreakable;
                            placingPoint = graphics.getResizingEngine().getCellPlacingPoint(new Pair(i, j));
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
        Dimension playerPosition = graphics.getResizingEngine().getCharacterPlacingPoint(graphics.getController().getMainPlayer().getCharacterPosition());
        g2d.drawImage(playerImage,
            playerPosition.width, playerPosition.height,
            null
        );
        /*
        for(int i = 0; i < Utils.NUM_OF_ENEMIES; i++) {
            Dimension enemyPos = graphics.getResizingEngine().getCharacterPlacingPoint(graphics.getController().getEnemies().get(i).getCharacterPosition());
            g2d.drawImage(enemiesImages[i], enemyPos.width, enemyPos.height, null);
        } */
        enemies.entrySet().forEach(enemy -> {
            Dimension enemyPos = graphics.getResizingEngine().getCharacterPlacingPoint(enemy.getKey().getCharacterPosition());
            g2d.drawImage(enemy.getValue().displayedImage(), enemyPos.width, enemyPos.height, null);
        });
    }

    public void updateMap() {
        cells = graphics.getController().getMap();
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

        enemies.entrySet().forEach(enemy -> {
            enemy.getValue().sprite.update();
            BombarderoOrientedSprite sprite = enemy.getValue().sprite();
            Image image = enemy.getValue().displayedImage();
            if(enemy.getKey().getFacingDirection().equals(Direction.DEFAULT)) {
                image = enemy.getValue().sprite().getStandingImage();
            }
            else {
                sprite = enemy.getValue().sprite().getNewSprite(enemy.getKey().getFacingDirection());
                image = sprite.getStandingImage();
            }
            enemies.put(
                enemy.getKey(),
                new EnemyImage(sprite, image)
            );
        });
        /* Update enemy sprites: 
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
        normalBomb.update();
        bomb_image = normalBomb.getImage();
    }


    protected void scaleEverything() {
        mapImage = graphics.getResizingEngine().getScaledMapImage(mapImage);
        grass_bg_image = graphics.getResizingEngine().getScaledBackgroundImage(grass_bg_image);
        unbreakable = graphics.getResizingEngine().getScaledCellImage(unbreakable);
        obstacle = graphics.getResizingEngine().getScaledCellImage(obstacle);
        bombLine = graphics.getResizingEngine().getScaledCellImage(bombLine);
        bombPlusOne = graphics.getResizingEngine().getScaledCellImage(bombPlusOne);
        bombMinusOne = graphics.getResizingEngine().getScaledCellImage(bombMinusOne);
        bombPower = graphics.getResizingEngine().getScaledCellImage(bombPower);
        bombRemote = graphics.getResizingEngine().getScaledCellImage(bombRemote);
        bombPierce = graphics.getResizingEngine().getScaledCellImage(bombPierce);
        fireMax = graphics.getResizingEngine().getScaledCellImage(fireMax);
        firePlusOne = graphics.getResizingEngine().getScaledCellImage(firePlusOne);
        fireMinusOne = graphics.getResizingEngine().getScaledCellImage(fireMinusOne);
        skatesPlusOne = graphics.getResizingEngine().getScaledCellImage(skatesPlusOne);
        skateMinusOne = graphics.getResizingEngine().getScaledCellImage(skateMinusOne);
        skull = graphics.getResizingEngine().getScaledCellImage(skull);
    }

    private record EnemyImage (BombarderoOrientedSprite sprite, Image displayedImage) {
    }
}