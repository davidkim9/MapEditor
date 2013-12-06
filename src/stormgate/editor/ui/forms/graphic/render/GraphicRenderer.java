/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stormgate.editor.ui.forms.graphic.render;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import stormgate.data.MapEntity;
import stormgate.image.LibraryResource;
import stormgate.data.MapGraphic;
import stormgate.editor.ui.forms.graphic.Level;
import stormgate.filter.Filter;
import stormgate.geom.MapPoint;
import stormgate.image.ResourceTracker;

/**
 *
 * @author David Kim
 */
public class GraphicRenderer implements Renderer
{

	private Level level;
	private Filter filter;
	private int offsetX;
	private int offsetY;
	private MapPoint camera;
	private ResourceTracker tracker;

	public void setImage(Level level, MapPoint camera)
	{
		this.level = level;
		this.camera = camera;
	}

	public void setFilter(Filter filter)
	{
		this.filter = filter;
	}

	private int zoomFilter(float a)
	{
		return Math.round(a * filter.getZoom());
	}

	public void setTracker(ResourceTracker tracker)
	{
		this.tracker = tracker;
	}

	public void paint(Graphics p)
	{
		int camX = camera.getX();
		int camY = camera.getY();

		ArrayList<MapGraphic> graphics = level.getGraphics();

		ArrayList<DepthInfo> depthInfo = null;

		if (filter.showDepth) {
			depthInfo = new ArrayList<DepthInfo>();
		}

		for (int i = 0; i < graphics.size(); i++) {
			MapGraphic graphic = graphics.get(i);
			if (!graphic.hide || filter.showHidden) {
				if (filter.entities || !(graphic instanceof MapEntity) || filter.entityTool) {
					LibraryResource resource = tracker.getResource(graphic.getURL());
					if (resource != null) {
						MapPoint point = graphic.getPoint();

						int x = zoomFilter(point.getX() - camX) + offsetX;
						int y = zoomFilter(point.getY() - camY) + offsetY;

						int scaledWidth = zoomFilter(resource.getWidth());
						int scaledHeight = zoomFilter(resource.getHeight());

						if (graphic.reverse) {
							scaledWidth *= -1;
						}

						resource.drawImage(p, x, y, scaledWidth, scaledHeight);

						if (graphic instanceof MapEntity) {
							//Show Entity Data
							MapEntity entity = (MapEntity) graphic;
							if (entity.getType() == MapEntity.SOUND) {
								p.setColor(Color.WHITE);
								int radius = zoomFilter(entity.radius);
								p.drawOval(x - radius, y - radius, radius * 2, radius * 2);
								p.setColor(new Color(0, 255, 0, 20));
								p.fillOval(x - radius, y - radius, radius * 2, radius * 2);
							}
							if (entity.getType() == MapEntity.BEACON) {
								p.setColor(Color.WHITE);
								p.drawString("Port to: " + entity.map + " " + entity.tileX + "," + entity.tileY + ": " + entity.x + "," + entity.y, x, y);
							}
							if (entity.getType() == MapEntity.UNIT) {
								p.setColor(Color.WHITE);
								p.drawString((entity.unitType ? "Mob" : "NPC") + " ID: " + entity.id, x, y);
							}
						}

						if (filter.showDepth && resource.getDepth()) {
							DepthInfo di = new DepthInfo(point, resource);
							depthInfo.add(di);
						}
					}
				}
			}
		}

		if (filter.showDepth) {

			for (int i = 0; i < depthInfo.size(); i++) {
				DepthInfo a = depthInfo.get(i);
				for (int j = i; j < depthInfo.size(); j++) {
					DepthInfo b = depthInfo.get(j);
					if (a != b && a.checkCollision(b)) {

						MapPoint aPoint = a.getPoint();
						LibraryResource aResource = a.getResource();
						int x1 = aPoint.getX() - aResource.getOriginX();
						int y1 = aPoint.getY() - aResource.getOriginY();
						int aX = aResource.getDepthAX() + x1;
						int aY = aResource.getDepthAY() + y1;
						int aW = aResource.getDepthBX() + x1;
						int aH = aResource.getDepthBY() + y1;

						int temp;
						if (aX > aW) {
							temp = aX;
							aX = aW;
							aW = temp;
						}

						if (aY > aH) {
							temp = aY;
							aY = aH;
							aH = temp;
						}

						aW = zoomFilter(aW - aX);
						aH = zoomFilter(aH - aY);
						aX = zoomFilter(aX - camX) + offsetX;
						aY = zoomFilter(aY - camY) + offsetY;

						p.setColor(new Color(255, 0, 0, 120));
						p.fillRect(aX, aY, aW, aH);
						p.setColor(new Color(255, 0, 0, 255));
						p.drawRect(aX, aY, aW, aH);

						MapPoint bPoint = b.getPoint();
						LibraryResource bResource = b.getResource();

						int x2 = bPoint.getX() - bResource.getOriginX();
						int y2 = bPoint.getY() - bResource.getOriginY();
						int bX = bResource.getDepthAX() + x2;
						int bY = bResource.getDepthAY() + y2;
						int bW = bResource.getDepthBX() + x2;
						int bH = bResource.getDepthBY() + y2;

						if (bX > bW) {
							temp = bX;
							bX = bW;
							bW = temp;
						}

						if (bY > bH) {
							temp = bY;
							bY = bH;
							bH = temp;
						}

						bW = zoomFilter(bW - bX);
						bH = zoomFilter(bH - bY);
						bX = zoomFilter(bX - camX) + offsetX;
						bY = zoomFilter(bY - camY) + offsetY;

						p.setColor(new Color(255, 0, 0, 120));
						p.fillRect(bX, bY, bW, bH);
						p.setColor(new Color(255, 0, 0, 255));
						p.drawRect(bX, bY, bW, bH);
					}
				}
			}
		}
	}

