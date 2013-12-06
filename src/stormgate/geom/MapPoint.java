package stormgate.geom;

public class MapPoint {
	
	private int x;
	private int y;
	
	public static int tileSize = 1000;
	
	public MapPoint(int x, int y){
		this.x = x;
		this.y = y;
	}

	public MapPoint(int tileX, int tileY, int pointX, int pointY)
	{
		this.x = tileX * tileSize + pointX;
		this.y = tileY * tileSize + pointY;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public void setX(int x)
	{
		this.x = x;
	}
	
	public void setY(int y)
	{
		this.y = y;
	}
	
	public int getTileX()
	{
		//return (int) Math.floor(1.0*x/tileSize);
		return x/tileSize;
	}
	
	public int getTileY()
	{
		//return (int) Math.floor(1.0*y/tileSize);
		return y/tileSize;
	}
	
	public int getLocalX()
	{
		return x%tileSize;
	}
	
	public int getLocalY()
	{
		return y%tileSize;
	}

	public boolean equals(MapPoint p)
	{
		return x == p.x && y == p.y;
	}
}