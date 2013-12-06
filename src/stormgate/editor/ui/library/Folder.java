package stormgate.editor.ui.library;

import java.io.File;

public class Folder {
	public File path;
	
	public Folder(File file) throws Exception{
		if(file != null && file.isDirectory())
			path = file;
		
		throw new Exception("File is not a folder");
	}
    public File[] listDirectory() {
    	return path.listFiles();
    }
}