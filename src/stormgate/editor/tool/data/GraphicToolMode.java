/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package stormgate.editor.tool.data;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import stormgate.editor.data.EditorData;
import stormgate.editor.tool.GraphicsTool;

/**
 *
 * @author David
 */
public abstract class GraphicToolMode {

	protected EditorData data;
	protected final GraphicsTool tool;
	protected int mouseX;
	protected int mouseY;

	public GraphicToolMode(GraphicsTool tool)
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
