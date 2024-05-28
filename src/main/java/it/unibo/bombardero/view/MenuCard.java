package it.unibo.bombardero.view;

import javax.swing.JPanel;
import javax.swing.OverlayLayout;

import it.unibo.bombardero.Bombardero;
import it.unibo.bombardero.core.api.Controller;
import it.unibo.bombardero.utils.Utils;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;

import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class MenuCard extends JPanel {

    private final ResourceGetter rg;
    private final BombarderoGraphics graphicsEngine;
    private final JButton play;
    private final JButton guide;
    private final Controller controller;

    /* Resources: */
    private final Image logo;
    private final Image playImage;
    private final Image guideImage;
    private final BufferedImage background;

    public MenuCard(final Controller controller, final BombarderoGraphics graphicsEngine, final ResourceGetter rg) {
        this.rg = rg;
        this.controller = controller;
        this.graphicsEngine = graphicsEngine;

        // CHECKSTYLE: MagicNumber OFF
        logo = rg.loadImage("menu/logo");
        playImage = rg.loadImage("menu/play");
        guideImage = rg.loadImage("menu/guide");
        background = rg.loadImage("menu/background");
        // CHECKSTYLE: MagicNumber ON

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        play = new JButton(new ImageIcon(playImage));
        guide = new JButton(new ImageIcon(guideImage));
        /* TODO: better style JButtons, in windows you can stil see the buttons highlight, in MacOS however no...  */
        play.setBorder(null);
        guide.setBorder(null);

        /* TODO: smaller logo and dynamic resizing menu (dependant on JFrame size) */
        this.add(new JLabel(new ImageIcon(logo)));
        this.add(play);
        this.add(guide);

        play.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                controller.startGame();
            }
            
        });

        guide.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Guide pressed");
            }
            
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        Dimension cropSize = graphicsEngine.getFrameSize();
        /* TODO: original background image is 4k, the image is to be scaled and the parameteres modified */
        Image img = background.getSubimage(3840 - cropSize.width, 2160 - cropSize.height, cropSize.width, cropSize.height);
        g2d.drawImage(img, 0, 0, null);
    }
}
