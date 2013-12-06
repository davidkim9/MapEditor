/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stormgate.action.tile;

import stormgate.action.Action;
import stormgate.data.Tile;
import stormgate.editor.tool.data.SelectedTile;
import stormgate.map.Map;

/**
 *
 * @author keviixp
 */
public class TileDeleteAction implements Action
{

	private Map map;
	private SelectedTile[] delete;
	private String[][][] oldGraphics;

	public TileDeleteAction(Map map, SelectedTile[] delete)
	{
		this.map = map;
		this.delete = delete;

		oldGraphics = new String[delete.length][Tile.divideY][Tile.divideX];
	}

	public final void perform()
	{
		for (int i = 0; i < delete.length; i++) {

			SelectedTile selected = delete[i];

			Tile tile = map.getTile(selected.x, selected.y);
			if (tile != null) {
				//Store old graphic information
				oldGraphics[i] = copyArray(tile.getTileGraphics());

				if (selected.selectAll) {
					//Whole tile is selected
					tile.setTileGraphics(new String[Tile.divideY][Tile.divideX]);
				} else {
					boolean[][] tileCells = selected.selected;
					for (int j = 0; j < tileCells.length; j++) {
						for (int k = 0; k < tileCells[j].length; k++) {
							boolean selectedValue = tileCells[j][k];
							if (selectedValue) {
								tile.setTileGraphic(k, j, null);
							}
						}
					}
				}

				map.editTile(tile);
			}
		}
	}

	public void undo()
	{
		for (int i = 0; i < delete.length; i++) {
			SelectedTile selected = delete[i];

			Tile tile = map.getTile(selected.x, selected.y);

			tile.setTileGraphics(oldGraphics[i]);

			map.editTile(tile);
		}
	}

	private String[][] copyArray(String[][] selected)
	{
		if (selected != null) {
			String[][] copy = new String[selected.length][selected[0].length];

			for (int i = 0; i < selected.length; i++) {
				for (int j = 0; j < selected[i].length; j++) {
					copy[i][j] = selected[i][j];
				}
			}

			return copy;
		}
		return null;
	}
}
