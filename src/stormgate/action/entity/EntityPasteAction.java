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
public class EntityPasteAction implements Action {

	Map map;
	MapEntity[] mapEntities;
	MapPoint point;
	MapPoint offset;
	EntityTool tool;

	public EntityPasteAction(Map map, EntityTool tool, MapEntity[] graphic, MapPoint point, MapPoint offset)
	{
		this.tool = tool;
		this.map = map;
		this.point = point;
		this.offset = offset;

		int addX = point.getX() - offset.getX();
		int addY = point.getY() - offset.getY();

		mapEntities = new MapEntity[graphic.length];
		for(int i = 0 ; i < graphic.length; i++){
			MapEntity oldGraphic = graphic[i];
			//int type = oldGraphic.getType();
			MapPoint pt = oldGraphic.getPoint();
			MapPoint newPoint = new MapPoint(pt.getX() + addX, pt.getY() + addY);
			MapEntity entity = oldGraphic.clone(); //MapEntity.makeEntity(type);
			entity.setPoint(newPoint);
			mapEntities[i] = entity;
		}

	}

	public void undo()
	{
		for(int i = 0 ; i < mapEntities.length; i++){
			MapEntity g = mapEntities[i];
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
	}

	public void perform()
	{
		for(int i = 0 ; i < mapEntities.length; i++){
			MapEntity g = mapEntities[i];
			MapPoint p = g.getPoint();

			int tileX = p.getTileX();
			int tileY = p.getTileY();

			Tile t = map.getTile(tileX, tileY);
			t.addEntity(g);

			map.editTile(t);
		}

		tool.setSelectedEntities(mapEntities);
	}

}
