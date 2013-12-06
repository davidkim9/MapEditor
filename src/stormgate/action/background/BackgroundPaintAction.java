/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package stormgate.action.background;

import stormgate.action.Action;
import stormgate.data.Tile;
import stormgate.map.Map;

/**
 *
 * @author David
 */
public class BackgroundPaintAction implements Action {

	private Map map;
	private Tile[] tiles;
	private String url;
	private String[] oldGraphics;

	public BackgroundPaintAction(Map map, Tile[] tiles, String url)
	{
		this.map = map;
		this.tiles = tiles;
		this.url = url;

		oldGraphics = new String[tiles.length];

		for(int i = 0 ; i < tiles.length; i++)
		{
			Tile t = tiles[i];
			if(t != null){
				oldGraphics[i] = t.getTileBackground();
			}
			map.editTile(t);
		}
	}

	public void undo()
	{
		for(int i = 0 ; i < tiles.length; i++)
		{
			Tile t = tiles[i];
			if(t != null){
				t.setTileBackground(oldGraphics[i]);
			}
			map.editTile(t);
		}
	}

	public void perform()
	{
		for(int i = 0 ; i < tiles.length; i++)
		{
			Tile t = tiles[i];
			if(t != null){
				t.setTileBackground(url);
			}
			map.editTile(t);
		}
	}
}
