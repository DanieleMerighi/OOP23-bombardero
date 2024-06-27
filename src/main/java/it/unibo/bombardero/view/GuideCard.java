package it.unibo.bombardero.view;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.jgrapht.generate.GridGraphGenerator;
import org.jgrapht.util.SupplierException;

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

    //private String message = "";
    private Image messageBoxImage;
    private final Font font;

    private JButton back;
    private JButton start;
    private final JLabel messageBox;

    private final Dimension messageBoxPlacingPoint;
    private final Dimension messageBoxSize = null;

    public GuideCard(final JFrame parentFrame, final Controller controller, final BombarderoGraphics graphics, final ResourceGetter resourceGetter, final ResizingEngine resizingEngine) {
        super(graphics);

        // CHECKSTYLE: MagicNumber OFF
        font = resourceGetter.loadFont("mono");
        messageBoxImage = resourceGetter.loadImage("overlay/dialog");
        startImage = resourceGetter.loadImage("menu/play");
        backImage = resourceGetter.loadImage("menu/play");
        // CHECKSTYLE: MagicNumber ON
        
        messageBoxPlacingPoint = graphics.getResizingEngine().getMessageBoxPosition(); 
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

    public void showMessage(final BombarderoViewMessages message) {
        messageBox.setText(message.getMessage());
    }

    public void displayEndGuide() {
        this.remove(messageBox);
        this.add(new JLabel());
        this.add(start);
        this.add(back);
        this.revalidate();
        this.repaint();
    }
}
