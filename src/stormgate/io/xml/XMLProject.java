/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stormgate.io.xml;

import java.awt.Frame;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import stormgate.common.Common;
import stormgate.data.GraphicLevel;
import stormgate.data.MapEntity;
import stormgate.data.MapGraphic;
import stormgate.data.MapZone;
import stormgate.data.SpawnData;
import stormgate.data.Tile;
import stormgate.editor.data.EditorData;
import stormgate.editor.ui.forms.browser.LibraryViewer;
import stormgate.editor.ui.forms.dialog.LibrarySelect;
import stormgate.geom.MapPoint;
import stormgate.log.Log;
import stormgate.map.Map;

/**
 *
 * @author David
 */
public class XMLProject
{

	public static Map loadMap(EditorData data, String path)
	{
		XMLParser parser = new XMLParser();

		boolean checkLoad = parser.readFile(path);

		if (!checkLoad) {
			return null;
		}

		XMLNode node = parser.getRoot();
		if (node != null) {
			return loadMap(data, node, path);
		}

		return null;
	}

	private static Map loadMap(EditorData data, XMLNode node, String path)
	{
		//System.out.println(node.toString());
		Map map = new Map(data);
		//Set map settings
		XMLNode projectNode = node.getChild("project");

		if (projectNode == null) {
			return null;
			//Project information

		}
		File projectFile = new File(path);
		File projectFolder = projectFile.getParentFile();
		String projectPath = projectFolder.getAbsolutePath();

		String mapName = projectNode.getProperty("name");
		//Set to default
		LibraryViewer.mainDirectory = "library";
		String mapLibrary = filter(projectNode.getProperty("library"), projectPath);
		//Make sure map library gets loaded before anything gets filtered
		String mapDirectory = filter(projectNode.getProperty("directory"), projectPath);

		System.out.println("Loading Map: " + mapName);
		System.out.println("Directory: " + mapDirectory);
		System.out.println("Library: " + mapLibrary);

		File m = new File(mapDirectory);

		map.setMapName(mapName);

		if (m.exists() && m.isDirectory()) {
			map.setMapPath(mapDirectory);
		} else {
			System.out.println("Map directory not found");
			Log.addLog("Map directory not found");
			return null;
			//Exception?
		}

		File l = new File(mapLibrary);
		if (l.exists() && l.isDirectory()) {
			//
			LibraryViewer.mainDirectory = mapLibrary;
		} else {
			System.out.println("Map library not found");
			Log.addLog("Map library not found");
			//Directory not found
			LibrarySelect selectLibrary = new LibrarySelect(new Frame(), true);
			selectLibrary.setVisible(true);

			l = new File(LibraryViewer.mainDirectory);
			if (!l.exists() || !l.isDirectory()) {
				return null;
			}
			//Exception?
		}

		//Project settings
		XMLNode cameraNode = projectNode.getChild("camera");
		if (cameraNode != null) {
			int pointX = cameraNode.getInteger("x");
			int pointY = cameraNode.getInteger("y");

			MapPoint point = new MapPoint(pointX, pointY);

			map.setCameraLocation(point);
		}

		//Default settings
		XMLNode defaultNode = projectNode.getChild("default");
		if (defaultNode != null) {
			XMLNode tileNode = defaultNode.getChild("tile");
			if (tileNode != null) {
				String graphic = filter(tileNode.getProperty("graphic"), projectPath);
				String background = filter(tileNode.getProperty("background"), projectPath);
				Tile.defaultTile = graphic;
				Tile.defaultBackground = background;
			}
		}

		map.setMapFile(path);

		map.loadMap();

		return map;
	}

