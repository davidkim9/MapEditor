package stormgate.action.zone;

import java.io.File;
import stormgate.action.Action;
import stormgate.data.MapZone;
import stormgate.map.Map;

/**
 *
 * @author David
 */
public class ZoneAddFootstepAction implements Action
{

	private Map map;
	private MapZone[] selected;
	private File value;

	public ZoneAddFootstepAction(Map map, MapZone[] selected, File value)
	{
		this.map = map;
		this.selected = selected;
		this.value = value;
	}

	public void undo()
	{
		for(int i = 0 ; i < selected.length ; i++){
			MapZone zone = selected[i];
			zone.footstep.remove(value);
			map.editTiles(zone);
		}
	}

	public void perform()
	{
		//Remove the zone from all tiles that it was added to
		for(int i = 0 ; i < selected.length ; i++){
			MapZone zone = selected[i];
			if(!zone.footstep.contains(value)){
				zone.footstep.add(value);
				map.editTiles(zone);
			}
		}
	}
}
