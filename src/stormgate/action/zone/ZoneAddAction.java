package stormgate.action.zone;

import java.util.ArrayList;
import stormgate.action.Action;
import stormgate.data.MapZone;
import stormgate.data.Tile;
import stormgate.editor.tool.data.ZoneToolNewMode;
import stormgate.map.Map;

/**
 *
 * @author David
 */
public class ZoneAddAction implements Action
{
	private ZoneToolNewMode mode;
	private Map map;
	private MapZone zone;

	public ZoneAddAction(ZoneToolNewMode mode, Map map, MapZone zone)
	{
		this.mode = mode;
		this.map = map;
		this.zone = zone;
	}

	public void undo()
	{
		//Remove the zone from all tiles that it was added to
		map.editTiles(zone);
		map.removeZone(zone);
		mode.setEdit(zone);
	}

	public void perform()
	{
		ArrayList<Tile> tiles = map.getTiles(zone);
		map.setZoneTiles(zone, tiles);
		map.editTiles(zone);
		mode.clear();
	}
}
