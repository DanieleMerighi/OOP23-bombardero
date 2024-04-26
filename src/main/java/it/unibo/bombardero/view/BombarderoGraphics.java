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

import javax.swing.ImageIcon;
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
    /* TODO: do and import icon: private final ImageIcon game_icon = resourceGetter.loadImage(""); */ 

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
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(resizingEngine.getInitialWindowSize(frame));
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        /* TODO: set icon once has been done, frame.setIconImage(); */ 
        
        this.gameCard = new GameCard(frame, resizingEngine, controller);
        this.endGameCard = new GameoverCard();
        this.menuCard = new MenuCard();

        /* This listener calls for the ResizingEngine to dinamically update the 
        * frame's size when it is resized, see the implementation for more */
        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                frame.setSize(resizingEngine.getNewWindowSize(frame));
            }
        });

        frame.add(gameCard);
        this.frame.setVisible(true);
    }
}
