/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package stormgate.action.background;

import stormgate.action.Action;
import stormgate.data.Tile;
import stormgate.editor.tool.BackgroundTool;

/**
 *
 * @author David
 */
public class BackgroundSelectAction implements Action
{
	private BackgroundTool tool;
	private Tile[] oldSelected;
	private Tile[] selected;

	public BackgroundSelectAction(BackgroundTool tool, Tile[] selected)
	{
		this.tool = tool;
		this.selected = selected;

		//Keep a copy
		oldSelected = tool.getSelectedTiles();
	}

	public void undo()
	{
		tool.setSelected(oldSelected);
	}

	public void perform()
	{
		tool.setSelected(selected);
	}
}
