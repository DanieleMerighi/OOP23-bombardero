package it.unibo.bombardero.view;

import javax.swing.JButton;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.Image;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.GridLayout;
import javax.swing.JLabel;

import it.unibo.bombardero.view.GraphicsEngine.EndGameState;

import java.awt.Font;
import javax.swing.ImageIcon;

import java.util.List;
import java.util.Map;

/**
 * This class will never be serialised.
 */
@SuppressWarnings("serial")
/** 
 * This class represents a JPanel containing a the view of some 
 * playable part of the game (e.g. guide, gameplay, sandbox... etc).
 * @author Federico Bagattoni
 */
public final class GameCard extends GamePlayCard {

    private static final SimpleDateFormat TIMER_FORMAT = new SimpleDateFormat("mm:ss");
    private static final int LAYOUT_COLS = 1;
    private static final int LAYOUT_ROWS = 5;

    private final Image clockImage;
    private final Font clockFont;

    /* Pause state buttons: */
    private final JButton resumeButton;
    private final JButton quitButton;

    private final Dimension imageClockPosition;
    private final Dimension timerPosition;
    private long timeLeft;

    /** 
     * Creates a new game card, togheter with two buttons to be displayed when
     * the game is paused, and an arena that can be updated throught the {@link #update(Map, List, List)}
     * method.
     * @param graphics the {@link GraphicsEngine} managing this class
     */
    public GameCard(final GraphicsEngine graphics) {
        super(graphics, Map.of(), List.of(), List.of());
 
        clockImage = graphics.getResizingEngine().getScaledClockImage(
            graphics.getResourceGetter().loadImage("overlay/clock")
        );
        clockFont = graphics.getResourceGetter().loadFont("mono");
        final Image resumeButtonImage = graphics.getResourceGetter().loadImage("overlay/buttons/RESUME");
        final Image quitButtonImage = graphics.getResourceGetter().loadImage("overlay/buttons/QUIT");

        imageClockPosition = graphics.getResizingEngine().getImageClockPosition();
        timerPosition = graphics.getResizingEngine().getTimerPosition();

        resumeButton = new JButton(new ImageIcon(resumeButtonImage));
        quitButton = new JButton(new ImageIcon(quitButtonImage));

        quitButton.setBorder(null);
        resumeButton.setBorder(null);
        quitButton.setBorderPainted(false);
        resumeButton.setBorderPainted(false);
        resumeButton.setContentAreaFilled(false);
        quitButton.setContentAreaFilled(false);
        resumeButton.setFocusPainted(false);
        quitButton.setFocusPainted(false);
        this.add(new JLabel());
        this.add(new JLabel());

        this.setFont(clockFont);
        this.setLayout(new GridLayout(LAYOUT_ROWS, LAYOUT_COLS));
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
     * Sets the paused view, adding the buttons to the panel and performing
     * an update.
     */
    public void setPausedView() {
        this.add(resumeButton);
        this.add(quitButton);
        this.revalidate();
        this.repaint(0);
    }

    /**
     * Removes the paused view, removing the buttons from the panel
     * and performing ad update.
     */
    public void setUnpausedView() {
        this.remove(quitButton);
        this.remove(resumeButton);
        this.revalidate();
        this.repaint(0);
    }

    /**
     * Displays the end game state based off the passed argument. Displays
     * a panel with the report of the game. 
     * @param stateToDisplay the state to be displayed
     * @see {@link endGameState}
     */
    public void displayEndGameState(final EndGameState stateToDisplay) {
        blurView();
    }

    /**
     * Sets the time left for the displayed timer.
     * @param timeLeft the time left, in milliseconds
     */
    public void setTimeLeft(final long timeLeft) {
        this.timeLeft = timeLeft;
    }

    private String getFormattedTime() {
        return TIMER_FORMAT.format(new Date(timeLeft));
    }

}
