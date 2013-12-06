/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package stormgate.action.graphic;

import stormgate.action.Action;
import stormgate.data.MapGraphic;
import stormgate.editor.tool.GraphicsTool;

/**
 *
 * @author David
 */
public class GraphicSelectAction implements Action {

	GraphicsTool tool;
	MapGraphic[] oldSelected;
	MapGraphic[] selected;

	public GraphicSelectAction(GraphicsTool tool, MapGraphic[] selected)
	{
		this.tool = tool;
		this.selected = selected;

		oldSelected = tool.getSelectedGraphics();
	}

	public void undo()
	{
		tool.setSelectedGraphics(oldSelected);
	}

	public void perform()
	{
		tool.setSelectedGraphics(selected);
	}
}
