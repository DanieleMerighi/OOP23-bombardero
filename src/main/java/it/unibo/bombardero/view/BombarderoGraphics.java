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
    /* TODO: add component listener to update the GUI at each resize, the GUI must get the scale at each update */
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

        this.gameCard = new GameCard(frame, resizingEngine);
        this.endGameCard = new GameoverCard();
        this.menuCard = new MenuCard();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(gameCard);
        frame.setSize(resizingEngine.computeTotalWindowSize(frame));
        this.frame.setVisible(true);
    }

    /* TODO: initialize main menu, game and gamover panels: one class for each that extends JPanel and in which
     * we draw
     */
}
