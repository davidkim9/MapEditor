package stormgate.editor.tool.data;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import stormgate.action.entity.EntityPlaceAction;
import stormgate.data.MapEntity;
import stormgate.editor.tool.EntityTool;
import stormgate.geom.MapPoint;
import stormgate.image.LibraryResource;

public class EntityToolPlaceMode extends EntityToolMode {

  private String url;
  private LibraryResource resource;
  private int imgWidth;
  private int imgHeight;
  public MapEntity entity;

  public EntityToolPlaceMode(EntityTool tool) {
    super(tool);
  }

  public void setEntity(MapEntity entity) {
    this.entity = entity;
  }

  private int zoomFilter(float a) {
    return Math.round(a * data.getZoom());
  }

  @Override
  public void paint(Graphics g) {
    if (!data.pan) {
      String url2 = entity.getURL();

      if ((url != null && !url.equals(url2)) || url == null) {
        url = url2;
        if (url != null) {
          resource = data.getManager().getResource(url);
        }
      }
      if (resource != null) {
        imgWidth = zoomFilter(resource.getWidth());
        imgHeight = zoomFilter(resource.getHeight());
        if (data.snap()) {
          MapPoint gridSnap = data.gridWorkspace(mouseX, mouseY);
          resource.drawImage(g, gridSnap.getX(), gridSnap.getY(), imgWidth, imgHeight);
        } else {
          resource.drawImage(g, mouseX, mouseY, imgWidth, imgHeight);
        }
      }
    }
  }

  //Controls
  @Override
  public void mousePressed(MouseEvent e) {
    if (e.getButton() == 1) {

      if (entity != null) {
        int mouseDownX = e.getX();
        int mouseDownY = e.getY();

        //Place graphic
        MapPoint pt;
        if (data.snap()) {
          pt = data.gridMap(mouseX, mouseY);
        } else {
          pt = data.convertToMap(mouseDownX, mouseDownY);
        }

        EntityPlaceAction p = new EntityPlaceAction(data.getMap(), entity, pt);
        data.performAction(p);
        tool.selectMode();
      }
    } else if (e.getButton() == 3) {
      tool.selectMode();
    }
  }
}
