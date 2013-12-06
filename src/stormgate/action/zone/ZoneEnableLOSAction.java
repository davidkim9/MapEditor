package stormgate.action.zone;

import stormgate.action.Action;
import stormgate.data.MapZone;
import stormgate.map.Map;

/**
 *
 * @author David
 */
public class ZoneEnableLOSAction implements Action
{

	private Map map;
	private MapZone[] selected;
	private boolean[] oldEnable;
	private boolean value;

	public ZoneEnableLOSAction(Map map, MapZone[] selected, boolean value)
	{
		this.map = map;
		this.selected = selected;
		this.value = value;

		oldEnable = new boolean[selected.length];

		for(int i = 0 ; i < selected.length; i++){
			MapZone select = selected[i];
			if(select != null){
				oldEnable[i] = select.los;
			}
		}
	}

	public void undo()
	{
		for(int i = 0 ; i < selected.length ; i++){
			MapZone zone = selected[i];
			zone.los = oldEnable[i];
			map.editTiles(zone);
		}
	}

	public void perform()
	{
		//Remove the zone from all tiles that it was added to
		for(int i = 0 ; i < selected.length ; i++){
			MapZone zone = selected[i];
			zone.los = value;
			map.editTiles(zone);
		}
	}
}
