package it.unibo.bombardero.view;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jgrapht.alg.spanning.EsauWilliamsCapacitatedMinimumSpanningTree;

import it.unibo.bombardero.core.api.Controller;
import it.unibo.bombardero.core.impl.BombarderoController;

/** 
 * The graphics engine for the game, managing the layout and the component's update
 * @author Federico Bagattoni
 */
public class BombarderoGraphics {

    private Controller controller;

    private final ResourceGetter resourceGetter = new ResourceGetter();
    private final ResizingEngine resizingEngine = new ResizingEngine();
    private final BufferedImage staticMapImage = resourceGetter.loadImage("map");

    private final JFrame frame; 
    private JPanel deck;
    private CardLayout layout;

    private final GameCard gameCard;
    private final GameoverCard endGameCard;
    private final MenuCard menuCard;
    
    public BombarderoGraphics() {
        this.controller = new BombarderoController(this);
        this.frame = new JFrame();
        this.deck = new JPanel(new CardLayout());
        this.layout = (CardLayout)deck.getLayout();
        
        frame.pack(); // calling pack on the frame generates the insets 
        this.gameCard = new GameCard(frame, resizingEngine, controller);
        this.endGameCard = new GameoverCard();
        this.menuCard = new MenuCard();

        /* Adding a listener so that the Frame cannot resize less than the size of the map */
        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                Dimension d = frame.getSize();
                Dimension minDimension = gameCard.getMinimumSize();
                if(minDimension.getWidth() > d.getWidth()) {
                    d.width = minDimension.width;
                }
                else if(minDimension.getHeight() > d.getHeight()) {
                    d.height = minDimension.height;
                }
                frame.setSize(d);
            }
        });
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(gameCard);
        /* frame.setSize(resizingEngine.computeTotalWindowSize(frame)); */
        frame.setSize(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width, GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height);
        this.frame.setVisible(true);
    }
}
