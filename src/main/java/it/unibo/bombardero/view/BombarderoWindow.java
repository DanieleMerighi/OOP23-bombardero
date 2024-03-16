package it.unibo.bombardero.view;

import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class BombarderoWindow extends JFrame {

    private final ResourceGetter resourceGetter = new ResourceGetter();
    private final BufferedImage staticMap = resourceGetter.loadImage("map");
    
    public BombarderoWindow() {
        this.pack();
        this.setVisible(true);
    }
}
