package stormgate.editor.ui.forms.graphic;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import javax.swing.JComponent;
import stormgate.data.MapZone;
import stormgate.data.Tile;
import stormgate.editor.data.RenderData;
import stormgate.editor.tool.ToolInterface;
import stormgate.editor.ui.forms.graphic.render.BackgroundRenderer;
import stormgate.editor.ui.forms.graphic.render.GraphicRenderer;
import stormgate.editor.ui.forms.graphic.render.TileRenderer;
import stormgate.editor.ui.forms.graphic.render.ZoneRenderer;
import stormgate.filter.Filter;
import stormgate.geom.MapPoint;
import stormgate.image.ResourceTracker;

/**
 *
 * @author David Kim
 */
public class Graphic extends JComponent
{

	private RenderData renderData;
	private ArrayList<Tile> tilesOnScreen;
	private Level[] levels;
	private TileRenderer tileRender;
	private GraphicRenderer graphicRender;
	private ZoneRenderer zoneRender;
	private BackgroundRenderer backgroundRender;

	public Graphic()
	{
		super();
		tilesOnScreen = new ArrayList<Tile>();
	}

	public Graphic(Level[] levels)
	{
		super();

		tilesOnScreen = new ArrayList<Tile>();
		this.levels = levels;
	}

	public void clean()
	{
		for (int i = 0; i < 3; i++) {
			levels[i].removeAll();
		}
	}

	public void setInformation(RenderData renderData)
	{
		this.renderData = renderData;
		//this.tracker = tracker;
		//data.setGraphic(this);
		//tracker = new ResourceTracker(data.getManager());
		ResourceTracker tracker = renderData.getTracker();
		backgroundRender = new BackgroundRenderer();
		backgroundRender.setTracker(tracker);
		tileRender = new TileRenderer();
		tileRender.setTracker(tracker);
		graphicRender = new GraphicRenderer();
		graphicRender.setTracker(tracker);
		zoneRender = new ZoneRenderer();
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		Graphics2D p = (Graphics2D) g;
		p.clearRect(0, 0, getWidth(), getHeight());

		p.setColor(new Color(50, 50, 50));
		p.fillRect(0, 0, getWidth(), getHeight());

		if (renderData == null) {
			//Background
			p.setColor(new Color(100, 100, 100));
			p.fillRect(0, 0, getWidth(), getHeight());
		} else {
			render(g);
		}
		revalidate();
	}

	private int zoomFilter(int a)
	{
		return Math.round(a * renderData.getZoom());
	}

	private void removeTileContents(Tile tile)
	{
		//Remove all content that belongs to this tile
		for (int i = 0; i < 3; i++) {
			levels[i].removeGraphics(tile.getLevel(i));
		}
	}

	private void updateLevels(Tile tile, Rectangle screenRect)
	{
		for (int i = 0; i < 3; i++) {
			levels[i].updateLevel(renderData.getCameraLocation(), tile.getLevel(i), screenRect);
		}
	}

	private void paintLevels(GraphicRenderer renderer, Graphics g)
	{
		for (int i = 0; i < 3; i++) {
			renderer.setImage(levels[i], renderData.getCameraLocation());
			renderer.paint(g);
		}
	}

