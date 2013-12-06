package stormgate.image;

import java.util.HashMap;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageManager {

    private HashMap<String, LibraryResource> source;
    private final GraphicsEnvironment ge;
    private final GraphicsDevice gs;
    private final GraphicsConfiguration gc;
    
    ErrorImage errorImage;

    public ImageManager() {
        source = new HashMap<String, LibraryResource>();

        ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        gs = ge.getDefaultScreenDevice();
        gc = gs.getDefaultConfiguration();
        
        Image img = loadImage("default/error.png");
        if (img != null) {
            //Convert to JPG
            BufferedImage bufferedImage = gc.createCompatibleImage(img.getWidth(null), img.getHeight(null), Transparency.TRANSLUCENT);
            Graphics2D g2 = bufferedImage.createGraphics();
            bufferedImage.flush();
            //bufferedImage.getRGB(x, y);
            g2.drawImage(img, null, null);
            //g2.finalize();
            errorImage = new ErrorImage(this, "", bufferedImage);
            bufferedImage = null;
        }
    }

    /**
     * Returns the image wrapper for the given url
     * @param url Relative URL only
     * @return ImageWrapper
     */
    private ImageWrapper getWrapper(String url) {
        Image img = loadImage(url);
        if (img != null) {
            //Convert to JPG
            //BufferedImage bufferedImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);
            BufferedImage bufferedImage = gc.createCompatibleImage(img.getWidth(null), img.getHeight(null), Transparency.TRANSLUCENT);
            Graphics2D g2 = bufferedImage.createGraphics();
            bufferedImage.flush();
            //bufferedImage.getRGB(x, y);
            g2.drawImage(img, null, null);
            //g2.finalize();
            ImageWrapper iw = new ImageWrapper(this, url, bufferedImage);
            bufferedImage = null;

            return iw;
        }
        System.out.println("cant load " + url);
        return errorImage;
        //return null;
    }

    public LibraryResource getResource(String url) {
        //File filePath = new File(url);

        //url = toRelative(filePath);

        if (source.get(url) == null) {
            //System.out.println("LOAD " + url);
            LibraryResource resource = new LibraryResource(new File(url));

            ImageWrapper iw = null;

            File reference = resource.getReferenceFile();
            if (reference != null && reference.exists()) {
                iw = getWrapper(toRelative(reference));
            } else {
                iw = getWrapper(url);
            }
            resource.setImage(iw);

            return resource;
        }
        return source.get(url);
    }

    public void updateResource(String url) {
        File filePath = new File(url);

        url = toRelative(filePath);

        if (source.get(url) != null) {
            LibraryResource resource = source.get(url);
            resource.parseIni();

            ImageWrapper iw = null;

            File reference = resource.getReferenceFile();
            if (reference != null && reference.exists()) {
                iw = getWrapper(toRelative(reference));
            } else {
                iw = getWrapper(url);
            }
            resource.destroy();
            resource.setImage(iw);
        }
    }

    /**
     * This should only be called from the ResourceTracker
     * @param url
     * @return LibraryResource
     */
    protected LibraryResource getSource(String url) {
        //File filePath = new File(url);

        //url = toRelative(filePath);

        if (source.get(url) == null) {
            File f = new File(url);
            if (f.exists()) {

                LibraryResource resource = new LibraryResource(new File(url));

                ImageWrapper iw = null;

                File reference = resource.getReferenceFile();
                if (reference != null && reference.exists()) {
                    iw = getWrapper(toRelative(reference));
                } else {
                    iw = getWrapper(url);
                }
                resource.setImage(iw);
                source.put(url, resource);
                //System.out.println("ADD SOURCE " + f.getAbsolutePath());
                return resource;
/*
                ImageWrapper iw = getWrapper(url);
                if (iw != null) {
                    //System.out.println("ADD SOURCE " + f.getAbsolutePath());
                    LibraryResource resource = new LibraryResource(f, iw);
                    source.put(url, resource);
                    return resource;
                }
*/

            }
            System.out.println("SOURCE NOT FOUND " + f.getAbsolutePath());
            stormgate.log.Log.addLog("Error: Source not found " + f.getAbsolutePath());
            return new LibraryResource(new File(url), null);
        }
        return source.get(url);
    }

    /**
     * This should only be called from the ResourceTracker
     * @param url
     * @return LibraryResource
     */
    protected void removeSource(String url) {
        //System.out.println("REMOVE SOURCE " + url);
        LibraryResource iw = source.get(url);
        if (iw != null) {
            iw.destroy();
            source.remove(url);
        }
    }

    private Image loadImage(String path) {
        Image img = null;

        try {
            img = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.out.println("Cant open image: " + path);
            stormgate.log.Log.addLog("Error: Cant open image: " + path);
        }

        return img;
    }

    public static String toRelative(File file) {
        return new File(".").toURI().relativize(file.toURI()).getPath();
    }

    public static String toRelative(String url) {
        return new File(".").toURI().relativize(new File(url).toURI()).getPath();
    }

    public static String fixUrl(String url) {
        if (url != null) {
            return toRelative(url);
        }
        return null;
    }
}