	public void setOffset(int offsetX, int offsetY)
	{
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}

	class DepthInfo
	{

		private MapPoint point;
		private LibraryResource resource;

		public DepthInfo(MapPoint point, LibraryResource resource)
		{
			this.point = point;
			this.resource = resource;
		}

		public boolean checkCollision(DepthInfo compare)
		{
			int x1 = point.getX() - resource.getOriginX();
			int y1 = point.getY() - resource.getOriginY();
			int aX1 = resource.getDepthAX() + x1;
			int aY1 = resource.getDepthAY() + y1;
			int aX2 = resource.getDepthBX() + x1;
			int aY2 = resource.getDepthBY() + y1;

			int temp;
			if (aX1 > aX2) {
				temp = aX1;
				aX1 = aX2;
				aX2 = temp;
			}

			if (aY1 > aY2) {
				temp = aY1;
				aY1 = aY2;
				aY2 = temp;
			}

			int x2 = compare.point.getX() - compare.resource.getOriginX();
			int y2 = compare.point.getY() - compare.resource.getOriginY();
			int bX1 = compare.resource.getDepthAX() + x2;
			int bY1 = compare.resource.getDepthAY() + y2;
			int bX2 = compare.resource.getDepthBX() + x2;
			int bY2 = compare.resource.getDepthBY() + y2;

			if (bX1 > bX2) {
				temp = bX1;
				bX1 = bX2;
				bX2 = temp;
			}

			if (bY1 > bY2) {
				temp = bY1;
				bY1 = bY2;
				bY2 = temp;
			}
			
			if (aX1 > bX2) {
				return false;
			}

			if (aX2 < bX1) {
				return false;
			}

			if (aY1 > bY2) {
				return false;
			}

			if (aY2 < bY1) {
				return false;
			}

			return true;
		}

		public MapPoint getPoint()
		{
			return point;
		}

		public LibraryResource getResource()
		{
			return resource;
		}
	}
}
