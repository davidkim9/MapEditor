/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package stormgate.editor.controls;

import java.awt.Cursor;
import stormgate.editor.data.EditorData;

/**
 *
 * @author David
 */
public class GlobalKeyHandler {

	private EditorData data;

	private Cursor oldCursor;

	public GlobalKeyHandler(EditorData data) {
		this.data = data;
	}

	public void keyPressed(java.awt.event.KeyEvent evt) {
		//System.out.println(evt.getKeyCode());
		//GLOBAL HOTKEYS
		int keyCode = evt.getKeyCode();
		if(keyCode == 32){
			if(!data.isMouseDown()){
				if(!data.pan){
					oldCursor = data.cursors.getCursor();
					//Panning hand
					data.cursors.setCursor("grab");
				}

				data.pan = true;
			}
		}
		//updateGraphics();
	}

	public void keyReleased(java.awt.event.KeyEvent evt) {
		//GLOBAL HOTKEYS
		if(data.pan){
			if(oldCursor != null){
				data.cursors.setCursor( oldCursor );
			}else{
				data.cursors.setDefault();
			}
		}
		data.pan = false;
	}
}
