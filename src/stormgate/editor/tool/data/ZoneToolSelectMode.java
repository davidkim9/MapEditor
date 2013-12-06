package stormgate.editor.tool.data;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import stormgate.action.zone.ZoneSelectAction;
import stormgate.action.zone.ZoneSelectDeleteAction;
import stormgate.action.zone.ZoneSelectMoveAction;
import stormgate.common.Common;
import stormgate.data.MapZone;
import stormgate.data.Tile;
import stormgate.editor.tool.ZoneTool;
import stormgate.filter.Filter;
import stormgate.geom.Geometry;
import stormgate.geom.MapPoint;
import stormgate.map.Map;

/**
 *
 * @author David
 */
public class ZoneToolSelectMode extends ZoneToolMode
{

	private SelectedZone[] selected;
	private int mouseDownX;
	private int mouseDownY;
	private boolean mouseDown;
	private boolean dragging = false;
	private SelectedZone[] dragPositions;
	private MapPoint dragFrom;
	private final int clickDistance = 5;

	public ZoneToolSelectMode(ZoneTool tool)
	{
		super(tool);

		selected = new SelectedZone[0];
		dragPositions = new SelectedZone[0];
	}

	private int zoomFilter(float a)
	{
		Filter filter = data.getRenderData().getFilter();
		return Math.round(a * filter.getZoom());
	}

