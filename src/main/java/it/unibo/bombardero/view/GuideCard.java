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
import it.unibo.bombardero.view.GraphicsEngine.viewCards;
import it.unibo.bombardero.view.sprites.api.Sprite;
import it.unibo.bombardero.view.sprites.impl.SimpleBombarderoSprite;

/**
 * This class will never be serialised.
 */
@SuppressWarnings("serial")
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

    private static final int LAYOUT_ROWS = 1;
    private static final int LAYOUT_COLS = 1;

    private final transient Image startImage;
    private final transient Image backImage;

    // private String message = "";
    private final transient Image messageBoxImage;
    private final Font font;

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
        super(graphics, gameMap, playersList, enemiesList);

        final ResourceGetter resourceGetter = graphics.getResourceGetter();

        // CHECKSTYLE: MagicNumber OFF
        font = resourceGetter.loadFont("mono");
        messageBoxImage = resourceGetter.loadImage("overlay/dialog");
        startImage = graphics.getResizingEngine()
                .getScaledButtonImage(resourceGetter.loadImage("overlay/buttons/PLAY"));
        backImage = graphics.getResizingEngine().getScaledButtonImage(resourceGetter.loadImage("overlay/buttons/BACK"));
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
        this.add(new JLabel());
        this.add(messageBox);

        back.addActionListener(e -> {
            controller.endGuide();
            graphics.showCard(viewCards.MENU);
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

    /**
     * Shows a message to the view. Only one message will be displayed
     * at the time. The messages' content can be seen at
     * {@link BombarderoViewMessages}.
     * 
     * @param message the message to be shown
     * @see BombarderoViewMessages
     */
    public void showMessage(final BombarderoViewMessages message) {
        messageBox.setText(message.getMessage());
        showAnimatedKeys(message);
    }

    /**
     * Displays the end of the guide: two buttons, one to proceed to the game,
     * the other to go back to the menu.
     */
    public void displayEndGuide() {
        this.remove(messageBox);
        this.add(new JLabel());
        this.add(new JLabel());
        this.add(start);
        this.add(back);
        this.revalidate();
        this.repaint();
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
