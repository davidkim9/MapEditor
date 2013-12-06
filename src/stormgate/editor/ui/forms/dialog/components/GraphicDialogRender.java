package stormgate.editor.ui.forms.dialog.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JComponent;
import stormgate.editor.ui.forms.dialog.GraphicDialog;
import stormgate.image.LibraryResource;

/**
 *
 * @author David Kim
 */
public class GraphicDialogRender extends JComponent {

    private LibraryResource resource;
    private int imgw;
    private int imgh;
    private int originX = 0;
    private int originY = 0;
    private boolean depth = false;
    private int depthPoint1X = 0;
    private int depthPoint1Y = 0;
    private int depthPoint2X = 0;
    private int depthPoint2Y = 0;
    private float zoom = -1;
    private GraphicDialogRenderAction actionListener;

    public GraphicDialogRender() {
        super();
        actionListener = new GraphicDialogRenderAction();
        addMouseListener(actionListener);
        addMouseMotionListener(actionListener);
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

    public void setType(int type) {
        actionListener.setType(type);
    }

    public void setResource(LibraryResource resource) {
        this.resource = resource;
    }

    public void setZoom(float amount) {
        zoom = amount;
    }

    private void defineSize() {
        if (resource != null) {
            imgw = resource.getWidth();
            imgh = resource.getHeight();

            if (zoom <= 0) {
                if (imgw > imgh) {
                    //Landscape
                    if (imgw > getSize().width) {
                        //Constrain
                        imgh = (int) ((getSize().width / ((double) imgw)) * imgh);
                        imgw = getSize().width;
                    }
                    if (imgh > getSize().height) {
                        //Constrain
                        imgw = (int) ((getSize().height / ((double) imgh)) * imgw);
                        imgh = getSize().height;
                    }
                } else {
                    //Portrait
                    if (imgh > getSize().height) {
                        //Constrain
                        imgw = (int) ((getSize().height / ((double) imgh)) * imgw);
                        imgh = getSize().height;
                    }
                    if (imgw > getSize().width) {
                        //Constrain
                        imgh = (int) ((getSize().width / ((double) imgw)) * imgh);
                        imgw = getSize().width;
                    }
                }
                this.setPreferredSize(new Dimension(0, 0));
                revalidate();
            } else {
                imgw *= zoom;
                imgh *= zoom;
                this.setPreferredSize(new Dimension(imgw, imgh));
                revalidate();
            }
        } else {
            imgw = 0;
            imgh = 0;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        defineSize();

        Graphics2D p = (Graphics2D) g;
        p.clearRect(0, 0, getWidth(), getHeight());

        p.setColor(Color.WHITE);
        p.fillRect(0, 0, getWidth(), getHeight());

        Color c1 = new Color(240, 240, 240);
        int squareDivideWidth = getWidth() / 10 + 1;
        int squareDivideHeight = getHeight() / 10 + 1;
        p.setColor(c1);

        int tileResize = 10;

        for (int i = 0; i < squareDivideHeight; i++) {
            for (int j = i % 2; j < squareDivideWidth; j += 2) {
                p.fillRect(j * tileResize, i * tileResize, tileResize, tileResize);
            }
        }
        //p.setColor(new Color(50, 50, 50));
        //p.fillRect(0, 0, getWidth(), getHeight());
        int middleX = getWidth() / 2 - imgw / 2;
        int middleY = getHeight() / 2 - imgh / 2;
        double frameRatioX = 1;
        double frameRatioY = 1;

        if (resource != null) {
            resource.drawImageNoOffset(p, middleX, middleY, imgw, imgh);
            frameRatioX = ((double) imgw) / resource.getWidth();
            frameRatioY = ((double) imgh) / resource.getHeight();
        }

        actionListener.setDimensions(middleX, middleY, frameRatioX, frameRatioY);

        if (depth) {
            p.setColor(new Color(255, 00, 00));
            p.drawLine((int) (middleX + depthPoint1X * frameRatioX), (int) (middleY + depthPoint1Y * frameRatioY), (int) (middleX + depthPoint2X * frameRatioX), (int) (middleY + depthPoint2Y * frameRatioY));
            p.fillOval((int) (middleX + depthPoint1X * frameRatioX - 5), (int) (middleY + depthPoint1Y * frameRatioY - 5), 10, 10);
            p.fillOval((int) (middleX + depthPoint2X * frameRatioX - 5), (int) (middleY + depthPoint2Y * frameRatioY - 5), 10, 10);
        }

        p.setColor(new Color(0, 0, 255));
        p.fillOval((int) (middleX + originX * frameRatioX - 5), (int) (middleY + originY * frameRatioY - 5), 10, 10);
    }

    public void setGraphicDialog(GraphicDialog dialog) {
        actionListener.setGraphicDialog(dialog);
    }
}

class GraphicDialogRenderAction implements MouseListener, MouseMotionListener {

    private int type = 0;
    private int middleX = 0;
    private int middleY = 0;
    private double xRatio = 0;
    private double yRatio = 0;
    private GraphicDialog graphicDialog;

    public GraphicDialogRenderAction() {
    }

    public void setGraphicDialog(GraphicDialog dialog) {
        graphicDialog = dialog;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setDimensions(int x, int y, double xRatio, double yRatio) {
        this.middleX = x;
        this.middleY = y;
        this.xRatio = xRatio;
        this.yRatio = yRatio;
    }

    private void update(int x, int y) {

        //Convert points to relative

        x = (int) ((x - middleX) / xRatio);
        y = (int) ((y - middleY) / yRatio);

        if (type == 1) {
            //Get Point And Set Origin
            graphicDialog.setOrigin(x, y);
        } else if (type == 2) {
            //Get Depth A
            graphicDialog.setDepthPointA(x, y);
        } else if (type == 3) {
            //Get Depth B
            graphicDialog.setDepthPointB(x, y);
        }

        graphicDialog.updateGraphic();
    }

    public void mouseClicked(MouseEvent e) {
        update(e.getX(), e.getY());
    }

    public void mouseDragged(MouseEvent e) {
        update(e.getX(), e.getY());
    }

    public void mouseReleased(MouseEvent e) {
        update(e.getX(), e.getY());
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }
}