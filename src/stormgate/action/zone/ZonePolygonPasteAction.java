package stormgate.action.zone;

import java.util.ArrayList;
import stormgate.action.Action;
import stormgate.data.MapZone;
import stormgate.data.Tile;
import stormgate.editor.tool.data.ZoneToolPolygonMode;
import stormgate.geom.MapPoint;
import stormgate.map.Map;

/**
 *
 * @author David
 */
public class ZonePolygonPasteAction implements Action
{

	private Map map;
	private MapZone[] selected;
	private MapZone[] newZones;
	private ZoneToolPolygonMode mode;
	private MapPoint displacement;

	public ZonePolygonPasteAction(ZoneToolPolygonMode mode, Map map, MapZone[] selected, MapPoint displacement)
	{
		this.mode = mode;
		this.map = map;
		this.selected = selected;
		this.displacement = displacement;

		MapZone a = selected[0];
		MapPoint p = a.getPoint(0);
		MapPoint displaceBy = new MapPoint(displacement.getX() - p.getX(), displacement.getY() - p.getY());

		newZones = new MapZone[selected.length];
		for(int i = 0 ; i < selected.length ; i++){
			MapZone zone = selected[i];
			MapZone newZone = zone.clone();
			newZone.offsetZone(displaceBy);
			newZones[i] = newZone;
		}
	}

	public void undo()
	{
		//Remove the zone from all tiles that it was added to
		for(int i = 0 ; i < newZones.length ; i++){
			MapZone zone = newZones[i];
			map.editTiles(zone);
			map.removeZone(zone);
		}
		//Deselect
		mode.deselect();
	}

	public void perform()
	{
		for(int i = 0 ; i < newZones.length ; i++){
			MapZone newZone = newZones[i];
			ArrayList<Tile> tiles = map.getTiles(newZone);
			map.setZoneTiles(newZone, tiles);
			map.editTiles(newZone);
			newZones[i] = newZone;
		}
		//Keep selected
		mode.select(newZones);
	}
}
