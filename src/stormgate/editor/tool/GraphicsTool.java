package stormgate.editor.tool;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import stormgate.action.graphic.GraphicDeleteAction;
import stormgate.action.graphic.GraphicPasteAction;
import stormgate.data.MapGraphic;
import stormgate.editor.data.EditorData;
import stormgate.editor.tool.data.GraphicToolMode;
import stormgate.editor.tool.data.GraphicToolPlaceMode;
import stormgate.editor.tool.data.GraphicToolSelectMode;
import stormgate.editor.tool.panel.GraphicToolPanel;
import stormgate.editor.tool.panel.ToolPanel;
import stormgate.geom.MapPoint;
import stormgate.image.LibraryResource;

/**
 *
 * @author David
 */
public class GraphicsTool extends Tool
{

	int mouseX = 0;
	int mouseY = 0;
	private MapGraphic[] selectedGraphics;
	private MapGraphic[] clipboard;
	private GraphicToolMode mode;
	int level = 1;
	GraphicToolPanel panel;
	LinkedList<MapGraphic> hidden;
	private GraphicToolSelectMode selectMode;
	private GraphicToolPlaceMode placeMode;

	public GraphicsTool(EditorData data)
	{
		super(data);

		selectMode = new GraphicToolSelectMode(this);
		placeMode = new GraphicToolPlaceMode(this);
		selectMode.setData(data);
		placeMode.setData(data);

		mode = selectMode;

		panel = new GraphicToolPanel();
		panel.setTool(this);
		panel.setData(data);

		hidden = new LinkedList<MapGraphic>();
	}

	@Override
	public void selectGraphic(String resource)
	{
		selected = resource;
		if (resource != null) {
			panel.setGraphic(data.getManager().getResource(resource));
		}
	}

	@Override
	public ToolPanel getPanel()
	{
		return panel;
	}

	public int getLevel()
	{
		return level;
	}

	public String getSelected()
	{
		return selected;
	}

	public void setSelectedGraphics(MapGraphic[] selectedGraphics)
	{
		this.selectedGraphics = selectedGraphics;
	}

	public MapGraphic[] getSelectedGraphics()
	{
		return selectedGraphics;
	}

	//Paint instructions
	public void paint(Graphics g)
	{
		//Paint Selected Boundaries

		if (selectedGraphics != null) {
			g.setColor(Color.GREEN);

			for (int i = 0; i < selectedGraphics.length; i++) {
				//Draw graphic boundaries
				MapGraphic mapGraphic = selectedGraphics[i];
				MapPoint graphicPoint = mapGraphic.getPoint();
				LibraryResource resource = data.getManager().getResource(mapGraphic.getURL());
				if (resource != null) {
					Rectangle bounds = resource.getBoundary();
					int gX = graphicPoint.getX();
					int gY = graphicPoint.getY();
					int rectX1 = bounds.x + gX;
					int rectY1 = bounds.y + gY;
					int rectX2 = zoomFilter(bounds.width);
					int rectY2 = zoomFilter(bounds.height);
                                        
                                        if(mapGraphic.reverse){
                                            rectX1 = gX - bounds.x - bounds.width;
                                        }
                                        
					MapPoint A = new MapPoint(rectX1, rectY1);

					MapPoint screenPointA = data.convertToWorkspace(A);

					g.drawRect(screenPointA.getX(), screenPointA.getY(), rectX2, rectY2);
				}
			}
		}

		//Paint Tool
		if (mode != null) {
			mode.paint(g);
		}
	}

	public int zoomFilter(float a)
	{
		return Math.round(a * filter.getZoom());
	}

	//Controls
	public void mousePressed(MouseEvent e)
	{
		if (mode != null) {
			mode.mousePressed(e);
		}
		data.refresh();
	}

	public void mouseReleased(MouseEvent e)
	{
		if (mode != null) {
			mode.mouseReleased(e);
		}
		data.refresh();
	}

	public void mouseDragged(MouseEvent e)
	{
		mouseMoved(e);
	}

	public void mouseMoved(MouseEvent e)
	{
		if (mode != null) {
			mode.mouseMoved(e);
		}

		mouseX = e.getX();
		mouseY = e.getY();

		data.refresh();
	}

	public void selectMode()
	{
		mode = selectMode;
	}

	public void keyPressed(KeyEvent e)
	{
		int keyCode = e.getKeyCode();
		//Delete Key
		if (keyCode == 127) {
			//DELETE
			GraphicDeleteAction d = new GraphicDeleteAction(data.getMap(), this, getLevel());
			data.performAction(d);
		}

		if (keyCode == 27) {
			selectMode();
		}
	}

	public void doubleClickGraphic(String resource)
	{
		//Change tool mode to placement
		mode = placeMode;
	}

	public void cut()
	{
		copy();

		//DELETE
		GraphicDeleteAction d = new GraphicDeleteAction(data.getMap(), this, getLevel());
		data.performAction(d);
	}

	public void copy()
	{
		clipboard = selectedGraphics;
	}

	public void paste()
	{
		//PLACE GRAPHICS

		MapPoint pasteLocation = data.convertToMap(mouseX, mouseY);

		int top = -1;
		int left = -1;

		for (int i = 0; i < clipboard.length; i++) {
			MapGraphic graphic = clipboard[i];
			if (graphic != null) {
				MapPoint graphicPoint = graphic.getPoint();
				if (graphicPoint.getX() < left || left == -1) {
					left = graphicPoint.getX();
				}
				if (graphicPoint.getY() < top || top == -1) {
					top = graphicPoint.getY();
				}
			}
		}

		MapPoint offset = new MapPoint(left, top);
		if (data.snap()) {
			//c = data.getClosestGrid(c);

			pasteLocation = data.getClosestGrid(pasteLocation);
		}

		GraphicPasteAction p = new GraphicPasteAction(data.getMap(), this, getLevel(), clipboard, pasteLocation, offset);
		data.performAction(p);
	}

	public void hide(MapGraphic g, boolean hide)
	{
		g.hide = hide;
		if (hide) {
			hidden.add(g);
		} else {
			hidden.remove(g);
		}
	}

	public void unhideAll()
	{
		MapGraphic g = hidden.poll();
		while (g != null) {
			g.hide = false;
			g = hidden.poll();
		}
	}

	public LinkedList<MapGraphic> getHidden()
	{
		return hidden;
	}

	public void setLevel(int level)
	{
		this.level = level;
	}

	public void update()
	{
		panel.updatePanel();
	}

	@Override
	public void deselect()
	{
		setSelectedGraphics(null);
		clipboard = null;
		panel.setGraphic(null);
	}

	public void setReverse(boolean reverse)
	{
		placeMode.setReverse(reverse);
	}

	public boolean getReverse()
	{
		return placeMode.getReverse();
	}
}
