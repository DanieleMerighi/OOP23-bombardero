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
import java.awt.Font;
import javax.swing.ImageIcon;

import java.util.List;
import java.util.Map;
/** 
 * This class represents a JPanel containing a the view of some 
 * playable part of the game (e.g. guide, gameplay, sandbox... etc).
 * @author Federico Bagattoni
 */
public final class GameCard extends GamePlayCard {

    private final static SimpleDateFormat format = new SimpleDateFormat("mm:ss");

    private final Image clockImage;
    private final Image resumeButtonImage;
    private final Image quitButtonImage;
    private Font clockFont;

    /* Pause state buttons: */
    private final JButton resumeButton;
    private final JButton quitButton;

    private Dimension imageClockPosition;
    private Dimension timerPosition;
    private long timeLeft;

    public GameCard(final BombarderoGraphics graphics) {
        super(graphics, Map.of(), List.of(), List.of());
        
        clockImage = graphics.getResizingEngine().getScaledClockImage(
            graphics.getResourceGetter().loadImage("overlay/clock")
        );
        clockFont = graphics.getResourceGetter().loadFont("mono");
        resumeButtonImage = graphics.getResourceGetter().loadImage("overlay/buttons/RESUME");
        quitButtonImage = graphics.getResourceGetter().loadImage("overlay/buttons/QUIT");

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
        this.setLayout(new GridLayout(5, 1));
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
        this.add(resumeButton);
        this.add(quitButton);
        this.revalidate();
        this.repaint(0);
    }

    public void setUnpausedView() {
        this.remove(quitButton);
        this.remove(resumeButton);
        this.revalidate();
        this.repaint(0);
    }

    public void setTimeLeft(final long timeLeft) {
        this.timeLeft = timeLeft;
    }

    private String getFormattedTime() {
        return format.format(new Date(timeLeft));
    }
    
}
