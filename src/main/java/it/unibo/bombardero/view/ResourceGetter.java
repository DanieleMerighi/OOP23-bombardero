package it.unibo.bombardero.view;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.awt.GraphicsEnvironment;
import javax.imageio.ImageIO;
import java.awt.FontFormatException;

/** 
 * A class that gets the files in the resources/ directory given the name
 * @author Federico Bagattoni   
 */

public final class ResourceGetter { 

    private final static String FONT_PATH = "font/";
    private final static String RESOURCE_PATH = "it/unibo/bombardero/";
    private final static String IMAGE_EXTENSION = ".png";
    private final static String FONT_EXTENSION = ".TFF";

    /**  
     * Gets a resource named @name
     * @param name the name of the resource to get
    */
    public BufferedImage loadImage(final String name) {
        try {
            return ImageIO.read(ClassLoader.getSystemResource(RESOURCE_PATH + name + IMAGE_EXTENSION));
        }
        catch(IOException e) {
            System.out.println("There was an exception loading " + name + IMAGE_EXTENSION + " resource.\n");
        }
        return null;
    }

    /**
     * Register the requested font in the local {@code GraphicsEnvironment} and
     * returns it.
     * @param name
     * @return the requested Font
     */
    public Font loadFont(final String name) {
        try {
            InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream(RESOURCE_PATH + FONT_PATH + name + FONT_EXTENSION);
            Font font = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            return font;
        } catch (FontFormatException | IOException e) {
            System.err.println("Exception in loading font " + name);
            System.err.println(e.getMessage());
            return null;
        }
    }
}

