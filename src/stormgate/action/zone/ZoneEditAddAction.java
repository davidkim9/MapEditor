package stormgate.action.zone;

import java.util.ArrayList;
import stormgate.action.Action;
import stormgate.data.MapZone;
import stormgate.data.Tile;
import stormgate.geom.MapPoint;
import stormgate.map.Map;

/**
 *
 * @author David
 */
public class ZoneEditAddAction implements Action
{
	private Map map;
	private MapZone zone;
	private int pointIndex;
	private MapPoint point;

	public ZoneEditAddAction(Map map, MapZone zone, int pointIndex, MapPoint point)
	{
		this.map = map;
		this.zone = zone;
		this.pointIndex = pointIndex;
		this.point = point;
	}

	public void undo()
	{
		map.editTiles(zone);
		map.removeZone(zone);
		zone.removePoint(point);
		ArrayList<Tile> tiles = map.getTiles(zone);
		map.setZoneTiles(zone, tiles);
		map.editTiles(zone);
	}

	public void perform()
	{
		map.editTiles(zone);
		map.removeZone(zone);
		zone.addPoint(pointIndex, point);
		ArrayList<Tile> tiles = map.getTiles(zone);
		map.setZoneTiles(zone, tiles);
		map.editTiles(zone);
	}
}
