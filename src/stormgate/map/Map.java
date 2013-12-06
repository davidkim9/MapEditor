package stormgate.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import stormgate.data.MapZone;
import stormgate.data.Tile;
import stormgate.editor.data.EditorData;
import stormgate.geom.MapPoint;
import stormgate.io.xml.XMLProject;

public class Map
{

	public static int loadTiles = 128;
	public static int loadDistance = 20;
	private Tile[][] tile;
	//Edited Tile List
	private HashMap<String, Tile> edited;
	private int loadX = -loadTiles / 2;
	private int loadY = -loadTiles / 2;
	private boolean reloadingMap = false;
	private int tileLoad = 0;
	private int loadAmount = 0;
	private MapPoint cameraLocation;
	private String mapName = "map";
	private String mapFile;
	private String path = null;
	private MapLoader loaderRunnable;
	private Thread loaderThread;
        private EditorData data;

	public Map(EditorData data)
	{
                this.data = data;
		cameraLocation = new MapPoint(0, 0);
		tile = new Tile[loadTiles][loadTiles];
		edited = new HashMap<String, Tile>();
		createMap();

		loaderRunnable = new MapLoader(this, path);
		loaderThread = new Thread(loaderRunnable);
		loaderThread.start();
	}

	@Override
	protected void finalize() throws Throwable
	{
		try {
			loaderRunnable.stopThread();
			loaderThread.interrupt();
		} finally {
			super.finalize();
		}
	}

	public void setMapPath(String path)
	{
		this.path = path;
		loaderRunnable.setPath(path);
	}

	public String getMapPath()
	{
		return path;
	}

	private void createMap()
	{

		tile = new Tile[loadTiles][loadTiles];

		for (int y = 0; y < loadTiles; y++) {
			for (int x = 0; x < loadTiles; x++) {
				//Make default
				tile[y][x] = new Tile(loadX + x, loadY + y);
			}
		}
	}

	public MapPoint getCameraLocation()
	{
		return cameraLocation;
	}

	public void setCameraLocation(MapPoint location)
	{
		//cameraLocation = location;
		cameraLocation.setX(location.getX());
		cameraLocation.setY(location.getY());
		checkLoadMap();
	}

	public int loadedX()
	{
		return loadX;
	}

	public int loadedY()
	{
		return loadY;
	}

	public void saveTiles()
	{
		//SAVE ALL TILES AND CLEAR EDITED LIST
		Set entries = edited.keySet();

		Iterator<String> itr = entries.iterator();

		while (itr.hasNext()) {
			String key = itr.next();
			Tile edit = edited.get(key);
			//edit.save(null);
			XMLProject.saveTile(edit, path);
		}

		edited = new HashMap<String, Tile>();
	}

	public void editTiles(MapZone z)
	{
		ArrayList<Tile> zoneTiles = z.getTiles();
		//ArrayList<Tile> tiles = getTiles(z);
		//setZoneTiles(z, tiles);
		//ArrayList<Tile> zoneTiles = tiles;

		if (zoneTiles != null) {
			for (int i = 0; i < zoneTiles.size(); i++) {
				editTile(zoneTiles.get(i));
			}
		}
	}

	public void editTile(Tile t)
	{
		//Add tile to edited list
		int x = t.getX();
		int y = t.getY();
		String keyName = x + "_" + y;
		edited.put(keyName, t);
	}

	public Tile getTileNoLoad(int x, int y)
	{
		int offsetX = x - loadX;
		int offsetY = y - loadY;

		Tile t = null;
		if (offsetX >= 0 && offsetY >= 0 && tile.length > offsetY && tile[offsetY].length > offsetX) {
			t = tile[offsetY][offsetX];
		}
		if (t == null) {
			String keyName = x + "_" + y;
			t = edited.get(keyName);
		}
		return t;
	}

	public Tile getTile(int x, int y)
	{
		int offsetX = x - loadX;
		int offsetY = y - loadY;

		Tile t = null;
		if (offsetX >= 0 && offsetY >= 0 && tile.length > offsetY && tile[offsetY].length > offsetX) {
			t = tile[offsetY][offsetX];
		} else {
			String keyName = x + "_" + y;
			t = edited.get(keyName);
			if (t != null) {
				return t;
			}
			return loadTile(x, y);
		}
		return t;
	}

	public boolean reloading()
	{
		return reloadingMap;
	}

	public void loadMap()
	{
		loaderRunnable.clearQueue();

		loadX = cameraLocation.getTileX() - loadTiles / 2;
		loadY = cameraLocation.getTileY() - loadTiles / 2;

		tileLoad = 0;
		loadAmount = loadTiles * loadTiles;

		createMap();
		reloadingMap = true;

		for (int i = loadY; i < loadY + loadTiles; i++) {
			for (int j = loadX; j < loadX + loadTiles; j++) {
				setTile(j, i);

				//tile[i - loadY][j - loadX] = new Tile(j, i);
			}
		}
	}

	public void removeAll()
	{
		for (int y = 0; y < loadTiles; y++) {
			for (int x = 0; x < loadTiles; x++) {
				if (tile != null && loadDistance > y && tile[y] != null && tile[y][x] != null) {
					Tile t = tile[y][x];
					if (t != null) {
						//Remove tile and contents inside
						//screen.removeTileContents(t);
					}
				}
			}
		}
	}

