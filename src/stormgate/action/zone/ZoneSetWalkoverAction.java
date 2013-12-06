package stormgate.action.zone;

import java.io.File;
import stormgate.action.Action;
import stormgate.data.MapZone;
import stormgate.map.Map;

/**
 *
 * @author David
 */
public class ZoneSetWalkoverAction implements Action
{

	private Map map;
	private MapZone[] selected;
	private File[] old;
	private File value;

	public ZoneSetWalkoverAction(Map map, MapZone[] selected, File value)
	{
		this.map = map;
		this.selected = selected;
		this.value = value;

		old = new File[selected.length];

		for(int i = 0 ; i < selected.length; i++){
			MapZone select = selected[i];
			if(select != null){
				old[i] = select.walkover;
			}
		}
	}

	public void undo()
	{
		for(int i = 0 ; i < selected.length ; i++){
			MapZone zone = selected[i];
			zone.walkover = old[i];
			map.editTiles(zone);
		}
	}

	public void perform()
	{
		//Remove the zone from all tiles that it was added to
		for(int i = 0 ; i < selected.length ; i++){
			MapZone zone = selected[i];
			zone.walkover = value;
			map.editTiles(zone);
		}
	}
}
