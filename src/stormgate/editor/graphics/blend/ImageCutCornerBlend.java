package stormgate.editor.graphics.blend;

/**
 *
 * @author David
 */
public class ImageCutCornerBlend implements ImageBlendType
{

	public boolean side = false;

	public ImageCutCornerBlend()
	{
	}

	public ImageCutCornerBlend(boolean side)
	{
		this.side = side;
	}

	public double getScale(int x, int y, int width, int height)
	{
            if(side){
		if(x < y) {
                    return 1;
                }else{
                    return 0;
                }
            }else{
		if(x < height-y) {
                    return 1;
                }else{
                    return 0;
                }
            }
	}
}
