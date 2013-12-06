/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package stormgate.data;

import stormgate.geom.MapPoint;
import stormgate.image.ImageManager;

/**
 *
 * @author David
 */
public class MapNode {

	private String url;
	private MapPoint point;
	private boolean onScreen;

	public MapNode(String url){
		this.url = ImageManager.fixUrl(url);
	}

	public MapNode(String url, MapPoint point){
		this.point = point;
		this.url = ImageManager.fixUrl(url);
	}

	public String getURL(){
		return url;
	}

	public void addToScreen(){
		onScreen = true;
	}

	public void removeFromScreen(){
		onScreen = false;
	}

	public boolean onScreen(){
		return onScreen;
	}

	public MapPoint getPoint()
	{
		return point;
	}

	public void setPoint(MapPoint point)
	{
		this.point = point;
		//Update Graphic
	}
}
