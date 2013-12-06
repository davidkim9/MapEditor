package stormgate.editor.ui.forms.graphic;

import java.awt.Rectangle;
import java.util.ArrayList;
import stormgate.data.GraphicLevel;
import stormgate.data.MapGraphic;
import stormgate.geom.MapPoint;

public class Level {
	
	private ArrayList<MapGraphic> graphics;

	public Level()
	{
		graphics = new ArrayList<MapGraphic>();
	}

	public ArrayList<MapGraphic> getGraphics()
	{
		return graphics;
	}

	public void removeAll()
	{
		graphics = new ArrayList<MapGraphic>();
	}

	private int getIndex(MapGraphic g)
	{
		MapPoint graphicPoint = g.getPoint();
		for(int i = 0; i < graphics.size(); i++){
			MapGraphic graphic = graphics.get(i);
			MapPoint point = graphic.getPoint();
			if(point.getY() > graphicPoint.getY()){
				return i;
			}
		}
		return graphics.size();
	}

	public void updateLevel(MapPoint cameraLocation, GraphicLevel level, Rectangle screenRect)
	{
    //FIX

		int camX = (int) (cameraLocation.getX() - screenRect.getWidth() / 2);
		int camY = (int) (cameraLocation.getY() - screenRect.getHeight() / 2);

		//Update screen bounds to alittle higher
		Rectangle screenBounds = new Rectangle();
		screenBounds.x = screenRect.x - 100;
		screenBounds.y = screenRect.y - 100;
		screenBounds.width = screenRect.width + 200;
		screenBounds.height = screenRect.height + 200;

		ArrayList<MapGraphic> tileGraphics = level.graphics;
		for(int i = 0; i < tileGraphics.size(); i++)
		{

			MapGraphic graphic = tileGraphics.get(i);
			MapPoint p = graphic.getPoint();
			if(p != null){
				int objectX = p.getX() - camX;
				int objectY = p.getY() - camY;

				if(screenBounds.contains( objectX, objectY ) || true)
				{
					if(!graphic.onScreen())
					{
						//data.setImage(graphic.getResource());
						graphic.addToScreen();
						int graphicIndex = getIndex(graphic);
						graphics.add(graphicIndex, graphic);
						graphic.setLevel(this);
					}
				}else if(graphic.onScreen())
				{
					graphic.removeFromScreen();
					graphics.remove(graphic);
					graphic.setLevel(null);
				}
			}else{
				System.err.println("Point not found on graphic " + graphic);
				stormgate.log.Log.addLog("Point not found on graphic " + graphic);
			}
		}
	}

	public void removeGraphics(GraphicLevel level)
	{
		ArrayList<MapGraphic> tileGraphics = level.graphics;
		for(int i = 0; i < tileGraphics.size(); i++)
		{
			MapGraphic graphic = tileGraphics.get(i);
			if(graphic.onScreen()){
				graphic.removeFromScreen();
				graphics.remove(graphic);
				graphic.setLevel(null);
			}
		}

	}

	public void removeGraphic(MapGraphic graphic)
	{
		if(graphic.onScreen())
		{
			graphic.removeFromScreen();
			graphics.remove(graphic);
			graphic.setLevel(null);
		}
	}

}