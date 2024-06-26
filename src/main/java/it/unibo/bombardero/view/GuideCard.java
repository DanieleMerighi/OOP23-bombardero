package it.unibo.bombardero.view;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;

import javax.swing.JButton;
import javax.swing.JFrame;

import it.unibo.bombardero.core.api.Controller;

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

    private Image startImage;
    private Image backImage;

    private String message = "";
    private Image messageBoxImage;
    private final Font font;

    private JButton back;
    private JButton start;

    private final Dimension messageBoxPlacingPoint;

    public GuideCard(final JFrame parentFrame, final Controller controller, final BombarderoGraphics graphics, final ResourceGetter resourceGetter, final ResizingEngine resizingEngine) {
        super(graphics);

        // CHECKSTYLE: MagicNumber OFF
        font = resourceGetter.loadFont("mono");
        messageBoxImage = resourceGetter.loadImage("overlay/dialog");
        startImage = resourceGetter.loadImage("menu/play");
        backImage = resourceGetter.loadImage("menu/play");
        // CHECKSTYLE: MagicNumber ON
        
        messageBoxPlacingPoint = graphics.getResizingEngine().getMessageBoxPosition();

        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        back = new JButton(new ImageIcon(backImage));
        start = new JButton(new ImageIcon(startImage));
        back.setBorder(null);
        start.setBorder(null);
        
        back.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                graphics.showCard(BombarderoGraphics.MENU_CARD);
            }
            
        });

        start.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                controller.startGame();
            }
            
        });
    }

    @Override 
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        g2d.drawString(message, messageBoxPlacingPoint.width, messageBoxPlacingPoint.height);
        g2d.drawImage(messageBoxImage, messageBoxPlacingPoint.width, messageBoxPlacingPoint.height, null);
    }

    public void showMessage(final BombarderoViewMessages message) {
        this.message = message.getMessage();
    }

    public void displayEndGuide() {
        this.add(back);
        this.add(start);
    }
    
}
