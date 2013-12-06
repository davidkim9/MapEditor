package stormgate.editor.tool.data;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import stormgate.action.zone.ZoneAddAction;
import stormgate.action.zone.ZoneNewAddAction;
import stormgate.action.zone.ZoneNewClearAction;
import stormgate.action.zone.ZoneNewRemoveAction;
import stormgate.data.MapZone;
import stormgate.editor.tool.ZoneTool;
import stormgate.editor.ui.forms.graphic.render.ZoneRenderer;
import stormgate.filter.Filter;
import stormgate.geom.MapPoint;

/**
 *
 * @author David
 */
public class ZoneToolNewMode extends ZoneToolMode
{

	private final int clickDistance = 5;
	private MapZone zone;
	private ZoneRenderer zoneRender;
	private ArrayList<MapZone> paintZone;

	public ZoneToolNewMode(ZoneTool tool)
	{
		super(tool);
		zoneRender = new ZoneRenderer();
		zoneRender.showPoints = true;
		//zoneRender.fillColor = new Color(250, 150, 100, 100);
		//zoneRender.borderColor = new Color(180, 50, 50, 200);
		paintZone = new ArrayList<MapZone>();
	}

	private int zoomFilter(float a)
	{
		Filter filter = data.getRenderData().getFilter();
		return Math.round(a * filter.getZoom());
	}

	@Override
	public void paint(Graphics g)
	{
		Filter filter = data.getRenderData().getFilter();
		zoneRender.setFilter(filter);

		int offsetX = data.getScreenWidth() / 2;
		int offsetY = data.getScreenHeight() / 2;

		MapPoint camera = data.getCameraLocation();

		zoneRender.setOffset(offsetX, offsetY);
		zoneRender.setCamera(camera);
		zoneRender.setZones(paintZone);
		zoneRender.paint(g);

		//Cursor point
		int camX = camera.getX();
		int camY = camera.getY();
		g.setColor(new Color(0, 100, 150, 200));
		int radius = 2;
		int diam = radius * 2;

		MapPoint mouseLocation = data.convertToMap(mouseX, mouseY);

		if (data.snap()) {
			//Weird type of snap that I think could work out nicely
			mouseLocation = data.getClosestGrid(mouseLocation);
		}

		int xPt = zoomFilter(mouseLocation.getX() - camX) + offsetX;
		int yPt = zoomFilter(mouseLocation.getY() - camY) + offsetY;

		g.drawRect(xPt - radius, yPt - radius, diam, diam);

	}

	public double magnitude(MapPoint pointA, MapPoint pointB)
	{
		int a = pointA.getX() - pointB.getX();
		int b = pointA.getY() - pointB.getY();
		return Math.sqrt(a * a + b * b);
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		int mX = e.getX();
		int mY = e.getY();

		if (zone == null) {
			while (paintZone.size() > 0) {
				paintZone.remove(0);
			}
			zone = new MapZone();
			paintZone.add(zone);
		} else {
			MapPoint mouseScreen = new MapPoint(mX, mY);

			//Check for delete click
			ArrayList<MapPoint> polygon = zone.getPolygon();
			for (int i = 0; i < polygon.size(); i++) {
				MapPoint point = polygon.get(i);

				MapPoint screenPoint = data.convertToWorkspace(point);
				//Check for click distance
				if (magnitude(mouseScreen, screenPoint) < clickDistance) {
					//Delete this point
					//zone.removePoint(point);
					ZoneNewRemoveAction a = new ZoneNewRemoveAction(this, zone, point);
					data.performAction(a);
					//data.refresh();
					updateCursor();
					return;
				}
			}
		}
		//If nothing has been deleted the code will reach this
		MapPoint mapLocation = data.convertToMap(mX, mY);

		if (data.snap()) {
			//Weird type of snap that I think could work out nicely
			mapLocation = data.getClosestGrid(mapLocation);
		}

		//Add point
		//zone.addPoint(mapLocation);
		ZoneNewAddAction a = new ZoneNewAddAction(this, zone, mapLocation);
		data.performAction(a);

		updateCursor();
		//data.refresh();
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		mouseX = e.getX();
		mouseY = e.getY();
		updateCursor();
	}

	public void updateCursor()
	{
		data.cursors.setCursor("penAdd");
		//Check for delete cursor
		MapPoint mouseScreen = new MapPoint(mouseX, mouseY);
		if (zone != null) {
			ArrayList<MapPoint> polygon = zone.getPolygon();
			for (int i = 0; i < polygon.size(); i++) {
				MapPoint point = polygon.get(i);

				MapPoint screenPoint = data.convertToWorkspace(point);
				//Check for click distance
				if (magnitude(mouseScreen, screenPoint) < clickDistance) {
					//SET DELETE CURSOR
					data.cursors.setCursor("penSub");
					break;
				}
			}
		}
		data.refresh();
	}

	public void setEdit(MapZone zone)
	{
		this.zone = zone;
		paintZone = new ArrayList<MapZone>();
		if (zone != null) {
			paintZone.add(zone);
		}
	}

	public void clear()
	{
		zone = null;
		paintZone = new ArrayList<MapZone>();
	}

	public MapZone getZone()
	{
		return zone;
	}

	@Override
	public void deselect()
	{
		if (zone != null && zone.getPolygon().size() > 2) {
			ZoneAddAction a = new ZoneAddAction(this, data.getMap(), zone);
			data.performAction(a);
		} else {
			ZoneNewClearAction c = new ZoneNewClearAction(this, zone);
			data.performAction(c);
		}
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		if (e.getKeyCode() == 27 || e.getKeyCode() == 10) {
			//Set
			deselect();
		}
	}
}
