package stormgate.editor.ui.forms.graphic.render;

import java.awt.Color;
import java.awt.Graphics;
import stormgate.data.Tile;
import stormgate.filter.Filter;
import stormgate.geom.MapPoint;
import stormgate.image.LibraryResource;
import stormgate.image.ResourceTracker;

/**
 *
 * @author David Kim
 */
public class TileRenderer implements Renderer
{

	private Tile tile;
	private Filter filter;
	private int zoomX;
	private int zoomY;
	private int tileZoom;
	private int offsetX;
	private int offsetY;
	private ResourceTracker tracker;

	public void setTile(Tile tile, int x, int y)
	{
		this.tile = tile;
		zoomX = zoomFilter(x);
		zoomY = zoomFilter(y);
	}

	public void setFilter(Filter filter)
	{
		this.filter = filter;
		tileZoom = zoomFilter(MapPoint.tileSize);
	}

	private int zoomFilter(float a)
	{
		return Math.round(a * filter.getZoom());
	}

	public void setTracker(ResourceTracker tracker)
	{
		this.tracker = tracker;
	}

	public void setOffset(int offsetX, int offsetY)
	{
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}

	public void paint(Graphics p)
	{
		/*
		p.setColor(Color.WHITE);
		p.fillRect(zoomX + offsetX, zoomY + offsetY, tileZoom, tileZoom);

		Color c1 = new Color(240, 240, 240);
		int squareDivide = 10;
		p.setColor(c1);

		int tileResize = zoomFilter(MapPoint.tileSize / squareDivide);

		for (int i = 0; i < squareDivide; i++) {
		for (int j = i % 2; j < squareDivide; j += 2) {
		p.fillRect(zoomX + j * tileResize + offsetX, zoomY + i * tileResize + offsetY, tileResize, tileResize);
		}
		}
		 */
		if (tile != null) {

			String[][] resources = tile.getTileGraphics();

			for (int y = 0; y < resources.length; y++) {
				for (int x = 0; x < resources[y].length; x++) {
					String url = resources[y][x];

					int tileXOffset = (tileZoom / Tile.divideX) * x;
					int tileYOffset = (tileZoom / Tile.divideY) * y;

					if (url != null) {
						LibraryResource resource = tracker.getResource(url);

						int x1 = zoomX + tileXOffset + offsetX;
						int y1 = zoomY + tileYOffset + offsetY;

						if (resource != null) {

							int scaledWidth = zoomFilter(resource.getWidth());
							int scaledHeight = zoomFilter(resource.getHeight());

							resource.drawImage(p, x1, y1, scaledWidth, scaledHeight);

						} else {
							p.setColor(Color.RED);
							p.fillRect(x1, y1, (tileZoom / Tile.divideX), (tileZoom / Tile.divideY));
						}
					}
				}
			}
			if (filter.tileEnable) {
				if (!tile.getEnabled()) {
					p.setColor(new Color(50, 140, 160, 80));
					p.fillRect(zoomX + offsetX, zoomY + offsetY, tileZoom, tileZoom);
				}
			}
		}
	}
}
