package it.unibo.bombardero.view;

import javax.swing.JPanel;
import it.unibo.bombardero.core.api.Controller;
import javax.swing.JLabel;

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

public final class MenuCard extends JPanel {

    private final transient BombarderoGraphics graphicsEngine;
    private final transient JButton play;
    private final transient JButton guide;

    /* Resources: */
    private final transient Image logo;
    private final transient Image playImage;
    private final transient Image guideImage;
    private final transient BufferedImage background;

    public MenuCard(final Controller controller, final BombarderoGraphics graphicsEngine, final ResourceGetter rg) {
        this.graphicsEngine = graphicsEngine;

        // CHECKSTYLE: MagicNumber OFF
        logo = rg.loadImage("menu/logo");
        playImage = graphicsEngine.getResizingEngine().getScaledButtonImage(rg.loadImage("overlay/buttons/PLAY"));
        guideImage = graphicsEngine.getResizingEngine().getScaledButtonImage(rg.loadImage("overlay/buttons/GUIDE"));
        background = rg.loadImage("menu/background");
        // CHECKSTYLE: MagicNumber ON

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        play = new JButton(new ImageIcon(playImage));
        guide = new JButton(new ImageIcon(guideImage));
        /* TODO: better style JButtons, in windows you can stil see the buttons highlight, in MacOS however no...  */
        guide.setBorder(null);
        play.setBorder(null);
        guide.setBorderPainted(false);
        play.setBorderPainted(false);
        play.setContentAreaFilled(false);
        guide.setContentAreaFilled(false);
        play.setFocusPainted(false);
        guide.setFocusPainted(false);

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
                controller.startGuide();
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
