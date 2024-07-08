package it.unibo.bombardero.view;

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import it.unibo.bombardero.core.api.Controller;

/**
 * This class will never be serialised.
 */
@SuppressWarnings("serial")
/**
 * The class containing the main menu of the game.
 */
public final class MenuCard extends JPanel {

    private static final int DISTANCE_FROM_LOGO = 60;
    private static final int DISTANCE_FROM_PLAY = 80;
    /* Resources: */
    private final transient Image background;

    /**
     * Resize the immages thank to the ResizeEngine, set the style and function of
     * button play and guide.
     * 
     * @param controller     to initialize the game when the button play is pressed
     * @param graphicsEngine to get the ResizeEngine
     * @param rg             to take the images from the resouces
     */
    public MenuCard(final Controller controller, final BombarderoGraphics graphicsEngine, final ResourceGetter rg) {
        // CHECKSTYLE: MagicNumber OFF
        final Image logo = graphicsEngine.getResizingEngine()
                .getScaledMenuLogoImage(rg.loadImage("menu/logo"));
        final Image playImage = graphicsEngine.getResizingEngine()
                .getScaledButtonImage(rg.loadImage("overlay/buttons/PLAY"));
        final Image guideImage = graphicsEngine.getResizingEngine()
                .getScaledButtonImage(rg.loadImage("overlay/buttons/GUIDE"));
        final Image playPressedImage = graphicsEngine.getResizingEngine()
                .getScaledButtonImage(rg.loadImage("overlay/buttons/PLAY_PRESSED"));
        final Image guidePressedImage = graphicsEngine.getResizingEngine()
                .getScaledButtonImage(rg.loadImage("overlay/buttons/GUIDE_PRESSED"));
        background = graphicsEngine.getResizingEngine()
                .getSubImageFromBackground(rg.loadImage("menu/background"));
        // CHECKSTYLE: MagicNumber ON

        this.setLayout(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();
        final JButton play = new JButton(new ImageIcon(playImage));
        final JButton guide = new JButton(new ImageIcon(guideImage));
        guide.setBorder(null);
        play.setBorder(null);
        guide.setBorderPainted(false);
        play.setBorderPainted(false);
        play.setContentAreaFilled(false);
        guide.setContentAreaFilled(false);
        play.setFocusPainted(false);
        guide.setFocusPainted(false);
        play.setPressedIcon(new ImageIcon(playPressedImage));
        guide.setPressedIcon(new ImageIcon(guidePressedImage));

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
        gbc.insets = new Insets(DISTANCE_FROM_PLAY, 0, 0, 0);
        this.add(play, gbc);

        // Configurazione del bottone "Guide"
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER; // Utilizza anchor per centrare la componente
        gbc.fill = GridBagConstraints.NONE; // Nessun riempimento, lascia la componente nella sua dimensione preferita
        gbc.insets = new Insets(DISTANCE_FROM_LOGO, 0, 0, 0);
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
        g.drawImage(background, 0, 0, null);
    }
}
