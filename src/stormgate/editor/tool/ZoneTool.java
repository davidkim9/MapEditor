package stormgate.editor.tool;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import stormgate.editor.data.EditorData;
import stormgate.editor.tool.data.ZoneToolEditMode;
import stormgate.editor.tool.data.ZoneToolMode;
import stormgate.editor.tool.data.ZoneToolNewMode;
import stormgate.editor.tool.data.ZoneToolSelectMode;
import stormgate.editor.tool.data.ZoneToolPolygonMode;
import stormgate.editor.tool.panel.ToolPanel;
import stormgate.editor.tool.panel.ZoneToolPanel;

/**
 *
 * @author David
 */
public class ZoneTool extends Tool
{
	private ZoneToolPanel panel;
	private ZoneToolMode mode;
	private ZoneToolPolygonMode polygonMode;
	private ZoneToolNewMode newMode;
	private ZoneToolSelectMode selectMode;
	private ZoneToolEditMode editMode;

	//private MapZone[] clipboard;
	public ZoneTool(EditorData data)
	{
		super(data);

		panel = new ZoneToolPanel();
		panel.setTool(this);
		panel.setData(data);

		newMode = new ZoneToolNewMode(this);
		newMode.setData(data);

		selectMode = new ZoneToolSelectMode(this);
		selectMode.setData(data);

		editMode = new ZoneToolEditMode(this);
		editMode.setData(data);
		
		polygonMode = new ZoneToolPolygonMode(this);
		polygonMode.setData(data);

		mode = polygonMode;
	}

	@Override
	public ToolPanel getPanel()
	{
		return panel;
	}

	/*
	public void setMode(boolean mode){
	this.mode = mode;
	data.refresh();
	}
	 */
	public void doubleClickGraphic(String resource)
	{
	}

	public void paint(Graphics g)
	{
		/*
		if(mode){
		g.setColor(Color.red);
		}else{
		g.setColor(Color.cyan);
		}
		g.fillRect(0, 0, 100, 100);
		 */
		if (mode != null) {
			mode.paint(g);
		}
	}

	public void cut()
	{
		if(mode == polygonMode){
			//copy();
			polygonMode.cut();
		}
	}

	public void copy()
	{
		//Copy selected zone(s)
		if(mode == polygonMode){
			polygonMode.copy();
		}
	}

	public void paste()
	{
		//Will only paste the zone(if there is one)
		//Switch to zone selection
		setMode(polygonMode);
		polygonMode.paste();
		//clipboard
		//ZoneAddAction(ZoneToolNewMode mode, Map map, MapZone zone)
	}

	public void mousePressed(MouseEvent e)
	{
		if (mode != null) {
			mode.mousePressed(e);
		}
	}

	public void mouseReleased(MouseEvent e)
	{
		if (mode != null) {
			mode.mouseReleased(e);
		}
	}

	public void mouseDragged(MouseEvent e)
	{
		mouseMoved(e);
	}

	public void mouseMoved(MouseEvent e)
	{
		if (mode != null) {
			mode.mouseMoved(e);
		}
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		if (mode != null) {
			mode.keyPressed(e);
		}
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		if (mode != null) {
			mode.keyReleased(e);
		}
	}

	public ZoneToolMode getMode()
	{
		return mode;
	}

	/*
	 * Should only be called from the action
	 */
	public void setMode(ZoneToolMode mode)
	{
		if(mode == polygonMode){
			panel.setPolygonMode();
		}else if(mode == selectMode){
			panel.setSelectMode();
		}else if(mode == editMode){
			panel.setEditMode();
		}else if(mode == newMode){
			panel.setNewMode();
		}
		this.mode = mode;
		updatePanel();
	}

	public void updatePanel(){
		panel.updatePanel();
	}

	private void changeMode(ZoneToolMode mode)
	{
		this.mode.deselect();
		//ZoneModeChangeAction m = new ZoneModeChangeAction(this, data, mode);
		//data.performAction(m);
		data.zoneModeChange(mode);
	}

	public void setPolygonMode()
	{
		changeMode(polygonMode);
	}

	public void setSelectMode()
	{
		changeMode(selectMode);
	}

	public void setEditMode()
	{
		changeMode(editMode);
	}

	public void setNewMode()
	{
		changeMode(newMode);
	}

	@Override
	public void deselect()
	{
		mode.deselect();
		data.cursors.setDefault();
	}

	public static final int NEW = 1;
	public static final int POLYGON = 2;
	public static final int POINT = 3;
	public static final int EDIT = 4;

	public int getModeType() {
		if(mode == polygonMode){
			return POLYGON;
		}else if(mode == selectMode){
			return POINT;
		}else if(mode == editMode){
			return EDIT;
		}else if(mode == newMode){
			return NEW;
		}
		return 0;
	}

	public void clearNewZone()
	{
		newMode.clear();
	}
	
	public void update()
	{
		panel.updatePanel();
	}

	public void resetTool()
	{
		newMode.clear();
		polygonMode.clear();
		selectMode.deselect();
	}
}
