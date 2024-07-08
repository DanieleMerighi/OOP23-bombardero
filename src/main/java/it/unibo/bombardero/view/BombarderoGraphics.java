package it.unibo.bombardero.view;

import java.awt.CardLayout;
import java.awt.image.BufferedImage;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

import it.unibo.bombardero.cell.Cell;
import it.unibo.bombardero.core.KeyboardInput;
import it.unibo.bombardero.core.api.Controller;
import it.unibo.bombardero.map.api.GenPair;
import it.unibo.bombardero.view.api.GraphicsEngine;
import it.unibo.bombardero.character.Character;

/** 
 * The graphics engine for the game, managing the layout and the component's update.
 */
public final class BombarderoGraphics implements GraphicsEngine {

    private final ResourceGetter resourceGetter = new ResourceGetter();
    private final ResizingEngine resizingEngine; 
    private final BufferedImage gameIconImage = resourceGetter.loadImage("icons/icon_happy");

    private final JFrame frame; 
    private final JPanel deck;
    private final CardLayout layout = new CardLayout();

    private final GamePlayCard gameCard;
    private final GamePlayCard guideCard;
    private final MenuCard menuCard;
    private ViewCards currentCard;
    private final Map<ViewCards, GamePlayCard> cardsMap = new HashMap<>();

    /**
     * Creates a new Graphics engine, creating and showing the window frame toghether
     * with the various game modes panels. 
     * @param controller the controller with whom the Graphics has to comunicate
     */
    public BombarderoGraphics(final Controller controller) {
        this.frame = new JFrame("Bombardero: the Bomberman remake");
        this.deck = new JPanel(layout);

        frame.pack(); // calling pack on the frame generates the insets

        frame.addKeyListener(new KeyboardInput(controller, 0));

        resizingEngine = new ResizingEngine(this, frame.getInsets());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(resizingEngine.getGameWindowSize());
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setIconImage(gameIconImage.getScaledInstance(64, 64, Image.SCALE_SMOOTH));

        menuCard = new MenuCard(controller, this, resourceGetter);
        this.guideCard = new GuideCard(controller, this, Map.of(), List.of(), List.of());
        this.gameCard = new GameCard(controller, this);

        deck.add(ViewCards.GUIDE.getStringId(), guideCard);
        layout.addLayoutComponent(guideCard, ViewCards.GUIDE.getStringId());
        cardsMap.put(ViewCards.GUIDE, guideCard);

        deck.add(ViewCards.MENU.getStringId(), menuCard);
        layout.addLayoutComponent(menuCard, ViewCards.MENU.getStringId());

        deck.add(ViewCards.GAME.getStringId(), gameCard);
        layout.addLayoutComponent(gameCard, ViewCards.GAME.getStringId());
        cardsMap.put(ViewCards.GAME, gameCard);
        deck.validate();
 
        frame.add(deck);
        showGameScreen(ViewCards.MENU);
        frame.setVisible(true);
    }

    @Override
    public void showGameScreen(final ViewCards cardName) {
        layout.show(deck, cardName.getStringId());
        currentCard = cardName;
        frame.requestFocus();
    }

    @Override
    public void update(
        final Map<GenPair<Integer, Integer>, Cell> map,
        final List<Character> playerList,
        final List<Character> enemiesList,
        final Optional<Long> timeLeft) {
        cardsMap.get(currentCard).update(map, playerList, enemiesList);
        cardsMap.get(currentCard).setTimeLeft(timeLeft.orElse(0L));
        cardsMap.get(currentCard).repaint(0L);
    }

    @Override
    public void setPausedView() {
        cardsMap.get(currentCard).setPausedView();
    }

    @Override
    public void setUnpausedView() {
        cardsMap.get(currentCard).setUnpausedView();
    }

    @Override
    public void setMessage(final BombarderoViewMessages message) {
        cardsMap.get(currentCard).showMessage(message);
    }

    @Override
    public void showEndScreen(final EndGameState gameState) {
        cardsMap.get(currentCard).displayEndView(gameState);
    }

    @Override
    public ResizingEngine getResizingEngine() {
        return this.resizingEngine;
    }

    @Override
    public ResourceGetter getResourceGetter() {
        return this.resourceGetter;
    }

}
