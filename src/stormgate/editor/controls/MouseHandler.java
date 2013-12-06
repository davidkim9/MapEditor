/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package stormgate.editor.controls;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import stormgate.editor.data.EditorData;
import stormgate.editor.tool.ToolInterface;
import stormgate.geom.MapPoint;

/**
 *
 * @author David
 */
public class MouseHandler implements MouseListener, MouseMotionListener, MouseWheelListener {

	private EditorData data;

	private boolean mouseDown = false;

	private int mouseDownX = 0;
	private int mouseDownY = 0;

	private int panX = 0;
	private int panY = 0;
	private ToolInterface tool;

	public MouseHandler(EditorData data) {
		this.data = data;
	}

	public boolean isMouseDown(){
		return mouseDown;
	}

	public void setTool(ToolInterface tool) {
		this.tool = tool;
	}

	public void mouseClicked(MouseEvent e) {
		if(tool != null)
			tool.mouseClicked(e);
	}

	public void mousePressed(MouseEvent e) {
		mouseDownX = e.getX();
		mouseDownY = e.getY();
		mouseDown = true;

		if(data.pan){
			data.cursors.setCursor("grabbing");
			MapPoint camera = data.getCameraLocation();
			panX = camera.getX();
			panY = camera.getY();
		}else{
			if(tool != null)
				tool.mousePressed(e);
		}

	}

	public void mouseReleased(MouseEvent e) {
		if(data.pan){
			data.cursors.setCursor("grab");
		}else{
			if(tool != null)
				tool.mouseReleased(e);
		}
		mouseDown = false;
	}

	public void mouseEntered(MouseEvent e) {
		if(tool != null)
			tool.mouseEntered(e);
	}

	public void mouseExited(MouseEvent e) {
		if(tool != null)
			tool.mouseExited(e);
	}

	public void mouseDragged(MouseEvent e) {
		if(data.pan){
			int mouseX = e.getX();
			int mouseY = e.getY();

			int xDiff = mouseDownX - mouseX;
			int yDiff = mouseDownY - mouseY;

			float zoom = data.getZoom();

			int dragX = panX + (int) (xDiff / zoom);
			int dragY = panY + (int) (yDiff / zoom);

			data.setCamera(dragX, dragY);
			
			data.refresh();
		}else{
			if(tool != null)
				tool.mouseDragged(e);
		}
	}

	public void mouseMoved(MouseEvent e) {
		if(data.pan){
			data.cursors.setCursor("grab");
		}else{
			if(tool != null)
				tool.mouseMoved(e);
		}
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		float zoomAmount = -e.getWheelRotation() * 0.1f;

		float oldZoom = data.getZoom();

		float zoom = oldZoom + zoomAmount;

		//Floating point offset fix + comfort scroll without 1%
		//Also make sures the scroll won't bring the zoom to less than 10%
		if((oldZoom == 0.001f && zoomAmount > 0) || zoom < 0.05f){
			zoom = 0.05f;
		}

		int mouseX = e.getX();
		int mouseY = e.getY();

		MapPoint oldPos = data.convertToMap(mouseX, mouseY);

		if(zoom < 1.1f && zoom > 0.9f){
			zoom = 1f;
		}

		data.setZoom(zoom);

		//Mouse relative
		MapPoint newPos = data.convertToMap(mouseX, mouseY);

		MapPoint camera = data.getCameraLocation();
		camera.setX(camera.getX() - newPos.getX() + oldPos.getX());
		camera.setY(camera.getY() - newPos.getY() + oldPos.getY());
		
		data.refresh();
	}
}