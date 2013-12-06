package stormgate.editor.data;

import stormgate.data.Tile;
import stormgate.editor.tool.ToolInterface;
import stormgate.filter.Filter;
import stormgate.geom.MapPoint;
import stormgate.image.ResourceTracker;
import stormgate.map.Map;

/**
 *
 * @author David
 */
public class RenderData
{

	private ResourceTracker tracker;
	private Map map;
	private Filter filter;
	private ToolInterface tool;

	public RenderData(ResourceTracker tracker)
	{
		this.tracker = tracker;
	}

	public ResourceTracker getTracker()
	{
		return tracker;
	}
	
	public void setFilter(Filter filter)
	{
		this.filter = filter;
	}

	public void setZoom(float v)
	{
		filter.setZoom(v);
	}

	public void setTool(ToolInterface tool)
	{
		this.tool = tool;
	}

	public ToolInterface getTool()
	{
		return tool;
	}

	public void setMap(Map map)
	{
		this.map = map;
	}

	public MapPoint getCameraLocation()
	{
		return map.getCameraLocation();
	}

	void checkLoadMap()
	{
		map.checkLoadMap();
	}

	public float getZoom()
	{
		return filter.getZoom();
	}

	public Filter getFilter()
	{
		return filter;
	}

	public Tile getTile(int x, int y)
	{
		return map.getTileNoLoad(x, y);
	}

	public Map getMap()
	{
		return map;
	}
}
