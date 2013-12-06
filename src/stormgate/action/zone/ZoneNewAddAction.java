package stormgate.action.zone;

import stormgate.action.Action;
import stormgate.data.MapZone;
import stormgate.editor.tool.data.ZoneToolNewMode;
import stormgate.geom.MapPoint;

/**
 *
 * @author David
 */
public class ZoneNewAddAction implements Action {

	private ZoneToolNewMode mode;
	private MapZone zone;
	private MapPoint point;

	public ZoneNewAddAction(ZoneToolNewMode mode, MapZone zone, MapPoint point){
		this.mode = mode;
		this.zone = zone;
		this.point = point;
	}

	public void undo()
	{
		mode.setEdit(zone);
		zone.removePoint(point);
	}

	public void perform()
	{
		mode.setEdit(zone);
		zone.addPoint(point);
	}

}
