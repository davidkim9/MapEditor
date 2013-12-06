package stormgate.action.entity;

import stormgate.action.Action;
import stormgate.data.MapEntity;
import stormgate.data.Tile;
import stormgate.editor.ui.forms.graphic.Level;
import stormgate.geom.MapPoint;
import stormgate.map.Map;

/**
 *
 * @author David
 */
public class EntityPlaceAction implements Action {

	Map map;
	MapPoint point;
	private MapEntity entity;

	public EntityPlaceAction(Map map, MapEntity entity, MapPoint point)
	{
		this.map = map;
		this.point = point;
		this.entity = entity;
    entity.setPoint(point);
	}

	public void undo()
	{
		int tileX = point.getTileX();
		int tileY = point.getTileY();
		Tile t = map.getTile(tileX, tileY);
		t.removeEntity(entity);

		map.editTile(t);

		Level l = entity.getLevel();
		if (l != null) {
			l.removeGraphic(entity);
		}
	}

	public void perform()
	{
		int tileX = point.getTileX();
		int tileY = point.getTileY();

		Tile t = map.getTile(tileX, tileY);
		t.addEntity(entity);

		map.editTile(t);
	}

}