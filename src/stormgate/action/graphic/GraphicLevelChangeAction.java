package stormgate.action.graphic;

import stormgate.action.Action;
import stormgate.data.MapGraphic;
import stormgate.editor.tool.GraphicsTool;
import stormgate.editor.tool.panel.GraphicToolPanel;

/**
 *
 * @author David
 */
public class GraphicLevelChangeAction implements Action
{

	GraphicsTool tool;
	int level;
	int oldLevel;
	MapGraphic[] oldSelected;
	GraphicToolPanel panel;

	public GraphicLevelChangeAction(GraphicToolPanel panel, GraphicsTool tool, int level)
	{
		this.panel = panel;
		this.tool = tool;
		this.level = level;
		oldSelected = tool.getSelectedGraphics();
	}

	public void undo()
	{
		tool.setLevel(oldLevel);
		panel.updatePanel();
	}

	public void perform()
	{
		oldLevel = tool.getLevel();
		tool.setLevel(level);
		if (oldLevel != level) {
			tool.setSelectedGraphics(new MapGraphic[0]);
		}
		panel.updatePanel();
	}
}
