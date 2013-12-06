package stormgate.editor.tool;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import stormgate.action.background.BackgroundPaintAction;
import stormgate.action.background.BackgroundSelectAction;
import stormgate.data.Tile;
import stormgate.editor.data.EditorData;
import stormgate.editor.tool.panel.BackgroundToolPanel;
import stormgate.editor.tool.panel.ToolPanel;
import stormgate.geom.MapPoint;

/**
 *
 * @author David Kim
 */
public class BackgroundTool extends Tool
{

	int mouseX = 0;
	int mouseY = 0;
	public static int tileDivisorX = MapPoint.tileSize / Tile.divideX;
	public static int tileDivisorY = MapPoint.tileSize / Tile.divideY;
	private Tile[] selectedTiles;
	private BackgroundToolPanel panel;
	private boolean mouseDown;
	private int mouseDownY;
	private int mouseDownX;
	private boolean shiftDown;

	public BackgroundTool(EditorData data)
	{
		super(data);

		panel = new BackgroundToolPanel();
	}

	@Override
	public void selectGraphic(String resource)
	{
		selected = resource;
		if (resource != null) {
			panel.setGraphic(data.getManager().getResource(resource));
		}
	}

	public void setSelected(Tile[] selectedTiles)
	{
		this.selectedTiles = selectedTiles;
	}

	@Override
	public ToolPanel getPanel()
	{
		return panel;
	}

	public Tile[] getSelectedTiles()
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
				Tile tile = selectedTiles[i];

				int tileX = tile.getX() * MapPoint.tileSize;
				int tileY = tile.getY() * MapPoint.tileSize;

				MapPoint mouseDividePoint = new MapPoint(tileX, tileY);
				MapPoint screenPoint = data.convertToWorkspace(mouseDividePoint);
				g.fillRect(screenPoint.getX(), screenPoint.getY(), tileZoom, tileZoom);
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
			if (mouseDown) {
				int rectX = mouseX;
				int rectWidth = mouseDownX - mouseX;
				int rectY = mouseY;
				int rectHeight = mouseDownY - mouseY;

				if (mouseX > mouseDownX) {
					rectX = mouseDownX;
					rectWidth = mouseX - mouseDownX;
				}
				if (mouseY > mouseDownY) {
					rectY = mouseDownY;
					rectHeight = mouseY - mouseDownY;
				}

				//Color
				g.setColor(Color.GREEN);
				g.drawRect(rectX, rectY, rectWidth, rectHeight);
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		mouseDownX = e.getX();
		mouseDownY = e.getY();
		mouseDown = true;

		if (e.isShiftDown()) {
			shiftDown = true;
		} else {
			shiftDown = false;
		}
		data.refresh();
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		mouseDown = false;
		if (e.getButton() == 3) {
			//Right click
			BackgroundSelectAction d = new BackgroundSelectAction(this, null);
			data.performAction(d);
			//Deselect
		}

		if (e.getButton() == 1) {
			//Left Click
			MapPoint A = data.convertToMap(mouseX, mouseY);
			MapPoint B = data.convertToMap(mouseDownX, mouseDownY);

			//System.out.println(A.getX()+","+A.getY()+","+B.getX()+","+B.getY());
			int fromX = A.getX();
			int fromY = A.getY();
			int toX = B.getX();
			int toY = B.getY();

			if (fromX > toX) {
				fromX = B.getX();
				toX = A.getX();
			}

			if (fromY > toY) {
				fromY = B.getY();
				toY = A.getY();
			}

			int fromTileX = getTile(fromX);
			int fromTileY = getTile(fromY);

			int toTileX = getTile(toX);
			int toTileY = getTile(toY);

			Tile[] selectTiles = new Tile[(toTileY - fromTileY + 1) * (toTileX - fromTileX + 1)];
			int count = 0;

			for (int i = fromTileY; i <= toTileY; i++) {
				for (int j = fromTileX; j <= toTileX; j++) {
					//Get the tile
					Tile t = data.getMap().getTile(j, i);
					selectTiles[count++] = t;
				}
			}


			if (shiftDown) {
				//INVERT SELECTION
				if (selectedTiles != null) {

					Tile[] totalSelected = new Tile[selectTiles.length + selectedTiles.length];
					int selectCounter = 0;
					//SelectedTile[] concat = new SelectedTile[tiles.length];
					//int concatCounter = 0;

					for (int a = 0; a < selectedTiles.length; a++) {
						Tile selA = selectedTiles[a];
						boolean unique = true;

						if (selA != null) {
							for (int b = 0; b < selectTiles.length; b++) {
								Tile selB = selectTiles[b];
								if (selB != null) {
									if (selA == selB) {
										//No Repeat
										selectTiles[b] = null;
										unique = false;
									}
								}
							}
						}

						//If there is no common selected tile, concat it to the selection
						if (unique) {
							totalSelected[selectCounter++] = selA;
						}

					}

					for (int b = 0; b < selectTiles.length; b++) {
						Tile selB = selectTiles[b];
						if (selB != null) {
							totalSelected[selectCounter++] = selB;
						}
					}

					if (selectCounter > 0) {

						Tile[] copySelected = new Tile[selectCounter];

						System.arraycopy(totalSelected, 0, copySelected, 0, selectCounter);
						selectTiles = copySelected;
					}
				}
			}

			BackgroundSelectAction d = new BackgroundSelectAction(this, selectTiles);
			data.performAction(d);
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

		data.refresh();
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		//Delete Key
		if (e.getKeyCode() == 127) {
			//DELETE
			/*
			for (int i = 0; i < selectedTiles.length; i++) {
				Tile t = selectedTiles[i];
				t.setTileBackground(null);
			}
			 */
			BackgroundPaintAction p = new BackgroundPaintAction(data.getMap(), selectedTiles, null);
			data.performAction(p);
		}
		data.refresh();
	}

	public void doubleClickGraphic(String resource)
	{
		//PAINT ACTION!
		String graphic = getSelected();
		if (selectedTiles != null) {
			//Paint
			/*
			for (int i = 0; i < selectedTiles.length; i++) {
				Tile t = selectedTiles[i];
				t.setTileBackground(graphic);
			}
			 */
			BackgroundPaintAction p = new BackgroundPaintAction(data.getMap(), selectedTiles, graphic);
			data.performAction(p);
		}
		data.refresh();
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

	public int getTile(int v)
	{
		return (int) Math.floor(v * 1f / MapPoint.tileSize);
	}
	
	public void update()
	{
	}

	@Override
	public void deselect()
	{
		selectedTiles = null;
		panel.setGraphic(null);
	}
}
