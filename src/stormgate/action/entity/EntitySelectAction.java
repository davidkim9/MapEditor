package stormgate.action.entity;

import stormgate.action.Action;
import stormgate.data.MapEntity;
import stormgate.editor.tool.EntityTool;

/**
 *
 * @author David
 */
public class EntitySelectAction implements Action {

	EntityTool tool;
	MapEntity[] oldSelected;
	MapEntity[] selected;

	public EntitySelectAction(EntityTool tool, MapEntity[] selected)
	{
		this.tool = tool;
		this.selected = selected;

		oldSelected = tool.getSelectedEntities();
	}

	public void undo()
	{
		tool.setSelectedEntities(oldSelected);
	}

	public void perform()
	{
		tool.setSelectedEntities(selected);
	}
}
