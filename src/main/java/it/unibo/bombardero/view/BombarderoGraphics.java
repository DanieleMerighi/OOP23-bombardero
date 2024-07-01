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
public final class BombarderoGraphics {

    public final static String MENU_CARD = "menu";
    public final static String END_CARD = "end";
    public final static String GAME_CARD = "game";
    public final static String GUIDE_CARD = "guide";

    private final Controller controller;

    private final ResourceGetter resourceGetter = new ResourceGetter();
    private final ResizingEngine resizingEngine; 
    private final BufferedImage gameIconImage = resourceGetter.loadImage("icons/icon_happy");

    private final JFrame frame; 
    private final JPanel deck;
    private final CardLayout layout = new CardLayout();

    private GameCard gameCard;
    private GameoverCard endGameCard;
    private final MenuCard menuCard;
    private GuideCard guideCard;
    private String currentShowedCard = MENU_CARD;
    
    public BombarderoGraphics() {
        this.controller = new BombarderoController(this);
        this.frame = new JFrame("Bombardero: the Bomberman remake");
        this.deck = new JPanel(layout);

        frame.pack(); // calling pack on the frame generates the insets

        frame.addKeyListener(new KeyboardInput(controller));

        resizingEngine = new ResizingEngine(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(resizingEngine.getGameWindowSize(frame));
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.setIconImage(gameIconImage.getScaledInstance(64, 64, Image.SCALE_SMOOTH));

        this.menuCard = new MenuCard(controller, this, resourceGetter);
        deck.add(MENU_CARD, menuCard);
        layout.addLayoutComponent(menuCard, MENU_CARD);
        deck.validate();

        /* This listener calls for the ResizingEngine to dinamically update the 
        * frame's size when it is resized, see the implementation for more */
        frame.addComponentListener(new ComponentAdapter() {
            
            @Override
            public void componentResized(final ComponentEvent e) {
                frame.setSize(resizingEngine.getNewWindowSize(frame));
            }
        });
        
        frame.add(deck);
        showCard(MENU_CARD);
        this.frame.setVisible(true);
    }

    public void showCard(final String cardName) {
        layout.show(deck, cardName);
        currentShowedCard = cardName;
        this.frame.requestFocus();
    }

    public void initGameCard() {
        this.gameCard = new GameCard(this);
        this.endGameCard = new GameoverCard();
        deck.add(GAME_CARD, gameCard);
        deck.add(END_CARD, endGameCard);
        layout.addLayoutComponent(gameCard, GAME_CARD);
        layout.addLayoutComponent(endGameCard, END_CARD);
        deck.validate();
    }

    public void initGuideCard() {
        this.guideCard = new GuideCard(frame, controller, this, resourceGetter, resizingEngine);
        deck.add(GUIDE_CARD, guideCard);
        layout.addLayoutComponent(guideCard, GUIDE_CARD);
        deck.validate();
    }

    public void update() {
        if (GAME_CARD.equals(currentShowedCard)) {
            gameCard.updateGameState(controller.getMap(), controller.getMainPlayer(), controller.getEnemies());
            gameCard.setTimeLeft(controller.getTimeLeft().get());
            gameCard.repaint(0);
        }
        else if (GUIDE_CARD.equals(currentShowedCard)) {
            guideCard.updateGameState(controller.getMap(), controller.getMainPlayer(), controller.getEnemies());
            guideCard.repaint(0);
        }
    }

    public Dimension getFrameSize() {
        final Dimension dim = frame.getSize();
        return new Dimension(
            dim.width - frame.getInsets().right - frame.getInsets().left,
            dim.height - frame.getInsets().top - frame.getInsets().bottom
        );
    }

    public void setPausedView() {
        gameCard.setPausedView();
    }

    public void setUnpausedView() {
        gameCard.setUnpausedView();
    }

    public void setMessage(final BombarderoViewMessages message) {
        guideCard.showMessage(message);
    }

    public void displayEndGuide() {
        guideCard.displayEndGuide();
    }

    public Controller getController() {
        return this.controller;
    }

    public ResizingEngine getResizingEngine() {
        return this.resizingEngine;
    }

    public ResourceGetter getResourceGetter() {
        return this.resourceGetter;
    }

    public JFrame getParentFrame() {
        return this.frame;
    }
}
