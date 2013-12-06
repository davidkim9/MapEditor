package stormgate.editor.graphics.blend;

/**
 *
 * @author David
 */
public class ImageCutHalfHorizontalBlend implements ImageBlendType
{

	public int side = 0;

	public ImageCutHalfHorizontalBlend()
	{
	}

	public ImageCutHalfHorizontalBlend(int side)
	{
		this.side = side;
	}

	public double getScale(int x, int y, int width, int height)
	{
            /*
            if(side == 0){
		if(y < x/2) {
                    return 1;
                }else{
                    return 0;
                }
            } else if(side == 1){
		if(y < width-x/2) {
                    return 1;
                }else{
                    return 0;
                }
            } else if(side == 2){
		if(y > width-x/2) {
                    return 0;
                }else{
                    return 0;
                }
            }else{
		if(y > x/2) {
                    return 0;
                }else{
                    return 0;
                }
            }
             */
            if(side == 0){
		if(y < x/2) {
                    return 1;
                }else{
                    return 0;
                }
            } else if(side == 1){
		if(y < x/2 + width/2) {
                    return 1;
                }else{
                    return 0;
                }
            } else if(side == 2){
		if(y < height/2 - x/2) {
                    return 1;
                }else{
                    return 0;
                }
            }else{
		if(y < width-x/2) {
                    return 1;
                }else{
                    return 0;
                }
            }
	}
}
