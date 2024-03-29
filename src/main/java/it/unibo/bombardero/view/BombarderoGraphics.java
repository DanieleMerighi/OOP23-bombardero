package it.unibo.bombardero.view;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.image.BufferedImage;
import java.awt.GraphicsEnvironment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/** 
 * The graphics engine for the game, managing the layout and the component's update
 * @author Federico Bagattoni
 */
public class BombarderoGraphics {

    private final ResourceGetter resourceGetter = new ResourceGetter();
    private final ResizingEngine resizingEngine = new ResizingEngine();
    private final BufferedImage staticMapImage = resourceGetter.loadImage("map");

    private JFrame frame = new JFrame();
    private JPanel deck = new JPanel(new CardLayout());
    private CardLayout layout = (CardLayout)deck.getLayout();
    
    public BombarderoGraphics() {

    }
}