	public void checkLoadMap()
	{
		if (!reloading()) {
			if (cameraLocation.getTileX() < loadX + loadDistance
					|| cameraLocation.getTileX() >= (loadX + loadTiles - loadDistance)
					|| cameraLocation.getTileY() < loadY + loadDistance
					|| cameraLocation.getTileY() >= (loadY + loadTiles - loadDistance)) {

				//loadX = cameraLocation.getTileX() - loadTiles / 2;
				//loadY = cameraLocation.getTileY() - loadTiles / 2;
				loadMap();
			}
		}
	}

	private void setTile(int x, int y)
	{
		String keyName = x + "_" + y;
		Tile t = edited.get(keyName);
		if (t != null) {
			tile[y - loadY][x - loadX] = t;
                        tileLoad++;
		} else {
			tile[y - loadY][x - loadX] = loadTile(x, y);
		}
		//tileLoad++;
		checkLoad();
	}

	protected synchronized void setTile(Tile t, int x, int y)
	{
		int offsetX = x - loadX;
		int offsetY = y - loadY;

		if (offsetX >= 0 && offsetY >= 0 && tile.length > offsetY && tile[offsetY].length > offsetX) {
			tile[y - loadY][x - loadX] = t;
			tileLoad++;
			checkLoad();
		}
	}

	private void queueLoad(int x, int y)
	{
		loaderRunnable.addLoad(x, y);
	}

	private Tile loadTile(int x, int y)
	{
		if (path != null) {
			/*
			Tile t = XMLProject.loadTile(this, path, x, y);

			if (t != null) {
			return t;
			}
			 */
			queueLoad(x, y);
		}
                tileLoad++;
		return new Tile(x, y);
	}

	private void checkLoad()
	{
		if (tileLoad >= loadAmount) {
			reloadingMap = false;
		}
	}

	public ArrayList<Tile> getTilesNoLoad(MapZone zone)
	{//System.out.println("^ offset is " + (loadX) + " , " + (loadY) );
		//Find the tiles needed for the map
		ArrayList<MapPoint> polygon = zone.getPolygon();
		int top = -1;
		int left = -1;
		int bottom = -1;
		int right = -1;
		//Get boundaries
		for (int i = 0; i < polygon.size(); i++) {
			MapPoint p = polygon.get(i);
			int x = p.getTileX();
			int y = p.getTileY();
			if (top == -1 || y < top) {
				top = y;
			}
			if (bottom == -1 || y > bottom) {
				bottom = y;
			}
			if (left == -1 || x < left) {
				left = x;
			}
			if (right == -1 || x > right) {
				right = x;
			}
		}
		//Scan the map
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		for (int y = top; y <= bottom; y++) {
			for (int x = left; x <= right; x++) {
				Tile t = getTileNoLoad(x, y);
				//Add a condition here to check if the polygon is in the tile
				tiles.add(t);
			}
		}

		return tiles;
	}

	public ArrayList<Tile> getTiles(MapZone zone)
	{
		//Find the tiles needed for the map
		ArrayList<MapPoint> polygon = zone.getPolygon();
		int top = -1;
		int left = -1;
		int bottom = -1;
		int right = -1;
		//Get boundaries
		for (int i = 0; i < polygon.size(); i++) {
			MapPoint p = polygon.get(i);
			int x = p.getTileX();
			int y = p.getTileY();
			if (top == -1 || y < top) {
				top = y;
			}
			if (bottom == -1 || y > bottom) {
				bottom = y;
			}
			if (left == -1 || x < left) {
				left = x;
			}
			if (right == -1 || x > right) {
				right = x;
			}
		}
		//Scan the map
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		for (int y = top; y <= bottom; y++) {
			for (int x = left; x <= right; x++) {
				Tile t = getTile(x, y);
				//Add a condition here to check if the polygon is in the tile
				tiles.add(t);
				if (t == null) {
					stormgate.log.Log.addLog("Tile " + x + ", " + y + " doesnt exist!");
				}
			}
		}

		return tiles;
	}

	public void setZoneTiles(MapZone zone)
	{
		setZoneTiles(zone, getTiles(zone));
	}

	public void setZoneTiles(MapZone zone, ArrayList<Tile> tiles)
	{
		removeZone(zone);
		//Assign tiles needed to the zone
		zone.setTiles(tiles);
		//Find and set the next highest id it can use from all the tiles
		int highest = 0;
		for (int i = 0; i < tiles.size(); i++) {
			Tile t = tiles.get(i);

			int id = t.getNextID();
			if (id > highest) {
				highest = id;
			}
			//Add to each tile

			t.addZone(zone);
		}
		zone.setID(highest);
	}

	public void removeZone(MapZone zone)
	{
		//Find and set the next highest id it can use from all the tiles
		ArrayList<Tile> tiles = zone.getTiles();
		if (tiles != null) {
			for (int i = 0; i < tiles.size(); i++) {
				Tile t = tiles.get(i);

				//remove from each tile
				t.removeZone(zone);
			}
		}
		//Remove tiles for this zone
		zone.setTiles(null);

		zone.setID(-1);
	}

	public void copyTiles(String newPath)
	{
		if (path != null) {
			XMLProject.copyTiles(path, newPath);
		} else {
			XMLProject.deleteFiles(newPath);
		}
	}

	public void saveMapFile()
	{
		XMLProject.saveMap(this, mapFile);
	}

	public String getMapFile()
	{
		return mapFile;
	}

	public void setMapFile(String mapFile)
	{
		this.mapFile = mapFile;
	}

	public void setMapName(String mapName)
	{
		this.mapName = mapName;
	}

	public String getMapName()
	{
		return mapName;
	}

	public void destroyMap()
	{
		loaderRunnable.stopThread();
		loaderThread.interrupt();
		tile = null;
		edited = null;
	}
        
        public void refresh(){
            data.refresh();
        }
}
