package it.unibo.bombardero.view;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import it.unibo.bombardero.cell.Cell;
import it.unibo.bombardero.core.KeyboardInput;
import it.unibo.bombardero.core.api.Controller;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.character.Character;

/** 
 * The graphics engine for the game, managing the layout and the component's update.
 */
public final class BombarderoGraphics implements Graphics {

    private final ResourceGetter resourceGetter = new ResourceGetter();
    private final ResizingEngine resizingEngine; 
    private final BufferedImage gameIconImage = resourceGetter.loadImage("icons/icon_happy");

    private final JFrame frame; 
    private final JPanel deck;
    private final CardLayout layout = new CardLayout();

    private GameCard gameCard;
    private final MenuCard menuCard;
    private GuideCard guideCard;
    private viewCards currentShowedCard = viewCards.MENU;

    /**
     * Creates a new Graphics engine, creating and showing the window frame toghether
     * with the various game modes panels. 
     * @param controller the controller with whom the Graphics has to comunicate
     */
    public BombarderoGraphics(final Controller controller) {
        this.frame = new JFrame("Bombardero: the Bomberman remake");
        this.deck = new JPanel(layout);

        frame.pack(); // calling pack on the frame generates the insets

        frame.addKeyListener(new KeyboardInput(controller));

        resizingEngine = new ResizingEngine(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(resizingEngine.getGameWindowSize(frame));
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setIconImage(gameIconImage.getScaledInstance(64, 64, Image.SCALE_SMOOTH));

        this.menuCard = new MenuCard(controller, this, resourceGetter);
        this.guideCard = new GuideCard(controller, this, Map.of(), List.of(), List.of());
        this.gameCard = new GameCard(this);

        deck.add(viewCards.GUIDE.getStringId(), guideCard);
        layout.addLayoutComponent(guideCard, viewCards.GUIDE.getStringId());

        deck.add(viewCards.MENU.getStringId(), menuCard);
        layout.addLayoutComponent(menuCard, viewCards.MENU.getStringId());

        deck.add(viewCards.GAME.getStringId(), gameCard);
        layout.addLayoutComponent(gameCard, viewCards.GAME.getStringId());
        deck.validate();
        
        frame.add(deck);
        showCard(viewCards.MENU);
        frame.setVisible(true);
    }

    @Override
    public void showCard(final viewCards cardName) {
        layout.show(deck, cardName.getStringId());
        currentShowedCard = cardName;
        frame.requestFocus();
    }

    @Override
    public void update(
        final Map<Pair, Cell> map,
        final List<Character> playerList,
        final List<Character> enemiesList,
        final Optional<Long> timeLeft) {
        if (viewCards.GAME.equals(currentShowedCard)) {
            gameCard.update(map, playerList, enemiesList);
            gameCard.setTimeLeft(timeLeft.get());
            gameCard.repaint(0);
        }
        else if (viewCards.GUIDE.equals(currentShowedCard)) {
            guideCard.update(map, playerList, enemiesList);
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

    @Override
    public void setPausedView() {
        gameCard.setPausedView();
    }

    @Override
    public void setUnpausedView() {
        gameCard.setUnpausedView();
    }

    @Override
    public void setMessage(final BombarderoViewMessages message) {
        guideCard.showMessage(message);
    }

    public void displayEndGuide() {
        guideCard.displayEndGuide();
    }
 
    public void displayEndGame(final endGameState stateToDisplay) {
        gameCard.displayEndGameState(stateToDisplay);
    }

    @Override
    public ResizingEngine getResizingEngine() {
        return this.resizingEngine;
    }

    @Override
    public ResourceGetter getResourceGetter() {
        return this.resourceGetter;
    }

    @Override
    public JFrame getParentFrame() {
        return this.frame;
    }
}
