/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package stormgate.editor.tool;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import stormgate.editor.data.EditorData;
import stormgate.editor.tool.panel.DefaultPanel;
import stormgate.editor.tool.panel.ToolPanel;
import stormgate.filter.Filter;

public class DefaultTool implements ToolInterface {

	protected EditorData data;
	protected String selected;
	protected Filter filter;
	protected int offsetX;
	protected int offsetY;

	private ToolPanel panel;

	public DefaultTool(){
		panel = new DefaultPanel();
	}

	public void selectGraphic(String resource) {
		selected = resource;
	}

	public void setEditorData(EditorData data) {
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

	public void paint(Graphics g) {

	}

	public void mouseClicked(MouseEvent e) {
		
	}

	public void mousePressed(MouseEvent e) {
		
	}

	public void mouseReleased(MouseEvent e) {
		
	}

	public void mouseEntered(MouseEvent e) {
		
	}

	public void mouseExited(MouseEvent e) {
		
	}

	public void keyTyped(KeyEvent e) {
		
	}

	public void keyPressed(KeyEvent e) {
		
	}

	public void keyReleased(KeyEvent e) {
		
	}

	public void mouseDragged(MouseEvent e) {

	}

	public void mouseMoved(MouseEvent e) {

	}

	public void doubleClickGraphic(String resource)
	{

	}

	public ToolPanel getPanel()
	{
		return panel;
	}
	
	public void cut()
	{
	}

	public void copy()
	{
	}

	public void paste()
	{
	}

	public void select()
	{
	}

	public void deselect()
	{
	}

	public void update()
	{
	}
}
