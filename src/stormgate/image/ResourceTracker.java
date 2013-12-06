package stormgate.image;

import java.util.ArrayList;

/**
 *
 * @author David
 */
public class ResourceTracker
{

	private ImageManager manager;
	private ArrayList<String> oldLoaded;
	private ArrayList<String> loaded;

	public ResourceTracker(ImageManager manager)
	{
		this.manager = manager;
		oldLoaded = new ArrayList<String>();
		loaded = new ArrayList<String>();
	}

	public LibraryResource getResource(String url)
	{
		if (loaded.indexOf(url) == -1) {
			loaded.add(url);
		}
		return manager.getSource(url);
	}
	
	//totally hacks, i need to clean from outside both the graphics, fix it later
	private boolean offset = false;

	public void clean()
	{
		if (offset == false) {
			//Find the difference
			oldLoaded.removeAll(loaded);

			for (int i = 0; i < oldLoaded.size(); i++) {
				String url = oldLoaded.get(i);
				//REMOVE THESE
				manager.removeSource(url);
			}

			oldLoaded = loaded;
			loaded = new ArrayList<String>();
			offset = true;
		} else {
			offset = false;
		}
	}
}
