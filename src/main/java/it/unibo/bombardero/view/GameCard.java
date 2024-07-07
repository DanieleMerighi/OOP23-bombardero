package it.unibo.bombardero.view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.Image;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    private static final SimpleDateFormat TIMER_FORMAT = new SimpleDateFormat("mm:ss");

    private final Image clockImage;
    private final Font clockFont;

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

        imageClockPosition = graphics.getResizingEngine().getImageClockPosition();
        timerPosition = graphics.getResizingEngine().getTimerPosition();
        this.setFont(clockFont);
    }

    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2d = (Graphics2D) g;
        final int fontYOffset = (int) (g.getFontMetrics(clockFont).getAscent() / 2);
        g2d.setFont(clockFont.deriveFont(Font.PLAIN, 16));
        g2d.drawString(getFormattedTime(), timerPosition.width, timerPosition.height + fontYOffset);
        g2d.drawImage(clockImage, imageClockPosition.width, imageClockPosition.height, null);
    }
    
    /**
     * Sets the time left for the displayed timer.
     * @param timeLeft the time left, in milliseconds
     */
    @Override
    public void setTimeLeft(final Long timeLeft) {
        this.timeLeft = timeLeft;
    }

    private String getFormattedTime() {
        return TIMER_FORMAT.format(new Date(timeLeft));
    }

    @Override
    void showMessage(BombarderoViewMessages message) {
    }

    @Override
    void displayEndView() {
    }

    @Override
    void displayEndView(EndGameState endingType) {
    }
}
