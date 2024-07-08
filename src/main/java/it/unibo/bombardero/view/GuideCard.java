package it.unibo.bombardero.view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import it.unibo.bombardero.cell.Cell;
import it.unibo.bombardero.character.Character;
import it.unibo.bombardero.core.api.Controller;
import it.unibo.bombardero.map.api.GenPair;
import it.unibo.bombardero.view.api.GraphicsEngine;
import it.unibo.bombardero.view.api.GraphicsEngine.EndGameState;
import it.unibo.bombardero.view.api.GraphicsEngine.ViewCards;
import it.unibo.bombardero.view.sprites.api.Sprite;
import it.unibo.bombardero.view.sprites.impl.SimpleBombarderoSprite;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
/**
 * This class will never be serialised.
 */
@SuppressWarnings("serial")
@SuppressFBWarnings(
    value="SE_TRANSIENT_FIELD_NOT_RESTORED",
    justification = "This class will never be serialized"
)
/**
 * This class contains the panel for the Guide of
 * the game. It essentially works the same as the {@link GameCard}
 * with minor adjustments; such as removal of the timer and the Graphics
 * and adding instruction for the player.
 * <p>
 * Instructions on what to display are given by the {@link Controller}
 * through appropriate methods.
 * 
 * @author Federico Bagattoni
 */
public final class GuideCard extends GamePlayCard {

    private static final int LAYOUT_ROWS = 5;
    private static final int LAYOUT_COLS = 1;

    // private String message = "";

    private final JButton back;
    private final JButton start;
    private final JLabel messageBox;

    private final transient Sprite wasdSprite;
    private final transient Sprite spacebarSprite;
    private transient Optional<Sprite> currentShowedSprite;

    private final transient Map<Sprite, Dimension> spritesPlacingPoint = new HashMap<>();

    /**
     * Creates a new GuideCard using the passed arguments to build the button's actions
     * and using the character's lists to build the view.
     * 
     * @param controller  the {@link Controller} overseering the view.
     * @param graphics    the {@link GraphicsEngine} that manages this card
     * @param gameMap     the gamemap to render initially
     * @param playersList the players to render initally
     * @param enemiesList the enemies to rendere initially
     */
    public GuideCard(
            final Controller controller,
            final GraphicsEngine graphics,
            final Map<GenPair<Integer, Integer>, Cell> gameMap,
            final List<Character> playersList,
            final List<Character> enemiesList) {
        super(controller, graphics, gameMap, playersList, enemiesList);

        final ResourceGetter resourceGetter = graphics.getResourceGetter();

        // CHECKSTYLE: MagicNumber OFF
        final Font font = resourceGetter.loadFont("mono");
        final Image messageBoxImage = resourceGetter.loadImage("overlay/dialog");
        final Image startImage = graphics.getResizingEngine()
            .getScaledButtonImage(resourceGetter.loadImage("overlay/buttons/PLAY"));
        final Image backImage = graphics.getResizingEngine()
            .getScaledButtonImage(resourceGetter.loadImage("overlay/buttons/BACK"));
        final Image backImagePressed = graphics.getResizingEngine().getScaledButtonImage(
            resourceGetter.loadImage("overlay/buttons/BACK_PRESSED")
        );
        final Image startImagePressed = graphics.getResizingEngine().getScaledButtonImage(
            resourceGetter.loadImage("overlay/buttons/PLAY_PRESSED")
        );
        wasdSprite = new SimpleBombarderoSprite(
                SimpleBombarderoSprite.importAssets("WASD", "overlay/buttons/WASD", resourceGetter,
                        graphics.getResizingEngine()::getScaledWASDImage, 8),
                8, 12);
        spacebarSprite = new SimpleBombarderoSprite(
                SimpleBombarderoSprite.importAssets("SPACE", "overlay/buttons/SPACEBAR", resourceGetter,
                        graphics.getResizingEngine()::getScaledSpaceImage, 2),
                2, 32);
        // CHECKSTYLE: MagicNumber ON

        currentShowedSprite = Optional.of(wasdSprite);

        spritesPlacingPoint.put(wasdSprite, graphics.getResizingEngine().getWasdGuidePosition());
        spritesPlacingPoint.put(spacebarSprite, graphics.getResizingEngine().getSpaceGuidePosition());

        this.setLayout(new GridLayout(LAYOUT_ROWS, LAYOUT_COLS));

        back = new JButton(new ImageIcon(backImage));
        start = new JButton(new ImageIcon(startImage));
        messageBox = new JLabel(new ImageIcon(messageBoxImage));

        messageBox.setHorizontalTextPosition(SwingConstants.CENTER);
        messageBox.setVerticalTextPosition(SwingConstants.CENTER);
        messageBox.setFont(font.deriveFont(10.0f));

        back.setBorder(null);
        start.setBorder(null);
        back.setBorderPainted(false);
        start.setBorderPainted(false);
        start.setContentAreaFilled(false);
        back.setContentAreaFilled(false);
        start.setFocusPainted(false);
        back.setFocusPainted(false);
        back.setPressedIcon(new ImageIcon(backImagePressed));
        start.setPressedIcon(new ImageIcon(startImagePressed));
        this.add(messageBox);

        back.addActionListener(e -> {
            controller.endGuide();
            graphics.showGameScreen(ViewCards.MENU);
        });

        start.addActionListener(e -> {
            controller.endGuide();
            controller.startGame();
        });
    }

    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        if (currentShowedSprite.isPresent()) {
            g.drawImage(currentShowedSprite.get().getImage(), spritesPlacingPoint.get(currentShowedSprite.get()).width,
                    spritesPlacingPoint.get(currentShowedSprite.get()).height, null);
        }
    }

    @Override
    public void showMessage(final BombarderoViewMessages message) {
        messageBox.setText(message.getMessage());
        showAnimatedKeys(message);
    }

    /**
     * Displays the end of the guide: two buttons, one to proceed to the game,
     * the other to go back to the menu.
     */
    @Override
    public void displayEndView() {
        darkenView(GamePlayCard.PAUSE_DARKEN_ALPHA);
        this.add(start);
        this.add(back);
        this.revalidate();
        this.repaint();
    }

    @Override
    public void displayEndView(final EndGameState endingType) {
        displayEndView();
    }

    @Override
    public void setTimeLeft(final Long timeLeft) {

    }

    @Override
    protected void updateSprites() {
        super.updateSprites();
        wasdSprite.update();
        spacebarSprite.update();
    }

    private void showAnimatedKeys(final BombarderoViewMessages message) {
        currentShowedSprite = switch (message) {
            case END_GUIDE -> Optional.empty();
            case EXPLAIN_MOVEMENT -> Optional.of(wasdSprite);
            case EXPLAIN_POWERUP -> Optional.empty();
            case KILL_ENEMY -> Optional.empty();
            case PLACE_BOMB -> Optional.of(spacebarSprite);
            default -> Optional.empty();
        };
    }
}