	public static Tile loadTile(Map map, String path, int x, int y)
	{
		Tile t = new Tile(x, y);

		//Tile filename
		String tilePath = path + "\\tile_" + y + "_" + x + ".xml";

		File tileFile = new File(tilePath);

		boolean tileLoaded = false;

		if (tileFile.exists()) {
			//Load script here
			XMLParser parser = new XMLParser();

			boolean checkLoad = parser.readFile(tilePath);

			if (!checkLoad) {
				//Error Message
				System.err.println("Corrupt XML File: " + tilePath);
				Log.addLog("Corrupt XML File: " + tilePath);
				return t;
			}

			XMLNode node = parser.getRoot();

			if (node != null) {

				XMLNode tileNode = node.getChild("tile");
				int tileX = tileNode.getInteger("x");
				int tileY = tileNode.getInteger("y");

				String enabled = tileNode.getProperty("enabled");
				if (enabled != null && enabled.length() > 0) {
					if (!enabled.toLowerCase().equals("false") && !enabled.equals("0")) {
						t.setEnabled(true);
					}
				}

				String background = tileNode.getProperty("background");

				if (background != null) {
					background = filter(background, path);
					t.setTileBackground(background);
				}

				//TILE DATA

				String[][] tileGraphics = new String[5][5];

				XMLNode graphicNode = tileNode.getChild("graphics");
				if (graphicNode != null) {

					ArrayList<XMLNode> children = graphicNode.getChildren();

					for (int i = 0; i < children.size(); i++) {
						XMLNode child = children.get(i);
						if (child.name.equals("cell")) {
							//Handle tile graphic information
							int cellX = child.getInteger("x");
							int cellY = child.getInteger("y");
							String graphic = filter(child.getProperty("src"), path);
							if (graphic != null && graphic.length() > 0) {
								tileGraphics[cellY][cellX] = graphic;
							}
						}
					}
				}
				t.setTileGraphics(tileGraphics);

				//GRAPHIC DATA

				XMLNode levelNode = tileNode.getChild("levels");
				if (levelNode != null) {

					ArrayList<XMLNode> children = levelNode.getChildren();

					for (int i = 0; i < children.size(); i++) {
						XMLNode child = children.get(i);
						if (child.name.equals("level")) {

							int level = child.getInteger("number");
							GraphicLevel graphicLevel = t.getLevel(level);

							if (graphicLevel != null) {

								ArrayList<XMLNode> graphics = child.getChildren();

								for (int j = 0; j < graphics.size(); j++) {
									XMLNode graphic = graphics.get(j);
									if (graphic.name.equals("graphic")) {
										int graphicX = graphic.getInteger("x");
										int graphicY = graphic.getInteger("y");

										MapPoint point = new MapPoint(graphicX, graphicY);

										String source = filter(graphic.getProperty("src"), path);
                                                                                boolean reverse = graphic.getBoolean("reverse");
										if (source != null && source.length() > 0) {
											String graphicPath = source;

											MapGraphic mapGraphic = new MapGraphic(graphicPath, point);
                                                                                        mapGraphic.reverse = reverse;
											graphicLevel.addGraphic(mapGraphic);

										} else {
											//GRAPHIC NOT FOUND
											System.err.println("Graphic not specified " + source);
											stormgate.log.Log.addLog("Load Tile Error: Graphic not specified " + source);
										}
									}
								}
							} else {
								System.err.println("Invalid graphic level " + level);
								stormgate.log.Log.addLog("Load Tile Error: Invalid graphic level " + level);
							}
						}
					}
				}

				//ZONE DATA
				XMLNode zoneNode = tileNode.getChild("zones");
				if (zoneNode != null) {

					ArrayList<XMLNode> children = zoneNode.getChildren();

					for (int i = 0; i < children.size(); i++) {
						XMLNode child = children.get(i);
						if (child.name.equals("zone")) {

							int zoneId = child.getInteger("id");
							MapZone zone = new MapZone(zoneId);

							zone.name = child.getProperty("name");
							zone.collision = child.getBoolean("collision");
							zone.los = child.getBoolean("los");
							zone.skillBank = child.getBoolean("skillBank");
							zone.town = child.getBoolean("town");
							String music = filter(child.getProperty("music"), path);
							if (music != null) {
								zone.music = new File(music);
							}
                                                        
							String walkover = filter(child.getProperty("walkover"), path);
							if (walkover != null) {
								zone.walkover = new File(walkover);
							}
                                                        
							XMLNode polygonData = child.getChild("polygon");
							ArrayList<XMLNode> points = polygonData.getChildren();
							for (int j = 0; j < points.size(); j++) {
								XMLNode point = points.get(j);
								if (point.name.equals("point")) {
									int zX = point.getInteger("x");
									int zY = point.getInteger("y");
									MapPoint p = new MapPoint(zX, zY);

									zone.addPoint(p);
								}
							}

							XMLNode ambientData = child.getChild("ambient");
							ArrayList<XMLNode> ambientFiles = ambientData.getChildren();
							for (int j = 0; j < ambientFiles.size(); j++) {
								XMLNode file = ambientFiles.get(j);
								if (file.name.equals("file")) {
									String src = filter(file.getProperty("src"), path);
									if (src != null) {
										zone.ambient.add(new File(src));
									}
								}
							}

							XMLNode footstepData = child.getChild("footstep");
							ArrayList<XMLNode> footstepFiles = footstepData.getChildren();
							for (int j = 0; j < footstepFiles.size(); j++) {
								XMLNode file = footstepFiles.get(j);
								if (file.name.equals("file")) {
									String src = filter(file.getProperty("src"), path);
									if (src != null) {
										zone.footstep.add(new File(src));
									}
								}
							}

							XMLNode spawnData = child.getChild("spawns");
							ArrayList<XMLNode> spawnFiles = spawnData.getChildren();
							for (int j = 0; j < spawnFiles.size(); j++) {
								XMLNode file = spawnFiles.get(j);
								if (file.name.equals("spawn")) {
									SpawnData spawn = new SpawnData();
									spawn.type = file.getBoolean("type");
									spawn.id = file.getInteger("id");
									spawn.count = file.getInteger("count");
									zone.spawn.add(spawn);
								}
							}
							//Get rid of duplicates
							if(t.checkID(zoneId)){
								//Get tiles without loading more
								ArrayList<Tile> tiles = map.getTilesNoLoad(zone);
								//Search for already existing zone objects
								boolean found = false;
								for (int j = 0; j < tiles.size(); j++) {
									Tile tile = tiles.get(j);
									if (tile != null) {
										//Don't check the older version of this tile
										if(tile.getX() != x || tile.getY() != y){
											MapZone checkZone = tile.getZoneID(zoneId);
											if (checkZone != null) {
												if (checkZone.equals(zone)) {
													if(!t.contains(checkZone)){
														t.addZone(checkZone);
														checkZone.addTile(t);

														//System.out.println("ADDING2 " + t + " : " + checkZone.getID() + " " + checkZone);
													}
													//map.setZoneTiles(checkZone);
													found = true;
													break;
												} else {
													System.err.println("Zone mismatched on loaded tile, map file might be corrupt! Mismatched tile: " + t + " with " + checkZone + " on " + tile);
													stormgate.log.Log.addLog("Load Tile Error: Zone mismatched on loaded tile, map file might be corrupt! Mismatched tile: " + t + " with " + checkZone + " on " + tile);
												}
											}
										}
									}
								}
								if (!found) {
									if(!t.contains(zone)){
										t.addZone(zone);
										zone.addTile(t);
										//System.out.println("ADDING " + t + " : " + zone.getID() + " " + zone + " " + t.hashCode() );
									}
									//map.setZoneTiles(zone);
								}
							}
						}
					}
				}
				//ENTITY DATA
				XMLNode entityNode = tileNode.getChild("entities");
				if (entityNode != null) {

					ArrayList<XMLNode> children = entityNode.getChildren();

					for (int i = 0; i < children.size(); i++) {
						XMLNode child = children.get(i);
						if (child.name.equals("entity")) {

							int entityType = child.getInteger("type");
							MapEntity entity = MapEntity.makeEntity(entityType);

							int entityX = child.getInteger("x");
							int entityY = child.getInteger("y");
							MapPoint p = new MapPoint(entityX, entityY);
							entity.setPoint(p);

							if (entity != null) {
								if (entityType == MapEntity.SOUND) {
									String src = filter(child.getProperty("src"), path);
									if (src != null) {
										entity.sound = new File(src);
										entity.radius = child.getInteger("radius");
									}
								} else {
									if (entityType == MapEntity.BEACON) {
										entity.map = child.getProperty("map");
										entity.tileX = child.getInteger("tileX");
										entity.tileY = child.getInteger("tileY");
										entity.x = child.getInteger("px");
										entity.y = child.getInteger("py");
									} else {
										if (entityType == MapEntity.UNIT) {
											entity.unitType = child.getBoolean("unitType");
											entity.id = child.getInteger("id");
										}
									}
								}

								t.addEntity(entity);
							}
						}
					}
				}

				tileLoaded = true;
			} else {
				//Error Message
				System.err.println("TILE CAN NOT BE PARSED PROPERLY 2: " + tilePath);
				stormgate.log.Log.addLog("Tile can't be parsed properly: " + tilePath);
			}
		}

		if (!tileLoaded) {
			//Default tile
			String[][] tileGraphics = new String[5][5];

			if (x >= 0 && y >= 0) {
				for (int i = 0; i < tileGraphics.length; i++) {
					for (int j = 0; j < tileGraphics[i].length; j++) {
						//Make default tile
						tileGraphics[i][j] = Tile.defaultTile;
					}
				}
			}
			t.setTileGraphics(tileGraphics);
		}

		return t;
	}

