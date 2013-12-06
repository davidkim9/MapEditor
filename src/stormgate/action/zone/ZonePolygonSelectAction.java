package stormgate.action.zone;

import stormgate.action.Action;
import stormgate.data.MapZone;
import stormgate.editor.tool.data.ZoneToolPolygonMode;

/**
 *
 * @author David
 */
public class ZonePolygonSelectAction implements Action {

	private MapZone[] selected;
	private MapZone[] oldSelected;

	private ZoneToolPolygonMode mode;

	public ZonePolygonSelectAction(ZoneToolPolygonMode mode, MapZone[] selected)
	{
		this.mode = mode;
		this.selected = selected;
		this.oldSelected = mode.getSelected();
	}

	public void undo()
	{
		mode.select(oldSelected);
	}

	public void perform()
	{
		mode.select(selected);
	}

}
