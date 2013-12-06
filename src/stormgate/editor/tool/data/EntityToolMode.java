package stormgate.editor.tool.data;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import stormgate.editor.data.EditorData;
import stormgate.editor.tool.EntityTool;

/**
 *
 * @author David
 */
public abstract class EntityToolMode {

	protected EditorData data;
	protected EntityTool tool;
	protected int mouseX;
	protected int mouseY;

	public EntityToolMode(EntityTool tool)
	{
		this.tool = tool;
	}

	public void paint(Graphics g)
	{
	}

	public void setData(EditorData data)
	{
		this.data = data;
	}

	public void mousePressed(MouseEvent e)
	{
	}

	public void mouseReleased(MouseEvent e)
	{
	}

	public void mouseMoved(MouseEvent e)
	{
		mouseX = e.getX();
		mouseY = e.getY();
	}

}
