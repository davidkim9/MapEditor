package stormgate.action.zone;

import stormgate.action.Action;
import stormgate.data.MapZone;
import stormgate.editor.tool.data.ZoneToolNewMode;

/**
 *
 * @author David
 */
public class ZoneNewClearAction implements Action {

	private ZoneToolNewMode mode;
	private MapZone zone;

	public ZoneNewClearAction(ZoneToolNewMode mode, MapZone zone)
	{
		this.mode = mode;
		this.zone = zone;
	}

	public void undo()
	{
		mode.setEdit(zone);
	}

	public void perform()
	{
		mode.clear();
	}
}
