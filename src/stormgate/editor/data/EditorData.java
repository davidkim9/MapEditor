package stormgate.editor.data;

import stormgate.action.Action;
import stormgate.action.ActionHistory;
import stormgate.action.tool.ToolChangeAction;
import stormgate.action.zone.ZoneModeChangeAction;
import stormgate.editor.controls.MouseHandler;
import stormgate.editor.controls.ToolKeyHandler;
import stormgate.editor.controls.CursorHandler;
import stormgate.editor.tool.BackgroundTool;
import stormgate.editor.tool.EntityTool;
import stormgate.editor.tool.GraphicsTool;
import stormgate.editor.tool.TileTool;
import stormgate.editor.tool.ToolInterface;
import stormgate.editor.tool.ZoneTool;
import stormgate.editor.tool.data.ZoneToolMode;
import stormgate.editor.ui.forms.EditorForm;
import stormgate.editor.ui.forms.graphic.Graphic;
import stormgate.editor.ui.forms.graphic.Level;
import stormgate.editor.ui.forms.tool.Toolbox;
import stormgate.filter.Filter;
import stormgate.geom.MapPoint;
import stormgate.image.ImageManager;
import stormgate.image.ResourceTracker;
import stormgate.map.Map;

/**
 *
 * @author David Kim
 */
public class EditorData
{

	private EditorForm form;
	private ActionHistory actions;
	private RenderData renderData;
	private RenderData miniRenderData;
	private MouseHandler mouseHandler;
	private ToolKeyHandler keyHandler;
	//public LibraryInterface library;
	private ImageManager images;
	//Key Controls
	public boolean pan = false;
	//Cursor Info
	public CursorHandler cursors;
	private ToolInterface tool;
	private String selectedGraphic;
	private Graphic graphic;
	private ResourceTracker tracker;
	public GraphicsTool graphicTool;
	public TileTool tileTool;
	public ZoneTool zoneTool;
	//Create
	public ToolInterface backgroundTool;
	public EntityTool entityTool;
	private Level[] levels;

	public EditorData(EditorForm form)
	{
		this.form = form;
		actions = new ActionHistory();
		defineTools();

		levels = new Level[3];
		levels[0] = new Level();
		levels[1] = new Level();
		levels[2] = new Level();
	}
        
	public void refreshLibrary()
	{
		//form.refreshLibrary();
		getMap().loadMap();
                
		clean();
		form.cleanGraphics();
		form.refreshLibrary();
	}

	public Level[] getLevels()
	{
		return levels;
	}

	private void defineTools()
	{
		tileTool = new TileTool(this);
		graphicTool = new GraphicsTool(this);
		zoneTool = new ZoneTool(this);
		backgroundTool = new BackgroundTool(this);
		//backgroundTool = new DefaultTool();
		entityTool = new EntityTool(this);
	}

	public void setMouseHandler(MouseHandler mouseHandler)
	{
		this.mouseHandler = mouseHandler;
		mouseHandler.setTool(tool);
	}

	public boolean isMouseDown()
	{
		return mouseHandler.isMouseDown();
	}

	public void setKeyHandler(ToolKeyHandler toolKeyHandler)
	{
		this.keyHandler = toolKeyHandler;
		keyHandler.setTool(tool);
	}

	public ImageManager getManager()
	{
		return images;
	}

	public RenderData getRenderData()
	{
		return renderData;
	}

	public RenderData getMiniRenderData()
	{
		return miniRenderData;
	}

	public Map getMap()
	{
		return renderData.getMap();
	}

	public void setFilter(Filter filter)
	{
		renderData.setFilter(filter);
	}

	public void setZoom(float v)
	{
		renderData.setZoom(v);
	}

	public float getZoom()
	{
		return renderData.getZoom();
	}

	public MapPoint getCameraLocation()
	{
		return renderData.getCameraLocation();
	}

	public void setCamera(int x, int y)
	{
		MapPoint camera = renderData.getCameraLocation();
		camera.setX(x);
		camera.setY(y);
		renderData.checkLoadMap();
		refresh();
	}

	public void refresh()
	{
		form.updateMemory();
		form.updateGraphics();
		tool.update();
	}

	public void performAction(Action a)
	{
		actions.performAction(a);
		refresh();
	}

	public void undoAction()
	{
		actions.undoAction();
		refresh();
	}

	public void redoAction()
	{
		actions.redoAction();
		refresh();
	}

	public void setGraphic(Graphic g)
	{
		graphic = g;
	}

	public ToolInterface getTool()
	{
		return tool;
	}

	public void selectTool(ToolInterface tool)
	{
		this.tool = tool;
		//tool.select();
		tool.selectGraphic(selectedGraphic);
		if (mouseHandler != null) {
			mouseHandler.setTool(tool);
		}
		if (keyHandler != null) {
			keyHandler.setTool(tool);
		}
		form.setPanel(tool.getPanel());

		renderData.setTool(tool);

		Filter filter = renderData.getFilter();

		if (filter != null) {
			if (tool == zoneTool) {
				filter.zoneTool = true;
			} else {
				filter.zoneTool = false;
			}
			if (tool == entityTool) {
				filter.entityTool = true;
			} else {
				filter.entityTool = false;
			}
		}
	}

	public void selectGraphic(String resource)
	{
		if (tool != null && resource != null) {
			selectedGraphic = resource;
			tool.selectGraphic(selectedGraphic);
		}
	}

	public void selectDoubleGraphic(String resource)
	{
		if (tool != null && resource != null) {
			selectedGraphic = resource;
			tool.doubleClickGraphic(selectedGraphic);
		}
	}

