package it.unibo.bombardero.view;

import javax.swing.JFrame;

import it.unibo.bombardero.utils.Utils;

import java.awt.Dimension;
import java.awt.Toolkit;

/** 
 * A class to compute the scale of the game relative to the window size and display size
 */
public class ResizingEngine {

    private double currentScale = 1.125; /* default scale size for every device */
    private final int SCALED_CELL_SIZE;
    private final static double initialMenuScale = 0.75;
    private static final double minimumScale = 0.25; 
    private static final double maximumScale = 1.75; 
    private Dimension minimumFrameSize;

    public ResizingEngine() {
        int resolution = Toolkit.getDefaultToolkit().getScreenResolution();
        if(resolution >= 200) {              
            currentScale = 1.25; 
        }
        SCALED_CELL_SIZE = (int)(currentScale * Utils.CELL_SIZE);
    }  

    /* Checks if the minimunFrameSize is satisfied */
    public Dimension getNewWindowSize(JFrame frame) {
        Dimension frameSize = frame.getSize();
        if(frameSize.height < minimumFrameSize.height || frameSize.width < minimumFrameSize.width) {
            return minimumFrameSize;
        }
        return frame.getSize();
    }
    
    public Dimension getInitialMenuSize() {
        return new Dimension(
            (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() * initialMenuScale), 
            (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight() * initialMenuScale)
        );
    }
    
    /* The initial windows size is calculated scaling the map and adding some grass on the sides, other than the insets */
    public Dimension getInitialWindowSize(JFrame frame) {       
        minimumFrameSize = new Dimension(
            frame.getInsets().left + frame.getInsets().right + (int)(Utils.MAP_WIDTH * currentScale), 
            frame.getInsets().top + frame.getInsets().bottom + (int)(Utils.MAP_HEIGHT * currentScale)
        ); 
        return new Dimension(
            frame.getInsets().left + frame.getInsets().right + (int)(Utils.MAP_WIDTH * currentScale + Utils.GRASS_PADDING_RATIO * Utils.MAP_WIDTH), 
            frame.getInsets().top + frame.getInsets().bottom + (int)(Utils.MAP_HEIGHT * currentScale + Utils.GRASS_PADDING_RATIO * Utils.MAP_HEIGHT)
        );
    }

    public Dimension getMapSize() {
        return new Dimension(
            (int)(Utils.MAP_WIDTH * currentScale),
            (int)(Utils.MAP_HEIGHT * currentScale)
        );
    }
    
    public Dimension getBackgroundImageSize() {
        return new Dimension(
            (int)(Utils.BG_WIDTH * currentScale),
            (int)(Utils.BG_HEIGHT * currentScale)
        );
    }

    public double getScale() {
        return currentScale;
    }   

    public int getScaledCellSize() {
        return SCALED_CELL_SIZE;
    }
    
    public boolean hasScaleChanged() {
        return false;
    }
    
}
