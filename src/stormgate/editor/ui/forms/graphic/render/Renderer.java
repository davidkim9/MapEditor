package stormgate.editor.ui.forms.graphic.render;

import java.awt.Graphics;
import stormgate.filter.Filter;
import stormgate.image.ResourceTracker;

/**
 *
 * @author David Kim
 */
public interface Renderer
{
	public void setTracker(ResourceTracker tracker);

	public void setFilter(Filter filter);

	public void setOffset(int offsetX, int offsetY);

	public void paint(Graphics p);
}
