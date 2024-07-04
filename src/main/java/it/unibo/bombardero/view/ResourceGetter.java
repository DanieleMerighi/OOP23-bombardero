package it.unibo.bombardero.view;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import java.awt.FontFormatException;

/**
 * A class that gets the files in the resources/ directory given the name.
 * 
 * @author Federico Bagattoni
 */

public final class ResourceGetter {

    private static final String FONT_PATH = "font/";
    private static final String RESOURCE_PATH = "it/unibo/bombardero/";
    private static final String IMAGE_EXTENSION = ".png";
    private static final String FONT_EXTENSION = ".TTF";

    /**
     * Gets a resource named @name.
     * 
     * @param name the name of the resource to get
     * @return the image loaded, if the loading fails then {@code null} is returned
     */
    public BufferedImage loadImage(final String name) {
        try {
            return ImageIO.read(ClassLoader.getSystemResource(RESOURCE_PATH + name + IMAGE_EXTENSION));
        } catch (final IOException e) {
            System.out.println("There was an exception loading " + name + IMAGE_EXTENSION + " resource.\n");
        }
        return null;
    }

    /**
     * Register the requested font in the local {@code GraphicsEnvironment} and
     * returns it.
     * 
     * @param name
     * @return the requested Font
     */
    public Font loadFont(final String name) {
        try {
            System.out.println(RESOURCE_PATH + FONT_PATH + name + FONT_EXTENSION);
            final InputStream inputStream = ClassLoader.getSystemClassLoader()
                    .getResourceAsStream(RESOURCE_PATH + FONT_PATH + name + FONT_EXTENSION);
            return Font.createFont(Font.TRUETYPE_FONT, inputStream);
        } catch (FontFormatException | IOException e) {
            System.err.println("Exception in loading font " + name);
            System.err.println(e.getMessage());
            return null;
        }
    }
}
