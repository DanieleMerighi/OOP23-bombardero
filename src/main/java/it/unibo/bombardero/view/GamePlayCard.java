package it.unibo.bombardero.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

import it.unibo.bombardero.cell.Cell;
import it.unibo.bombardero.cell.FlameImpl;
import it.unibo.bombardero.cell.Flame;
import it.unibo.bombardero.cell.powerup.api.PowerUp;
import it.unibo.bombardero.utils.Utils;
import it.unibo.bombardero.view.api.GraphicsEngine;
import it.unibo.bombardero.view.api.GraphicsEngine.EndGameState;
import it.unibo.bombardero.view.sprites.api.OrientedSprite;
import it.unibo.bombardero.view.sprites.api.Sprite;
import it.unibo.bombardero.view.sprites.impl.BombarderoFlameSprite;
import it.unibo.bombardero.view.sprites.impl.BombarderoOrientedSprite;
import it.unibo.bombardero.view.sprites.impl.SimpleBombarderoSprite;
import it.unibo.bombardero.view.sprites.impl.TimedBombarderoSprite;
import it.unibo.bombardero.map.api.GenPair;
import it.unibo.bombardero.character.Character;
import it.unibo.bombardero.character.Direction;
import it.unibo.bombardero.core.api.Controller;

/**
 * This class will never be serialised.
 */
@SuppressWarnings("serial")

/**
 * This abstract class implements the basic idea of a generic 
 * container to display the game elements. Imports the various items,
 * is updatable and the "pause screen" can be set from the outside. 
 * <p> 
 * This class leaves several methods as abstract so the subclasses can personalize
 * the response to precise actions.
 */
public abstract class GamePlayCard extends JPanel {

    /**
     * The optimal alpha channel value to use when darkening the background, 
     * for example when the pause menu is displayed.
     */
    public static final float PAUSE_DARKEN_ALPHA = 0.35f;
    private static final int LAYOUT_COLS = 1;
    private static final int LAYOUT_ROWS = 5;
    /** 
     * For the first part of the class, the magic number checkstyle will be 
     * deactivated, as the resource are imported with their file name.
     */
    // CHECKSTYLE: MagicNumber OFF

    /* Game resources: */
    private final transient ResourceGetter resourceGetter = new ResourceGetter();
    private transient Image grassBackgroundImage = resourceGetter.loadImage("grass_background");
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
    private final transient ResizingEngine resizingEngine;
    private transient Map<GenPair<Integer, Integer>, Cell> cells;
    private transient List<Character> playersList;
    private final transient Map<Character, SpriteImageCombo> charactersImages = new HashMap<>();
    private transient List<Character> enemiesList;

    /* Sprites and images: */
    private final transient BombarderoFlameSprite flamesSprite;
    private final transient Sprite normalBomb;
    private transient Image bombImage;
    private transient Map<Character, TimedBombarderoSprite> dyingCharactersMap = new HashMap<>();
    private List<String> colorCodes = List.of("black", "blue", "red");

    /* Static positions for quicker access: */
    private final Dimension mapPlacingPoint;

    /* Pause state buttons: */
    private final JButton resumeButton;
    private final JButton quitButton;
    private float darkenValue;

