package stormgate.editor.tool.data;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import stormgate.action.entity.EntityMoveAction;
import stormgate.action.entity.EntitySelectAction;
import stormgate.common.Common;
import stormgate.data.MapEntity;
import stormgate.data.Tile;
import stormgate.editor.tool.EntityTool;
import stormgate.editor.ui.forms.graphic.Level;
import stormgate.geom.MapPoint;
import stormgate.image.LibraryResource;

/**
 *
 * @author David
 */
public class EntityToolSelectMode extends EntityToolMode
{

	private int mouseDownX = 0;
	private int mouseDownY = 0;
	private boolean mouseDown = false;
	private boolean shiftDown = false;
	private boolean dragging = false;
	private MapPoint dragFrom;
	private MapPoint[] dragPositions;

	public EntityToolSelectMode(EntityTool tool)
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
			//check if mouse is down on a selected entity
			MapEntity[] selected = tool.getSelectedEntities();
			if (selected != null) {

				dragPositions = new MapPoint[selected.length];

				for (int i = 0; i < selected.length; i++) {
					MapEntity entity = selected[i];

					MapPoint entityPoint = entity.getPoint();
					dragPositions[i] = entityPoint;

					LibraryResource resource = data.getManager().getResource(entity.getURL());

					//Don't need to check for drag once found
					if (resource != null && !dragging) {
						int gX = entityPoint.getX() - resource.getOriginX();
						int gY = entityPoint.getY() - resource.getOriginY();
						int gX2 = gX + resource.getWidth();
						int gY2 = gY + resource.getHeight();

						if (Common.abb(aX, aY, gX, gY, gX2, gY2)) {
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

			EntityMoveAction m = new EntityMoveAction(data.getMap(), tool, displacement);
			data.performAction(m);
		}
		if (!dragging || (dragging && !dragged)) {
			if (e.getButton() == 3) {
				//Right click
				//Deselect
				//ACTION
				tool.setSelectedEntities(new MapEntity[0]);
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

				MapEntity[] selectedArray = null;

				int fromTileX = getTile(fromX) - 1;
				int fromTileY = getTile(fromY) - 1;

				int toTileX = getTile(toX) + 1;
				int toTileY = getTile(toY) + 1;

				LinkedList<MapEntity> selectedEntities = new LinkedList<MapEntity>();

				for (int i = fromTileY; i <= toTileY; i++) {
					for (int j = fromTileX; j <= toTileX; j++) {
						//Get the tile
						Tile t = data.getMap().getTile(j, i);

						ArrayList<MapEntity> tileEntities = t.getEntities();

						for (int k = 0; k < tileEntities.size(); k++) {

							MapEntity mapEntity = tileEntities.get(k);
							if (mapEntity != null) {
								MapPoint entityPoint = mapEntity.getPoint();
								LibraryResource resource = data.getManager().getResource(mapEntity.getURL());

								if (resource != null) {
									int gX = entityPoint.getX();
									int gY = entityPoint.getY();
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

									if (resource.checkSelect(rectX1, rectY1, rectX2, rectY2)) {
										selectedEntities.add(mapEntity);
									}

								}
							}
						}

					}
				}

				selectedArray = new MapEntity[selectedEntities.size()];
				selectedArray = selectedEntities.toArray(selectedArray);

				//One click
				//Single select
				if (fromX == toX && fromY == toY && selectedArray.length > 0) {
					MapEntity selected = null;
					int lowestY = -1;
					for (int k = 0; k < selectedArray.length; k++) {
						MapEntity mapEntity = selectedArray[k];
						MapPoint entityPoint = mapEntity.getPoint();
						int gY = entityPoint.getY();
						if (selected == null || lowestY < gY) {
							selected = mapEntity;
							lowestY = gY;
						}
					}

					selectedArray = new MapEntity[]{selected};
				}

				if (shiftDown) {
					//Invert selection
					MapEntity[] selected = tool.getSelectedEntities();

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

					MapEntity[] newSelection = new MapEntity[selected.length + selectedArray.length];
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

					selectedArray = new MapEntity[selectionCount];
					System.arraycopy(newSelection, 0, selectedArray, 0, selectionCount);
				}

				//ACTION
				EntitySelectAction s = new EntitySelectAction(tool, selectedArray);
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
			MapEntity[] entities = tool.getSelectedEntities();
			MapPoint b = data.convertToMap(mouseX, mouseY);

			if (data.snap()) {
				b = data.getClosestGrid(b);
			}

			int bX = b.getX() - dragFrom.getX();
			int bY = b.getY() - dragFrom.getY();

			for (int i = 0; i < entities.length; i++) {
				MapEntity entity = entities[i];

				MapPoint original = dragPositions[i];

				int newX = original.getX() + bX;
				int newY = original.getY() + bY;

				MapPoint newPoint = new MapPoint(newX, newY);

				entity.setPoint(newPoint);

				Level l = entity.getLevel();
				if (l != null) {
					l.removeGraphic(entity);
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
		MapEntity[] entities = tool.getSelectedEntities();
		for (int i = 0; i < entities.length; i++) {
			MapEntity entity = entities[i];
			MapPoint original = dragPositions[i];
			entity.setPoint(original);
		}
	}
}
