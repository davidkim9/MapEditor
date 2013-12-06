package stormgate.editor.graphics.blend;

/**
 *
 * @author David
 */
public class ImageCutHalfVerticalBlend implements ImageBlendType
{

	public int side = 0;

	public ImageCutHalfVerticalBlend()
	{
	}

	public ImageCutHalfVerticalBlend(int side)
	{
		this.side = side;
	}

	public double getScale(int x, int y, int width, int height)
	{
            if(side == 0){
		if(x < y/2) {
                    return 1;
                }else{
                    return 0;
                }
            } else if(side == 1){
		if(x < y/2 + height/2) {
                    return 1;
                }else{
                    return 0;
                }
            } else if(side == 2){
		if(x < width/2 - y/2) {
                    return 1;
                }else{
                    return 0;
                }
            }else{
		if(x < height-y/2) {
                    return 1;
                }else{
                    return 0;
                }
            }
	}
}
