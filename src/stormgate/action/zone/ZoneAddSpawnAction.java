package stormgate.action.zone;

import stormgate.action.Action;
import stormgate.data.MapZone;
import stormgate.data.SpawnData;
import stormgate.map.Map;

/**
 *
 * @author David
 */
public class ZoneAddSpawnAction implements Action
{

	private Map map;
	private MapZone[] selected;
	private SpawnData value;

	public ZoneAddSpawnAction(Map map, MapZone[] selected, SpawnData value)
	{
		this.map = map;
		this.selected = selected;
		this.value = value;
	}

	public void undo()
	{
		for(int i = 0 ; i < selected.length ; i++){
			MapZone zone = selected[i];
			zone.spawn.remove(value);
			map.editTiles(zone);
		}
	}

	public void perform()
	{
		//Remove the zone from all tiles that it was added to
		for(int i = 0 ; i < selected.length ; i++){
			MapZone zone = selected[i];
			if(!zone.spawn.contains(value)){
				zone.spawn.add(value);
				map.editTiles(zone);
			}
		}
	}
}
