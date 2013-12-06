package stormgate.common;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;

/**
 *
 * @author David Kim
 */
public class ScreenInformation {

	public static Rectangle getScreenBounds() {
		GraphicsDevice[] screens = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
		int x = 0;
		int y = 0;
		int w = 0;
		int h = 0;
		for(int i = 0 ; i < screens.length; i++ ){
			Rectangle bounds = screens[i].getDefaultConfiguration().getBounds();
			if(bounds.x < x) x = bounds.x;
			if(bounds.y < y) y = bounds.y;
			if(bounds.width > w) w = bounds.width;
			if(bounds.height > h) h = bounds.height;
		}
		return new Rectangle(x, y, w, h);
	}

	public static Rectangle getScreenBounds(Point p)
	{
		GraphicsDevice[] screens = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
		for(int i = 0 ; i < screens.length; i++ ){
			Rectangle bounds = screens[i].getDefaultConfiguration().getBounds();
			if(bounds.x < p.x && bounds.y < p.y && (bounds.x + bounds.width) > p.x && (bounds.y + bounds.height) > p.y){
				return bounds;
			}
		}
		return new Rectangle(0,0,0,0);
	}
}
