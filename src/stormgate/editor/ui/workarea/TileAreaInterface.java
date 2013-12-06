package stormgate.editor.ui.workarea;

import stormgate.filter.Filter;
import stormgate.geom.MapPoint;

public interface TileAreaInterface {
	public void update(MapPoint camera);
	public void filter(Filter filter);
}
