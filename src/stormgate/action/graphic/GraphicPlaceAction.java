package stormgate.action.graphic;

import stormgate.action.Action;
import stormgate.data.GraphicLevel;
import stormgate.data.MapGraphic;
import stormgate.data.Tile;
import stormgate.editor.ui.forms.graphic.Level;
import stormgate.geom.MapPoint;
import stormgate.map.Map;

/**
 *
 * @author David
 */
public class GraphicPlaceAction implements Action
{

  Map map;
  int level;
  MapPoint point;
  String graphic;
  private MapGraphic mapGraphic;

  public GraphicPlaceAction(Map map, int level, String graphic, MapPoint point, boolean reverse)
  {
	this.map = map;
	this.level = level;
	this.graphic = graphic;
	this.point = point;

	mapGraphic = new MapGraphic(graphic, point);
	mapGraphic.reverse = reverse;
  }

  public void undo()
  {
	int tileX = point.getTileX();
	int tileY = point.getTileY();
	Tile t = map.getTile(tileX, tileY);
	GraphicLevel graphicLevel = t.getLevel(level);
	//graphicLevel.removeGraphic(mapGraphic);

	graphicLevel.removeGraphic(mapGraphic);
	Level l = mapGraphic.getLevel();
	if (l != null) {
	  l.removeGraphic(mapGraphic);
	}

	map.editTile(t);
  }

  public void perform()
  {
	int tileX = point.getTileX();
	int tileY = point.getTileY();

	Tile t = map.getTile(tileX, tileY);
	GraphicLevel graphicLevel = t.getLevel(level);
	graphicLevel.addGraphic(mapGraphic);

	map.editTile(t);
  }
}