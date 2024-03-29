package it.unibo.bombardero.view;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

/** 
 * A class that gets the files in the resources/ directory given the name
 * @author Federico Bagattoni   
 */

public class ResourceGetter {

    private final static String RESOURCE_PATH = "it/unibo/bombardero/";
    private final static String EXTENSION = ".png";

    /**  
     * Gets a resource named @name
     * @param name the name of the resource to get
    */
    public BufferedImage loadImage(final String name) {
        try {
            return ImageIO.read(ClassLoader.getSystemResource(RESOURCE_PATH + name + EXTENSION));
        }
        catch(IOException e) {
            System.out.println("There was an exception loading " + name + EXTENSION + " resource.\n");
        }
        return null;
    }
}

