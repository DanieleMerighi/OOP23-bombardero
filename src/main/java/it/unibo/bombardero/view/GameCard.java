package it.unibo.bombardero.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import it.unibo.bombardero.utils.Utils;

public class GameCard extends JPanel {

    private final ResourceGetter resourceGetter = new ResourceGetter();
    private final BufferedImage grass_bg_image = resourceGetter.loadImage("grass_background");
    private final BufferedImage map = resourceGetter.loadImage("map_square");
    private final JFrame parentFrame;
    private final ResizingEngine resizingEngine;

    public GameCard(final JFrame parentFrame, final ResizingEngine resizingEngine) {
        this.parentFrame = parentFrame;
        this.resizingEngine = resizingEngine;
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        g.drawImage(grass_bg_image, 0, 0, null);
        g.drawImage(map, 0, 0, null);
    }

    private Dimension computeMapSize() {
        return new Dimension(
            (int)(Utils.MAP_WIDTH * resizingEngine.getScale()),
            (int)(Utils.MAP_HEIGHT * resizingEngine.getScale())
        );
    }
}