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
 * @author David
 */
public class TilePaintAction implements Action
{

	private Map map;
	private SelectedTile[] paint;
	private String url;
	private String[][][] oldGraphics;

	public TilePaintAction(Map map, SelectedTile[] paint, String url)
	{
		this.map = map;
		this.paint = paint;
		this.url = url;

		oldGraphics = new String[paint.length][Tile.divideY][Tile.divideX];
	}

	public final void perform()
	{


		for (int i = 0; i < paint.length; i++) {

			SelectedTile selected = paint[i];

			Tile tile = map.getTile(selected.x, selected.y);
			if (tile != null) {
				//Store old graphic information
				oldGraphics[i] = copyArray(tile.getTileGraphics());

				if (selected.selectAll) {
					//Whole tile is selected
					String[][] fullTile = new String[Tile.divideY][Tile.divideX];
					for (int j = 0; j < fullTile.length; j++) {
						for (int k = 0; k < fullTile[j].length; k++) {
							fullTile[j][k] = url;
						}
					}
					tile.setTileGraphics(fullTile);
				} else {
					boolean[][] tileCells = selected.selected;
					for (int j = 0; j < tileCells.length; j++) {
						for (int k = 0; k < tileCells[j].length; k++) {
							boolean selectedValue = tileCells[j][k];
							if (selectedValue) {
								tile.setTileGraphic(k, j, url);
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
		for (int i = 0; i < paint.length; i++) {
			SelectedTile selected = paint[i];

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
				System.arraycopy(selected[i], 0, copy[i], 0, selected[i].length);
			}

			return copy;
		}
		return null;
	}
}
