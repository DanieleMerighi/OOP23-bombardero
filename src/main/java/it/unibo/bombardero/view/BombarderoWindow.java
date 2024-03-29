package it.unibo.bombardero.view;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.GraphicsEnvironment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class BombarderoWindow extends JFrame {

    private final ResourceGetter resourceGetter = new ResourceGetter();
    private final ResizingEngine resizingEngine = new ResizingEngine();
    private final BufferedImage staticMapImage = resourceGetter.loadImage("map");
    private JPanel deck = new JPanel(new CardLayout());
    private CardLayout layout = (CardLayout)deck.getLayout();
    
    public BombarderoWindow() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        this.setBounds(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds());
        this.setVisible(true);
    }
}
