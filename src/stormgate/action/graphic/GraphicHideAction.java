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
public class GraphicHideAction implements Action {

	Map map;
	GraphicsTool tool;
	boolean hide;
	MapGraphic[] graphics;
	LinkedList<MapGraphic> hidden;

	public GraphicHideAction(Map map, GraphicsTool tool, boolean hide)
	{
		this.map = map;
		this.tool = tool;
		this.hide = hide;
		graphics = tool.getSelectedGraphics();
	}

	public void undo()
	{
		if(hide){
			for(int i = 0 ; i <graphics.length; i++) {
				MapGraphic g = graphics[i];

				tool.hide(g, !hide);

				MapPoint p = g.getPoint();
				int tileX = p.getTileX();
				int tileY = p.getTileY();

				Tile t = map.getTile(tileX, tileY);
				map.editTile(t);
			}

			tool.setSelectedGraphics(graphics);
		}else{
			MapGraphic g = hidden.poll();
			while(g != null){
				g.hide = true;
				tool.hide(g, hide);

				MapPoint p = g.getPoint();
				int tileX = p.getTileX();
				int tileY = p.getTileY();

				Tile t = map.getTile(tileX, tileY);
				map.editTile(t);
			}
		}
	}

	public void perform()
	{
		if(hide){
			if(graphics != null){
				for(int i = 0 ; i <graphics.length; i++) {
					MapGraphic g = graphics[i];

					tool.hide(g, hide);

					MapPoint p = g.getPoint();
					int tileX = p.getTileX();
					int tileY = p.getTileY();

					Tile t = map.getTile(tileX, tileY);
					map.editTile(t);
				}
				tool.setSelectedGraphics(new MapGraphic[0]);
			}
		}else{
			//SAVE HIDDEN
			hidden = tool.getHidden();
			tool.unhideAll();
		}
	}
}
