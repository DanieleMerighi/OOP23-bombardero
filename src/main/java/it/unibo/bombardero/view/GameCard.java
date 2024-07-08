package it.unibo.bombardero.view;

import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Image;
import java.text.SimpleDateFormat;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import it.unibo.bombardero.core.api.Controller;
import it.unibo.bombardero.view.api.GraphicsEngine;
import it.unibo.bombardero.view.api.GraphicsEngine.EndGameState;

import java.awt.Font;
import java.util.List;
import java.util.Map;

/**
 * This class will never be serialised.
 */
@SuppressWarnings("serial")
/** 
 * This class represents a JPanel containing a the view of the main  
 * playable part of the game. It adds time displaying and personalized 
 * end game based on cases. 
 * @author Federico Bagattoni
 */
public final class GameCard extends GamePlayCard {

    private static final String TIMER_FORMAT = "mm:ss";

    private final Image clockImage;
    private final Font clockFont;
    private final JLabel winningBannerLabel;
    private final JLabel losingBannerLabel; 

    private final JButton backButton;
    
    private final Dimension imageClockPosition;
    private final Dimension timerPosition;
    private long timeLeft;

    /** 
     * Creates a new game card, togheter with two buttons to be displayed when
     * the game is paused, and an arena that can be updated throught the {@link #update(Map, List, List)}
     * method.
     * @param graphics the {@link GraphicsEngine} managing this class
     */
    public GameCard(final Controller controller, final GraphicsEngine graphics) {
        super(controller, graphics, Map.of(), List.of(), List.of());
 
        clockImage = graphics.getResizingEngine().getScaledClockImage(
            graphics.getResourceGetter().loadImage("overlay/clock")
        );
        clockFont = graphics.getResourceGetter().loadFont("mono");
        final Image backButtonImage = graphics.getResizingEngine().getScaledButtonImage(
            graphics.getResourceGetter().loadImage("overlay/buttons/BACK")
        );
        final Image backButtonImagePressed = graphics.getResizingEngine().getScaledButtonImage(
            graphics.getResourceGetter().loadImage("overlay/buttons/BACK_PRESSED")
        );
        losingBannerLabel = new JLabel(new ImageIcon(graphics.getResourceGetter().loadImage("overlay/defeat-panel")));
        winningBannerLabel = new JLabel(new ImageIcon(graphics.getResourceGetter().loadImage("overlay/victory-panel")));

        imageClockPosition = graphics.getResizingEngine().getImageClockPosition();
        timerPosition = graphics.getResizingEngine().getTimerPosition();
        this.setFont(clockFont);

        backButton = new JButton(new ImageIcon(backButtonImage));
        backButton.setPressedIcon(new ImageIcon(backButtonImagePressed));
        backButton.setFocusPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);

        backButton.addActionListener(e -> controller.endGame());
    }

    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final int fontYOffset = (int) (g.getFontMetrics(clockFont).getAscent() / 2);
        final SimpleDateFormat timerFormatter = new SimpleDateFormat(TIMER_FORMAT   );
        g.setFont(clockFont.deriveFont(Font.PLAIN, 16));
        g.drawString(timerFormatter.format(timeLeft), timerPosition.width, timerPosition.height + fontYOffset);
        g.drawImage(clockImage, imageClockPosition.width, imageClockPosition.height, null);
    }
    
    /**
     * Sets the time left for the displayed timer.
     * @param timeLeft the time left, in milliseconds
     */
    @Override
    public void setTimeLeft(final Long timeLeft) {
        this.timeLeft = timeLeft;
    }

    @Override
    void showMessage(BombarderoViewMessages message) {
    }

    @Override
    void displayEndView() {
        displayEndView(EndGameState.LOSE);
    }

    @Override
    void displayEndView(EndGameState endingType) {
        darkenView(PAUSE_DARKEN_ALPHA);
        if (endingType.equals(EndGameState.LOSE)) {
            this.add(losingBannerLabel);
        } else {
            this.add(winningBannerLabel);
        }
        this.add(backButton);
        this.revalidate();
        this.repaint(0);
    }

}
