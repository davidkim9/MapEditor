package stormgate.editor.tool.data;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import stormgate.action.graphic.GraphicPlaceAction;
import stormgate.editor.tool.GraphicsTool;
import stormgate.geom.MapPoint;
import stormgate.image.LibraryResource;

public class GraphicToolPlaceMode extends GraphicToolMode
{

	private String url;
	private LibraryResource resource;
	private int imgWidth;
	private int imgHeight;
	private boolean reverse;

	public GraphicToolPlaceMode(GraphicsTool tool)
	{
		super(tool);
	}

	private int zoomFilter(float a)
	{
		return Math.round(a * data.getZoom());
	}

	@Override
	public void paint(Graphics g)
	{
		if (!data.pan) {
			String url2 = tool.getSelected();

			if ((url != null && !url.equals(url2)) || url == null) {
				url = url2;
				if (url != null) {
					resource = data.getManager().getResource(url);
				}
			}
			if (resource != null) {
				imgWidth = zoomFilter(resource.getWidth());
				imgHeight = zoomFilter(resource.getHeight());

				if (reverse) {
					imgWidth *= -1;
				}

				if (data.snap()) {
					MapPoint gridSnap = data.gridWorkspace(mouseX, mouseY);
					resource.drawImage(g, gridSnap.getX(), gridSnap.getY(), imgWidth, imgHeight);
				} else {
					resource.drawImage(g, mouseX, mouseY, imgWidth, imgHeight);
				}
			}
		}
	}

	//Controls
	@Override
	public void mousePressed(MouseEvent e)
	{
		if (e.getButton() == 1) {

			String graphicUrl = tool.getSelected();

			if (graphicUrl != null) {
				int mouseDownX = e.getX();
				int mouseDownY = e.getY();

				//Place graphic
				MapPoint pt;
				if (data.snap()) {
					pt = data.gridMap(mouseX, mouseY);
				} else {
					pt = data.convertToMap(mouseDownX, mouseDownY);
				}

				GraphicPlaceAction p = new GraphicPlaceAction(data.getMap(), tool.getLevel(), graphicUrl, pt, reverse);
				data.performAction(p);

			}
		} else if (e.getButton() == 3) {
			tool.selectMode();
		}
	}

	public void setReverse(boolean reverse)
	{
		this.reverse = reverse;
	}

	public boolean getReverse()
	{
		return reverse;
	}
}
