package it.unibo.bombardero.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import it.unibo.bombardero.core.api.Controller;

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
        logo = graphicsEngine.getResizingEngine().getScaledMenuLogoImage(rg.loadImage("menu/logo"));
        playImage = graphicsEngine.getResizingEngine().getScaledButtonImage(rg.loadImage("overlay/buttons/PLAY"));
        guideImage = graphicsEngine.getResizingEngine().getScaledButtonImage(rg.loadImage("overlay/buttons/GUIDE"));
        background = rg.loadImage("menu/background");
        // CHECKSTYLE: MagicNumber ON

        this.setLayout(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();
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
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER; // Utilizza anchor per centrare la componente
        gbc.fill = GridBagConstraints.NONE; // Nessun riempimento, lascia la componente nella sua dimensione preferita
        this.add(new JLabel(new ImageIcon(logo)), gbc);

        // Configurazione del bottone "Play"
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER; // Utilizza anchor per centrare la componente
        gbc.fill = GridBagConstraints.NONE; // Nessun riempimento, lascia la componente nella sua dimensione preferita
        gbc.insets = new Insets(80, 0, 0, 0);
        this.add(play, gbc);

        // Configurazione del bottone "Guide"
        gbc.gridx = 0;
        gbc.gridy = 2; // Cambiato da 2 a 3 per evitare sovrapposizione con "Play"
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER; // Utilizza anchor per centrare la componente
        gbc.fill = GridBagConstraints.NONE; // Nessun riempimento, lascia la componente nella sua dimensione preferita
        gbc.insets = new Insets(60, 0, 0, 0);
        this.add(guide, gbc);

        play.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                controller.startGame();
            }
            
        });

        guide.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                controller.startGuide();
            }
            
        });
    }

    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2d = (Graphics2D)g;
        final Dimension cropSize = graphicsEngine.getFrameSize();
        /* TODO: original background image is 4k, the image is to be scaled and the parameteres modified */
        final Image img = background.getSubimage(3840 - cropSize.width, 2160 - cropSize.height, cropSize.width, cropSize.height);
        g2d.drawImage(img, 0, 0, null);
    }
}