    /**
     * Creates a new GamePlayCard creating all the sprites needed and setting the initial state 
     * of the view with the parameter passed.
     * @param graphics the GraphicsEngine that manager this class
     * @param map the game map initally rendered by this class
     * @param playersList the players initally rendered by this class
     * @param enemies the enemies initally rendered by this class
     * @param controller the game's controller
     */
    public GamePlayCard(
        final Controller controller,
        final GraphicsEngine graphics,
        final Map<GenPair<Integer, Integer>, Cell> map,
        final List<Character> playersList,
        final List<Character> enemies
        ) {
        this.resizingEngine = graphics.getResizingEngine().getNewEngine();
        this.setMinimumSize(graphics.getResizingEngine().getMapSize());
        this.setLayout(new BorderLayout());

        mapPlacingPoint = graphics.getResizingEngine().getMapPlacingPoint();
        flamesSprite = new BombarderoFlameSprite(500, 6, graphics.getResizingEngine()::getScaledCellImage, resourceGetter);
        normalBomb = new SimpleBombarderoSprite("bomb", resourceGetter, graphics.getResizingEngine()::getScaledBombImage, 4);
        bombImage = normalBomb.getImage();
        final Image resumeButtonImage = graphics.getResizingEngine().getScaledButtonImage(
            graphics.getResourceGetter().loadImage("overlay/buttons/RESUME")
        );
        final Image quitButtonImage = graphics.getResizingEngine().getScaledButtonImage(
            graphics.getResourceGetter().loadImage("overlay/buttons/QUIT")
        );
        final Image quitButtonPressedImage = graphics.getResizingEngine().getScaledButtonImage(
            graphics.getResourceGetter().loadImage("overlay/buttons/QUIT_PRESSED")
        );
        final Image resumeButtonPressedImage = graphics.getResizingEngine().getScaledButtonImage(
            graphics.getResourceGetter().loadImage("overlay/buttons/RESUME_PRESSED")
        );

        updateGameState(map, playersList, enemies);
        scaleEverything();

        resumeButton = new JButton(new ImageIcon(resumeButtonImage));
        quitButton = new JButton(new ImageIcon(quitButtonImage));

        quitButton.setBorder(null);
        resumeButton.setBorder(null);
        quitButton.setBorderPainted(false);
        resumeButton.setBorderPainted(false);
        resumeButton.setContentAreaFilled(false);
        quitButton.setContentAreaFilled(false);
        resumeButton.setFocusPainted(false);
        quitButton.setFocusPainted(false);
        quitButton.setPressedIcon(new ImageIcon(quitButtonPressedImage));
        resumeButton.setPressedIcon(new ImageIcon(resumeButtonPressedImage));
        resumeButton.setFocusable(false);
        quitButton.setFocusable(false);

        this.add(new JLabel());
        this.setLayout(new GridLayout(LAYOUT_ROWS, LAYOUT_COLS));

        quitButton.addActionListener(e -> controller.endGame());
        resumeButton.addActionListener(e -> controller.escape());
    }

    // CHECKSTYLE: MagicNumber ON

    /**
     * Paints the whole component. This method has been overriden
     * to paint from scratch all the game's elements.
     * <p>
     * It paints:
     * <ul>
     *  <li> The grass background
     *  <li> The arena 
     *  <li> All of the elements present in the arena
     *  <li> The characters (also the dying characters)
     *  <li> The flames, bombs 
     *  <li> Eventually, the blur, if the relative {@link #blurView} method is called
     * </ul>
     */
    @Override
    public void paintComponent(final Graphics g) {
        /* Drawing the Map and the Background */
        // CHECKSTYLE: MagicNumber OFF
        g.drawImage(
            grassBackgroundImage,
            0, 0,
            null);
        // CHECKSTYLE: MagicNumber ON
        g.drawImage(
            mapImage,
            mapPlacingPoint.width,
            mapPlacingPoint.height,
            null
        );
        /* Drawing the breakable obstacles, the bombs and the power ups */
        IntStream
            .range(0, Utils.MAP_ROWS)
            .boxed()
            .flatMap(x -> IntStream
                .range(0, Utils.MAP_COLS)
                .mapToObj(y -> new GenPair<Integer, Integer>(x, y))
            )
            .filter(position -> cells.containsKey(position))
            .forEach(position -> {
                Image img = unbreakable;
                Dimension placingPoint = resizingEngine.getCellPlacingPoint(position);
                final Cell currentCell = cells.get(position);
                switch (currentCell.getCellType()) {
                    case WALL_BREAKABLE:
                        img = obstacle;
                        break;
                    case WALL_UNBREAKABLE:
                        img = unbreakable;
                        break;
                    case BOMB: 
                        img = bombImage;
                        placingPoint = resizingEngine.getBombPlacingPoint(position);
                        break;
                    case POWERUP:
                        final PowerUp pu = (PowerUp) currentCell;
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
                        final Flame fl = (FlameImpl) currentCell;
                        img = flamesSprite.getImage(fl.getTimePassed(), fl.getFlameType());
                        break;
                    default:
                        img = unbreakable;
                        break;
                }
                g.drawImage(
                    img,
                    placingPoint.width,
                    placingPoint.height,
                    null
                );
        });
        /* Drawing the player and the enemies */
        charactersImages.entrySet().forEach(enemy -> {
            final Dimension enemyPos = resizingEngine.getCharacterPlacingPoint(enemy.getKey().getCharacterPosition());
            g.drawImage(enemy.getValue().displayedImage(), enemyPos.width, enemyPos.height, null);
        });
        dyingCharactersMap.entrySet().forEach(entry -> {
            final Dimension pos = resizingEngine.getCharacterPlacingPoint(entry.getKey().getCharacterPosition());
            g.drawImage(
                entry.getValue().getImage(),
                pos.width, pos.height,
                null
            );
        });
        g.setColor(new Color(0, 0, 0, darkenValue));
        g.fillRect(0, 0, getSize().width, getSize().height);
    }

