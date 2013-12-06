package stormgate.editor.graphics.blend;

/**
 *
 * @author David
 */
public class ImageCutHalfCornerBlend implements ImageBlendType
{

	public int side = 0;

	public ImageCutHalfCornerBlend()
	{
	}

	public ImageCutHalfCornerBlend(int side)
	{
		this.side = side;
	}

	public double getScale(int x, int y, int width, int height)
	{
            if(side == 0){
		if(y < height/2 && x < width/2 && x < height/2-y) {
                    return 1;
                }else{
                    return 0;
                }

            }else if(side == 1){
		if(y < height/2 && x > width/2 && x - width/2 > y) {
                    return 1;
                }else{
                    return 0;
                }
            }else if(side == 2){
		if(y > height/2 && x > width/2 && y > height - x + width/2) {
                    return 1;
                }else{
                    return 0;
                }
            }else if(side == 3){
		if(y > height/2 && x < width/2 && x < y - height/2) {
                    return 1;
                }else{
                    return 0;
                }
            }
            
            return 0;
	}
}

