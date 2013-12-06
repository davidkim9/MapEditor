package stormgate.editor.tool;

import java.awt.Graphics;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import stormgate.editor.data.EditorData;
import stormgate.editor.tool.panel.ToolPanel;
import stormgate.filter.Filter;

/**
 *
 * @author David
 */
public interface ToolInterface extends MouseListener, MouseMotionListener, KeyListener
{

	public void selectGraphic(String resource);

	public void doubleClickGraphic(String resource);

	public void setEditorData(EditorData data);

	public void paint(Graphics g);

	public void setFilter(Filter filter);

	public void setOffset(int centerX, int centerY);

	public ToolPanel getPanel();

	public void cut();

	public void copy();

	public void paste();

	public void select();

	public void deselect();

	public void update();
}
