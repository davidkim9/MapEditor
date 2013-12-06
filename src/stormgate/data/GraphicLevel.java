package stormgate.data;

import java.util.ArrayList;

/**
 *
 * @author David
 */
public class GraphicLevel {
	public ArrayList<MapGraphic> graphics;

	public GraphicLevel(){
		graphics = new ArrayList<MapGraphic>(0);
	}

	public void addGraphic(MapGraphic graphic)
	{
		graphics.add(graphic);
	}

	public void removeGraphic(MapGraphic graphic)
	{
		graphics.remove(graphic);
	}

	@Override
	protected void finalize() throws Throwable
	{
		try {
			graphics.clear();
			graphics = null;
		} finally {
			super.finalize();
		}
	}
}
