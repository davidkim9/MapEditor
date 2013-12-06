package stormgate.action.tile;

import stormgate.action.Action;
import stormgate.data.Tile;
import stormgate.editor.tool.data.SelectedTile;
import stormgate.map.Map;

/**
 *
 * @author David
 */
public class TileEnableAction implements Action
{

	private Map map;
	private SelectedTile[] selection;
	private boolean[] oldEnable;
	private boolean enabled;

	public TileEnableAction(Map map, SelectedTile[] selection, boolean enabled)
	{
		this.map = map;
		this.selection = selection;
		this.enabled = enabled;

		oldEnable = new boolean[selection.length];

		for(int i = 0 ; i < selection.length; i++){
			SelectedTile select = selection[i];
			Tile tile = map.getTile(select.x, select.y);
			if(tile != null){
				oldEnable[i] = tile.getEnabled();
			}
		}
	}

	public final void perform()
	{
		for(int i = 0 ; i < selection.length; i++){
			SelectedTile select = selection[i];
			Tile tile = map.getTile(select.x, select.y);
			if(tile != null){
				tile.setEnabled(enabled);
				map.editTile(tile);
			}
		}
	}

	public void undo()
	{
		for(int i = 0 ; i < selection.length; i++){
			SelectedTile select = selection[i];
			Tile tile = map.getTile(select.x, select.y);
			if(tile != null){
				tile.setEnabled(oldEnable[i]);
				map.editTile(tile);
			}
		}
	}
}
