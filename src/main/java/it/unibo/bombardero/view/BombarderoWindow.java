package it.unibo.bombardero.view;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class BombarderoWindow extends JFrame {

    private final ResourceGetter resourceGetter = new ResourceGetter();
    private final ResizingEngine resizingEngine = new ResizingEngine();
    private final BufferedImage staticMap = resourceGetter.loadImage("map");
    private JPanel deck = new JPanel(new CardLayout());
    private CardLayout layout = (CardLayout)deck.getLayout();
    
    public BombarderoWindow() {
        this.pack();
        this.setVisible(true);
    }
}
