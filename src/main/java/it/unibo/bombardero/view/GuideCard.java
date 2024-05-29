package it.unibo.bombardero.view;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import javax.swing.JButton;

import it.unibo.bombardero.core.api.Controller;

/**
 * IDEA PER LA GUIDA:
 * due pulsanti return to menu o start playing
 * sopra un personaggio che guarda a destra o a sinistra con i tast quadrati 
 * che si illuminano di verde quando premuti sotto. Il personaggio alla pressione di ogni tasto cambia verso
 * la bomba con sotto il suo tasto e poi una fila di power up con il loro tasto. IL TUTTO STATICO
 */
public final class GuideCard extends JPanel {

    private final ResourceGetter rg;
    private final BombarderoGraphics graphicsEngine;
    private final Controller controller;

    private final Image background;
    private final Image playerUp;
    private final Image playerDown;
    private final Image playerLeft;
    private final Image playerRight;
    private final Image bomb;

    public GuideCard(final Controller controller, final BombarderoGraphics graphicsEngine, final ResourceGetter rg) {
        this.rg = rg;
        this.controller = controller;
        this.graphicsEngine = graphicsEngine;
        
        // CHECKSTYLE: MagicNumbers OFF
        playerUp = rg.loadImage("character/main/walking/up/up_standing");
        playerDown = rg.loadImage("character/main/walking/down/down_standing");
        playerLeft = rg.loadImage("character/main/walking/left/left_standing");
        playerRight = rg.loadImage("character/main/walking/right/right_standing");
        bomb = rg.loadImage("bomb/bomb3");
        background = rg.loadImage("grass_background");
        // CHECKSTYLE: MagicNumbers ON

        JButton back = new JButton("back");
        JButton start = new JButton("start");

        this.add(back);
        this.add(start);
        
        back.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                graphicsEngine.showCard(BombarderoGraphics.MENU_CARD);
            }
            
        });

        start.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                controller.startGame();
            }
            
        });

    }

    @Override 
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        super.paintComponent(g);
        g2d.drawImage(background, 0, 0, null);
    }
    
}
