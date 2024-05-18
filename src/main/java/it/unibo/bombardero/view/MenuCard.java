package it.unibo.bombardero.view;

import javax.swing.JPanel;

import it.unibo.bombardero.Bombardero;
import it.unibo.bombardero.core.api.Controller;

import javax.swing.JLabel;
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
    
    public MenuCard(final Controller controller, final BombarderoGraphics graphicsEngine, final ResourceGetter rg) {
        this.rg = rg;
        this.controller = controller;
        this.graphicsEngine = graphicsEngine;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(new JLabel(new ImageIcon(rg.loadImage("menu/logo"))));
        play = new JButton(new ImageIcon(rg.loadImage("menu/play")));
        guide = new JButton(new ImageIcon(rg.loadImage("menu/guide")));
        play.setBorder(null);
        guide.setBorder(null);

        this.add(play);
        this.add(guide);

        play.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                controller.startGame();
            }
            
        });
    }
}
