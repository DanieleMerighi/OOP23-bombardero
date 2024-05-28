package it.unibo.bombardero.view;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;

import it.unibo.bombardero.core.KeyboardInput;
import it.unibo.bombardero.core.api.Controller;
import it.unibo.bombardero.core.impl.BombarderoController;

/** 
 * The graphics engine for the game, managing the layout and the component's update
 * @author Federico Bagattoni
 */
public class BombarderoGraphics {

    public final static String MENU_CARD = "menu";
    public final static String END_CARD = "end";
    public final static String GAME_CARD = "game";

    private Controller controller;

    private final ResourceGetter resourceGetter = new ResourceGetter();
    private final ResizingEngine resizingEngine = new ResizingEngine();
    private final BufferedImage game_icon = resourceGetter.loadImage("icons/icon_happy");

    private final JFrame frame; 
    private final JPanel deck;
    private final CardLayout layout = new CardLayout();
    private final KeyboardInput keyInput;

    private GameCard gameCard;
    private GameoverCard endGameCard;
    private final MenuCard menuCard;
    
    public BombarderoGraphics() {
        this.controller = new BombarderoController(this);
        this.frame = new JFrame("Bombardero: the Bomberman remake");
        this.deck = new JPanel(layout);

        keyInput = new KeyboardInput(controller);
        frame.addKeyListener(keyInput);
        
        frame.pack(); // calling pack on the frame generates the insets
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(resizingEngine.getInitialWindowSize(frame));
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.setIconImage(game_icon.getScaledInstance(64, 64, Image.SCALE_SMOOTH));

        this.menuCard = new MenuCard(controller, this, resourceGetter);
        deck.add(MENU_CARD, menuCard);
        layout.addLayoutComponent(menuCard, MENU_CARD);
        deck.validate();

        /* This listener calls for the ResizingEngine to dinamically update the 
        * frame's size when it is resized, see the implementation for more */
        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                frame.setSize(resizingEngine.getNewWindowSize(frame));
            }
        });
        // Recives keyboard input
        frame.add(deck);
        showCard(MENU_CARD);
        this.frame.setVisible(true);
    }

    public void showCard(final String cardName) {
        layout.show(deck, cardName);
        System.out.println("\"" + cardName + "\"" + " card showed");
    }

    public void initGameCard() {
        this.gameCard = new GameCard(frame, resizingEngine, controller);
        this.endGameCard = new GameoverCard();
        deck.add(GAME_CARD, gameCard);
        deck.add(END_CARD, endGameCard);
        layout.addLayoutComponent(gameCard, GAME_CARD);
        layout.addLayoutComponent(endGameCard, END_CARD);
        deck.validate();
    }

    public void update() {
        gameCard.updateMap();
        gameCard.repaint(0);
    }

    public Dimension getFrameSize() {
        Dimension dim = frame.getSize();
        return new Dimension(
            dim.width - frame.getInsets().right - frame.getInsets().left,
            dim.height - frame.getInsets().top - frame.getInsets().bottom
        );
    }
}
