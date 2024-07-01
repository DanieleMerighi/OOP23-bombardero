package it.unibo.bombardero.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JPanel;

import it.unibo.bombardero.cell.Cell;
import it.unibo.bombardero.cell.Flame;
import it.unibo.bombardero.cell.powerup.api.PowerUp;
import it.unibo.bombardero.utils.Utils;
import it.unibo.bombardero.view.sprites.api.OrientedSprite;
import it.unibo.bombardero.view.sprites.api.Sprite;
import it.unibo.bombardero.view.sprites.impl.BombarderoFlameSprite;
import it.unibo.bombardero.view.sprites.impl.BombarderoOrientedSprite;
import it.unibo.bombardero.view.sprites.impl.SimpleBombarderoSprite;
import it.unibo.bombardero.view.sprites.impl.TimedBombarderoSprite;
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
    private transient final ResourceGetter resourceGetter = new ResourceGetter();
    private transient Image grass_bg_image = resourceGetter.loadImage("grass_background");
    private transient Image mapImage = resourceGetter.loadImage("map_square_nowalls");
    private transient Image obstacle = resourceGetter.loadImage("obstacles/cassa_prosp3");
    private transient Image unbreakable = resourceGetter.loadImage("obstacles/wall_prosp2");
    private transient Image bombLine = resourceGetter.loadImage("powerup/line_bomb");
    private transient Image bombPlusOne = resourceGetter.loadImage("powerup/bomb_plus_one");
    private transient Image bombMinusOne = resourceGetter.loadImage("powerup/bomb_minus_one");
    private transient Image bombPower = resourceGetter.loadImage("powerup/bomb_power");
    private transient Image bombRemote = resourceGetter.loadImage("powerup/bomb_remote");
    private transient Image bombPierce = resourceGetter.loadImage("powerup/bomb_pierce");
    private transient Image fireMax = resourceGetter.loadImage("powerup/fire_max");
    private transient Image firePlusOne = resourceGetter.loadImage("powerup/fire_plusone");
    private transient Image fireMinusOne = resourceGetter.loadImage("powerup/fire_minusone");
    private transient Image skatesPlusOne = resourceGetter.loadImage("powerup/skates_plus_one");
    private transient Image skateMinusOne = resourceGetter.loadImage("powerup/skates_minus_one");
    private transient Image skull = resourceGetter.loadImage("powerup/skull");

    /* References to model components: */
    private transient final BombarderoGraphics graphics;
    private transient Map<Pair, Cell> cells;
    private transient final Character player;
    private transient final Map<Character, SpriteImageCombo> characterImages = new HashMap<>(); // every enemy is linked to its own sprite
    private transient List<Character> enemiesList;

    /* Sprites and images: */
    private transient final BombarderoFlameSprite flamesSprite;
    private transient final Sprite normalBomb;
    private transient Image bomb_image;
    private transient Map<Character, TimedBombarderoSprite> dyingCharacters = new HashMap<>(); // dead characters are stored here to be displayed
    private List<String> colorCodes = List.of("blue", "red", "main");

    /* Static positions for quicker access: */
    private final Dimension mapPlacingPoint;


    public GamePlayCard(final BombarderoGraphics graphics) {
        this.graphics = graphics;
        this.setMinimumSize(graphics.getResizingEngine().getMapSize());
        this.setLayout(new BorderLayout());

        mapPlacingPoint = graphics.getResizingEngine().getMapPlacingPoint();

        cells = graphics.getController().getMap(); 
        player = graphics.getController().getMainPlayer();
        enemiesList = graphics.getController().getEnemies();

        checkForNewEnemies();

        flamesSprite = new BombarderoFlameSprite(500, 6, graphics.getResizingEngine()::getScaledCellImage, resourceGetter);
        OrientedSprite playerSprite = new BombarderoOrientedSprite("character/main/walking", resourceGetter, Direction.DOWN, graphics.getResizingEngine()::getScaledCharacterImage);
        Image playerImage = playerSprite.getStandingImage();
        characterImages.put(player, new SpriteImageCombo(playerSprite, playerImage, "main"));
        normalBomb = new SimpleBombarderoSprite("bomb", resourceGetter, graphics.getResizingEngine()::getScaledBombImage, 4);
        bomb_image = normalBomb.getImage();

        scaleEverything();
    }

    // CHECKSTYLE: MagicNumber ON

    @Override
    public void paintComponent(Graphics g) {
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
        /* Drawing the breakable obstacles, the bombs and the power ups */
        for (int i = 0; i < Utils.MAP_ROWS; i++) {
            for (int j = 0; j < Utils.MAP_COLS; j++) {
                if (cells.containsKey(new Pair(i, j))) {
                    Cell entry = cells.get(new Pair(i ,j));
                    Image img = unbreakable;
                    Dimension placingPoint = graphics.getResizingEngine().getCellPlacingPoint(new Pair(i, j));
                    switch (entry.getCellType()) {
                        case WALL_BREAKABLE:
                            img = obstacle;
                            break;
                        case WALL_UNBREAKABLE:
                            img = unbreakable;
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
                                case SKULL -> skull;
                                default -> throw new IllegalArgumentException("texture not present for \"" + pu.getType() + "\"");
                            };
                            break;
                        case FLAME: 
                            Flame fl = (Flame)entry;
                            img = flamesSprite.getImage(fl.getTimePassed(), fl.getFlameType());
                            break;
                        default:
                            img = unbreakable;
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
        characterImages.entrySet().forEach(enemy -> {
            Dimension enemyPos = graphics.getResizingEngine().getCharacterPlacingPoint(enemy.getKey().getCharacterPosition());
            g2d.drawImage(enemy.getValue().displayedImage(), enemyPos.width, enemyPos.height, null);
        });
        dyingCharacters.entrySet().forEach(entry -> {
            Dimension pos = graphics.getResizingEngine().getCharacterPlacingPoint(entry.getKey().getCharacterPosition());
            g2d.drawImage(
                entry.getValue().getImage(),
                pos.width, pos.height,
                null
            );
        });
    }

    public void updateMap() {
        cells = graphics.getController().getMap();
        updateSprites();
    }

    public void updateSprites() {
        checkForNewEnemies();

        characterImages.entrySet().forEach(character -> {
            character.getValue().sprite.update();
            OrientedSprite sprite = character.getValue().sprite();
            Image image = character.getValue().displayedImage();
            if(character.getKey().isStationary()) {
                image = character.getValue().sprite().getStandingImage();
            }
            else if(character.getValue().sprite().getCurrentFacingDirection().equals(character.getKey().getFacingDirection())) {
                image = character.getValue().sprite().getImage();
            }
            else {
                sprite = character.getValue().sprite().getNewSprite(character.getKey().getFacingDirection());
                image = sprite.getImage();
            }
            characterImages.put(
                character.getKey(),
                new SpriteImageCombo(sprite, image, "main")
            );
        });
        /* Update bomb sprites: */
        normalBomb.update();
        bomb_image = normalBomb.getImage();
        /* Update dying characters: */
        updateDeadCharacters();
    }


    private final void scaleEverything() {
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

    private void checkForNewEnemies() {
        enemiesList = graphics.getController().getEnemies();
        enemiesList.stream()
            .filter(enemy -> !characterImages.keySet().contains(enemy))
            .forEach(enemy -> {
                String colorCode =  colorCodes.isEmpty() ? "red" : colorCodes.get(0);
                colorCodes = !colorCodes.isEmpty() ? colorCodes.subList(1, colorCodes.size()) : List.of();
                OrientedSprite sprite = new BombarderoOrientedSprite("character/" + colorCode + "/walking", resourceGetter, Direction.DOWN, graphics.getResizingEngine()::getScaledCharacterImage);
                characterImages.put(
                    enemy,
                    new SpriteImageCombo(
                        sprite,
                        sprite.getStandingImage(),
                        colorCode
                    )
                );
            });
    }

    /* I had to do it in separates for-loops because of ConcurrentModificationException(s) */
    private void updateDeadCharacters() {
        for (Entry<Character, TimedBombarderoSprite> entry : dyingCharacters.entrySet()) {
            entry.getValue().update();
        };
        dyingCharacters = dyingCharacters.entrySet().stream()
            .filter(entry -> !entry.getValue().isOver())
            .collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));
        characterImages.entrySet().forEach(entry -> {
            if (!entry.getKey().isAlive()) {
                Image[] dyingAsset = SimpleBombarderoSprite.importAssets(
                    "dying",
                    "character/" + entry.getValue().colorCode() + "/dying",
                    resourceGetter,
                    graphics.getResizingEngine()::getScaledCharacterImage,
                    4
                );
                dyingCharacters.put(
                    entry.getKey(),
                    new TimedBombarderoSprite(dyingAsset, 4, 4)
                );
            }
        });
        dyingCharacters.keySet().forEach(characterImages::remove);
    }

    private record SpriteImageCombo (OrientedSprite sprite, Image displayedImage, String colorCode) {
    }
}