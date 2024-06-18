package it.unibo.bombardero.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

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

    private Image messageBox;
    private final Font font;

    public GuideCard(final JFrame parentFrame, final Controller controller, final BombarderoGraphics graphics, final ResourceGetter resourceGetter, final ResizingEngine resizingEngine) {
        super(graphics);

        font = resourceGetter.loadFont("mono");
        messageBox = resourceGetter.loadImage("overlay/dialog");
        JButton back = new JButton("back");
        JButton start = new JButton("start");

        this.add(back);
        this.add(start);
        
        back.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // controller.endGuide(); or some kind of thing
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
    public void paint(Graphics g) {
        super.paint(g);
    }

    public void showMessage(final BombarderoViewMessages message) {
        return;
    }
    
}
