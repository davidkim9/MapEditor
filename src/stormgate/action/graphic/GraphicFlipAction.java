package stormgate.action.graphic;

import java.util.LinkedList;
import stormgate.action.Action;
import stormgate.data.MapGraphic;
import stormgate.data.Tile;
import stormgate.editor.tool.GraphicsTool;
import stormgate.geom.MapPoint;
import stormgate.map.Map;

/**
 *
 * @author David
 */
public class GraphicFlipAction implements Action
{

	Map map;
	GraphicsTool tool;
	MapGraphic[] graphics;
	LinkedList<MapGraphic> flipped;

	public GraphicFlipAction(Map map, GraphicsTool tool)
	{
		this.map = map;
		this.tool = tool;
		graphics = tool.getSelectedGraphics();
	}

	public void undo()
	{
		for (int i = 0; i < graphics.length; i++) {
			MapGraphic g = graphics[i];

			g.reverse = !g.reverse;

			MapPoint p = g.getPoint();
			int tileX = p.getTileX();
			int tileY = p.getTileY();

			Tile t = map.getTile(tileX, tileY);
			map.editTile(t);
		}
	}

	public void perform()
	{
		if (graphics != null) {
			for (int i = 0; i < graphics.length; i++) {
				MapGraphic g = graphics[i];
				g.reverse = !g.reverse;

				MapPoint p = g.getPoint();
				int tileX = p.getTileX();
				int tileY = p.getTileY();

				Tile t = map.getTile(tileX, tileY);
				map.editTile(t);
			}
		}
	}
}
