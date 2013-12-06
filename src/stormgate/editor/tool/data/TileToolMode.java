/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stormgate.editor.tool.data;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import stormgate.editor.data.EditorData;
import stormgate.editor.tool.TileTool;

/**
 *
 * @author David
 */
public abstract class TileToolMode
{

	protected EditorData data;
	protected int mouseX;
	protected int mouseY;
	protected TileTool tool;

	public TileToolMode(TileTool tool)
	{
		this.tool = tool;
	}

	public void setTileTool(TileTool tool)
	{
		this.tool = tool;
	}

	public void setData(EditorData data)
	{
		this.data = data;
	}

	public void paint(Graphics g)
	{
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