	public int getScreenWidth()
	{
		return graphic.getWidth();
	}

	public int getScreenHeight()
	{
		return graphic.getHeight();
	}

	public MapPoint convertToMap(int x, int y)
	{
		MapPoint camera = getCameraLocation();

		int screenSizeWidth = graphic.getWidth();
		int screenSizeHeight = graphic.getHeight();

		int mapX = Math.round(camera.getX() + (x - screenSizeWidth / 2) / renderData.getZoom());
		int mapY = Math.round(camera.getY() + (y - screenSizeHeight / 2) / renderData.getZoom());

		return new MapPoint(mapX, mapY);
	}

	public MapPoint convertToWorkspace(MapPoint pt)
	{
		int x = pt.getX();
		int y = pt.getY();

		MapPoint camera = getCameraLocation();

		int screenSizeWidth = graphic.getWidth();
		int screenSizeHeight = graphic.getHeight();

		int mapX = Math.round((x - camera.getX()) * renderData.getZoom() + screenSizeWidth / 2);
		int mapY = Math.round((y - camera.getY()) * renderData.getZoom() + screenSizeHeight / 2);

		return new MapPoint(mapX, mapY);
	}

	public MapPoint getClosestGrid(MapPoint pt)
	{
		int gridSize = 30;
		int modX = pt.getX() % gridSize;
		int modY = pt.getY() % (gridSize / 2);

		int gridX = 0;
		int gridY = 0;

		if (modX > gridSize / 2) {
			//Round up
			gridX = pt.getX() + (gridSize - modX);
		} else {
			//Round down
			gridX = pt.getX() - modX;
		}

		if (modY > gridSize / 2) {
			//Round up
			gridY = pt.getY() + (gridSize - modY);
		} else {
			//Round down
			gridY = pt.getY() - modY;
		}

		return new MapPoint(gridX, gridY);
	}

	public boolean snap()
	{
		return renderData.getFilter().snap;
	}

	public MapPoint gridMap(int x, int y)
	{
		return getClosestGrid(convertToMap(x, y));
	}

	public MapPoint gridWorkspace(int x, int y)
	{
		return convertToWorkspace(getClosestGrid(convertToMap(x, y)));
	}

	public boolean mapChanged()
	{
		return actions.isChanged();
	}
	
	private void clearActions()
	{
		actions.clearChanged();
	}

	public void newMap()
	{
		actions = new ActionHistory();
		images = new ImageManager();
		tracker = new ResourceTracker(images);
		renderData = new RenderData(tracker);

		Map map = new Map(this);

		//Destroy old map
		Map oldMap = renderData.getMap();
		if(oldMap != null){
			oldMap.destroyMap();
		}

		renderData.setMap(map);
		miniRenderData = new RenderData(tracker);
		miniRenderData.setMap(map);

		Filter minimapFilter = new Filter(null);
		minimapFilter.showHidden = true;
		minimapFilter.zones = false;
		minimapFilter.entities = false;
		minimapFilter.graphics = true;
		minimapFilter.selected = false;
		minimapFilter.grid = false;
		minimapFilter.showDepth = false;
		minimapFilter.setZoom(0.05f);

		miniRenderData.setFilter(minimapFilter);
		//Set Default Tool
		selectTool(graphicTool);

		refresh();
	}

	public void setNewMap()
	{
		actions = new ActionHistory();
		Map map = new Map(this);

		//Destroy old map
		Map oldMap = renderData.getMap();
		if(oldMap != null){
			oldMap.destroyMap();
		}

		renderData.setMap(map);
		miniRenderData.setMap(map);

		selectTool(graphicTool);

		refresh();
	}

	public void setMap(Map map)
	{
		actions = new ActionHistory();
		
		//Destroy old map
		Map oldMap = renderData.getMap();
		if(oldMap != null){
			oldMap.destroyMap();
		}

		renderData.setMap(map);
		miniRenderData.setMap(map);
		map.loadMap();

		refresh();
	}

	public void cut()
	{
		if (tool != null) {
			tool.cut();
		}
	}

	public void copy()
	{
		if (tool != null) {
			tool.copy();
		}
	}

	public void paste()
	{
		if (tool != null) {
			tool.paste();
		}
	}

	public MapPoint convertToMap(MapPoint p)
	{
		return convertToMap(p.getX(), p.getY());
	}
	//Tool Stuff
	private Toolbox toolbox;

	public void setToolbox(Toolbox toolbox)
	{
		this.toolbox = toolbox;
	}

	public void changeTool(ToolInterface tool)
	{
		if (getTool() != null) {
			getTool().deselect();
		}

		ToolChangeAction t = new ToolChangeAction(form, toolbox, this, tool);
		performAction(t);
	}

	public void zoneModeChange(ZoneToolMode mode)
	{
		ZoneModeChangeAction m = new ZoneModeChangeAction(form, zoneTool, mode);
		performAction(m);
	}

	private void clean()
	{
		renderData.getTracker().clean();
		miniRenderData.getTracker().clean();
	}

	public void reset()
	{
		//Things to do when the editor needs to be reset
		graphicTool.deselect();
		tileTool.deselect();
		zoneTool.resetTool();
		backgroundTool.deselect();
		entityTool.deselect();
		selectedGraphic = null;
		changeTool(graphicTool);
		clean();
		form.cleanGraphics();
		
		form.resetLibrary();
		System.gc();
		refresh();
                
                clearActions();
	}
}
