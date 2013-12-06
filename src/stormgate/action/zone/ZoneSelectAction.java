package stormgate.action.zone;

import stormgate.action.Action;
import stormgate.editor.tool.data.SelectedZone;
import stormgate.editor.tool.data.ZoneToolSelectMode;

/**
 *
 * @author David
 */
public class ZoneSelectAction implements Action {

	private SelectedZone[] selected;
	private SelectedZone[] oldSelected;

	private ZoneToolSelectMode mode;

	public ZoneSelectAction(ZoneToolSelectMode mode, SelectedZone[] selected)
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
