package stormgate.editor.graphics.blend;

/**
 *
 * @author David
 */
public class ImageRadialBlend implements ImageBlendType
{

	public int side = 0;

	public ImageRadialBlend()
	{
	}

	public ImageRadialBlend(int side)
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
		double sqrt = Math.sqrt(x * x + y * y);
		int bigger = width > height ? width : height;
		return sqrt / bigger;
	}
}
