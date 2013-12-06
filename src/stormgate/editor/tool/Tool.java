/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stormgate.editor.tool;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import stormgate.editor.data.EditorData;
import stormgate.editor.tool.panel.ToolPanel;
import stormgate.filter.Filter;

/**
 *
 * @author David
 */
public abstract class Tool implements ToolInterface
{

	protected EditorData data;

	public Tool(EditorData data)
	{
		this.data = data;
	}

	protected String selected;
	protected Filter filter;
	protected int offsetX;
	protected int offsetY;

	public void selectGraphic(String resource)
	{
		selected = resource;
	}

	public void setEditorData(EditorData data)
	{
		this.data = data;
	}

	public void setFilter(Filter filter)
	{
		this.filter = filter;
	}

	public void setOffset(int centerX, int centerY)
	{
		this.offsetX = centerX;
		this.offsetY = centerY;
	}

	public ToolPanel getPanel()
	{
		return null;
	}

	public void mouseClicked(MouseEvent e)
	{
	}

	public void mouseEntered(MouseEvent e)
	{
	}

	public void mouseExited(MouseEvent e)
	{
	}

	public void keyTyped(KeyEvent e)
	{
	}

	public void keyPressed(KeyEvent e)
	{
	}

	public void keyReleased(KeyEvent e)
	{
	}

	public void select()
	{
	}
	
	public void deselect()
	{
	}
}
