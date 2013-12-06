package stormgate.editor.graphics.blend;

/**
 *
 * @author David
 */
public class ImageLinearBlend implements ImageBlendType
{

	public boolean side = true;

	public ImageLinearBlend()
	{
	}

	public ImageLinearBlend(boolean side)
	{
		this.side = side;
	}

	public double getScale(int x, int y, int width, int height)
	{
		if (side) {
			return x * 1.0 / width;
		}else{
			return y * 1.0 / height;
		}
	}
}
