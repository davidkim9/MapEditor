package stormgate.editor.tool.data;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import stormgate.action.graphic.GraphicMoveAction;
import stormgate.action.graphic.GraphicSelectAction;
import stormgate.common.Common;
import stormgate.data.GraphicLevel;
import stormgate.data.MapEntity;
import stormgate.data.MapGraphic;
import stormgate.data.Tile;
import stormgate.editor.tool.GraphicsTool;
import stormgate.editor.ui.forms.graphic.Level;
import stormgate.geom.MapPoint;
import stormgate.image.LibraryResource;

/**
 *
 * @author David
 */
public class GraphicToolSelectMode extends GraphicToolMode
{

	private int mouseDownX = 0;
	private int mouseDownY = 0;
	private boolean mouseDown = false;
	private boolean shiftDown = false;
	private boolean dragging = false;
	private MapPoint dragFrom;
	private MapPoint[] dragPositions;

	public GraphicToolSelectMode(GraphicsTool tool)
	{
		super(tool);
	}

	@Override
	public void paint(Graphics g)
	{
		if (mouseDown && !dragging) {
			//Select
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

	//Controls
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

		if (e.getButton() == 1 && !dragging) {

			dragFrom = data.convertToMap(mouseDownX, mouseDownY);

			if (data.snap()) {
				dragFrom = data.getClosestGrid(dragFrom);
			}

			int aX = dragFrom.getX();
			int aY = dragFrom.getY();
			//check if mouse is down on a selected graphic
			MapGraphic[] selected = tool.getSelectedGraphics();
			if (selected != null) {

				dragPositions = new MapPoint[selected.length];

				for (int i = 0; i < selected.length; i++) {
					MapGraphic graphic = selected[i];

					MapPoint graphicPoint = graphic.getPoint();
					dragPositions[i] = graphicPoint;

					LibraryResource resource = data.getManager().getResource(graphic.getURL());

					//Don't need to check for drag once found
					if (resource != null && !dragging) {
                                                /*
						int gX = graphicPoint.getX() - resource.getOriginX();
						int gY = graphicPoint.getY() - resource.getOriginY();

                                                if(graphic.reverse){
                                                    gX = graphicPoint.getX() - resource.getOriginX() - resource.getWidth();
                                                }
                                                
						int gX2 = gX + resource.getWidth();
						int gY2 = gY + resource.getHeight();
                                                
						if (Common.abb(aX, aY, gX, gY, gX2, gY2)) {
							dragging = true;
						}
                                                */

                                                int gX = graphicPoint.getX();
                                                int gY = graphicPoint.getY();
                                                int rectX1 = aX - gX;
                                                int rectY1 = aY - gY;
                                                int rectX2 = aX - gX + 1;
                                                int rectY2 = aY - gY + 1;
                                                
                                                boolean selectGraphic = false;
                                                
                                                if(graphic.reverse){
                                                    selectGraphic = resource.checkSelectReverse(rectX1, rectY1, rectX2, rectY2);
                                                }else{
                                                    selectGraphic = resource.checkSelect(rectX1, rectY1, rectX2, rectY2);
                                                }

                                                if ( selectGraphic ) {
                                                        dragging = true;
                                                }


					}
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		mouseDown = false;

		boolean dragged = true;
		if (dragging) {
			//SET NEW POSITIONS
			MapPoint b = data.convertToMap(mouseX, mouseY);

			if (data.snap()) {
				b = data.getClosestGrid(b);
			}

			int bX = b.getX() - dragFrom.getX();
			int bY = b.getY() - dragFrom.getY();
			MapPoint displacement = new MapPoint(bX, bY);

			if (bX == 0 && bY == 0) {
				dragged = false;
			}
			resetPositions();

			GraphicMoveAction m = new GraphicMoveAction(data.getMap(), tool, tool.getLevel(), displacement);
			data.performAction(m);
		}
		if (!dragging || (dragging && !dragged)) {
			if (e.getButton() == 3) {
				//Right click
				//Deselect
				//ACTION
				tool.setSelectedGraphics(new MapGraphic[0]);
				//END ACTION
			}
			if (e.getButton() == 1) {
				//Left Click
				//SELECT

				MapPoint A = data.convertToMap(mouseX, mouseY);
				MapPoint B = data.convertToMap(mouseDownX, mouseDownY);
				int fromX = A.getX();
				int fromY = A.getY();
				int toX = B.getX();
				int toY = B.getY();

				//Swap if needed
				if (fromX > toX) {
					fromX = B.getX();
					toX = A.getX();
				}

				if (fromY > toY) {
					fromY = B.getY();
					toY = A.getY();
				}

				int level = tool.getLevel();

				MapGraphic[] selectedArray = null;

				int fromTileX = getTile(fromX) - 1;
				int fromTileY = getTile(fromY) - 1;

				int toTileX = getTile(toX) + 1;
				int toTileY = getTile(toY) + 1;

				LinkedList<MapGraphic> selectedGraphics = new LinkedList<MapGraphic>();

				for (int i = fromTileY; i <= toTileY; i++) {
					for (int j = fromTileX; j <= toTileX; j++) {
						//Get the tile
						Tile t = data.getMap().getTile(j, i);

						GraphicLevel graphicLevel = t.getLevel(level);
						ArrayList<MapGraphic> tileGraphics = graphicLevel.graphics;

						for (int k = 0; k < tileGraphics.size(); k++) {

							MapGraphic mapGraphic = tileGraphics.get(k);
							if (!mapGraphic.hide && !(mapGraphic instanceof MapEntity)) {
								MapPoint graphicPoint = mapGraphic.getPoint();
								LibraryResource resource = data.getManager().getResource(mapGraphic.getURL());

								if (resource != null) {
									int gX = graphicPoint.getX();
									int gY = graphicPoint.getY();
									int rectX1 = fromX - gX;
									int rectY1 = fromY - gY;
									int rectX2 = toX - gX;
									int rectY2 = toY - gY;
									if (rectX1 == rectX2) {
										rectX2 = rectX1 + 1;
									}
									if (rectY1 == rectY2) {
										rectY2 = rectY1 + 1;
									}
                                                                        boolean selectGraphic = false;
                                                                        if(mapGraphic.reverse){
                                                                            selectGraphic = resource.checkSelectReverse(rectX1, rectY1, rectX2, rectY2);
                                                                        }else{
                                                                            selectGraphic = resource.checkSelect(rectX1, rectY1, rectX2, rectY2);
                                                                        }
                                                                        
                                                                        
									if ( selectGraphic ) {
										selectedGraphics.add(mapGraphic);
									}

								}
							}
						}

					}
				}

				selectedArray = new MapGraphic[selectedGraphics.size()];
				selectedArray = selectedGraphics.toArray(selectedArray);

				//One click
				//Single select
				if (fromX == toX && fromY == toY && selectedArray.length > 0) {
					MapGraphic selected = null;
					int lowestY = -1;
					for (int k = 0; k < selectedArray.length; k++) {
						MapGraphic mapGraphic = selectedArray[k];
						MapPoint graphicPoint = mapGraphic.getPoint();
						int gY = graphicPoint.getY();
						if (selected == null || lowestY < gY) {
							selected = mapGraphic;
							lowestY = gY;
						}
					}

					selectedArray = new MapGraphic[]{selected};
				}

				if (shiftDown) {
					//Invert selection
					MapGraphic[] selected = tool.getSelectedGraphics();

					for (int i = 0; i < selected.length; i++) {
						for (int j = 0; j < selectedArray.length; j++) {
							//Check for match
							if (selected[i] == selectedArray[j]) {
								//REMOVE or DON'T ADD either
								selected[i] = null;
								selectedArray[j] = null;
							}
						}
					}

					MapGraphic[] newSelection = new MapGraphic[selected.length + selectedArray.length];
					int selectionCount = 0;

					for (int i = 0; i < selected.length; i++) {
						if (selected[i] != null) {
							newSelection[selectionCount++] = selected[i];
						}
					}

					for (int i = 0; i < selectedArray.length; i++) {
						if (selectedArray[i] != null) {
							newSelection[selectionCount++] = selectedArray[i];
						}
					}

					selectedArray = new MapGraphic[selectionCount];
					System.arraycopy(newSelection, 0, selectedArray, 0, selectionCount);
				}

				//ACTION
				GraphicSelectAction s = new GraphicSelectAction(tool, selectedArray);
				data.performAction(s);
				//END ACTION
			}
		}

		dragging = false;
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		mouseX = e.getX();
		mouseY = e.getY();

		if (dragging) {
			//Drag
			MapGraphic[] graphics = tool.getSelectedGraphics();
			MapPoint b = data.convertToMap(mouseX, mouseY);

			if (data.snap()) {
				b = data.getClosestGrid(b);
			}
			
			int bX = b.getX() - dragFrom.getX();
			int bY = b.getY() - dragFrom.getY();

			for (int i = 0; i < graphics.length; i++) {
				MapGraphic graphic = graphics[i];

				MapPoint original = dragPositions[i];

				int newX = original.getX() + bX;
				int newY = original.getY() + bY;

				MapPoint newPoint = new MapPoint(newX, newY);

				graphic.setPoint(newPoint);

				Level l = graphic.getLevel();
				if (l != null) {
					l.removeGraphic(graphic);
				}
			}
		}
	}

	public int getTile(int v)
	{
		return (int) Math.floor(v * 1f / MapPoint.tileSize);
	}

	private void resetPositions()
	{
		//Reset Positions
		MapGraphic[] graphics = tool.getSelectedGraphics();
		for (int i = 0; i < graphics.length; i++) {
			MapGraphic graphic = graphics[i];
			MapPoint original = dragPositions[i];
			graphic.setPoint(original);
		}
	}
}
