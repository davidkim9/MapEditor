package stormgate.editor.tool.data;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import stormgate.action.zone.ZoneEditAddAction;
import stormgate.action.zone.ZoneEditRemoveAction;
import stormgate.data.MapZone;
import stormgate.data.Tile;
import stormgate.editor.tool.ZoneTool;
import stormgate.editor.ui.forms.graphic.render.ZoneRenderer;
import stormgate.filter.Filter;
import stormgate.geom.Geometry;
import stormgate.geom.MapPoint;
import stormgate.map.Map;

/**
 *
 * @author David
 */
public class ZoneToolEditMode extends ZoneToolMode {

	private ZoneRenderer zoneRender;

	private final int clickDistance = 5;

	public ZoneToolEditMode(ZoneTool tool)
	{
		super(tool);

		zoneRender = new ZoneRenderer();
		zoneRender.showPoints = true;
		zoneRender.fillColor = new Color(0, 0, 0, 0);
		zoneRender.borderColor = new Color(0, 0, 0, 70);
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

		zoneRender.setOffset(offsetX, offsetY);
		zoneRender.setCamera(data.getCameraLocation());

		ArrayList<MapZone> allZones = new ArrayList<MapZone>();

		int screenWidth = data.getScreenWidth();
		int screenHeight = data.getScreenHeight();

		int tilesizeZoom = zoomFilter(MapPoint.tileSize);

		int viewDistanceX = (screenWidth / tilesizeZoom) / 2 + 1;
		int viewDistanceY = (screenHeight / tilesizeZoom) / 2 + 1;

		Map map = data.getMap();

		MapPoint cam = data.getCameraLocation();

		for (int y = -viewDistanceY; y <= viewDistanceY; y++) {
			for (int x = -viewDistanceX; x <= viewDistanceX; x++) {
				Tile t = map.getTile(x + cam.getTileX(), y + cam.getTileY());
				ArrayList<MapZone> zones = t.getZones();
				for (int i = 0; i < zones.size(); i++) {
					MapZone z = zones.get(i);

					if (!allZones.contains(z)) {
						allZones.add(z);
					}
				}
			}
		}

		zoneRender.setZones(allZones);
		zoneRender.paint(g);
	}

