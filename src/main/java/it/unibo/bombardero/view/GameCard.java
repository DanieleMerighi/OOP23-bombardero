package it.unibo.bombardero.view;

import javax.swing.JButton;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
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

    private final Image clockImage;
    private Font clockFont;

    /* Pause state buttons: */
    private final JButton resumeButton = new JButton("Resume");
    private final JButton quitButton = new JButton("Quit");

    private Dimension imageClockPosition;
    private Dimension timerPosition;
    private long timeLeft;

    public GameCard(final BombarderoGraphics graphics) {
        super(graphics);
        
        clockImage = graphics.getResizingEngine().getScaledClockImage(
            graphics.getResourceGetter().loadImage("overlay/clock")
        );
        clockFont = graphics.getResourceGetter().loadFont("mono");

        imageClockPosition = graphics.getResizingEngine().getImageClockPosition();
        timerPosition = graphics.getResizingEngine().getTimerPosition();

        this.setFont(clockFont);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        int fontYOffset = (int)(g.getFontMetrics(clockFont).getAscent() / 2);
        g2d.setFont(clockFont.deriveFont(Font.PLAIN, 16));
        g2d.drawString(getFormattedTime(), timerPosition.width, timerPosition.height + fontYOffset);
        g2d.drawImage(clockImage, imageClockPosition.width, imageClockPosition.height, null);
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