	private void render(Graphics g)
	{
		MapPoint camera = renderData.getCameraLocation();
		Filter filter = renderData.getFilter();

		int screenWidth = getWidth();
		int screenHeight = getHeight();

		int tilesizeZoom = zoomFilter(MapPoint.tileSize);

		int centerX = (int) (screenWidth / 2);
		int centerY = (int) (screenHeight / 2);

		int viewDistanceX = (screenWidth / tilesizeZoom) / 2 + 1;
		int viewDistanceY = (screenHeight / tilesizeZoom) / 2 + 1;

		//System.out.println("VIEW DISTANCE: " + viewDistanceX+","+ viewDistanceY);

		tileRender.setFilter(filter);
		tileRender.setOffset(centerX, centerY);

		backgroundRender.setFilter(filter);
		backgroundRender.setOffset(centerX, centerY);

		ArrayList<Tile> newTiles = new ArrayList<Tile>();

		Rectangle screenRect = new Rectangle(0, 0, screenWidth, screenHeight);
		/*
		int maxViewDistance = 10;//data.map.loadTiles;
		if(viewDistanceX > maxViewDistance){
		viewDistanceX = maxViewDistance;
		}
		if(viewDistanceY > maxViewDistance){
		viewDistanceY = maxViewDistance;
		}
		 */
		ArrayList<MapZone> allZones = new ArrayList<MapZone>();

		//Add one because it will be offset by pixels that are less than a tile size
		for (int y = -viewDistanceY; y <= viewDistanceY + 1; y++) {
			for (int x = -viewDistanceX; x <= viewDistanceX + 1; x++) {
				//Get tile location difference
				int mapX = x + camera.getTileX() - 1;
				int mapY = y + camera.getTileY() - 1;

				//System.out.print(mapX+","+mapY+"	");
				Tile t = renderData.getTile(mapX, mapY);
				//test tile
				//t = new Tile(mapX, mapY);

				if (t != null) {
					//Give back the offset tilesize because it will add one more row/col of tiles
					int positionX = (x * MapPoint.tileSize - camera.getLocalX()) - MapPoint.tileSize;
					int positionY = (y * MapPoint.tileSize - camera.getLocalY()) - MapPoint.tileSize;
					backgroundRender.setTile(t, positionX, positionY);
					backgroundRender.paint(g);
				}
			}
		}
		//Add one because it will be offset by pixels that are less than a tile size
		for (int y = -viewDistanceY; y <= viewDistanceY + 1; y++) {
			for (int x = -viewDistanceX; x <= viewDistanceX + 1; x++) {
				//Get tile location difference
				int mapX = x + camera.getTileX() - 1;
				int mapY = y + camera.getTileY() - 1;

				//System.out.print(mapX+","+mapY+"	");
				Tile t = renderData.getTile(mapX, mapY);
				//test tile
				//t = new Tile(mapX, mapY);

				if (t != null) {
					//Give back the offset tilesize because it will add one more row/col of tiles
					int positionX = (x * MapPoint.tileSize - camera.getLocalX()) - MapPoint.tileSize;
					int positionY = (y * MapPoint.tileSize - camera.getLocalY()) - MapPoint.tileSize;

					tileRender.setTile(t, positionX, positionY);
					tileRender.paint(g);
					if (!t.onScreen()) {
						t.addToScreen();
					}
					newTiles.add(t);
					updateLevels(t, screenRect);

					ArrayList<MapZone> zones = t.getZones();
					if (zones != null) {
						//allZones.addAll(zones);
						for(int i = 0 ; i < zones.size(); i++){
							MapZone z = zones.get(i);
							if(!allZones.contains(z)){
								allZones.add(z);
							}
						}
					}
				}
			}
		}

		//Graphic Layer
		graphicRender.setFilter(filter);
		graphicRender.setOffset(centerX, centerY);
		paintLevels(graphicRender, g);
		//Entity Layer

		//Zone Layer
		if (filter.zones || filter.zoneTool) {
			zoneRender.setFilter(filter);
			zoneRender.setOffset(centerX, centerY);
			zoneRender.setCamera(camera);
			zoneRender.setZones(allZones);
			zoneRender.paint(g);
		}
		
		//Get old tiles
		ArrayList<Tile> tilesToRemove = tilesOnScreen;
		tilesToRemove.removeAll(newTiles);
		//Set new tiles on screen
		tilesOnScreen = newTiles;

		//Unload old tiles
		for (int i = 0; i < tilesToRemove.size(); i++) {
			Tile oldTile = tilesToRemove.get(i);
			oldTile.removeFromScreen();

			removeTileContents(oldTile);
		}

		ResourceTracker tracker = renderData.getTracker();
		tracker.clean();

		ToolInterface tool = renderData.getTool();
		if (tool != null) {
			tool.setFilter(filter);
			tool.setOffset(centerX, centerY);
			tool.paint(g);
		}

		if (filter.grid) {
			drawGrid(g, camera);
		}

	}

