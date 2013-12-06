package stormgate.editor.graphics.blend;

/**
 *
 * @author David
 */
public class ImageCutSideBlend implements ImageBlendType
{

	public boolean side = false;

	public ImageCutSideBlend()
	{
	}

	public ImageCutSideBlend(boolean side)
	{
		this.side = side;
	}

	public double getScale(int x, int y, int width, int height)
	{
            if(side){
		if(x < width/2) {
                    return 1;
                }else{
                    return 0;
                }
            }else{
		if(y < height/2) {
                    return 1;
                }else{
                    return 0;
                }
            }
	}
}