    /**
     * Updates the various aspects of the class. Renders the Characters and the Map passed as argument
     * and updates the relative sprites. 
     * @param map the map containing the references to the game's arena
     * @param playersList the list of players present in the game
     * @param enemiesList the list of enemies present in the game
     */
    public void update(
        final Map<GenPair<Integer, Integer>, Cell> map,
        final List<Character> playersList,
        final List<Character> enemiesList
        ) {
        updateGameState(map, playersList, enemiesList);
        updateSprites();
        repaint(0);
    }

    /**
     * Clears the view of everything that has been displayed during the 
     * game phase.
     * <ul>
     *  <li> Removes sprites </li>
     *  <li> Removes messages </li>
     *  <li> Removes eventual paused views </li>
     * </ul>
     */
    public void clear() {
        setUnpausedView();
        charactersImages.clear();
        dyingCharactersMap.clear();
        updateGameState(Map.of(), List.of(), List.of());
        repaint(0);
    }

    /** 
     * Displays a "pause screen" to appropriately indicate that the game is
     * paused and displays alterantives for the user to choose.
     */
    public final void setPausedView() {
        darkenView(PAUSE_DARKEN_ALPHA);
        this.add(resumeButton);
        this.add(quitButton);
        this.revalidate();
        this.repaint(0);
    }

    /** 
     * Stops to display the "pause screen" that displayed after calling {@link #setPausedView()},
     * otherwise does nothing.
     */
    public final void setUnpausedView() {
        darkenView(0.0f);
        this.remove(resumeButton);
        this.remove(quitButton);
        this.revalidate();
        this.repaint(0);
    }

    /**
     * Darkens the view by a certain parameter valued between one (1) and 
     * zero (0), corresponding to the alpha channel.
     * @param amount the amount to darken the whole view
     * @see Color
     */
    public final void darkenView(final float amount) {
        darkenValue = amount;
    }

    /** 
     * Displays the end screen of this card. 
     */
    abstract void displayEndView();

    /** 
     * Displays the end screen of this card, displaying a different
     * image depending on the parameter passed as argument.
     * @param endingType the type of ending to be displayed
     */
    abstract void displayEndView(EndGameState endingType);

    /**
     * Shows the message contained in the passed argument, in an appropriate
     * part of the screen.
     * @param message the message to be displayed
     * @see BombarderoViewMessages
     */
    abstract void showMessage(BombarderoViewMessages message);

    /** 
     * Updates the time displayed in the view. 
     * @param timeLeft the time to be displayed
     */
    abstract void setTimeLeft(Long timeLeft);

    private void updateGameState(
        final Map<GenPair<Integer, Integer>, Cell> map,
        final List<Character> playersList,
        final List<Character> enemiesList) {
        cells = Map.copyOf(map);
        this.playersList = List.copyOf(playersList);
        this.enemiesList = List.copyOf(enemiesList);
    }

    /** 
     * Updates the sprites contained in this class.
     * <p>
     * Calling this method will consist in the update of bombs and characters (dead or alive)
     * sprites, other than the update of the data structures containing the sprites, adding or removing
     * new characters or dead ones. 
     */
    protected void updateSprites() {
        checkForNewCharacters();

        charactersImages.entrySet().forEach(character -> {
            character.getValue().sprite.update();
            OrientedSprite sprite = character.getValue().sprite();
            Image image;
            if (character.getKey().isStationary()) {
                image = character.getValue().sprite().getStandingImage();
            } else if (
                character.getValue().sprite().getCurrentFacingDirection()
                    .equals(character.getKey().getFacingDirection())) {
                image = character.getValue().sprite().getImage();
            } else {
                sprite = character.getValue().sprite().getNewSprite(character.getKey().getFacingDirection());
                image = sprite.getImage();
            }
            charactersImages.put(
                character.getKey(),
                new SpriteImageCombo(sprite, image, "main")
            );
        });
        /* Update bomb sprites: */
        normalBomb.update();
        bombImage = normalBomb.getImage();
        /* Update dying characters: */
        updateDeadCharacters();
    }