	public void updateCursor()
	{
		data.cursors.setCursor("pen");

		Map map = data.getMap();
		MapPoint mouseMap = data.convertToMap(mouseX, mouseY);
		int tileX = mouseMap.getTileX();
		int tileY = mouseMap.getTileY();
		MapPoint mouseScreen = new MapPoint(mouseX, mouseY);

		for (int y = -1; y <= 1; y++) {
			for (int x = -1; x <= 1; x++) {
				int tX = tileX+x;
				int tY = tileY+y;

				Tile t = map.getTile(tX, tY);
				ArrayList<MapZone> zones = t.getZones();
				for (int i = 0; i < zones.size(); i++) {
					MapZone z = zones.get(i);
					ArrayList<MapPoint> polygon = z.getPolygon();

					for (int j = 0; j < polygon.size(); j++) {
						MapPoint point = polygon.get(j);

						MapPoint screenPoint = data.convertToWorkspace(point);
						//Check for click distance
						if (magnitude(mouseScreen, screenPoint) < clickDistance) {
							//SET DELETE CURSOR
							data.cursors.setCursor("penSub");
							data.refresh();
							return;
						}

					}
				}
			}
		}
		for (int y = -1; y <= 1; y++) {
			for (int x = -1; x <= 1; x++) {
				int tX = tileX+x;
				int tY = tileY+y;

				Tile t = map.getTile(tX, tY);
				ArrayList<MapZone> zones = t.getZones();
				for (int i = 0; i < zones.size(); i++) {
					MapZone z = zones.get(i);
					ArrayList<MapPoint> polygon = z.getPolygon();
					MapPoint firstPoint = data.convertToWorkspace(polygon.get(0));
					MapPoint prevPoint = firstPoint;

					for (int j = 1; j < polygon.size(); j++) {
						MapPoint point = polygon.get(j);
						MapPoint screenPoint = data.convertToWorkspace(point);

						MapPoint closestPoint = Geometry.closestPointOnLine(mouseScreen, prevPoint, screenPoint);

						//Check for click distance
						if (magnitude(mouseScreen, closestPoint) < clickDistance) {
							//SET ADD CURSOR
							data.cursors.setCursor("penAdd");
							data.refresh();
							return;
						}

						prevPoint = screenPoint;
					}

					//Connect last to first
					MapPoint closestPoint = Geometry.closestPointOnLine(mouseScreen, prevPoint, firstPoint);

					//Check for click distance
					if (magnitude(mouseScreen, closestPoint) < clickDistance) {
						//SET ADD CURSOR
						data.cursors.setCursor("penAdd");
						data.refresh();
						return;
					}
				}
			}
		}
		data.refresh();
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
		Map map = data.getMap();
		MapPoint mouseMap = data.convertToMap(mouseX, mouseY);
		int tileX = mouseMap.getTileX();
		int tileY = mouseMap.getTileY();
		MapPoint mouseScreen = new MapPoint(mouseX, mouseY);

		for (int y = -1; y <= 1; y++) {
			for (int x = -1; x <= 1; x++) {
				int tX = tileX+x;
				int tY = tileY+y;

				Tile t = map.getTile(tX, tY);
				ArrayList<MapZone> zones = t.getZones();
				for (int i = 0; i < zones.size(); i++) {
					MapZone z = zones.get(i);
					ArrayList<MapPoint> polygon = z.getPolygon();

					for (int j = 0; j < polygon.size(); j++) {
						MapPoint point = polygon.get(j);

						MapPoint screenPoint = data.convertToWorkspace(point);
						//Check for click distance
						if (magnitude(mouseScreen, screenPoint) < clickDistance) {
							//REMOVE POINT
							removePoint(z, point);
							data.refresh();
							return;
						}

					}
				}
			}
		}
		for (int y = -1; y <= 1; y++) {
			for (int x = -1; x <= 1; x++) {
				int tX = tileX+x;
				int tY = tileY+y;

				Tile t = map.getTile(tX, tY);
				ArrayList<MapZone> zones = t.getZones();
				for (int i = 0; i < zones.size(); i++) {
					MapZone z = zones.get(i);
					ArrayList<MapPoint> polygon = z.getPolygon();
					MapPoint firstPoint = data.convertToWorkspace(polygon.get(0));
					MapPoint prevPoint = firstPoint;

					for (int j = 1; j < polygon.size(); j++) {
						MapPoint point = polygon.get(j);
						MapPoint screenPoint = data.convertToWorkspace(point);

						MapPoint closestPoint = Geometry.closestPointOnLine(mouseScreen, prevPoint, screenPoint);

						//Check for click distance
						if (magnitude(mouseScreen, closestPoint) < clickDistance) {
							//ADD POINT
							addPoint(z, j, data.convertToMap(closestPoint));
							data.refresh();
							return;
						}

						prevPoint = screenPoint;
					}

					//Connect last to first
					MapPoint closestPoint = Geometry.closestPointOnLine(mouseScreen, prevPoint, firstPoint);

					//Check for click distance
					if (magnitude(mouseScreen, closestPoint) < clickDistance) {
						//ADD POINT
						addPoint(z, 0, data.convertToMap(closestPoint));
						data.refresh();
						return;
					}
				}
			}
		}
		data.refresh();
		updateCursor();
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		mouseX = e.getX();
		mouseY = e.getY();
		updateCursor();
	}

	private void addPoint(MapZone z, int j, MapPoint point)
	{
		if (data.snap()) {
			point = data.getClosestGrid(point);
		}
		ZoneEditAddAction s = new ZoneEditAddAction(data.getMap(), z, j, point);
		data.performAction(s);
	}

	private void removePoint(MapZone z, MapPoint point)
	{
		ZoneEditRemoveAction s = new ZoneEditRemoveAction(data.getMap(), z, point);
		data.performAction(s);
	}
}
