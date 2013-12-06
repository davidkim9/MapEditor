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
public class GraphicPasteAction implements Action {

	Map map;
	int level;
	MapGraphic[] mapGraphic;
	MapPoint point;
	MapPoint offset;
	GraphicsTool tool;

	public GraphicPasteAction(Map map, GraphicsTool tool, int level, MapGraphic[] graphic, MapPoint point, MapPoint offset)
	{
		this.tool = tool;
		this.map = map;
		this.level = level;
		this.point = point;
		this.offset = offset;

		int addX = point.getX() - offset.getX();
		int addY = point.getY() - offset.getY();

		mapGraphic = new MapGraphic[graphic.length];
		for(int i = 0 ; i < graphic.length; i++){
			MapGraphic oldGraphic = graphic[i];
			String url = oldGraphic.getURL();
			MapPoint pt = oldGraphic.getPoint();
			MapPoint newPoint = new MapPoint(pt.getX() + addX, pt.getY() + addY);
			mapGraphic[i] = new MapGraphic(url, newPoint);
                        mapGraphic[i].reverse = oldGraphic.reverse;
		}
	}

	public void undo()
	{
		for(int i = 0 ; i < mapGraphic.length; i++){
			MapGraphic g = mapGraphic[i];
			MapPoint p = g.getPoint();

			int tileX = p.getTileX();
			int tileY = p.getTileY();

			Tile t = map.getTile(tileX, tileY);
			GraphicLevel graphicLevel = t.getLevel(level);

			graphicLevel.removeGraphic(g);
			Level l = g.getLevel();
			if(l != null){
				l.removeGraphic(g);
			}
			map.editTile(t);
		}
	}

	public void perform()
	{
		for(int i = 0 ; i < mapGraphic.length; i++){
			MapGraphic g = mapGraphic[i];
			MapPoint p = g.getPoint();

			int tileX = p.getTileX();
			int tileY = p.getTileY();

			Tile t = map.getTile(tileX, tileY);
			GraphicLevel graphicLevel = t.getLevel(level);

			graphicLevel.addGraphic(g);

			map.editTile(t);
		}

		tool.setSelectedGraphics(mapGraphic);
	}

}
