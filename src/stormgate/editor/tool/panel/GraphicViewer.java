package stormgate.editor.tool.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JComponent;
import stormgate.image.LibraryResource;

/**
 *
 * @author David
 */
public class GraphicViewer extends JComponent {

	private LibraryResource resource;
	private int imgw;
	private int imgh;

	private float zoom = -1;

	public void setResource(LibraryResource resource) {
		this.resource = resource;
	}

	public void setZoom(float amount){
		zoom = amount;
	}

	private void defineSize() {
		if (resource != null) {
			imgw = resource.getWidth();
			imgh = resource.getHeight();

			if(zoom <= 0){
				if (imgw > imgh) {
					//Landscape
					if (imgw > getSize().width) {
						//Constrain
						imgh = (int) ((getSize().width / ((double) imgw)) * imgh);
						imgw = getSize().width;
					}
					if (imgh > getSize().height) {
						//Constrain
						imgw = (int) ((getSize().height / ((double) imgh)) * imgw);
						imgh = getSize().height;
					}
				} else {
					//Portrait
					if (imgh > getSize().height) {
						//Constrain
						imgw = (int) ((getSize().height / ((double) imgh)) * imgw);
						imgh = getSize().height;
					}
					if (imgw > getSize().width) {
						//Constrain
						imgh = (int) ((getSize().width / ((double) imgw)) * imgh);
						imgw = getSize().width;
					}
				}
				this.setPreferredSize(new Dimension(0, 0));
				revalidate();
			}else{
				imgw *= zoom;
				imgh *= zoom;
				this.setPreferredSize(new Dimension(imgw, imgh));
				revalidate();
			}
		} else {
			imgw = 0;
			imgh = 0;
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		defineSize();

		Graphics2D p = (Graphics2D) g;
		p.clearRect(0, 0, getWidth(), getHeight());

		p.setColor(Color.WHITE);
		p.fillRect(0, 0, getWidth(), getHeight());

		Color c1 = new Color(240, 240, 240);
		int squareDivideWidth = getWidth() / 10 + 1;
		int squareDivideHeight = getHeight() / 10 + 1;
		p.setColor(c1);

		int tileResize = 10;

		for (int i = 0; i < squareDivideHeight; i++) {
			for (int j = i % 2; j < squareDivideWidth; j += 2) {
				p.fillRect(j * tileResize, i * tileResize, tileResize, tileResize);
			}
		}

		int middleX = getWidth() / 2 - imgw / 2;
		int middleY = getHeight() / 2 - imgh / 2;

		if (resource != null) {
			resource.drawImageNoOffset(p, middleX, middleY, imgw, imgh);
		}
	}
}
