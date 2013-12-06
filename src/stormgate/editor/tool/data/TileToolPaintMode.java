package stormgate.editor.tool.data;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import stormgate.action.tile.TilePaintAction;
import stormgate.data.Tile;
import stormgate.editor.tool.TileTool;
import stormgate.geom.MapPoint;

/**
 *
 * @author David
 */
public class TileToolPaintMode extends TileToolMode {

	public TileToolPaintMode(TileTool tool)
	{
		super(tool);
	}

	@Override
	public void paint(Graphics g)
	{
		//Paint all possible?
		if(mouseOverSelected()){
			SelectedTile[] selectedTiles = tool.getSelectedTiles();

			int tileZoom = tool.zoomFilter(MapPoint.tileSize);

			for(int i = 0 ; i < selectedTiles.length ; i++){
				SelectedTile t = selectedTiles[i];
				int tileX = t.x * MapPoint.tileSize;
				int tileY = t.y * MapPoint.tileSize;
				if(t.selectAll){
					//PAINT ALL
					MapPoint mouseDividePoint = new MapPoint(tileX, tileY);
					MapPoint screenPoint = data.convertToWorkspace(mouseDividePoint);
					g.fillRect(screenPoint.getX(), screenPoint.getY(), tileZoom, tileZoom);
				}else{
					int cellWidth = tileZoom / Tile.divideX;
					int cellHeight = tileZoom / Tile.divideY;

					boolean[][] selected = t.selected;

					for(int y = 0 ; y < selected.length; y++){
						for(int x = 0 ; x < selected[y].length; x++){
							if(selected[y][x]){
								//PAINT JUST THIS
								int pointX = tileX + TileTool.tileDivisorX * x;
								int pointY = tileY + TileTool.tileDivisorY * y;
								MapPoint mouseDividePoint = new MapPoint(pointX, pointY);
								MapPoint screenPoint = data.convertToWorkspace(mouseDividePoint);
								g.fillRect(screenPoint.getX(), screenPoint.getY(), cellWidth, cellHeight);
							}
						}
					}
				}
			}
		}

	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		if(mouseOverSelected()){
			SelectedTile[] selectedTiles = tool.getSelectedTiles();
			String graphic = tool.getSelected();System.out.println(graphic);
			TilePaintAction p = new TilePaintAction(data.getMap(), selectedTiles, graphic);
			data.performAction(p);
		}
	}

	public boolean mouseOverSelected() {
		MapPoint p = data.convertToMap(mouseX, mouseY);

		//Long Conversion
		int tileX = (int) Math.floor(p.getX() * 1f / MapPoint.tileSize);
		int tileY = (int) Math.floor(p.getY() * 1f / MapPoint.tileSize);

		int mouseLocalX = p.getLocalX();
		int mouseLocalY = p.getLocalY();
		mouseLocalX = mouseLocalX >= 0 ? mouseLocalX : MapPoint.tileSize + mouseLocalX;
		mouseLocalY = mouseLocalY >= 0 ? mouseLocalY : MapPoint.tileSize + mouseLocalY;

		int localX = (mouseLocalX / TileTool.tileDivisorX);
		int localY = (mouseLocalY / TileTool.tileDivisorY);

		SelectedTile[] selectedTiles = tool.getSelectedTiles();

		for(int i = 0 ; i < selectedTiles.length ; i++){
			SelectedTile t = selectedTiles[i];
			if(t.x == tileX && t.y == tileY) {
				if(t.selectAll){
					return true;
				}else{
					//Check inner
					boolean[][] selected = t.selected;
					for(int y = 0 ; y < selected.length; y++){
						for(int x = 0 ; x < selected[y].length; x++){
							if(selected[y][x] && localX == x && localY == y){
								return true;
							}
						}
					}
				}
			}
		}

		return false;
	}
}
