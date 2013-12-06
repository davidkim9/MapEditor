package stormgate.data;

import java.io.File;
import stormgate.geom.MapPoint;

/**
 *
 * @author David
 */
public class MapEntity extends MapGraphic
{

	private int type = 0;
	public File sound;
	public int radius;
	public String map;
	public int tileX;
	public int tileY;
	public int x;
	public int y;
	public boolean unitType;
	public int id;
	public static final int SOUND = 1;
	public static final int BEACON = 2;
	public static final int UNIT = 3;

	public static MapEntity makeEntity(int type)
	{
		//Use as constructor
		String url = null;
		if (type == SOUND) {
			url = "default/sound.png";
		} else if (type == BEACON) {
			url = "default/teleport.png";
		} else if (type == UNIT) {
			url = "default/unit.png";
		}
		if (url != null) {
			MapEntity entity = new MapEntity(url);
			entity.type = type;
			return entity;
		}
		return null;
	}

	public int getType(){
		return type;
	}

	private MapEntity(String url)
	{
		super(url);
	}

	private MapEntity(String url, MapPoint point)
	{
		super(url, point);
	}

	@Override
	public MapEntity clone()
	{
		MapEntity clone = new MapEntity(getURL());
                clone.type = type;
		clone.setPoint(getPoint());
		clone.sound = sound;
		clone.radius = radius;
		clone.sound = sound;
		clone.radius = radius;
		clone.map = map;
		clone.tileX = tileX;
		clone.tileY = tileY;
		clone.x = x;
		clone.y = y;
		clone.unitType = unitType;
		clone.id = id;
		return clone;
	}
}
