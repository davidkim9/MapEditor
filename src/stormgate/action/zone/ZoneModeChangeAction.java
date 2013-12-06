package stormgate.action.zone;

import stormgate.action.Action;
import stormgate.editor.tool.ZoneTool;
import stormgate.editor.tool.data.ZoneToolMode;
import stormgate.editor.ui.forms.EditorForm;

/**
 *
 * @author David
 */
public class ZoneModeChangeAction implements Action {

	private EditorForm form;
	private ZoneTool tool;
	private ZoneToolMode mode;
	private ZoneToolMode oldMode;

	public ZoneModeChangeAction(EditorForm form, ZoneTool tool, ZoneToolMode mode){

		this.form = form;
		this.tool = tool;
		this.mode = mode;
		
		oldMode = tool.getMode();
	}
	public void undo()
	{
		tool.setMode(oldMode);
		form.updateTool();
	}

	public void perform()
	{
		tool.setMode(mode);
		form.updateTool();
	}
}
