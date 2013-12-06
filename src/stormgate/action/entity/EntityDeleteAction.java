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
public class EntityDeleteAction implements Action {

	Map map;
	EntityTool tool;
	MapEntity[] graphics;

	public EntityDeleteAction(Map map, EntityTool tool)
	{
		this.map = map;
		this.tool = tool;
		graphics = tool.getSelectedEntities();
	}

	public void undo()
	{
		for(int i = 0 ; i <graphics.length; i++) {
			MapEntity g = graphics[i];
			MapPoint p = g.getPoint();
			int tileX = p.getTileX();
			int tileY = p.getTileY();

			Tile t = map.getTile(tileX, tileY);
			t.addEntity(g);
			map.editTile(t);
		}

		tool.setSelectedEntities(graphics);
	}

	public void perform()
	{
		for(int i = 0 ; i <graphics.length; i++) {
			MapEntity g = graphics[i];
			MapPoint p = g.getPoint();
			int tileX = p.getTileX();
			int tileY = p.getTileY();

			Tile t = map.getTile(tileX, tileY);
			t.removeEntity(g);
			map.editTile(t);

			Level l = g.getLevel();
			if(l != null){
				l.removeGraphic(g);
			}
		}
		tool.setSelectedEntities(new MapEntity[0]);
	}


}