    private void scaleEverything() {
        mapImage = resizingEngine.getScaledMapImage(mapImage);
        grassBackgroundImage = resizingEngine.getScaledBackgroundImage(grassBackgroundImage);
        unbreakable = resizingEngine.getScaledCellImage(unbreakable);
        obstacle = resizingEngine.getScaledCellImage(obstacle);
        bombLine = resizingEngine.getScaledCellImage(bombLine);
        bombPlusOne = resizingEngine.getScaledCellImage(bombPlusOne);
        bombMinusOne = resizingEngine.getScaledCellImage(bombMinusOne);
        bombPower = resizingEngine.getScaledCellImage(bombPower);
        bombRemote = resizingEngine.getScaledCellImage(bombRemote);
        bombPierce = resizingEngine.getScaledCellImage(bombPierce);
        fireMax = resizingEngine.getScaledCellImage(fireMax);
        firePlusOne = resizingEngine.getScaledCellImage(firePlusOne);
        fireMinusOne = resizingEngine.getScaledCellImage(fireMinusOne);
        skatesPlusOne = resizingEngine.getScaledCellImage(skatesPlusOne);
        skateMinusOne = resizingEngine.getScaledCellImage(skateMinusOne);
        skull = resizingEngine.getScaledCellImage(skull);
    }

    private void checkForNewCharacters() {
        enemiesList.stream()
            .filter(enemy -> !charactersImages.keySet().contains(enemy) && enemy.isAlive())
            .forEach(enemy -> {
                final String colorCode =  colorCodes.isEmpty() ? "red" : colorCodes.get(0);
                colorCodes = !colorCodes.isEmpty() ? colorCodes.subList(1, colorCodes.size()) : List.of();
                final OrientedSprite sprite = new BombarderoOrientedSprite(
                    "character/" + colorCode + "/walking",
                    resourceGetter,
                    Direction.DOWN,
                    resizingEngine::getScaledCharacterImage
                );
                charactersImages.put(
                    enemy,
                    new SpriteImageCombo(
                        sprite,
                        sprite.getStandingImage(),
                        colorCode
                    )
                );
            });
        playersList.stream()
            .filter(player -> !charactersImages.keySet().contains(player) && player.isAlive())
            .forEach(player -> {
                final String colorCode = "main";
                final OrientedSprite playerSprite = new BombarderoOrientedSprite(
                    "character/main/walking",
                    resourceGetter, 
                    Direction.DOWN,
                    resizingEngine::getScaledCharacterImage
                );
                charactersImages.put(
                    player,
                    new SpriteImageCombo(
                        playerSprite,
                        playerSprite.getStandingImage(),
                        colorCode
                    )
                );
            });
    }

    /* I had to do it in separates for-loops because of ConcurrentModificationException(s) */
    private void updateDeadCharacters() {
        for (final Entry<Character, TimedBombarderoSprite> entry : dyingCharactersMap.entrySet()) {
            entry.getValue().update();
        }
        dyingCharactersMap = dyingCharactersMap.entrySet().stream()
            .filter(entry -> !entry.getValue().isOver())
            .collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));
        charactersImages.entrySet().forEach(entry -> {
            if (!entry.getKey().isAlive()) {
                final Image[] dyingAsset = SimpleBombarderoSprite.importAssets(
                    "dying",
                    "character/" + entry.getValue().colorCode() + "/dying",
                    resourceGetter,
                    resizingEngine::getScaledCharacterImage,
                    4
                );
                dyingCharactersMap.put(
                    entry.getKey(),
                    new TimedBombarderoSprite(dyingAsset, 4, 4)
                );
            }
        });
        dyingCharactersMap.keySet().forEach(charactersImages::remove);
    }

    private record SpriteImageCombo(OrientedSprite sprite, Image displayedImage, String colorCode) {
    }

}
