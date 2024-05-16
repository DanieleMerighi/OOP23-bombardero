package it.unibo.bombardero.view;

import javax.swing.JPanel;

import javax.swing.JLabel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.BoxLayout;

import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import java.awt.Graphics2D;

public class MenuCard extends JPanel {

    private final ResourceGetter rg;
    
    public MenuCard(final ResourceGetter rg) {
        this.rg = rg;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.add(new JLabel(new ImageIcon(rg.loadImage("menu/logo"))));
        JButton play = new JButton(new ImageIcon(rg.loadImage("menu/play")));
        JButton guide = new JButton(new ImageIcon(rg.loadImage("menu/guide")));
        play.setBorder(null);
        guide.setBorder(null);

        this.add(play);
        this.add(guide);
    }
}
