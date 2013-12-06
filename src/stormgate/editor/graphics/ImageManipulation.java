package stormgate.editor.graphics;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * Class containing functions for manipulating and checking images.
 * @author Jesper "ChJees" Eriksson
 *
 */
public class ImageManipulation
{

	/**
	 * Checks if the specific region of the image contains Alpha.
	 * @param image The image to check.
	 * @param region The region of the image to check. If null it checks the whole image.
	 * @return Returns true if it finds a pixel with no Alpha.
	 */
	public static boolean containsNoAlpha(BufferedImage image, int x1, int y1, int x2, int y2)
	{
		//Checks
		if (image == null) {
			return false;
		}

		int imageWidth = image.getWidth(null);
		int imageHeight = image.getHeight(null);

		//System.out.println(x1+","+y1 +" "+ x2+","+y2 +" "+ rW+","+rH);
		//Make sure it is not out of bounds.
		Rectangle img = new Rectangle(0, 0, imageWidth, imageHeight);
		if (img.intersects(x1, y1, x2 - x1, y2 - y1)) {
			//Offsets
			if (x1 < 0) {
				x1 = 0;
			}
			if (x2 > imageWidth) {
				x2 = imageWidth;
			}
			if (y1 < 0) {
				y1 = 0;
			}
			if (y2 > imageHeight) {
				y2 = imageHeight;
			}
		} else {
			return false;
		}

		if (imageWidth > 0 && imageHeight > 0) {
			for (int y = y1; y < y2; y++) {
				for (int x = x1; x < x2; x++) {

					int px = image.getRGB(x, y);
					if (px != 0) {
						return true;
					}
				}
			}
		}

		return false;
	}
        
	/**
	 * Checks if the specific region of the image contains Alpha.
	 * @param image The image to check.
	 * @param region The region of the image to check. If null it checks the whole image.
	 * @return Returns true if it finds a pixel with no Alpha.
	 */
	public static boolean containsNoAlphaReverse(BufferedImage image, int x1, int y1, int x2, int y2)
	{
		//Checks
		if (image == null) {
			return false;
		}

		int imageWidth = image.getWidth(null);
		int imageHeight = image.getHeight(null);

		//System.out.println(x1+","+y1 +" "+ x2+","+y2 +" "+ rW+","+rH);
		//Make sure it is not out of bounds.
                x1 = imageWidth + x1;
                x2 = imageWidth + x2;
                
		Rectangle img = new Rectangle(0, 0, imageWidth, imageHeight);
		if (img.intersects(x1, y1, x2 - x1, y2 - y1)) {
			//Offsets
			if (x1 < 0) {
				x1 = 0;
			}
			if (x2 > imageWidth) {
				x2 = imageWidth;
			}
			if (y1 < 0) {
				y1 = 0;
			}
			if (y2 > imageHeight) {
				y2 = imageHeight;
			}
		} else {
			return false;
		}
                
		if (imageWidth > 0 && imageHeight > 0) {
			for (int y = y1; y < y2; y++) {
				for (int x = x1; x < x2; x++) {

					int px = image.getRGB(imageWidth - x - 1, y);
					if (px != 0) {
						return true;
					}
				}
			}
		}

		return false;
	}
}
