package stormgate.data;

import java.io.File;
import java.util.ArrayList;
import stormgate.geom.MapPoint;

/**
 *
 * @author David
 */
public class MapZone
{
	//private MapPoint location;

	private int id;
	private ArrayList<MapPoint> polygon;
	private ArrayList<Tile> tiles;

	//ZONE PROPERTIES
	public String name = null;
	public boolean collision = true;
	public boolean los = true;
	public boolean skillBank = false;
	public boolean town = false;

	//Sound
	public File music;
	public ArrayList<File> ambient;
	public ArrayList<File> footstep;
	
        //Walk Over
        public File walkover;
        
	//Spawn Info Missing
	public ArrayList<SpawnData> spawn;

	public MapZone()
	{
		id = -1;
		polygon = new ArrayList<MapPoint>();
		ambient = new ArrayList<File>();
		footstep = new ArrayList<File>();
		spawn = new ArrayList<SpawnData>();
	}

	public MapZone(int id)
	{
		this.id = id;
		polygon = new ArrayList<MapPoint>();
		ambient = new ArrayList<File>();
		footstep = new ArrayList<File>();
		spawn = new ArrayList<SpawnData>();
	}

	@Override
	public MapZone clone(){
		MapZone zone = new MapZone();
		ArrayList<MapPoint> poly = new ArrayList<MapPoint>(polygon.size());
		for(int i = 0; i < polygon.size(); i++){
			MapPoint point = polygon.get(i);
			MapPoint newPoint = new MapPoint(point.getX(), point.getY());
			poly.add(i, newPoint);
		}
		zone.polygon = poly;
		return zone;
	}

	public void setTiles(ArrayList<Tile> tiles)
	{
		this.tiles = tiles;
	}

	public ArrayList<Tile> getTiles()
	{
		return tiles;
	}

	public void addTile(Tile tile)
	{
		if(tiles == null){
			tiles = new ArrayList<Tile>(1);
		}
		tiles.add(tile);
	}

	public void removeTile(Tile tile)
	{
		if(tiles != null){
			tiles.remove(tile);
		}
	}

	public void setID(int id)
	{
		this.id = id;
	}

	public int getID()
	{
		return id;
	}

	public MapPoint getPoint(int i)
	{
		return polygon.get(i);
	}

	public int getPointIndex(MapPoint p)
	{
		return polygon.indexOf(p);
	}

	public void addPoint(MapPoint p)
	{
		polygon.add(p);
	}

	public void addPoint(int index, MapPoint p)
	{
		polygon.add(index, p);
	}

	public void offsetZone(MapPoint o)
	{

		int offsetX = o.getX();
		int offsetY = o.getY();

		for (int i = 0; i < polygon.size(); i++) {
			MapPoint mVector = polygon.get(i);
			mVector.setX(mVector.getX() + offsetX);
			mVector.setY(mVector.getY() + offsetY);
		}
	}

	public ArrayList<MapPoint> getPolygon()
	{
		return polygon;
	}

	public void removePoint(MapPoint point)
	{
		polygon.remove(point);
	}

	/**
	 * Checks if the point is in the zone
	 * @param x X
	 * @param y Y
	 * @return
	 */
	public boolean pointInPolygon(int x, int y) {
		boolean c = false;
		int i,j;
		for (i = 0, j = polygon.size()-1; i < polygon.size(); j = i++) {
			MapPoint p1 = polygon.get(i);
			MapPoint p2 = polygon.get(j);
			int p1x = p1.getX();
			int p1y = p1.getY();
			int p2x = p2.getX();
			int p2y = p2.getY();
			if ((((p1y <= y) && (y < p2y)) ||
				((p2y <= y) && (y < p1y))) &&
				(x < (p2x - p1x) * (y - p1y) / (p2y - p1y) + p1x)){
				c = !c;
			}
		}
		return c;
	}

	public int boundArea() {
		int lowX = -1;
		int lowY = -1;
		int highX = -1;
		int highY = -1;
		for (int i = 0; i < polygon.size(); i++) {
			MapPoint v = polygon.get(i);
			int x = v.getX();
			int y = v.getY();
			if(lowX == -1 || x < lowX){
				lowX = x;
			}
			if(lowY == -1 || y < lowY){
				lowY = y;
			}
			if(highX == -1 || x > highX){
				highX = x;
			}
			if(highY == -1 || y > highY){
				highY = y;
			}
		}
		int width = highX - lowX;
		int height = highY - lowY;
		return width * height;
	}

	public int numPoints()
	{
		return polygon.size();
	}

	public boolean equals(MapZone zone)
	{
		//Check polygon shape
		ArrayList<MapPoint> p = zone.polygon;
		if(polygon.size() == p.size()){

			for(int i = 0; i < polygon.size(); i++)
			{
				if( !polygon.get(i).equals( p.get(i) ) ) {
					return false;
				}
			}
		}else{
			return false;
		}
		
		//Compare data

		return true;
	}
}
