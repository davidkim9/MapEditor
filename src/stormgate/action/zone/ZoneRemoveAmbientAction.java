package stormgate.action.zone;

import java.io.File;
import java.util.ArrayList;
import stormgate.action.Action;
import stormgate.data.MapZone;
import stormgate.map.Map;

/**
 *
 * @author David
 */
public class ZoneRemoveAmbientAction implements Action
{

	private Map map;
	private MapZone[] selected;
	private File[] value;
	private Object[] old;

	public ZoneRemoveAmbientAction(Map map, MapZone[] selected, File[] value)
	{
		this.map = map;
		this.selected = selected;
		this.value = value;

		//MAKE SURE THE TILES YOU REMOVE FROM IS SAVED

		old = new Object[selected.length];

		for(int i = 0 ; i < selected.length; i++){
			MapZone select = selected[i];
			if(select != null){
				old[i] = select.ambient.clone();
			}
		}
	}

	public void undo()
	{
		for(int i = 0 ; i < selected.length ; i++){
			MapZone zone = selected[i];
			zone.ambient = (ArrayList<File>) old[i];
			map.editTiles(zone);
		}
	}

	public void perform()
	{
		for(int i = 0 ; i < selected.length ; i++){
			MapZone zone = selected[i];
			for(int j = 0 ; j < value.length ; j++){
				File f = value[j];
				zone.ambient.remove(f);
			}
			map.editTiles(zone);
		}
	}
}