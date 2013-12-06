package stormgate.editor.graphics.blend;

/**
 *
 * @author David
 */
public class ImageStraightBlend implements ImageBlendType
{

	public int side = 0;

	public ImageStraightBlend()
	{
	}

	public ImageStraightBlend(int side)
	{
		this.side = side;
	}

	public double getScale(int x, int y, int width, int height)
	{
		if (side == 1 || side == 2) {
			x = width - x;
		}
		if (side >= 2) {
			y = height - y;
		}
		double equation = x + y;
		
		int bigger = width > height ? width : height;
		return equation / bigger;
	}
}
