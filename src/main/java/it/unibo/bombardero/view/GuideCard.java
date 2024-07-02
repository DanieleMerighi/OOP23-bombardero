package it.unibo.bombardero.view;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import javax.swing.JButton;
import java.util.Map; 
import java.util.HashMap; 
import java.util.Optional; 
import java.util.List; 

import it.unibo.bombardero.core.api.Controller;
import it.unibo.bombardero.view.sprites.api.Sprite;
import it.unibo.bombardero.view.sprites.impl.SimpleBombarderoSprite;
import it.unibo.bombardero.cell.Cell;
import it.unibo.bombardero.character.Character;
import it.unibo.bombardero.map.api.Pair;

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

    private transient Image startImage;
    private transient Image backImage;

    //private String message = "";
    private transient final Image messageBoxImage;
    private final Font font;

    private final JButton back;
    private final JButton start;
    private final JLabel messageBox;

    private final transient Dimension messageBoxSize = null;
    private final transient Sprite wasdSprite;
    private final transient Sprite spacebarSprite;
    private transient Optional<Sprite> currentShowedSprite;

    private transient final Map<Sprite, Dimension> spritesPlacingPoint = new HashMap<>();

    public GuideCard(final Controller controller, final BombarderoGraphics graphics, final Map<Pair, Cell> gameMap, List<Character> playersList, List<Character> enemiesList) {
        super(graphics, gameMap, playersList, enemiesList);

        final ResourceGetter resourceGetter = graphics.getResourceGetter();

        // CHECKSTYLE: MagicNumber OFF
        font = resourceGetter.loadFont("mono");
        messageBoxImage = resourceGetter.loadImage("overlay/dialog");
        startImage = graphics.getResizingEngine().getScaledButtonImage(resourceGetter.loadImage("overlay/buttons/PLAY"));
        backImage = graphics.getResizingEngine().getScaledButtonImage(resourceGetter.loadImage("overlay/buttons/BACK"));
        wasdSprite = new SimpleBombarderoSprite(
            SimpleBombarderoSprite.importAssets("WASD", "overlay/buttons/WASD", resourceGetter, graphics.getResizingEngine()::getScaledWASDImage, 8),
            8, 12);
        spacebarSprite = new SimpleBombarderoSprite(
            SimpleBombarderoSprite.importAssets("SPACE", "overlay/buttons/SPACEBAR", resourceGetter, graphics.getResizingEngine()::getScaledSpaceImage, 2),
            2, 32);
        // CHECKSTYLE: MagicNumber ON
        
        currentShowedSprite = Optional.of(wasdSprite);
        
        spritesPlacingPoint.put(wasdSprite, graphics.getResizingEngine().getWasdGuidePosition());
        spritesPlacingPoint.put(spacebarSprite, graphics.getResizingEngine().getSpaceGuidePosition());

        this.setLayout(new GridLayout(5, 1));

        back = new JButton(new ImageIcon(backImage));
        start = new JButton(new ImageIcon(startImage));
        messageBox = new JLabel(new ImageIcon(messageBoxImage));
        
        messageBox.setHorizontalTextPosition(SwingConstants.CENTER);
        messageBox.setVerticalTextPosition(SwingConstants.CENTER);
        messageBox.setFont(font.deriveFont( 10.0f));

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

        back.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                controller.endGuide();
                graphics.showCard(BombarderoGraphics.MENU_CARD);
            }
            
        });

        start.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                controller.endGuide();
                controller.startGame();
            }
            
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (currentShowedSprite.isPresent()) {
            g.drawImage(currentShowedSprite.get().getImage(), spritesPlacingPoint.get(currentShowedSprite.get()).width, spritesPlacingPoint.get(currentShowedSprite.get()).height, null);
        }
    }

    @Override
    public void updateSprites() {
        super.updateSprites();
        wasdSprite.update();
        spacebarSprite.update();
    }

    public void showMessage(final BombarderoViewMessages message) {
        messageBox.setText(message.getMessage());
        showAnimatedKeys(message);
    }

    public void displayEndGuide() {
        this.remove(messageBox);
        this.add(new JLabel());
        this.add(new JLabel());
        this.add(start);
        this.add(back);
        this.revalidate();
        this.repaint();
    }

    private void showAnimatedKeys(final BombarderoViewMessages message) {
        currentShowedSprite = switch(message) {
            case END_GUIDE -> Optional.empty();
            case EXPLAIN_MOVEMENT -> Optional.of(wasdSprite);
            case EXPLAIN_POWERUP -> Optional.empty();
            case KILL_ENEMY -> Optional.empty();
            case PLACE_BOMB -> Optional.of(spacebarSprite);
            default -> Optional.empty();
        };
    }
}
