package stormgate.action.graphic;

import stormgate.action.Action;
import stormgate.data.GraphicLevel;
import stormgate.data.MapGraphic;
import stormgate.data.Tile;
import stormgate.editor.tool.GraphicsTool;
import stormgate.editor.tool.panel.GraphicToolPanel;
import stormgate.editor.ui.forms.graphic.Level;
import stormgate.geom.MapPoint;
import stormgate.map.Map;

/**
 *
 * @author David
 */
public class GraphicSendToLevelAction implements Action
{
	Map map;
	GraphicToolPanel panel;
	GraphicsTool tool;
	int level;
	int oldLevel;
	MapGraphic[] graphics;

	public GraphicSendToLevelAction(Map map, GraphicToolPanel panel, GraphicsTool tool, int level)
	{
		this.map = map;
		this.panel = panel;
		this.tool = tool;
		this.level = level;
		oldLevel = tool.getLevel();
		graphics = tool.getSelectedGraphics();
	}

	public void undo()
	{
		for (int i = 0; i < graphics.length; i++) {
			MapGraphic g = graphics[i];
			MapPoint p = g.getPoint();
			int tileX = p.getTileX();
			int tileY = p.getTileY();
			
			Tile tile = map.getTile(tileX, tileY);

			GraphicLevel oldGraphicLevel = tile.getLevel(level);
			oldGraphicLevel.removeGraphic(g);
			GraphicLevel newGraphicLevel = tile.getLevel(oldLevel);
			newGraphicLevel.addGraphic(g);

			map.editTile(tile);

			Level l = g.getLevel();
			if (l != null) {
				l.removeGraphic(g);
			}
		}
		tool.setSelectedGraphics(graphics);
		panel.updatePanel();
	}

	public void perform()
	{
		if(graphics != null){
			for (int i = 0; i < graphics.length; i++) {
				MapGraphic g = graphics[i];
				MapPoint p = g.getPoint();
				int tileX = p.getTileX();
				int tileY = p.getTileY();

				Tile tile = map.getTile(tileX, tileY);

				GraphicLevel oldGraphicLevel = tile.getLevel(oldLevel);
				oldGraphicLevel.removeGraphic(g);
				GraphicLevel newGraphicLevel = tile.getLevel(level);
				newGraphicLevel.addGraphic(g);

				map.editTile(tile);

				Level l = g.getLevel();
				if (l != null) {
					l.removeGraphic(g);
				}
			}
			tool.setSelectedGraphics(new MapGraphic[0]);
		}
		panel.updatePanel();
	}
}
