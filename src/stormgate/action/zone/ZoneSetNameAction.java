package stormgate.action.zone;

import stormgate.action.Action;
import stormgate.data.MapZone;
import stormgate.map.Map;

/**
 *
 * @author David
 */
public class ZoneSetNameAction implements Action
{

	private Map map;
	private MapZone[] selected;
	private String[] old;
	private String value;

	public ZoneSetNameAction(Map map, MapZone[] selected, String value)
	{
		this.map = map;
		this.selected = selected;
		this.value = value;

		old = new String[selected.length];

		for(int i = 0 ; i < selected.length; i++){
			MapZone select = selected[i];
			if(select != null){
				old[i] = select.name;
			}
		}
	}

	public void undo()
	{
		for(int i = 0 ; i < selected.length ; i++) {
			MapZone zone = selected[i];
			zone.name = old[i];
			map.editTiles(zone);
		}
	}

	public void perform()
	{
		//Remove the zone from all tiles that it was added to
		for(int i = 0 ; i < selected.length ; i++) {
			MapZone zone = selected[i];
			zone.name = value;
			map.editTiles(zone);
		}
	}
}