	public static void saveTile(Tile tile, String path)
	{
		//This function is called after the map file has been saved
		//Include version number and compile date
		//Zone, and add data
		//Need a way to save the map, and save as
		int tileX = tile.getX();
		int tileY = tile.getY();

		String tilePath = path + "\\tile_" + tileY + "_" + tileX + ".xml";

		File tileFile = new File(tilePath);

		XMLNode root = new XMLNode();
		root.name = "tile";

		root.setProperty("x", tileX);
		root.setProperty("y", tileY);
		root.setProperty("enabled", "" + tile.getEnabled());

		String background = tile.getTileBackground();
		if (background != null) {
			//System.out.println(background + " to " + unfilter(background, path) + " using " + path);
			//System.out.println("redo" + filter(unfilter(background, path), path));
			root.setProperty("background", unfilter(background, path));
		}
		//Tile graphics
		XMLNode graphicNode = new XMLNode();
		graphicNode.name = "graphic";

		XMLNode tileCells = new XMLNode();
		tileCells.name = "graphics";

		String[][] tileGraphics = tile.getTileGraphics();
		if (tileGraphics != null) {
			for (int i = 0; i < tileGraphics.length; i++) {
				for (int j = 0; j < tileGraphics[i].length; j++) {
					String tileGraphic = tileGraphics[i][j];
					if (tileGraphic != null) {
						XMLNode graphicCell = new XMLNode();
						graphicCell.name = "cell";
						graphicCell.setProperty("x", j);
						graphicCell.setProperty("y", i);
						graphicCell.setProperty("src", unfilter(tileGraphic, path));

						tileCells.addChild(graphicCell);
					}
				}
			}

			root.addChild(tileCells);
		}
		//Level Graphics
		XMLNode levels = new XMLNode();
		levels.name = "levels";

		for (int i = 0; i < 3; i++) {
			GraphicLevel level = tile.getLevel(i);
			if (level != null) {
				XMLNode levelNode = new XMLNode();
				levelNode.name = "level";
				levelNode.setProperty("number", i);
				ArrayList<MapGraphic> levelGraphics = level.graphics;

				for (int j = 0; j < levelGraphics.size(); j++) {
					MapGraphic graphic = levelGraphics.get(j);
					if (graphic != null && !(graphic instanceof MapEntity)) {
						XMLNode gNode = new XMLNode();
						gNode.name = "graphic";
						gNode.setProperty("src", unfilter(graphic.getURL(), path));
                                                gNode.setProperty("reverse", graphic.reverse);

						MapPoint graphicPoint = graphic.getPoint();

						gNode.setProperty("x", graphicPoint.getX());
						gNode.setProperty("y", graphicPoint.getY());
						levelNode.addChild(gNode);
					}
				}

				levels.addChild(levelNode);
			}
		}

		root.addChild(levels);

		//Zone information
		ArrayList<MapZone> zones = tile.getZones();
		if (zones != null) {
			XMLNode zonesNode = new XMLNode();
			zonesNode.name = "zones";
			for (int i = 0; i < zones.size(); i++) {

				MapZone zone = zones.get(i);
				if (zone != null) {
					XMLNode zoneNode = new XMLNode();
					zoneNode.name = "zone";
					zoneNode.setProperty("id", zone.getID());

					//Properties
					if (zone.name != null) {
						zoneNode.setProperty("name", zone.name);
					}

					zoneNode.setProperty("collision", zone.collision);
					zoneNode.setProperty("los", zone.los);
					zoneNode.setProperty("skillBank", zone.skillBank);
					zoneNode.setProperty("town", zone.town);

					if (zone.music != null) {
						zoneNode.setProperty("music", unfilter(zone.music.toString(), path));
					}
                                        
					if (zone.walkover != null) {
						zoneNode.setProperty("walkover", unfilter(zone.walkover.toString(), path));
					}

					XMLNode polygonNode = new XMLNode();
					polygonNode.name = "polygon";
					zoneNode.addChild(polygonNode);
					ArrayList<MapPoint> polygon = zone.getPolygon();
					for (int j = 0; j < polygon.size(); j++) {
						MapPoint p = polygon.get(j);
						XMLNode pointNode = new XMLNode();
						pointNode.name = "point";
						pointNode.setProperty("x", p.getX());
						pointNode.setProperty("y", p.getY());
						polygonNode.addChild(pointNode);
					}

					//Array related
					XMLNode ambientNode = new XMLNode();
					ambientNode.name = "ambient";
					zoneNode.addChild(ambientNode);

					ArrayList<File> ambient = zone.ambient;
					for (int j = 0; j < ambient.size(); j++) {
						File p = ambient.get(j);
						XMLNode soundNode = new XMLNode();
						soundNode.name = "file";
						soundNode.setProperty("src", unfilter(p.toString(), path));
						ambientNode.addChild(soundNode);
					}
					XMLNode footstepNode = new XMLNode();
					footstepNode.name = "footstep";
					zoneNode.addChild(footstepNode);

					ArrayList<File> footstep = zone.footstep;
					for (int j = 0; j < footstep.size(); j++) {
						File p = footstep.get(j);
						XMLNode soundNode = new XMLNode();
						soundNode.name = "file";
						soundNode.setProperty("src", unfilter(p.toString(), path));
						footstepNode.addChild(soundNode);
					}

					XMLNode spawnNode = new XMLNode();
					spawnNode.name = "spawns";
					zoneNode.addChild(spawnNode);

					ArrayList<SpawnData> spawn = zone.spawn;
					for (int j = 0; j < spawn.size(); j++) {
						SpawnData sData = spawn.get(j);
						XMLNode sNode = new XMLNode();
						sNode.name = "spawn";
						sNode.setProperty("type", sData.type);
						sNode.setProperty("id", sData.id);
						sNode.setProperty("count", sData.count);
						spawnNode.addChild(sNode);
					}

					zonesNode.addChild(zoneNode);
				}
			}
			root.addChild(zonesNode);
		}

		ArrayList<MapEntity> entities = tile.getEntities();
		if (entities != null) {
			XMLNode entitiesNode = new XMLNode();
			entitiesNode.name = "entities";
			for (int i = 0; i < entities.size(); i++) {

				MapEntity entity = entities.get(i);
				if (entity != null) {
					XMLNode entityNode = new XMLNode();
					entityNode.name = "entity";
					MapPoint p = entity.getPoint();

					entityNode.setProperty("x", p.getX());
					entityNode.setProperty("y", p.getY());

					entityNode.setProperty("type", entity.getType());
					//Properties
					if (entity.getType() == MapEntity.SOUND) {
						//Sound
						entityNode.setProperty("src", unfilter(entity.sound.toString(), path));
						entityNode.setProperty("radius", entity.radius);
						entityNode.setProperty("name", entity.radius);

					} else {
						if (entity.getType() == MapEntity.BEACON) {
							//Beacon
							entityNode.setProperty("map", entity.map);
							entityNode.setProperty("tileX", entity.tileX);
							entityNode.setProperty("tileY", entity.tileY);
							entityNode.setProperty("px", entity.x);
							entityNode.setProperty("py", entity.y);
						} else {
							if (entity.getType() == MapEntity.UNIT) {
								//Unit
								entityNode.setProperty("unit", entity.unitType);
								entityNode.setProperty("id", entity.id);
							}
						}
					}

					entitiesNode.addChild(entityNode);
				}
			}
			root.addChild(entitiesNode);
		}

		File pathFile = new File(path);
		if (!pathFile.exists()) {
			pathFile.mkdir();
		}

		FileOutputStream fout;
		try {
			fout = new FileOutputStream(tileFile);
			new PrintStream(fout).println(XMLWriter.getXML(root));
		} catch (FileNotFoundException ex) {
			Logger.getLogger(XMLProject.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public static String filter(String str, String path)
	{
		if (str != null && str.length() > 0) {
			path = path.replaceAll("(\\\\)", "\\\\\\\\");
			String formatedLibrary = LibraryViewer.mainDirectory.replaceAll("(\\\\)", "\\\\\\\\");
			str = str.replaceAll("\\$\\{(?i)(library)}", formatedLibrary);
			str = str.replaceAll("\\$\\{(?i)(project)}", path);
			str = str.replaceAll("\\$\\{(?i)(editor)}", "");
		}
		return str;
	}

	public static String unfilter(String str, String path)
	{
		if (str.length() > 0) {
			File fpath = new File(str);
			File epath = new File(".");
			File lpath = null;
			try {
				fpath = fpath.getCanonicalFile();
				epath = epath.getCanonicalFile();
				if(LibraryViewer.mainDirectory != null){
					lpath = new File(LibraryViewer.mainDirectory);
					lpath = lpath.getCanonicalFile();
				}
			} catch (Exception e) {
			}
			str = fpath.getAbsolutePath();
			String editorPath = epath.getAbsolutePath() + "\\";
			editorPath = editorPath.replaceAll("(\\\\)", "\\\\\\\\\\\\\\\\");

			path = path.replaceAll("(\\\\)", "\\\\\\\\\\\\\\\\");
			str = str.replaceAll("/", "\\");
			str = str.replaceAll("(\\\\)", "\\\\\\\\");

			if(LibraryViewer.mainDirectory != null){
				String libraryPath = lpath.getAbsolutePath();
				libraryPath = libraryPath.replaceAll("(\\\\)", "\\\\\\\\\\\\\\\\");
				//Create a new string so it doesnt replace twice
				String pathStr = str.replaceAll(libraryPath, "\\${library}");
				if(str.equals(pathStr)){
					String formatedDirectory = LibraryViewer.mainDirectory.replaceAll("(\\\\)", "\\\\\\\\\\\\\\\\");
					str = str.replaceAll(formatedDirectory, "\\${library}");
				}else{
					str = pathStr;
				}
			}

			str = str.replaceAll(path, "\\${project}");
			str = str.replaceAll(editorPath, "\\${editor}");
			
			return str.replaceAll("(\\\\\\\\)", "\\\\");

		}
		return str;
	}

	public static void saveMap(Map map, String path)
	{
		File projectFile = new File(path);
		File projectFolder = projectFile.getParentFile();
		path = projectFolder.getAbsolutePath();

		XMLNode root = new XMLNode();
		root.name = "project";
		root.setProperty("name", map.getMapName());
		root.setProperty("directory", unfilter(map.getMapPath(), path));
		
		//For filtering
		String mainLib = LibraryViewer.mainDirectory;
		LibraryViewer.mainDirectory = null;
		root.setProperty("library", unfilter(mainLib, path));
		LibraryViewer.mainDirectory = mainLib;

		//Input version number
		MapPoint camera = map.getCameraLocation();

		XMLNode cameraNode = new XMLNode();
		cameraNode.name = "camera";
		cameraNode.setProperty("x", camera.getX());
		cameraNode.setProperty("y", camera.getY());
		root.addChild(cameraNode);

		XMLNode defaultNode = new XMLNode();
		defaultNode.name = "default";

		//Default tile
		XMLNode defaultTile = new XMLNode();
		defaultTile.name = "tile";
		if (Tile.defaultBackground != null) {
			defaultTile.setProperty("background", unfilter(Tile.defaultBackground, path));
		}
		if (Tile.defaultTile != null) {
			defaultTile.setProperty("graphic", unfilter(Tile.defaultTile, path));
		}
		defaultNode.addChild(defaultTile);

		root.addChild(defaultNode);

		if (!projectFolder.exists()) {
			projectFolder.mkdir();
		}

		FileOutputStream fout;
		try {
			fout = new FileOutputStream(projectFile);
			new PrintStream(fout).println(XMLWriter.getXML(root));
		} catch (FileNotFoundException ex) {
			Logger.getLogger(XMLProject.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public static void copyTiles(String from, String to)
	{
		System.out.println("Copying Tiles");
		File tileFile = new File(from);

		if (tileFile.exists()) {
			File target = new File(to);
			if (!target.exists()) {
				target.mkdirs();
			}
			if(tileFile.equals(target)){
				System.out.println("Same directory");
				return;
			}
			File[] fileList = tileFile.listFiles();
			for (File file : fileList) {
				Common.copyFile(file, new File(to + "\\" + file.getName()));
				System.out.println("COPYING " + file);
			}

			File[] targetDirectory = target.listFiles();
			for (File fileA : targetDirectory) {

				boolean found = false;
				for (File fileB : fileList) {
					if (fileA.getName().equals(fileB.getName())) {
						found = true;
						break;
					}
				}
				if (!found) {
					//DELETE
					System.out.println("DELETING " + fileA);
					fileA.delete();
				}
			}
		}
	}

	public static void deleteFiles(String directory)
	{
		System.out.println("Deleting Directory");
		if (directory != null) {
			File target = new File(directory);
			if (target.exists()) {
				File[] targetDirectory = target.listFiles();
				for (File fileA : targetDirectory) {
					System.out.println("DELETING " + fileA);
					fileA.delete();
				}
			}
		}
	}
}
