package stormgate.action.zone;

import java.util.ArrayList;
import stormgate.action.Action;
import stormgate.data.MapZone;
import stormgate.data.SpawnData;
import stormgate.map.Map;

/**
 *
 * @author David
 */
public class ZoneRemoveSpawnAction implements Action
{

	private Map map;
	private MapZone[] selected;
	private SpawnData[] value;
	private Object[] old;

	public ZoneRemoveSpawnAction(Map map, MapZone[] selected, SpawnData[] value)
	{
		this.map = map;
		this.selected = selected;
		this.value = value;

		//MAKE SURE THE TILES YOU REMOVE FROM IS SAVED

		old = new Object[selected.length];

		for(int i = 0 ; i < selected.length; i++){
			MapZone select = selected[i];
			if(select != null){
				old[i] = select.spawn.clone();
			}
		}
	}

	public void undo()
	{
		for(int i = 0 ; i < selected.length ; i++){
			MapZone zone = selected[i];
			zone.spawn = (ArrayList<SpawnData>) old[i];
			map.editTiles(zone);
		}
	}

	public void perform()
	{
		for(int i = 0 ; i < selected.length ; i++){
			MapZone zone = selected[i];
			for(int j = 0 ; j < value.length ; j++){
				SpawnData f = value[j];
				zone.spawn.remove(f);
			}
			map.editTiles(zone);
		}
	}
}