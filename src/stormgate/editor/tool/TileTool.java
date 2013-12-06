/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stormgate.editor.tool;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import stormgate.action.tile.TileDeleteAction;
import stormgate.action.tile.TilePaintAction;
import stormgate.data.Tile;
import stormgate.editor.data.EditorData;
import stormgate.editor.tool.data.SelectedTile;
import stormgate.editor.tool.data.TileToolMode;
import stormgate.editor.tool.data.TileToolSelectMode;
import stormgate.editor.tool.panel.TileToolPanel;
import stormgate.editor.tool.panel.ToolPanel;
import stormgate.geom.MapPoint;

/**
 *
 * @author David
 */
public class TileTool extends Tool
{

	int mouseX = 0;
	int mouseY = 0;
	public static int tileDivisorX = MapPoint.tileSize / Tile.divideX;
	public static int tileDivisorY = MapPoint.tileSize / Tile.divideY;
	private SelectedTile[] selectedTiles;
	private TileToolMode mode;
	private TileToolPanel panel;

	public TileTool(EditorData data)
	{
		super(data);

		mode = new TileToolSelectMode(this);
		mode.setData(data);

		panel = new TileToolPanel();
		panel.setData(data);
		panel.setTool(this);
	}

	@Override
	public void selectGraphic(String resource)
	{
		selected = resource;
		if (resource != null) {
			panel.setGraphic(data.getManager().getResource(resource));
		}
	}

	public void setSelected(SelectedTile[] selectedTiles)
	{
		this.selectedTiles = selectedTiles;
		panel.updatePanel();
	}

	@Override
	public ToolPanel getPanel()
	{
		return panel;
	}

	public SelectedTile[] getSelectedTiles()
	{
		return selectedTiles;
	}

	public String getSelected()
	{
		return selected;
	}

	public int zoomFilter(float a)
	{
		return Math.round(a * filter.getZoom());
	}

	@Override
	public void paint(Graphics g)
	{
		int tileZoom = zoomFilter(MapPoint.tileSize);

		//Show Selected
		if (selectedTiles != null) {
			g.setColor(new Color(50, 50, 50, 100));

			for (int i = 0; i < selectedTiles.length; i++) {
				SelectedTile tile = selectedTiles[i];

				int tileX = tile.x * MapPoint.tileSize;
				int tileY = tile.y * MapPoint.tileSize;

				if (tile.selectAll) {

					MapPoint mouseDividePoint = new MapPoint(tileX, tileY);
					MapPoint screenPoint = data.convertToWorkspace(mouseDividePoint);
					g.fillRect(screenPoint.getX(), screenPoint.getY(), tileZoom, tileZoom);

				} else {
					boolean[][] selectedCells = tile.selected;
					for (int y = 0; y < selectedCells.length; y++) {
						for (int x = 0; x < selectedCells[y].length; x++) {
							if (selectedCells[y][x]) {
								int pointX = tileX + tileDivisorX * x;
								int pointY = tileY + tileDivisorY * y;
								MapPoint mouseDividePoint = new MapPoint(pointX, pointY);
								MapPoint screenPoint = data.convertToWorkspace(mouseDividePoint);
								g.fillRect(screenPoint.getX(), screenPoint.getY(), tileZoom / Tile.divideX, tileZoom / Tile.divideY);
							}
						}
					}

				}
			}
		}

		if (!data.pan) {
			//Converts the mouse to tile point in the back and converts it to screen points after

			MapPoint p = data.convertToMap(mouseX, mouseY);

			//Long Conversion
			//The floor is there for negative values since the bounding box for -1,-1 should be from -2,-2 to -1,-1 and not -1,-1 to 0,0
			int tileRelativeX = (int) Math.floor(p.getX() * 1f / MapPoint.tileSize) * MapPoint.tileSize;
			int tileRelativeY = (int) Math.floor(p.getY() * 1f / MapPoint.tileSize) * MapPoint.tileSize;

			MapPoint mouseTilePoint = new MapPoint(tileRelativeX, tileRelativeY);

			MapPoint screenPoint = data.convertToWorkspace(mouseTilePoint);

			//Draws the outline of the target tile
			g.setColor(Color.BLUE);
			g.drawRect(screenPoint.getX(), screenPoint.getY(), tileZoom, tileZoom);

			int mouseLocalX = p.getLocalX();
			int mouseLocalY = p.getLocalY();

			mouseLocalX = mouseLocalX >= 0 ? mouseLocalX : MapPoint.tileSize + mouseLocalX;
			mouseLocalY = mouseLocalY >= 0 ? mouseLocalY : MapPoint.tileSize + mouseLocalY;

			int localX = (mouseLocalX / tileDivisorX) * tileDivisorX;
			int localY = (mouseLocalY / tileDivisorY) * tileDivisorY;

			MapPoint mouseDividePoint = new MapPoint(localX + mouseTilePoint.getX(), localY + mouseTilePoint.getY());
			MapPoint screenDividePoint = data.convertToWorkspace(mouseDividePoint);

			g.setColor(Color.GREEN);
			g.drawRect(screenDividePoint.getX(), screenDividePoint.getY(), tileZoom / Tile.divideX, tileZoom / Tile.divideY);
		}

		if (mode != null) {
			mode.paint(g);
		}
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		if (mode != null) {
			mode.mousePressed(e);
		}
		data.refresh();
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		if (mode != null) {
			mode.mouseReleased(e);
		}
		data.refresh();
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		mouseMoved(e);
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		mouseX = e.getX();
		mouseY = e.getY();

		if (mode != null) {
			mode.mouseMoved(e);
		}

		data.refresh();
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		//Delete Key
		if (e.getKeyCode() == 127) {
			//DELETE
			TileDeleteAction d = new TileDeleteAction(data.getMap(), selectedTiles);
			data.performAction(d);
		}
		/*
		if (e.getKeyCode() == 65) {
		mode = new TileToolSelectMode(this);
		mode.setData(data);
		}
		if (e.getKeyCode() == 66) {
		mode = new TileToolPaintMode(this);
		mode.setData(data);
		System.out.println("SET TO PAINT!");
		}
		 */
	}

	public void doubleClickGraphic(String resource)
	{
		//PAINT ACTION!
		String graphic = getSelected();
		if(selectedTiles != null){
			TilePaintAction p = new TilePaintAction(data.getMap(), selectedTiles, graphic);
			data.performAction(p);
		}
	}

	public void cut()
	{
	}

	public void copy()
	{
	}

	public void paste()
	{
	}

	public void update()
	{
		panel.updatePanel();
	}

	@Override
	public void deselect()
	{
		selectedTiles = null;
		panel.setGraphic(null);
	}
}
