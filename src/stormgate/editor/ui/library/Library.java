package stormgate.editor.ui.library;

import java.io.File;
import java.util.Vector;

import stormgate.image.ImageWrapper;
import stormgate.image.data.ImageData;

public class Library implements LibraryInterface {
    
    private Vector<Folder> displayFolders;
    
    public Library(){
        //Selected Folders maybe?
        displayFolders = new Vector<Folder>();
        try {
			displayFolders.add(new Folder(new File("library")));
		} catch (Exception e) {
			System.err.println("Library does not exist");
			stormgate.log.Log.addLog("Library does not exist");
		}
    }
   
    //Maybe called from folderviewer?
	protected void updateSelected(Vector<Folder> folders){
		displayFolders = folders;
		//folders.updateSelected(folders);
		//thumbs.updateSelected(folders);
	}
   /*
    public ImageWrapper getSelectedGraphic(){
		return thumbs.getSelectedGraphic();
    }
   
    public Folder getSelectedFolder(){
            return folders.getSelectedFolder();
    }
   
    public void setGraphicIni(EditorGraphic g, IniData data){
            //Do something here, i dont know
            g.updateIni(data);
    }
   */

	public Folder getSelectedFolder() {
		// TODO Auto-generated method stub
		return null;
	}

	public ImageWrapper getSelectedGraphic() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setGraphicIni(ImageWrapper graphic, ImageData data) {
		// TODO Auto-generated method stub
		
	}
}