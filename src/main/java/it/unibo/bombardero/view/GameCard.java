package it.unibo.bombardero.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JPanel;

import it.unibo.bombardero.cell.Cell;
import it.unibo.bombardero.cell.Cell.CellType;
import it.unibo.bombardero.core.api.Controller;
import it.unibo.bombardero.utils.Utils;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.map.impl.GameMapImpl;

public class GameCard extends JPanel {

    private final static int MISCHIEVOUS_PADDING = 23;

    private final ResourceGetter resourceGetter = new ResourceGetter();
    private final BufferedImage grass_bg_image = resourceGetter.loadImage("grass_background");
    private final BufferedImage map = resourceGetter.loadImage("map_square");
    private final BufferedImage obstacle = resourceGetter.loadImage("powerup/skull");
    private final JFrame parentFrame;
    private final ResizingEngine resizingEngine;
    private final Map<Pair, Cell> cells;
    private final Controller controller;

    public GameCard(final JFrame parentFrame, final ResizingEngine resizingEngine, final Controller controller) {
        this.parentFrame = parentFrame;
        this.resizingEngine = resizingEngine;
        this.controller = controller;
        this.setMinimumSize(resizingEngine.getMapSize()); // Sets the minimum size so that the JFrame can use it

        /* Test map to see if placement goes well: */ 
        cells = new GameMapImpl(true).getMap();
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        Dimension actualMapSize = resizingEngine.getMapSize();
        Dimension actualBgSize = resizingEngine.getBackgroundImageSize();
        g.drawImage(
            grass_bg_image.getScaledInstance(actualBgSize.width, actualBgSize.height, Image.SCALE_SMOOTH),
            0, 0,
            null);
        g2d.drawImage(
            map.getScaledInstance(actualMapSize.width, actualMapSize.height, Image.SCALE_SMOOTH),
            computeMapPlacingPoint().width,
            computeMapPlacingPoint().height,
            null
        );
        cells.entrySet().stream()
            .filter(entry -> entry.getValue().getType().equals(CellType.WALL_BREAKABLE))
            .forEach(entry -> {
                g2d.drawImage(
                    obstacle.getScaledInstance((int)(resizingEngine.getScaledCellSize()), (int)(resizingEngine.getScaledCellSize()), Image.SCALE_SMOOTH),
                    computeCellPlacingPoint(entry.getKey()).width,
                    computeCellPlacingPoint(entry.getKey()).height,
                    null
                );
            });
    }
    private Dimension computeMapPlacingPoint() {
        return new Dimension(
            parentFrame.getSize().width/2 - resizingEngine.getMapSize().width/2 - (parentFrame.getInsets().right + parentFrame.getInsets().left),
            parentFrame.getSize().height/2 - resizingEngine.getMapSize().height/2 - (parentFrame.getInsets().top + parentFrame.getInsets().bottom)
        );
    }

    private Dimension computeCellPlacingPoint(Pair coordinate) {
        return new Dimension(
            computeMapPlacingPoint().width + (int)(resizingEngine.getScaledCellSize() * coordinate.row()) + resizingEngine.getScaledCellSize() + (int)(resizingEngine.getScale() * MISCHIEVOUS_PADDING),
            computeMapPlacingPoint().height + (int)(resizingEngine.getScaledCellSize() * coordinate.col()) + 2*resizingEngine.getScaledCellSize()
        );
    }
}