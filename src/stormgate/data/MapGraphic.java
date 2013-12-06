package stormgate.data;

import stormgate.editor.ui.forms.graphic.Level;
import stormgate.geom.MapPoint;

public class MapGraphic extends MapNode
{
	private Level level;
	public boolean hide = false;
        public boolean reverse = false;

	public MapGraphic(String url)
	{
		super(url);
	}

	public MapGraphic(String url, MapPoint point)
	{
		super(url, point);
	}

	public void setLevel(Level level)
	{
		this.level = level;
	}

	public Level getLevel()
	{
		return level;
	}

	@Override
	public MapGraphic clone()
	{
		MapGraphic clone = new MapGraphic(getURL());
		clone.setPoint(getPoint());
		clone.level = level;

		return clone;
	}
}
