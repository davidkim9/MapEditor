package stormgate.editor.ui.forms.graphic.render;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import stormgate.data.MapZone;
import stormgate.filter.Filter;
import stormgate.geom.MapPoint;
import stormgate.image.ResourceTracker;

/**
 *
 * @author David
 */
public class ZoneRenderer implements Renderer
{

	private Filter filter;
	private ArrayList<MapZone> zones;
	private int offsetX;
	private int offsetY;
	private MapPoint camera;

	public Color fillColor;
	public Color borderColor;
	public boolean showPoints = false;

	public ZoneRenderer(){
		fillColor = new Color(0, 180, 220, 100);
		borderColor = new Color(0, 100, 150, 200);
	}

	public void setTracker(ResourceTracker tracker)
	{
	}

	public void setFilter(Filter filter)
	{
		this.filter = filter;
	}

	public void setZones(ArrayList<MapZone> zones)
	{
		this.zones = zones;
	}

	public void setCamera(MapPoint camera)
	{
		this.camera = camera;
	}

	public void setOffset(int offsetX, int offsetY)
	{
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}

	private int zoomFilter(float a)
	{
		return Math.round(a * filter.getZoom());
	}

	public void drawPolygon(Graphics p, MapZone polygon)
	{
		//For text alignment
		int textX = -1;
		int textY = 0;
		
		int camX = camera.getX();
		int camY = camera.getY();

		ArrayList<MapPoint> points = polygon.getPolygon();

		int polygonSize = points.size();

		int[] xPt = new int[polygonSize];
		int[] yPt = new int[polygonSize];

		for (int i = 0; i < polygonSize; i++) {
			MapPoint pt = points.get(i);
			//Convert each point to the appropriate filter/offset

			xPt[i] = zoomFilter(pt.getX() - camX) + offsetX;
			yPt[i] = zoomFilter(pt.getY() - camY) + offsetY;
			if(textX == -1 || xPt[i] < textX){
				textX = xPt[i];
				textY = yPt[i];
			}
		}

		p.setColor(fillColor);
		p.fillPolygon(xPt, yPt, polygonSize);
		p.setColor(borderColor);
		p.drawPolygon(xPt, yPt, polygonSize);

		if(showPoints){
			p.setColor(borderColor);
			int radius = 2;
			int diam = radius*2;
			for (int i = 0; i < polygonSize; i++) {
				p.drawRect(xPt[i] - radius, yPt[i] - radius, diam, diam);
			}
		}
		if(polygon.name != null){
			p.setColor(Color.WHITE);
			p.drawString(polygon.name, textX, textY);
		}
	}

	public void paint(Graphics p)
	{
		for (int i = 0; i < zones.size(); i++) {
			MapZone zone = zones.get(i);
			drawPolygon(p, zone);
		}
	}
}