	@Override
	public void paint(Graphics g)
	{
		MapPoint camera = data.getCameraLocation();
		int camX = camera.getX();
		int camY = camera.getY();

		int offsetX = data.getScreenWidth() / 2;
		int offsetY = data.getScreenHeight() / 2;

		g.setColor(new Color(0, 100, 150, 200));
		int radius = 2;
		int diam = radius * 2;

		for (int i = 0; i < selected.length; i++) {
			SelectedZone zone = selected[i];
			MapPoint[] points = zone.points;
			for (int j = 0; j < points.length; j++) {
				MapPoint pt = points[j];
				//Convert each point to the appropriate filter/offset

				int xPt = zoomFilter(pt.getX() - camX) + offsetX;
				int yPt = zoomFilter(pt.getY() - camY) + offsetY;

				g.drawRect(xPt - radius, yPt - radius, diam, diam);
			}
		}

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

	public void updateCursor(boolean modifier)
	{
		data.cursors.setCursor("pen");

		Map map = data.getMap();
		MapPoint mouseMap = data.convertToMap(mouseX, mouseY);
		int tileX = mouseMap.getTileX();
		int tileY = mouseMap.getTileY();
		MapPoint mouseScreen = new MapPoint(mouseX, mouseY);

		for (int y = -1; y <= 1; y++) {
			for (int x = -1; x <= 1; x++) {
				int tX = tileX + x;
				int tY = tileY + y;

				Tile t = map.getTile(tX, tY);
				ArrayList<MapZone> zones = t.getZones();
				for (int i = 0; i < zones.size(); i++) {
					MapZone z = zones.get(i);
					ArrayList<MapPoint> polygon = z.getPolygon();
					MapPoint prevPointRef = polygon.get(0);
					MapPoint firstPoint = data.convertToWorkspace(prevPointRef);
					MapPoint prevPoint = firstPoint;

					for (int j = 0; j < polygon.size(); j++) {
						MapPoint point = polygon.get(j);

						MapPoint screenPoint = data.convertToWorkspace(point);
						//Check for click distance
						if (magnitude(mouseScreen, screenPoint) < clickDistance) {
							//Check if the point is selected
							for (int k = 0; k < selected.length; k++) {
								SelectedZone select = selected[k];
								if (select.zone == z) {
									MapPoint[] selectedPoints = select.points;
									for (int l = 0; l < selectedPoints.length; l++) {
										MapPoint p = selectedPoints[l];
										if (point == p) {
											if (modifier) {
												data.cursors.setCursor("penSub");
											} else {
												//SET MOVE CURSOR
												data.cursors.setCursor("penMove");
											}
											data.refresh();
											return;
										}
									}
								}
							}
							if (modifier) {
								data.cursors.setCursor("penAdd");
							} else {
								//SET MOVE CURSOR
								data.cursors.setCursor("penMove");
							}
							data.refresh();
							return;
						}

						MapPoint closestPoint = Geometry.closestPointOnLine(mouseScreen, prevPoint, screenPoint);

						//Check for click distance
						if (magnitude(mouseScreen, closestPoint) < clickDistance) {
							for (int k = 0; k < selected.length; k++) {
								SelectedZone select = selected[k];
								if (select.zone == z) {
									MapPoint[] selectedPoints = select.points;
									for (int l = 0; l < selectedPoints.length; l++) {
										MapPoint p = selectedPoints[l];
										if (point == p || prevPointRef == p) {
											//SET MOVE CURSOR
											data.cursors.setCursor("penMove");
											data.refresh();
											return;
										}
									}
								}
							}
						}

						prevPointRef = point;
						prevPoint = screenPoint;
					}
					//Connect last to first
					MapPoint closestPoint = Geometry.closestPointOnLine(mouseScreen, prevPoint, firstPoint);

					//Check for click distance
					if (magnitude(mouseScreen, closestPoint) < clickDistance) {
						for (int k = 0; k < selected.length; k++) {
							SelectedZone select = selected[k];
							if (select.zone == z) {
								MapPoint[] selectedPoints = select.points;
								for (int l = 0; l < selectedPoints.length; l++) {
									MapPoint p = selectedPoints[l];
									if (polygon.get(0) == p || prevPointRef == p) {
										//SET MOVE CURSOR
										data.cursors.setCursor("penMove");
										data.refresh();
										return;
									}
								}
							}
						}
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
		mouseDownX = e.getX();
		mouseDownY = e.getY();
		mouseDown = true;

		Map map = data.getMap();
		MapPoint mouseMap = data.convertToMap(mouseX, mouseY);
		int tileX = mouseMap.getTileX();
		int tileY = mouseMap.getTileY();
		MapPoint mouseScreen = new MapPoint(mouseX, mouseY);

		for (int y = -1; y <= 1; y++) {
			for (int x = -1; x <= 1; x++) {
				int tX = tileX + x;
				int tY = tileY + y;

				Tile t = map.getTile(tX, tY);
				ArrayList<MapZone> zones = t.getZones();
				for (int i = 0; i < zones.size(); i++) {
					MapZone z = zones.get(i);
					ArrayList<MapPoint> polygon = z.getPolygon();
					MapPoint prevPointRef = polygon.get(0);
					MapPoint firstPoint = data.convertToWorkspace(prevPointRef);
					MapPoint prevPoint = firstPoint;

					for (int j = 0; j < polygon.size(); j++) {
						MapPoint point = polygon.get(j);

						MapPoint screenPoint = data.convertToWorkspace(point);
						//Check for click distance
						if (magnitude(mouseScreen, screenPoint) < clickDistance) {
							//Check if the point is selected
							for (int k = 0; k < selected.length; k++) {
								SelectedZone select = selected[k];
								if (select.zone == z) {
									MapPoint[] selectedPoints = select.points;
									for (int l = 0; l < selectedPoints.length; l++) {
										MapPoint p = selectedPoints[l];
										if (point == p) {
											startDrag();
										}
									}
								}
							}
						}

						MapPoint closestPoint = Geometry.closestPointOnLine(mouseScreen, prevPoint, screenPoint);

						//Check for click distance
						if (magnitude(mouseScreen, closestPoint) < clickDistance) {
							for (int k = 0; k < selected.length; k++) {
								SelectedZone select = selected[k];
								if (select.zone == z) {
									MapPoint[] selectedPoints = select.points;
									for (int l = 0; l < selectedPoints.length; l++) {
										MapPoint p = selectedPoints[l];
										if (point == p || prevPointRef == p) {
											startDrag();
											return;
										}
									}
								}
							}
						}

						prevPointRef = point;
						prevPoint = screenPoint;
					}
					//Connect last to first
					MapPoint closestPoint = Geometry.closestPointOnLine(mouseScreen, prevPoint, firstPoint);

					//Check for click distance
					if (magnitude(mouseScreen, closestPoint) < clickDistance) {
						for (int k = 0; k < selected.length; k++) {
							SelectedZone select = selected[k];
							if (select.zone == z) {
								MapPoint[] selectedPoints = select.points;
								for (int l = 0; l < selectedPoints.length; l++) {
									MapPoint p = selectedPoints[l];
									if (polygon.get(0) == p || prevPointRef == p) {
										startDrag();
										return;
									}
								}
							}
						}
					}
				}
			}
		}
		updateCursor(e.isShiftDown() || e.isControlDown());
		/*
		if (!e.isShiftDown() && !e.isControlDown()) {

		MapPoint mouseScreen = new MapPoint(mouseDownX, mouseDownY);
		MapPoint mouseMap = data.convertToMap(mouseDownX, mouseDownY);
		int x = mouseMap.getX();
		int y = mouseMap.getY();
		dragPositions = new MapPoint[selectedPoints.length];
		for (int i = 0; i < selectedPoints.length; i++) {
		MapPoint p = selectedPoints[i];
		MapPoint screenPoint = data.convertToWorkspace(p);
		if (magnitude(screenPoint, mouseScreen) < clickDistance) {
		dragging = true;
		dragFrom = data.convertToMap(mouseDownX, mouseDownY);
		}
		MapPoint offset = new MapPoint(p.getX() - x, p.getY() - y);
		dragPositions[i] = offset;
		}


		}
		 */
	}

	public void startDrag()
	{
		MapPoint mouseMap = data.convertToMap(mouseX, mouseY);
		int x = mouseMap.getX();
		int y = mouseMap.getY();
		dragPositions = new SelectedZone[selected.length];
		for (int i = 0; i < selected.length; i++) {
			SelectedZone select = selected[i];
			MapZone selectedZone = select.zone;
			MapPoint[] selectedPoint = select.points;

			SelectedZone dragZone = new SelectedZone();
			dragZone.zone = selectedZone;
			dragZone.points = new MapPoint[selectedPoint.length];

			for (int j = 0; j < selectedPoint.length; j++) {
				MapPoint p = selectedPoint[j];

				MapPoint offset = new MapPoint(p.getX() - x, p.getY() - y);

				dragZone.points[j] = offset;
			}

			dragPositions[i] = dragZone;
		}

		dragging = true;
		dragFrom = data.convertToMap(mouseDownX, mouseDownY);
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

		mouseDown = false;

		MapPoint mouseDownScreen = data.convertToMap(mouseDownX, mouseDownY);
		MapPoint mouseScreen = new MapPoint(mouseX, mouseY);
		MapPoint mouseMap = data.convertToMap(mouseX, mouseY);

		Map map = data.getMap();

		int tileX1 = mouseDownScreen.getTileX();
		int tileY1 = mouseDownScreen.getTileY();
		int tileX2 = mouseMap.getTileX();
		int tileY2 = mouseMap.getTileY();
		int x1 = mouseDownScreen.getX();
		int y1 = mouseDownScreen.getY();
		int x2 = mouseMap.getX();
		int y2 = mouseMap.getY();

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

		ArrayList<SelectedZone> select = new ArrayList<SelectedZone>();

		if (modifier) {
			select = new ArrayList<SelectedZone>(selected.length);
			select.addAll(Arrays.asList(selected));
		} else {
			select = new ArrayList<SelectedZone>();
		}

		if (x1 == x2 && y1 == y2) {
			//Single Click
			int tileX = mouseMap.getTileX();
			int tileY = mouseMap.getTileY();

			for (int y = -1; y <= 1; y++) {
				for (int x = -1; x <= 1; x++) {
					int tX = tileX + x;
					int tY = tileY + y;

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
								boolean notSelected = true;
								//Check if the point is selected
								for (int k = 0; k < selected.length; k++) {
									SelectedZone selectedZone = selected[k];
									if (selectedZone.zone == z) {
										MapPoint[] selectedPoints = selectedZone.points;
										for (int l = 0; l < selectedPoints.length; l++) {
											MapPoint p = selectedPoints[l];
											if (point == p) {
												if (modifier) {
													//Remove this point
													SelectedZone newSelection = new SelectedZone();
													newSelection.zone = z;
													deselect(select, newSelection, point);
													System.out.println("deselect this point");
												} else {
													//Select this point
													select = new ArrayList<SelectedZone>(1);
													SelectedZone newSelection = new SelectedZone();
													newSelection.zone = z;
													newSelection.points = new MapPoint[1];
													newSelection.points[0] = point;
													select.add(newSelection);
												}
												notSelected = false;
											}
										}
									}
								}
								if (notSelected) {
									if (modifier) {
										//Add this point
										SelectedZone newSelection = new SelectedZone();
										newSelection.zone = z;
										select(select, newSelection, point);
									} else {
										//Select this point
										select = new ArrayList<SelectedZone>(1);
										SelectedZone newSelection = new SelectedZone();
										newSelection.zone = z;
										newSelection.points = new MapPoint[1];
										newSelection.points[0] = point;
										select.add(newSelection);
									}
								}
							}
						}
					}
				}
			}
			SelectedZone[] selectedArray = select.toArray(new SelectedZone[select.size()]);

			//ACTION
			ZoneSelectAction s = new ZoneSelectAction(this, selectedArray);
			data.performAction(s);
		} else {
			if (!dragging) {
				for (int y = tileY1; y <= tileY2; y++) {
					for (int x = tileX1; x <= tileX2; x++) {
						Tile t = map.getTile(x, y);
						ArrayList<MapZone> zones = t.getZones();
						for (int i = 0; i < zones.size(); i++) {
							MapZone z = zones.get(i);
							ArrayList<MapPoint> polygon = z.getPolygon();

							//Check if zone is already added
							if (!checkZone(select, z)) {

								ArrayList<MapPoint> selectPoints = new ArrayList<MapPoint>();

								for (int j = 0; j < polygon.size(); j++) {
									MapPoint polyPoint = polygon.get(j);
									if (Common.abb(polyPoint.getX(), polyPoint.getY(), x1, y1, x2, y2)) {
										selectPoints.add(polyPoint);
									}
								}

								if (selectPoints.size() > 0) {
									SelectedZone newSelection = new SelectedZone();
									newSelection.zone = z;
									newSelection.points = selectPoints.toArray(new MapPoint[selectPoints.size()]);

									select.add(newSelection);
								}
							}
						}
					}
				}
				SelectedZone[] selectedArray = select.toArray(new SelectedZone[select.size()]);

				//ACTION
				ZoneSelectAction s = new ZoneSelectAction(this, selectedArray);
				data.performAction(s);

			} else {
				//DRAGGING
				MapPoint mouseDownLocal = data.convertToMap(mouseDownX, mouseDownY);
				MapPoint b = data.convertToMap(mouseX, mouseY);
				resetPositions();

				ZoneSelectMoveAction m = new ZoneSelectMoveAction(data, data.getMap(), dragPositions, selected, b, mouseDownLocal);
				data.performAction(m);
			}
		}

		dragging = false;
		updateCursor(e.isShiftDown() || e.isControlDown());

	}

	public boolean checkZone(ArrayList<SelectedZone> selectedZone, MapZone zone)
	{
		for (int i = 0; i < selectedZone.size(); i++) {
			if (selectedZone.get(i).zone == zone) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		mouseX = e.getX();
		mouseY = e.getY();

		if (dragging) {
			MapPoint b = data.convertToMap(mouseX, mouseY);

			for (int i = 0; i < selected.length; i++) {
				SelectedZone zone = selected[i];
				MapPoint[] points = zone.points;
				SelectedZone originalZone = dragPositions[i];
				MapPoint[] originalPoints = originalZone.points;
				for (int j = 0; j < points.length; j++) {

					MapPoint p = points[j];
					MapPoint original = originalPoints[j];

					int displaceX = b.getX() + original.getX();
					int displaceY = b.getY() + original.getY();

					MapPoint c = new MapPoint(displaceX, displaceY);

					if (data.snap()) {
						//Weird type of snap that I think could work out nicely
						c = data.getClosestGrid(c);
					}

					//Displace Point
					p.setX(c.getX());
					p.setY(c.getY());

				}
			}
		}

		updateCursor(e.isShiftDown() || e.isControlDown());
	}

	private void resetPositions()
	{
		MapPoint mouseScreen = data.convertToMap(mouseDownX, mouseDownY);
		int x = mouseScreen.getX();
		int y = mouseScreen.getY();

		if (dragPositions != null) {

			for (int i = 0; i < dragPositions.length; i++) {
				SelectedZone select = dragPositions[i];
				MapPoint[] selectedPoints = select.points;
				SelectedZone original = selected[i];
				MapPoint[] originalPoints = original.points;

				for (int j = 0; j < selectedPoints.length; j++) {
					MapPoint offset = selectedPoints[j];

					int originalX = offset.getX() + x;
					int originalY = offset.getY() + y;

					MapPoint p = originalPoints[j];
					p.setX(originalX);
					p.setY(originalY);
				}
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		if (e.getKeyCode() == 127) {
			//Delete Selected
			ZoneSelectDeleteAction d = new ZoneSelectDeleteAction(this, data.getMap(), selected);
			data.performAction(d);
		}

		updateCursor(e.isShiftDown() || e.isControlDown());
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		updateCursor(e.isShiftDown() || e.isControlDown());
	}

	public void select(SelectedZone[] selected)
	{
		this.selected = selected;
	}

	@Override
	public void deselect()
	{
		selected = new SelectedZone[0];
	}

	public SelectedZone[] getSelected()
	{
		return selected;
	}

	private MapPoint[] deselect(MapPoint[] points, MapPoint deselectPoint)
	{
		ArrayList<MapPoint> selectPoints = new ArrayList<MapPoint>();

		for (int j = 0; j < points.length; j++) {
			if (points[j] != deselectPoint) {
				selectPoints.add(points[j]);
			}
		}
		MapPoint[] newSelection = selectPoints.toArray(new MapPoint[selectPoints.size()]);

		return newSelection;
	}

	private void select(ArrayList<SelectedZone> select, SelectedZone newSelection, MapPoint point)
	{
		for (int i = 0; i < select.size(); i++) {
			SelectedZone zone = select.get(i);
			if (zone.zone == newSelection.zone) {

				MapPoint[] points = zone.points;

				ArrayList<MapPoint> selectPoints = new ArrayList<MapPoint>();

				for (int j = 0; j < points.length; j++) {
					if (points[j] != point) {
						selectPoints.add(points[j]);
					}
				}
				selectPoints.add(point);

				newSelection.points = selectPoints.toArray(new MapPoint[selectPoints.size()]);

				select.remove(zone);
				select.add(newSelection);
				return;
			}
		}

		newSelection.points = new MapPoint[1];
		newSelection.points[0] = point;
		select.add(newSelection);
	}

	private void deselect(ArrayList<SelectedZone> select, SelectedZone newSelection, MapPoint point)
	{
		for (int i = 0; i < select.size(); i++) {
			SelectedZone zone = select.get(i);
			if (zone.zone == newSelection.zone) {

				MapPoint[] points = zone.points;

				ArrayList<MapPoint> selectPoints = new ArrayList<MapPoint>();

				for (int j = 0; j < points.length; j++) {
					if (points[j] != point) {
						selectPoints.add(points[j]);
					}
				}

				newSelection.points = selectPoints.toArray(new MapPoint[selectPoints.size()]);

				select.remove(zone);

				if (newSelection.points.length > 0) {
					select.add(newSelection);
				}
				return;
			}
		}
		newSelection.points = new MapPoint[1];
		newSelection.points[0] = point;
		select.add(newSelection);
	}
}
