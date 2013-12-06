/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stormgate.action.tile;

import stormgate.action.Action;
import stormgate.editor.tool.TileTool;
import stormgate.editor.tool.data.SelectedTile;

/**
 *
 * @author keviixp
 */
public class TileSelectAction implements Action
{

	private TileTool tool;
	private SelectedTile[] oldSelected;
	private SelectedTile[] selected;

	public TileSelectAction(TileTool tool, SelectedTile[] selected)
	{
		this.tool = tool;
		this.selected = selected;

		//Keep a copy
		oldSelected = copyArray(tool.getSelectedTiles());
	}

	public void undo()
	{
		tool.setSelected(oldSelected);
	}

	public void perform()
	{
		tool.setSelected(selected);
	}

	private SelectedTile[] copyArray(SelectedTile[] arr)
	{
		if (arr != null) {
			SelectedTile[] copy = new SelectedTile[arr.length];

			for (int i = 0; i < arr.length; i++) {
				copy[i] = arr[i].clone();
			}

			return copy;
		}

		return null;
	}
}
