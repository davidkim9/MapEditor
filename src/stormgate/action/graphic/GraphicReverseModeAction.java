package stormgate.action.graphic;

import stormgate.action.Action;
import stormgate.data.MapGraphic;
import stormgate.editor.tool.GraphicsTool;
import stormgate.editor.tool.panel.GraphicToolPanel;

/**
 *
 * @author David
 */
public class GraphicReverseModeAction implements Action
{
	GraphicsTool tool;
	boolean reverse;
	GraphicToolPanel panel;

	public GraphicReverseModeAction(GraphicToolPanel panel, GraphicsTool tool, boolean reverse)
	{
		this.panel = panel;
		this.tool = tool;
		this.reverse = reverse;
	}

	public void undo()
	{
		tool.setReverse(!reverse);
		panel.updatePanel();
	}

	public void perform()
	{
		tool.setReverse(reverse);
		panel.updatePanel();
	}
}
