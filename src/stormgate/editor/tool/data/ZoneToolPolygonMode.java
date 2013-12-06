package stormgate.editor.tool.data;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import stormgate.action.zone.ZonePolygonDeleteAction;
import stormgate.action.zone.ZonePolygonMoveAction;
import stormgate.action.zone.ZonePolygonPasteAction;
import stormgate.action.zone.ZonePolygonSelectAction;
import stormgate.data.MapZone;
import stormgate.data.Tile;
import stormgate.editor.tool.ZoneTool;
import stormgate.editor.ui.forms.graphic.render.ZoneRenderer;
import stormgate.filter.Filter;
import stormgate.geom.MapPoint;
import stormgate.map.Map;

/**
 *
 * @author David
 */
public class ZoneToolPolygonMode extends ZoneToolMode
{

	private ZoneRenderer zoneRender;
	private MapZone[] selected;
	private int mouseDownX;
	private int mouseDownY;
	private boolean mouseDown;
	private boolean dragging = false;
	private MapPoint[] dragPositions;
	private MapPoint dragFrom;

	public ZoneToolPolygonMode(ZoneTool tool)
	{
		super(tool);

		zoneRender = new ZoneRenderer();
		zoneRender.showPoints = true;
		zoneRender.fillColor = new Color(0, 0, 0, 70);
		//zoneRender.borderColor = new Color(180, 50, 50, 200);
		selected = new MapZone[0];
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
		ArrayList<MapZone> paintList = new ArrayList<MapZone>(Arrays.asList(selected));
		zoneRender.setZones(paintList);
		zoneRender.paint(g);

		if (mouseDown && !dragging) {
			//Select
			int rectX = mouseX;
			int rectWidth = mouseDownX - mouseX;
			int rectY = mouseY;
			int rectHeight = mouseDownY - mouseY;

			if (mouseX > mouseDownX) {
				rectX = mouseDownX;
				rectWidth = mouseX - mouseDownX;
			}
			if (mouseY > mouseDownY) {
				rectY = mouseDownY;
				rectHeight = mouseY - mouseDownY;
			}

			//Color
			g.setColor(Color.GREEN);
			g.drawRect(rectX, rectY, rectWidth, rectHeight);
		}
	}

	public void toggleSelection(MapZone zone)
	{
		if (zone != null) {
			boolean found = false;
			if (selected.length > 0) {
				//Deselect
				for (int i = 0; i < selected.length; i++) {
					MapZone z = selected[i];
					if (zone == z) {
						found = true;
						break;
					}
				}
				if (found) {
					MapZone[] selectedArray = new MapZone[selected.length - 1];
					int count = 0;
					for (int i = 0; i < selected.length; i++) {
						MapZone z = selected[i];
						if (zone != z) {
							selectedArray[count++] = z;
						}
					}
					ZonePolygonSelectAction s = new ZonePolygonSelectAction(this, selectedArray);
					data.performAction(s);
					return;
				}
			}
			addToSelection(zone);
		}
	}

	public boolean contains(MapZone zone)
	{
		return indexOf(zone) >= 0;
	}

	public int indexOf(MapZone zone)
	{
		for (int i = 0; i < selected.length; i++) {
			MapZone z = selected[i];
			if (zone == z) {
				return i;
			}
		}
		return -1;
	}

	public void addToSelection(MapZone zone)
	{
		int selection = selected.length + 1;
		MapZone[] selectedArray = new MapZone[selection];
		System.arraycopy(selected, 0, selectedArray, 0, selected.length);
		selectedArray[selected.length] = zone;

		ZonePolygonSelectAction s = new ZonePolygonSelectAction(this, selectedArray);
		data.performAction(s);
	}

