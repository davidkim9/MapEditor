package stormgate.image;

import java.awt.image.BufferedImage;

/**
 *
 * @author David
 */
public class ErrorImage extends ImageWrapper {
    
    protected ErrorImage(ImageManager parent, String url, BufferedImage image) {
        super(parent, url, image);
    }
    
    @Override
    protected void unload() {
    }
}
