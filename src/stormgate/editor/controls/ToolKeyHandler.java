/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stormgate.editor.controls;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import stormgate.editor.tool.ToolInterface;

/**
 *
 * @author David
 */
public class ToolKeyHandler implements KeyListener {

	ToolInterface tool;

	public void setTool(ToolInterface tool) {
		this.tool = tool;
	}

	public void keyTyped(KeyEvent e) {
		if(tool != null)
			tool.keyTyped(e);
	}

	public void keyPressed(KeyEvent e) {
		if(tool != null)
			tool.keyPressed(e);
	}

	public void keyReleased(KeyEvent e) {
		if(tool != null)
			tool.keyReleased(e);
	}
}
