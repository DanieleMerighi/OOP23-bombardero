package it.unibo.bombardero.view;

import javax.swing.JPanel;

import it.unibo.bombardero.core.api.Controller;

public class GuideCard extends JPanel {

    private final ResourceGetter rg;
    private final BombarderoGraphics graphicsEngine;
    private final Controller controller;

    public GuideCard(final Controller controller, final BombarderoGraphics graphicsEngine, final ResourceGetter rg) {
        this.rg = rg;
        this.controller = controller;
        this.graphicsEngine = graphicsEngine;
    }
    /**
     * IDEA PER LA GUIDA:
     * due pulsanti return to menu o start playing
     * sopra un personaggio che guarda a destra o a sinistra con i tast quadrati 
     * che si illuminano di verde quando premuti sotto. Il personaggio alla pressione di ogni tasto cambia verso
     * la bomba con sotto il suo tasto e poi una fila di power up con il loro tasto. IL TUTTO STATICO
     */
    
}
