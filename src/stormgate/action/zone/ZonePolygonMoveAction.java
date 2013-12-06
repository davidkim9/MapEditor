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
public class ZonePolygonMoveAction implements Action {
	private Map map;
	private MapZone[] selected;
	private MapPoint displacement;

	public ZonePolygonMoveAction(Map map, MapZone[] selected, MapPoint displacement)
	{
		this.map = map;
		this.selected = selected;
		this.displacement = displacement;
	}

	public void undo()
	{
		for (int i = 0; i < selected.length; i++) {
			MapZone z = selected[i];

			map.editTiles(z);
			//map.removeZone(z);
			MapPoint invertDisplacement = new MapPoint(-displacement.getX(), -displacement.getY());
			z.offsetZone(invertDisplacement);
			//Update which map the polygon belongs to
			ArrayList<Tile> tiles = map.getTiles(z);
			map.setZoneTiles(z, tiles);
			map.editTiles(z);
		}
	}

	public void perform()
	{
		for (int i = 0; i < selected.length; i++) {
			MapZone z = selected[i];

			map.editTiles(z);
			//map.removeZone(z);
			z.offsetZone(displacement);
			//Update which map the polygon belongs to
			ArrayList<Tile> tiles = map.getTiles(z);
			map.setZoneTiles(z, tiles);
			map.editTiles(z);
		}
	}
}
