/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stormgate.editor.tool.data;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import stormgate.action.tile.TileSelectAction;
import stormgate.data.Tile;
import stormgate.editor.tool.TileTool;
import stormgate.geom.MapPoint;

/**
 *
 * @author David
 */
public class TileToolSelectMode extends TileToolMode
{

	private int mouseDownX = 0;
	private int mouseDownY = 0;
	private boolean mouseDown = false;
	private boolean shiftDown = false;

	public TileToolSelectMode(TileTool tool)
	{
		super(tool);
	}

	@Override
	public void paint(Graphics g)
	{
		if(data.pan){
			mouseDown = false;
		}
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

	@Override
	public void mousePressed(MouseEvent e)
	{
		mouseDownX = e.getX();
		mouseDownY = e.getY();
		mouseDown = true;

		if(e.isShiftDown()){
			shiftDown = true;
		}else{
			shiftDown = false;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		mouseDown = false;
		if(e.getButton() == 3){
			//Right click
			TileSelectAction d = new TileSelectAction(tool, null);
			data.performAction(d);
		}

		if(e.getButton() == 1){
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
			int fromLocalX = getLocal(fromX) / TileTool.tileDivisorX;
			int fromLocalY = getLocal(fromY) / TileTool.tileDivisorY;

			int toTileX = getTile(toX);
			int toTileY = getTile(toY);
			int toLocalX = getLocal(toX) / TileTool.tileDivisorX;
			int toLocalY = getLocal(toY) / TileTool.tileDivisorY;

			SelectedTile[] selectedTiles = new SelectedTile[(toTileY - fromTileY + 1) * (toTileX - fromTileX + 1)];
			int count = 0;

			for (int i = fromTileY; i <= toTileY; i++) {
				for (int j = fromTileX; j <= toTileX; j++) {
					//Get the tile
					//Tile t = map.getTile(j, i);
					SelectedTile selectedTile = new SelectedTile();

					selectedTile.x = j;
					selectedTile.y = i;

					//Check if its a border selection and deselect all outside the border
					boolean[][] selection;

					if (j == fromTileX || i == fromTileY || j == toTileX || i == toTileY) {
						selection = new boolean[Tile.divideY][Tile.divideX];
						for (int k = 0; k < Tile.divideY; k++) {
							for (int l = 0; l < Tile.divideX; l++) {
								selection[k][l] = true;
							}
						}
						if (j == fromTileX) {
							deselect(selection, 0, fromLocalX, 0, Tile.divideY);
						}

						if (i == fromTileY) {
							deselect(selection, 0, Tile.divideX, 0, fromLocalY);
						}

						if (j == toTileX) {
							deselect(selection, toLocalX + 1, Tile.divideX, 0, Tile.divideY);
						}

						if (i == toTileY) {
							deselect(selection, 0, Tile.divideX, toLocalY + 1, Tile.divideY);
						}

						selectedTile.selectAll = false;
						selectedTile.selected = selection;

					} else {
						//If the tile is not a border, select everything in it
						selectedTile.selectAll = true;
					}

					selectedTiles[count++] = selectedTile;
				}
			}

			if(shiftDown){
				//INVERT SELECTION
				SelectedTile[] tiles = tool.getSelectedTiles();
				if(tiles != null){

					SelectedTile[] totalSelected = new SelectedTile[selectedTiles.length + tiles.length];
					int selectCounter = 0;
					//SelectedTile[] concat = new SelectedTile[tiles.length];
					//int concatCounter = 0;

					for(int a = 0 ; a < tiles.length ; a++){
						SelectedTile selA = tiles[a];
						boolean unique = true;

						if(selA != null){
							for(int b = 0 ; b < selectedTiles.length ; b++){
								SelectedTile selB = selectedTiles[b];
								if(selB != null){
									if(selA.x == selB.x && selA.y == selB.y){
										//Find the difference and invert selections
										unique = false;

										if(selA.selectAll){
											if(selB.selectAll){
												//Double selection, overlap, deselect all overlaps
											}else if(!selB.selectAll){
												//Invert selB tile cell selections
												SelectedTile selC = selB.clone();
												invert(selC.selected);
												totalSelected[selectCounter++] = selC;
											}
										}else if(!selA.selectAll){
											if(selB.selectAll){
												//Invert selA tile cell selections
												SelectedTile selC = selA.clone();
												invert(selC.selected);
												totalSelected[selectCounter++] = selC;
											}else if(!selB.selectAll){
												//Bitch process
												SelectedTile selC = selA.clone();
												selC.selected = xor(selA.selected, selB.selected);
												totalSelected[selectCounter++] = selC;
											}
										}

										//No Repeat
										selectedTiles[b] = null;
									}
								}
							}
						}

						//If there is no common selected tile, concat it to the selection
						if(unique){
							totalSelected[selectCounter++] = selA;
						}

					}

					for(int b = 0 ; b < selectedTiles.length ; b++){
						SelectedTile selB = selectedTiles[b];
						if(selB != null){
							totalSelected[selectCounter++] = selB;
						}
					}

					if(selectCounter > 0){

						SelectedTile[] copySelected = new SelectedTile[selectCounter];

						System.arraycopy(totalSelected, 0, copySelected, 0, selectCounter);
						selectedTiles = copySelected;
					}
				}
			}

			//SELECT ACTION!
			//old selected, maybe just forward tool into action tool.getSelected();
			//tool.setSelected(selectedTiles);
			TileSelectAction d = new TileSelectAction(tool, selectedTiles);
			data.performAction(d);
		}
	}

	public int getTile(int v)
	{
		return (int) Math.floor(v * 1f / MapPoint.tileSize);
	}

	public int getLocal(int v)
	{
		int local = v % MapPoint.tileSize;
		return local >= 0 ? local : MapPoint.tileSize + local;
	}

	public void deselect(boolean[][] selection, int xInit, int xMax, int yInit, int yMax) {
		for (int k = yInit; k < yMax; k++) {
			for (int l = xInit; l < xMax; l++) {
				selection[k][l] = false;
			}
		}
	}

	public void invert(boolean[][] selection) {
		for (int k = 0; k < selection.length; k++) {
			for (int l = 0; l < selection[k].length; l++) {
				selection[k][l] = !selection[k][l];
			}
		}
	}

	private boolean[][] xor(boolean[][] a, boolean[][] b)
	{
		boolean[][] c = new boolean[a.length][a[0].length];

		for(int i = 0; i < a.length; i++){
			for(int j = 0; j < a[i].length; j++){
				c[i][j] = a[i][j] ^ b[i][j];
			}
		}

		return c;
	}
}
