package stormgate.filter;

import stormgate.editor.ui.forms.EditorForm;

public class Filter {
	private float zoom = 1.0f;
	public boolean graphics = true;
	public boolean zones = false;
	public boolean selected = true;
	public boolean entities = false;
	public boolean showHidden = false;

	public boolean zoneTool = false;
	public boolean entityTool = false;
	
	public boolean grid = false;
	public boolean snap = false;

	private EditorForm form;
	
	public boolean showDepth = true;

	public boolean tileEnable = false;
	
	public Filter(EditorForm form){
		this.form = form;
	}
	
	public void setZoom(float value){
		zoom = Math.max(value, 0.01f);
		if(form != null)
			form.setZoom();
	}
	public float getZoom(){
		return zoom;
	}
}
