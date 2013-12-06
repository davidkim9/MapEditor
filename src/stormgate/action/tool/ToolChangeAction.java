package stormgate.action.tool;

import stormgate.action.Action;
import stormgate.editor.data.EditorData;
import stormgate.editor.tool.ToolInterface;
import stormgate.editor.ui.forms.EditorForm;
import stormgate.editor.ui.forms.tool.Toolbox;

/**
 *
 * @author David
 */
public class ToolChangeAction implements Action {

	private EditorForm form;
	private Toolbox toolbox;
	private EditorData data;
	private ToolInterface prev;
	private ToolInterface tool;

	public ToolChangeAction(EditorForm form, Toolbox toolbox, EditorData data, ToolInterface tool){
		this.form = form;
		this.toolbox = toolbox;
		this.data = data;
		this.tool = tool;
		prev = data.getTool();
	}

	public void undo()
	{
		data.selectTool(prev);
		toolbox.updateTool();
		form.updateTool();
	}

	public void perform()
	{
		data.selectTool(tool);
		toolbox.updateTool();
		form.updateTool();
	}

}