	public void updateCursor()
	{
		data.cursors.setCursor("pen");
		MapPoint mouseScreen = data.convertToMap(mouseX, mouseY);
		Map map = data.getMap();

		int tileX = mouseScreen.getTileX();
		int tileY = mouseScreen.getTileY();
		int x = mouseScreen.getX();
		int y = mouseScreen.getY();

		Tile t = map.getTile(tileX, tileY);
		ArrayList<MapZone> zones = t.getZones();
		for (int i = 0; i < zones.size(); i++) {
			MapZone z = zones.get(i);
			if (z.pointInPolygon(x, y)) {
				data.cursors.setCursor("penMove");
				data.refresh();
				return;
			}
		}
		data.refresh();
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		mouseX = e.getX();
		mouseY = e.getY();
		updateCursor();

		if (dragging) {
			MapPoint b = data.convertToMap(mouseX, mouseY);

			for (int i = 0; i < selected.length; i++) {
				MapZone z = selected[i];
				MapPoint original = dragPositions[i];
				int displaceX = b.getX() + original.getX();
				int displaceY = b.getY() + original.getY();
				MapPoint p = z.getPoint(0);

				MapPoint displacement = new MapPoint(displaceX - p.getX(), displaceY - p.getY());

				if (data.snap()) {
					displacement = data.getClosestGrid(displacement);
				}

				z.offsetZone(displacement);
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		mouseDownX = e.getX();
		mouseDownY = e.getY();
		mouseDown = true;
		updateCursor();

		if (!e.isShiftDown() && !e.isControlDown()) {
			MapPoint mouseScreen = data.convertToMap(mouseDownX, mouseDownY);
			int x = mouseScreen.getX();
			int y = mouseScreen.getY();
			dragPositions = new MapPoint[selected.length];
			for (int i = 0; i < selected.length; i++) {
				MapZone z = selected[i];
				if (z.pointInPolygon(x, y)) {
					dragging = true;
					dragFrom = data.convertToMap(mouseDownX, mouseDownY);
				}
				MapPoint p = z.getPoint(0);
				MapPoint offset = new MapPoint(p.getX() - x, p.getY() - y);
				dragPositions[i] = offset;
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		mouseX = e.getX();
		mouseY = e.getY();

		boolean modifier = false;

		if (e.isShiftDown() || e.isControlDown()) {
			modifier = true;
		}

		if (!modifier && !dragging) {
			ZonePolygonSelectAction s = new ZonePolygonSelectAction(this, new MapZone[0]);
			data.performAction(s);
		}

		mouseDown = false;

		MapPoint mouseDownScreen = data.convertToMap(mouseDownX, mouseDownY);
		MapPoint mouseScreen = data.convertToMap(mouseX, mouseY);
		Map map = data.getMap();

		int tileX1 = mouseDownScreen.getTileX();
		int tileY1 = mouseDownScreen.getTileY();
		int tileX2 = mouseScreen.getTileX();
		int tileY2 = mouseScreen.getTileY();
		int x1 = mouseDownScreen.getX();
		int y1 = mouseDownScreen.getY();
		int x2 = mouseScreen.getX();
		int y2 = mouseScreen.getY();

		int temp;
		if (tileX1 > tileX2) {
			temp = tileX1;
			tileX1 = tileX2;
			tileX2 = temp;
		}
		if (tileY1 > tileY2) {
			temp = tileY1;
			tileY1 = tileY2;
			tileY2 = temp;
		}
		if (x1 > x2) {
			temp = x1;
			x1 = x2;
			x2 = temp;
		}
		if (y1 > y2) {
			temp = y1;
			y1 = y2;
			y2 = temp;
		}

		if (dragging) {

			if (x1 != x2 || y1 != y2) {
				MapPoint b = data.convertToMap(mouseX, mouseY);

				int bX = b.getX() - dragFrom.getX();
				int bY = b.getY() - dragFrom.getY();

				MapPoint displacement = new MapPoint(bX, bY);

				if (data.snap()) {
					displacement = data.getClosestGrid(displacement);
				}

				resetPositions();

				ZonePolygonMoveAction m = new ZonePolygonMoveAction(data.getMap(), selected, displacement);
				data.performAction(m);
			}
		}

		ArrayList<MapZone> selectZones = new ArrayList<MapZone>();

		for (int y = tileY1; y <= tileY2; y++) {
			for (int x = tileX1; x <= tileX2; x++) {
				Tile t = map.getTile(x, y);
				ArrayList<MapZone> zones = t.getZones();
				for (int i = 0; i < zones.size(); i++) {
					MapZone z = zones.get(i);

					if (!selectZones.contains(z)) {
						selectZones.add(z);
					}
				}
			}
		}

		//Check for single click
		if (x1 == x2 && y1 == y2) {
			MapZone smallest = null;
			int area = -1;
			for (int i = 0; i < selectZones.size(); i++) {
				MapZone z = selectZones.get(i);
				if (z.pointInPolygon(x1, y1)) {
					int boundArea = z.boundArea();
					if (area == -1 || boundArea < area) {
						area = boundArea;
						smallest = z;
					}
				}
			}
			if (smallest != null) {
				if (modifier) {
					toggleSelection(smallest);
				} else {
					if (!contains(smallest)) {
						addToSelection(smallest);
					}
				}
			}
		} else {
			if (!dragging) {
				for (int i = 0; i < selectZones.size(); i++) {
					MapZone z = selectZones.get(i);
					//CONVERT!
					ArrayList<MapPoint> poly = z.getPolygon();
					int[] xpoints = new int[poly.size()];
					int[] ypoints = new int[poly.size()];

					for (int j = 0; j < poly.size(); j++) {
						MapPoint point = poly.get(j);
						xpoints[j] = point.getX();
						ypoints[j] = point.getY();
					}

					Polygon polygon = new Polygon(xpoints, ypoints, poly.size());
					if (polygon.intersects(x1, y1, x2 - x1, y2 - y1)) {
						if (!contains(z)) {
							addToSelection(z);
						}
					}
				}
			}
		}

		dragging = false;
		updateCursor();
	}

	private void resetPositions()
	{
		MapPoint mouseScreen = data.convertToMap(mouseDownX, mouseDownY);
		int x = mouseScreen.getX();
		int y = mouseScreen.getY();

		for (int i = 0; i < selected.length; i++) {
			MapPoint offset = dragPositions[i];

			int originalX = offset.getX() + x;
			int originalY = offset.getY() + y;

			MapZone z = selected[i];
			MapPoint p = z.getPoint(0);

			MapPoint displacement = new MapPoint(originalX - p.getX(), originalY - p.getY());
			z.offsetZone(displacement);
		}
	}

	@Override
	public void deselect()
	{
		selected = new MapZone[0];
	}

	public void select(MapZone[] selected)
	{
		this.selected = selected;
		tool.updatePanel();
	}

	public MapZone[] getSelected()
	{
		return selected;
	}

	public void deleteSelection()
	{
		//Delete Polygon
		ZonePolygonDeleteAction d = new ZonePolygonDeleteAction(this, data.getMap(), selected);
		data.performAction(d);
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		if (e.getKeyCode() == 127) {
			deleteSelection();
		}
	}
	private MapZone[] clipboard;

	public void cut()
	{
		copy();
		deleteSelection();
	}

	public void copy()
	{
		clipboard = selected;
	}

	public void clear()
	{
		selected = new MapZone[0];
		clipboard = null;
	}

	public void paste()
	{
		if (clipboard != null) {
			MapPoint displacement = data.convertToMap(mouseX, mouseY);
			if (data.snap()) {
				displacement = data.getClosestGrid(displacement);
			}
			ZonePolygonPasteAction p = new ZonePolygonPasteAction(this, data.getMap(), clipboard, displacement);
			data.performAction(p);
		}
	}
}
