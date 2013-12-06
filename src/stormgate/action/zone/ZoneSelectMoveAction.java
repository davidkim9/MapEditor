package stormgate.action.zone;

import java.util.ArrayList;
import stormgate.action.Action;
import stormgate.data.MapZone;
import stormgate.data.Tile;
import stormgate.editor.data.EditorData;
import stormgate.editor.tool.data.SelectedZone;
import stormgate.geom.MapPoint;
import stormgate.map.Map;

/**
 *
 * @author David
 */
public class ZoneSelectMoveAction implements Action
{

	private Map map;
	private SelectedZone[] selected;
	private SelectedZone[] originalPositions;
	private MapPoint displacement;
	private MapPoint mouseDown;
	private EditorData data;

	public ZoneSelectMoveAction(EditorData data, Map map, SelectedZone[] originalPositions, SelectedZone[] selected, MapPoint displacement, MapPoint mouseDown)
	{
		this.data = data;
		this.map = map;
		this.originalPositions = originalPositions;
		this.selected = selected;
		this.displacement = displacement;
		this.mouseDown = mouseDown;
	}

	public void undo()
	{
		for (int i = 0; i < selected.length; i++) {
			SelectedZone select = selected[i];
			MapZone zone = select.zone;

			map.editTiles(zone);
			//map.removeZone(zone);
			MapPoint[] points = select.points;

			SelectedZone originalZone = originalPositions[i];
			MapPoint[] originalPoints = originalZone.points;

			for (int j = 0; j < points.length; j++) {
				MapPoint p = points[j];
				MapPoint original = originalPoints[j];

				int displaceX = mouseDown.getX() + original.getX();
				int displaceY = mouseDown.getY() + original.getY();

				MapPoint c = new MapPoint(displaceX, displaceY);

				c = data.getClosestGrid(c);

				//Displace Point
				p.setX(c.getX());
				p.setY(c.getY());

			}

			ArrayList<Tile> tiles = map.getTiles(zone);
			map.setZoneTiles(zone, tiles);
			map.editTiles(zone);
		}
	}

	public void perform()
	{
		for (int i = 0; i < selected.length; i++) {
			SelectedZone select = selected[i];
			MapZone zone = select.zone;
			map.editTiles(zone);
			//map.removeZone(zone);
			MapPoint[] points = select.points;

			SelectedZone originalZone = originalPositions[i];
			MapPoint[] originalPoints = originalZone.points;

			for (int j = 0; j < points.length; j++) {
				MapPoint p = points[j];
				MapPoint original = originalPoints[j];

				int displaceX = displacement.getX() + original.getX();
				int displaceY = displacement.getY() + original.getY();

				MapPoint c = new MapPoint(displaceX, displaceY);

				if (data.snap()) {
					c = data.getClosestGrid(c);
				}

				//Displace Point
				p.setX(c.getX());
				p.setY(c.getY());

			}

			ArrayList<Tile> tiles = map.getTiles(zone);
			map.setZoneTiles(zone, tiles);
			map.editTiles(zone);
		}
	}
}
