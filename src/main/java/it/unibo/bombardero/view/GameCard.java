package it.unibo.bombardero.view;

import javax.swing.JButton;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.Dimension;
import java.awt.Image;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.Font;

/** 
 * This class represents a JPanel containing a the view of some 
 * playable part of the game (e.g. guide, gameplay, sandbox... etc).
 * @author Federico Bagattoni
 */
public final class GameCard extends GamePlayCard {

    private final static SimpleDateFormat format = new SimpleDateFormat("mm:ss");

    private Image clock;
    private final Font font;

    /* Pause state buttons: */
    private final JButton resumeButton = new JButton("Resume");
    private final JButton quitButton = new JButton("Quit");

    private Dimension imageClockPosition;
    private Dimension timerPosition;
    private long timeLeft;

    public GameCard(final BombarderoGraphics graphics) {
        super(graphics);
        
        clock = graphics.getResizingEngine().getScaledCellImage(graphics.getResourceGetter().loadImage("overlay/clock"));
        font = graphics.getResourceGetter().loadFont("clock_font");

        imageClockPosition = graphics.getResizingEngine().getImageClockPosition();
        timerPosition = graphics.getResizingEngine().getTimerPosition();
        
        this.setFont(font);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;
        g2d.setFont(font);
        g2d.drawString(getFormattedTime(), timerPosition.width, timerPosition.height);
        g2d.drawImage(clock,imageClockPosition.width, imageClockPosition.height, null);
    }

    
    public void setPausedView() {
        // this.add(quitButton, BorderLayout.CENTER);
        // this.add(resumeButton, BorderLayout.CENTER);
        this.repaint(0);
    }

    public void setUnpausedView() {
        this.remove(quitButton);
        this.remove(resumeButton);
        this.repaint(0);
    }

    public void setTimeLeft(final long timeLeft) {
        this.timeLeft = timeLeft;
    }

    private String getFormattedTime() {
        return format.format(new Date(timeLeft));
    }
    
}
