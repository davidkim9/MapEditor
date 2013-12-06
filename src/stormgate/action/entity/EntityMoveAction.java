package stormgate.action.entity;

import stormgate.action.Action;
import stormgate.data.MapEntity;
import stormgate.data.Tile;
import stormgate.editor.tool.EntityTool;
import stormgate.editor.ui.forms.graphic.Level;
import stormgate.geom.MapPoint;
import stormgate.map.Map;

/**
 *
 * @author David
 */
public class EntityMoveAction implements Action
{

	Map map;
	EntityTool tool;
	MapPoint displacement;
	MapEntity[] graphics;

	public EntityMoveAction(Map map, EntityTool tool, MapPoint displacement)
	{
		this.map = map;
		this.tool = tool;
		this.displacement = displacement;
		graphics = tool.getSelectedEntities();
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
			MapEntity g = graphics[i];
			MapPoint p = g.getPoint();

			int newX = p.getX() + x;
			int newY = p.getY() + y;

			MapPoint newPoint = new MapPoint(newX, newY);

			int oldTileX = p.getTileX();
			int oldTileY = p.getTileY();
			int newTileX = newPoint.getTileX();
			int newTileY = newPoint.getTileY();

			Tile oldTile = map.getTile(oldTileX, oldTileY);
			oldTile.removeEntity(g);
			map.editTile(oldTile);

			g.setPoint(newPoint);

			Tile newTile = map.getTile(newTileX, newTileY);
			newTile.addEntity(g);
			map.editTile(newTile);

			Level l = g.getLevel();
			if(l != null){
				l.removeGraphic(g);
			}
		}
	}
}
