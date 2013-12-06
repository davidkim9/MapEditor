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
public class GraphicMoveAction implements Action
{

	Map map;
	GraphicsTool tool;
	int level;
	MapPoint displacement;
	MapGraphic[] graphics;

	public GraphicMoveAction(Map map, GraphicsTool tool, int level, MapPoint displacement)
	{
		this.map = map;
		this.level = level;
		this.tool = tool;
		this.displacement = displacement;
		graphics = tool.getSelectedGraphics();
	}

	public void undo()
	{
		move(-displacement.getX(), -displacement.getY());
	}

	public void perform()
	{
		move(displacement.getX(), displacement.getY());
	}
	
	public void move(int x, int y) {
		for (int i = 0; i < graphics.length; i++) {
			MapGraphic g = graphics[i];
			MapPoint p = g.getPoint();

			int newX = p.getX() + x;
			int newY = p.getY() + y;

			MapPoint newPoint = new MapPoint(newX, newY);

			int oldTileX = p.getTileX();
			int oldTileY = p.getTileY();
			int newTileX = newPoint.getTileX();
			int newTileY = newPoint.getTileY();

			Tile oldTile = map.getTile(oldTileX, oldTileY);
			GraphicLevel oldGraphicLevel = oldTile.getLevel(level);
			oldGraphicLevel.removeGraphic(g);
			map.editTile(oldTile);

			g.setPoint(newPoint);

			Tile newTile = map.getTile(newTileX, newTileY);
			GraphicLevel newGraphicLevel = newTile.getLevel(level);
			newGraphicLevel.addGraphic(g);
			map.editTile(newTile);

			Level l = g.getLevel();
			if (l != null) {
				l.removeGraphic(g);
			}
		}
	}
}
