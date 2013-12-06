package stormgate.image;

import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.Graphics;
import java.awt.Color;
import stormgate.editor.graphics.ImageManipulation;

public class ImageWrapper {

    private ImageManager p;
    private BufferedImage img;

    protected ImageWrapper(ImageManager parent, String url, BufferedImage image) {
        p = parent;
        img = image;
    }

    /**
     * Destroys this image object
     */
    protected void unload() {
        if (img != null) {
            img.flush();
            //img.finalize();
            img = null;
        }
    }

    protected int getWidth() {
        return img.getWidth();
    }

    protected int getHeight() {
        return img.getHeight();
    }

    protected boolean checkSelect(int x1, int y1, int x2, int y2) {
        return ImageManipulation.containsNoAlpha(img, x1, y1, x2, y2);
    }
    
    protected boolean checkSelectReverse(int x1, int y1, int x2, int y2) {
        return ImageManipulation.containsNoAlphaReverse(img, x1, y1, x2, y2);
    }
    
    /**
     * Draws as much of the image as is currently available.
     */
    protected boolean drawImage(Graphics graphics, int x, int y, Color bgcolor, ImageObserver observer) {

        return graphics.drawImage(img, x, y, bgcolor, observer);
    }

    /**
     * Draws as much of the image as is currently available.
     */
    protected boolean drawImage(Graphics graphics, int x, int y, ImageObserver observer) {
        return graphics.drawImage(img, x, y, observer);
    }

    /**
     * Draws as much of the image as has already been scaled to fit inside the specified rectangle.
     */
    protected boolean drawImage(Graphics graphics, int x, int y, int width, int height, Color bgcolor, ImageObserver observer) {
        return graphics.drawImage(img, x, y, width, height, bgcolor, observer);
    }

    /**
     * Draws as much of the image as has already been scaled to fit inside the specified rectangle.
     */
    protected boolean drawImage(Graphics graphics, int x, int y, int width, int height, ImageObserver observer) {
        return graphics.drawImage(img, x, y, width, height, observer);
    }

    /**
     * Draws as much of the specified area of the image as is currently available, scaling it on the fly to fit inside the specified area of the destination drawable surface.
     */
    protected boolean drawImage(Graphics graphics, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, Color bgcolor, ImageObserver observer) {
        return graphics.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, bgcolor, observer);
    }

    /**
     * Draws as much of the specified area of the image as is currently available, scaling it on the fly to fit inside the specified area of the destination drawable surface.
     */
    protected boolean drawImage(Graphics graphics, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, ImageObserver observer) {
        return graphics.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer);
    }
}