	/**
	 * Checks if A is overlapping B
	 * @param A
	 * @param B
	 * @return true if inside
	 */
	public static boolean aabb(Rectangle A, Rectangle B)
	{
		final int A_x = A.x;
		final int A_y = A.y;
		final int A_w = A.x + A.width;
		final int A_h = A.y + A.height;

		final int B_x = B.x;
		final int B_y = B.y;
		final int B_w = B.x + B.width;
		final int B_h = B.y + B.height;

		if (A_x > B_w) {
			return false;
		}

		if (A_w < B_x) {
			return false;
		}

		if (A_y > B_h) {
			return false;
		}

		if (A_h < B_y) {
			return false;
		}

		return true;
	}

	private void drawGrid(Graphics g, MapPoint camera)
	{
		int gridSize = 30;

		int gridZoom = zoomFilter(gridSize);

		if (gridZoom <= 0) {
			return;
		}

		int screenWidth = getWidth();
		int screenHeight = getHeight();

		int centerX = (int) (screenWidth / 2);
		int centerY = (int) (screenHeight / 2);

		int viewDistanceX = (screenWidth / gridZoom) / 2 + 2;
		int viewDistanceY = ((screenHeight / gridZoom) / 2) * 2 + 2;

		int camLocalX = camera.getX() % gridSize;
		int camLocalY = camera.getY() % gridSize;

		g.setColor(new Color(0, 0, 0, 50));

		for (int x = -viewDistanceX; x <= viewDistanceX; x++) {

			int positionX = centerX + zoomFilter(x * gridSize - camLocalX);
			g.drawLine(positionX, 0, positionX, screenHeight);

			//Draw diagnal
			int position1 = centerY + zoomFilter((-viewDistanceY * gridSize) / 2 - camLocalY);
			int position2 = positionX + (screenHeight - position1) * 2;
			g.drawLine(positionX, position1, position2, screenHeight);

			//position1 = screenHeight+camLocalY;
			position1 = centerY + zoomFilter(((viewDistanceY + 1) * gridSize) / 2 - camLocalY);
			position2 = positionX + position1 * 2;
			g.drawLine(positionX, position1, position2, 0);
		}

		int position1 = centerX + zoomFilter((-viewDistanceX * gridSize) - camLocalX);
		for (int y = -viewDistanceY; y <= viewDistanceY; y++) {
			int positionY = centerY + zoomFilter((y * gridSize) / 2 - camLocalY);
			g.drawLine(0, positionY, screenWidth, positionY);

			//Draw diagnal
			if (y != -viewDistanceY) {

				int position2 = positionY + (screenWidth - position1) / 2;
				g.drawLine(position1, positionY, screenWidth, position2);
				position2 = positionY - (screenWidth - position1) / 2;
				g.drawLine(position1, positionY, screenWidth, position2);
			}
		}

		gridSize = MapPoint.tileSize;
		int tilesizeZoom = zoomFilter(gridSize);

		centerX = (int) (screenWidth / 2);
		centerY = (int) (screenHeight / 2);

		viewDistanceX = (screenWidth / tilesizeZoom) / 2 + 1;
		viewDistanceY = (screenHeight / tilesizeZoom) / 2 + 1;

		g.setColor(new Color(0, 0, 0, 150));
		for (int x = -viewDistanceX; x <= viewDistanceX + 1; x++) {
			int positionX = centerX + zoomFilter((x * gridSize - camera.getLocalX()) - gridSize);
			g.drawLine(positionX, 0, positionX, screenHeight);
		}
		for (int y = -viewDistanceY; y <= viewDistanceY + 1; y++) {
			int positionY = centerY + zoomFilter((y * gridSize - camera.getLocalY()) - gridSize);
			g.drawLine(0, positionY, screenWidth, positionY);
		}
	}
}
