package stormgate.map;

import java.util.concurrent.LinkedBlockingDeque;
import stormgate.data.Tile;
import stormgate.io.xml.XMLProject;

/**
 *
 * @author David
 */
public class MapLoader implements Runnable {

	private LinkedBlockingDeque<LoadAction> queue;
	private Map map;
	private String path;

	private boolean stopQueue = false;

	private boolean runThread = true;

	public MapLoader(Map map, String path)
	{
		queue = new LinkedBlockingDeque<LoadAction>();
		this.map = map;
		this.path = path;
		runThread = true;
	}

	public void setPath(String path){
		this.path = path;
	}

	public void addLoad(int x, int y){
		queue.add(new LoadAction(x, y));
	}

	public void clearQueue(){
		stopQueue = true;
	}

	public void stopThread(){
		runThread = false;
	}

	public void run()
	{
		while(runThread){
			if(queue.size() > 0){
				if(!stopQueue){

					LoadAction action = queue.pop();
					int x = action.x;
					int y = action.y;
					Tile t = XMLProject.loadTile(map, path, x, y);
					//Wait for map to start reloading
					//while(!map.reloading()){
					//}
					map.setTile(t, x, y);
                                        map.refresh();
				}else{
					queue.clear();
				}
			}else if(stopQueue) {
                                map.refresh();
				stopQueue = false;
			}
		}
		System.out.println("THREAD STOPPED!");
	}
}
class LoadAction {
	public int x;
	public int y;
	public LoadAction(int x, int y){
		this.x = x;
		this.y = y;
	}
}