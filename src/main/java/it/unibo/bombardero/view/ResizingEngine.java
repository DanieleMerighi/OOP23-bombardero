package it.unibo.bombardero.view;

import javax.swing.JFrame;
import java.awt.Dimension;

/*
 * Qui deve essere fatto il coso per calcolare la dimensione della finestra inset inclusi
 * e per calcolare la scale a cui sarà scalato tutto 
 * Dopo una certa dimensione della finestra, l'immagine rimarrà costante e ci aggiungeremo qualcosa di sfondo 
 * giusto per non lasciarla vuota 
 */

/** 
 * 
 */
public class ResizingEngine {

    public 

    /**
     * Computes the size that the window has to be in order to display correctly the panel, 
     * plus the size of the frame's insets
     * @param panelSize the size of the panel that the @frame has to contain
     * @param frame the frame, from which to get the insets, assuming the insets have been already generated
     * @return the total frame size accounting the panel and the insets in every direction
     */
    public Dimension computeTotalWindowSize(Dimension panelSize, JFrame frame) {
        return new Dimension(
            frame.getInsets().left + frame.getInsets().right + (int)panelSize.getWidth(), 
            frame.getInsets().top + frame.getInsets().bottom + (int)panelSize.getHeight()
            );
    }
    
}
