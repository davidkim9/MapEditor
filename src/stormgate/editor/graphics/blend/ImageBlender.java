package stormgate.editor.graphics.blend;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

/**
 *
 * @author David
 */
public class ImageBlender
{

	public static BufferedImage blend(BufferedImage imageA, BufferedImage imageB, ImageBlendType blend)
	{
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gs = ge.getDefaultScreenDevice();
		GraphicsConfiguration gc = gs.getDefaultConfiguration();

		int aWidth = imageA.getWidth();
		int aHeight = imageA.getHeight();
		int bWidth = imageB.getWidth();
		int bHeight = imageB.getHeight();

		int maxWidth = aWidth;
		int maxHeight = aHeight;
		int minWidth = bWidth;
		int minHeight = bHeight;

		if (maxWidth < bWidth) {
			maxWidth = bWidth;
			minWidth = aWidth;
		}

		if (maxHeight < bHeight) {
			maxHeight = bHeight;
			minHeight = aHeight;
		}

		BufferedImage blended = gc.createCompatibleImage(maxWidth, maxHeight, Transparency.TRANSLUCENT);

		Graphics g = blended.getGraphics();
		g.drawImage(imageA, aWidth, aHeight, null);
		g.drawImage(imageB, bWidth, bHeight, null);

		for (int y = 0; y < minHeight; y++) {
			for (int x = 0; x < minWidth; x++) {
				double scale = blend.getScale(x, y, minWidth, minHeight);
				scale = scale > 1 ? 1 : scale;
				scale = scale < 0 ? 0 : scale;

				int rgbA = 0;
				int rgbB = 0;

				if (x < aWidth && y < aHeight) {
					rgbA = imageA.getRGB(x, y);
				}

				if (x < bWidth && y < bHeight) {
					rgbB = imageB.getRGB(x, y);
				}
				int alphaA = (rgbA >> 24) & 0xff;
				int alphaB = (rgbB >> 24) & 0xff;
				
				Color colorA = new Color(rgbA, true);
				Color colorB = new Color(rgbB, true);

				Color color = blendColor(colorA, alphaA, colorB, alphaB, scale);
				int colorRGB = color.getRGB();

				blended.setRGB(x, y, colorRGB);
			}
		}

		return blended;
	}

	public static Color blendColor(Color color1, int alpha1, Color color2, int alpha2, double ratio)
	{
		float r = (float) ratio;
		float ir = (float) 1.0 - r;

		float rgb1[] = new float[3];
		float rgb2[] = new float[3];

		color1.getColorComponents(rgb1);
		color2.getColorComponents(rgb2);

		Color color = new Color(rgb1[0] * r + rgb2[0] * ir,
				rgb1[1] * r + rgb2[1] * ir,
				rgb1[2] * r + rgb2[2] * ir,
				(float) ((alpha1 / 255.0) * r + (alpha2 / 255.0) * ir));

		return color;
	}
}
