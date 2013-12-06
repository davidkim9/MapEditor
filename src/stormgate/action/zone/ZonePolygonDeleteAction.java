package stormgate.action.zone;

import java.util.ArrayList;
import stormgate.action.Action;
import stormgate.data.MapZone;
import stormgate.data.Tile;
import stormgate.editor.tool.data.ZoneToolPolygonMode;
import stormgate.map.Map;

/**
 *
 * @author David
 */
public class ZonePolygonDeleteAction implements Action
{

	private Map map;
	private MapZone[] selected;
	private ZoneToolPolygonMode mode;

	public ZonePolygonDeleteAction(ZoneToolPolygonMode mode, Map map, MapZone[] selected)
	{
		this.mode = mode;
		this.map = map;
		this.selected = selected;
	}

	public void undo()
	{
		for(int i = 0 ; i < selected.length ; i++){
			MapZone zone = selected[i];
			ArrayList<Tile> tiles = map.getTiles(zone);
			map.setZoneTiles(zone, tiles);
			map.editTiles(zone);
		}
		//Keep selected
		mode.select(selected);
	}

	public void perform()
	{
		//Remove the zone from all tiles that it was added to
		for(int i = 0 ; i < selected.length ; i++){
			MapZone zone = selected[i];
			map.editTiles(zone);
			map.removeZone(zone);
		}
		//Deselect
		mode.deselect();
	}
}
