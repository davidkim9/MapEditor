package stormgate.action.zone;

import java.util.ArrayList;
import stormgate.action.Action;
import stormgate.data.MapZone;
import stormgate.data.Tile;
import stormgate.editor.data.EditorData;
import stormgate.editor.tool.data.SelectedZone;
import stormgate.editor.tool.data.ZoneToolSelectMode;
import stormgate.geom.MapPoint;
import stormgate.map.Map;

/**
 *
 * @author David
 */
public class ZoneSelectDeleteAction implements Action
{
	private ZoneToolSelectMode mode;
	private Map map;
	private SelectedZone[] selected;
	private ArrayList<Integer> indexes;

	public ZoneSelectDeleteAction(ZoneToolSelectMode mode, Map map, SelectedZone[] selected)
	{
		this.mode = mode;
		this.map = map;
		this.selected = selected;
	}

	public void undo()
	{
		for (int i = selected.length - 1; i >= 0; i--) {

			SelectedZone select = selected[i];
			MapZone zone = select.zone;

			map.editTiles(zone);
			map.removeZone(zone);
			MapPoint[] points = select.points;

			for (int j = points.length - 1; j >= 0; j--) {

				MapPoint p = points[j];

				int index = indexes.remove(indexes.size() - 1);

				zone.addPoint(index, p);

			}

			ArrayList<Tile> tiles = map.getTiles(zone);
			map.setZoneTiles(zone, tiles);
			map.editTiles(zone);
		}
		mode.select(selected);
	}

	public void perform()
	{
		indexes = new ArrayList<Integer>();

		for (int i = 0; i < selected.length; i++) {

			SelectedZone select = selected[i];
			MapZone zone = select.zone;

			map.editTiles(zone);
			map.removeZone(zone);
			MapPoint[] points = select.points;

			for (int j = 0; j < points.length; j++) {

				MapPoint p = points[j];
				
				int index = zone.getPointIndex(p);

				zone.removePoint(p);

				indexes.add(index);

			}

			if(zone.numPoints() > 2){
				ArrayList<Tile> tiles = map.getTiles(zone);
				map.setZoneTiles(zone, tiles);
				map.editTiles(zone);
			}
		}
		mode.deselect();
	}
}
