/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package stormgate.editor.controls;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

import java.net.URL;
import java.util.HashMap;
import javax.swing.JComponent;

/**
 *
 * @author David
 */
public class CursorHandler {
	private HashMap<String, Cursor> cursors;
	private Toolkit toolkit;

	public JComponent mouseOver;

	public CursorHandler(JComponent mouseArea) {
		
		mouseOver = mouseArea;

		cursors = new HashMap<String, Cursor>();
		toolkit = Toolkit.getDefaultToolkit();

		addCursor("grab", "cursor-move.png");
		addCursor("grabbing", "cursor-grab.png");
		addCursor("penAdd", "penAdd.png");
		addCursor("penSub", "penSub.png");
		addCursor("pen", "pen.png");
		addCursor("penMove", "penMove.png");
	}

	private void addCursor(String name, String file){
		URL resource = getClass().getResource("/stormgate/editor/controls/cursors/" + file);
		Image image = toolkit.getImage(resource);
		
		Point hotSpot = new Point(16, 16);
		Cursor c = toolkit.createCustomCursor(image, hotSpot, "Custom");
		
		cursors.put(name, c);
	}

	public Cursor getCursor(){
		return mouseOver.getCursor();
	}
	
	public Cursor getCursor(String name){
		return cursors.get(name);
	}

	public void setCursor(String name){
		mouseOver.setCursor( cursors.get(name) );
	}

	public void setCursor(Cursor cursor){
		mouseOver.setCursor( cursor );
	}

	public void setDefault(){
		setCursor( new Cursor(Cursor.DEFAULT_CURSOR) );
	}
}
