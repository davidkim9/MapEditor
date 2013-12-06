package stormgate.data;

import java.util.ArrayList;
import stormgate.image.ImageManager;

public class Tile
{

	private int x;
	private int y;
	private boolean screen = false;
	private GraphicLevel[] levels;
	private ArrayList<MapEntity> entities;
	//Tile Data
	//Tile Background
	private String background;
	//Tile Graphics is in URL String
	private String[][] tileGraphics;
	//Zone Data
	private ArrayList<MapZone> zones;
	public final static int divideX = 5;
	public final static int divideY = 5;
	public final static int TILE_LEVEL = 0;
	public final static int OBJECT_LEVEL = 1;
	public final static int SKY_LEVEL = 2;
	public final static int tileCell = 1000;
	public static String defaultTile = "library/Tiles/tile1.png";
	public static String defaultBackground = null;
	private boolean enabled = false;

	public Tile()
	{
		this.x = 0;
		this.y = 0;
		createLevel();
	}

	public Tile(int x, int y)
	{
		this.x = x;
		this.y = y;
		createLevel();
	}

	public final void createLevel()
	{
		entities = new ArrayList<MapEntity>(0);

		zones = new ArrayList<MapZone>(0);

		tileGraphics = new String[divideY][divideX];

		levels = new GraphicLevel[3];

		for (int i = 0; i < levels.length; i++) {
			levels[i] = new GraphicLevel();
		}

		if (x >= 0 && y >= 0) {
			//tileGraphics[0][0] = "library/Tiles/tile1.png";

			background = defaultBackground;

			for (int i = 0; i < tileGraphics.length; i++) {
				for (int j = 0; j < tileGraphics[i].length; j++) {
					tileGraphics[i][j] = defaultTile;
				}
			}

			//levels[1].graphics.add(new MapGraphic(manager, "library/Trees/tree10.png", new MapPoint(x * 1000 + 500, y * 1000 + 500)));
			//levels[2].graphics.add(new MapGraphic(manager, "library/Trees/tree10.1.png", new MapPoint(x * 1000 + 500, y * 1000 + 600)));
		}
	}

	@Override
	protected void finalize() throws Throwable
	{
		try {
			for (int i = 0; i < levels.length; i++) {
				levels[i] = null;
			}
			entities.clear();
			entities = null;
			for(int i = 0 ; i < zones.size(); i++){
				MapZone z = zones.get(i);
				z.removeTile(this);
			}
			zones.clear();
			zones = null;
			tileGraphics = null;
		} finally {
			super.finalize();
		}
	}

	public String getTileBackground()
	{
		return background;
	}

	public void setTileBackground(String background)
	{
		this.background = background;
	}

	public String[][] getTileGraphics()
	{
		return tileGraphics;
	}

	public void setTileGraphics(String[][] tileGraphics)
	{
		this.tileGraphics = tileGraphics;
	}

	public GraphicLevel getLevel(int i)
	{
		if (i >= 0 && i < levels.length) {
			return levels[i];
		}

		return null;
	}

	public void addToScreen()
	{
		screen = true;
	}

	public void removeFromScreen()
	{
		screen = false;
	}

	public boolean onScreen()
	{
		return screen;
	}

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}

	public void setTileGraphic(int localX, int localY, String selected)
	{
		tileGraphics[localY][localX] = ImageManager.fixUrl(selected);
	}

	public boolean contains(MapZone zone)
	{
		return zones.contains(zone);
	}

	public ArrayList<MapZone> getZones()
	{
		return zones;
	}

	public void addZone(MapZone zone)
	{
		zones.add(zone);
	}

	public void removeZone(MapZone zone)
	{
		zones.remove(zone);
	}

	public MapZone getZoneID(int id)
	{
		for (int i = 0; i < zones.size(); i++) {
			MapZone zone = zones.get(i);
			if (zone.getID() == id) {
				return zone;
			}
		}
		return null;
	}

	public boolean checkID(int id)
	{
		for (int i = 0; i < zones.size(); i++) {
			MapZone zone = zones.get(i);
			if (zone.getID() == id) {
				return false;
			}
		}
		return true;
	}

	public int getNextID()
	{
		int nextID = 0;

		while (!checkID(nextID)) {
			nextID++;
		}

		return nextID;
	}

	public ArrayList<MapEntity> getEntities()
	{
		return entities;
	}

	public void addEntity(MapEntity entity)
	{
		entities.add(entity);
		levels[1].addGraphic(entity);
	}

	public void removeEntity(MapEntity entity)
	{
		entities.remove(entity);
		levels[1].removeGraphic(entity);
	}

	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}

	public boolean getEnabled()
	{
		return enabled;
	}

	@Override
	public String toString()
	{
		return "[Tile x=" + x + " y=" + y + "]";
	}
}
