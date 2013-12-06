/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stormgate.image;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.File;
import stormgate.io.ini.IniFile;

/**
 *
 * @author David
 */
public class LibraryResource {

    private File file;
    private ImageWrapper image;
    private File iniFile;
    private boolean iniExists;
    //ini info as well
    //START INFO
    private int originX = 0;
    private int originY = 0;
    private boolean depth = false;
    private int depthPoint1X = 0;
    private int depthPoint1Y = 0;
    private int depthPoint2X = 0;
    private int depthPoint2Y = 0;
    private String reference;

    public LibraryResource(File f1) {
        file = f1;
        parseIni();
    }

    public LibraryResource(File f1, ImageWrapper img) {
        file = f1;
        image = img;

        parseIni();
    }

    public void setReference(String ref) {
        reference = ref;
    }

    public void setOrigin(int x, int y) {
        originX = x;
        originY = y;
    }

    public void setDepth(boolean depth) {
        this.depth = depth;
    }

    public void setDepthPointA(int x, int y) {
        depthPoint1X = x;
        depthPoint1Y = y;
    }

    public void setDepthPointB(int x, int y) {
        depthPoint2X = x;
        depthPoint2Y = y;
    }

    public int getOriginX() {
        return originX;
    }

    public int getOriginY() {
        return originY;
    }

    public int getDepthAX() {
        return depthPoint1X;
    }

    public int getDepthAY() {
        return depthPoint1Y;
    }

    public int getDepthBX() {
        return depthPoint2X;
    }

    public int getDepthBY() {
        return depthPoint2Y;
    }

    public String getReference() {
        return reference;
    }

    public void setImage(ImageWrapper img) {
        image = img;
    }

    public File getFile() {
        return file;
    }

    public String getFileName() {
        return file.getName();
    }

    public String getPath() {
        String relativePath = ImageManager.toRelative(file);
        File relativeFile = new File(relativePath);
        File parentPath = relativeFile.getParentFile();
        if (parentPath != null) {
            return parentPath.toString() + "\\";
        }
        return "";
    }

    public void setIniFile() {
        String filename = file.getName();
        //int dotIndex = filename.lastIndexOf('.');
        //System.out.println(filename.substring(0, dotIndex) + " DOT " + filename.substring(dotIndex));
        String iniFilename = filename + ".ini";

        iniFile = new File(getPath() + iniFilename);

        iniExists = iniFile.exists();
    }

    public File getReferenceFile() {
        return new File(getPath() + reference);
    }

    public boolean iniExists() {
        return iniExists;
    }

    public void parseIni() {
        setIniFile();

        if (iniExists) {

            IniFile iniReader = new IniFile();
            iniReader.readFile(iniFile);
            originX = iniReader.getInt("originX");
            originY = iniReader.getInt("originY");

            depth = iniReader.getBoolean("depth");

            depthPoint1X = iniReader.getInt("depthPoint1X");
            depthPoint1Y = iniReader.getInt("depthPoint1Y");
            depthPoint2X = iniReader.getInt("depthPoint2X");
            depthPoint2Y = iniReader.getInt("depthPoint2Y");

            reference = iniReader.getValue("reference");

        }
    }

    public void saveIni() {
        setIniFile();

        //SAVE INI
        IniFile newIni = new IniFile();
        newIni.write("originX", originX);
        newIni.write("originY", originY);
        newIni.write("depth", depth);
        newIni.write("depthPoint1X", depthPoint1X);
        newIni.write("depthPoint1Y", depthPoint1Y);
        newIni.write("depthPoint2X", depthPoint2X);
        newIni.write("depthPoint2Y", depthPoint2Y);
        if (reference != null) {
            newIni.write("reference", reference);
        }
        newIni.saveFile(iniFile);

    }

    public int getWidth() {
        if (image == null) {
            return 0;
        }

        return image.getWidth();
    }

    public int getHeight() {
        if (image == null) {
            return 0;
        }

        return image.getHeight();
    }

    public Rectangle getBoundary() {
        return new Rectangle(-originX, -originY, getWidth(), getHeight());
    }

    public boolean checkSelect(int x1, int y1, int x2, int y2) {
        if (image == null) {
            return false;
        }
        return image.checkSelect(x1 + originX, y1 + originY, x2 + originX, y2 + originY);
    }
    
    public boolean checkSelectReverse(int x1, int y1, int x2, int y2) {
        if (image == null) {
            return false;
        }
        
        return image.checkSelectReverse(x1 - originX, y1 + originY, x2 - originX, y2 + originY);
    }
    
    public void drawImage(Graphics p, int x, int y, int scaledWidth, int scaledHeight) {
        if (image == null) {
            return;
        }

        int scaledOriginX = (int) (originX * ((scaledWidth * 1.0) / getWidth()));
        int scaledOriginY = (int) (originY * ((scaledHeight * 1.0) / getHeight()));

        image.drawImage(p, x - scaledOriginX, y - scaledOriginY, scaledWidth, scaledHeight, null);
    }

    public void drawImageNoOffset(Graphics p, int x, int y, int scaledWidth, int scaledHeight) {
        if (image == null) {
            return;
        }

        image.drawImage(p, x, y, scaledWidth, scaledHeight, null);
    }

    public boolean getDepth() {
        return depth;
    }

    public void destroy() {
        if (image != null) {
            image.unload();
        }
    }
}
