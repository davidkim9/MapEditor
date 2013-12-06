/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package stormgate.action.graphic;

import stormgate.action.Action;
import stormgate.data.GraphicLevel;
import stormgate.data.MapGraphic;
import stormgate.data.Tile;
import stormgate.editor.tool.GraphicsTool;
import stormgate.editor.ui.forms.graphic.Level;
import stormgate.geom.MapPoint;
import stormgate.map.Map;

/**
 *
 * @author David
 */
public class GraphicDeleteAction implements Action {

	Map map;
	GraphicsTool tool;
	int level;
	MapGraphic[] graphics;

	public GraphicDeleteAction(Map map, GraphicsTool tool, int level)
	{
		this.map = map;
		this.level = level;
		this.tool = tool;
		graphics = tool.getSelectedGraphics();
	}

	public void undo()
	{
		for(int i = 0 ; i <graphics.length; i++) {
			MapGraphic g = graphics[i];
			MapPoint p = g.getPoint();
			int tileX = p.getTileX();
			int tileY = p.getTileY();

			Tile t = map.getTile(tileX, tileY);
			GraphicLevel graphicLevel = t.getLevel(level);
			graphicLevel.addGraphic(g);
			map.editTile(t);
		}

		tool.setSelectedGraphics(graphics);
	}

	public void perform()
	{
		for(int i = 0 ; i <graphics.length; i++) {
			MapGraphic g = graphics[i];
			MapPoint p = g.getPoint();
			int tileX = p.getTileX();
			int tileY = p.getTileY();

			Tile t = map.getTile(tileX, tileY);
			GraphicLevel graphicLevel = t.getLevel(level);
			graphicLevel.removeGraphic(g);
			map.editTile(t);

			Level l = g.getLevel();
			if(l != null){
				l.removeGraphic(g);
			}
		}
		tool.setSelectedGraphics(new MapGraphic[0]);
	}


}
