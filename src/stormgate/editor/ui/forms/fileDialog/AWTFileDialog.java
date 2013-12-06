/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stormgate.editor.ui.forms.fileDialog;

/**
 *
 * @author David
 */
import java.awt.Component;
import java.awt.FileDialog;
import java.awt.Frame;

public class AWTFileDialog extends Component
{
	//Dimension   dSize     = new Dimension(500,400) ;
	boolean bLog = false;

	public static boolean locked = false;

	public AWTFileDialog(){
	}

	/*
	 *  Show an Open file dialog
	 */
	public String openFile(String title, String defDir, String fileType)
	{
		locked = true;
		String sFile = null;

		log("title:" + title);
		log("defdir:" + defDir);
		log("filetype:" + fileType);

		FileDialog fd = new FileDialog(new Frame(), title, FileDialog.LOAD);
		fd.setFile(fileType);
		fd.setDirectory(defDir);
		fd.setResizable(true);

		//fd.show();
		fd.setVisible(true);
		
		if (fd.getFile() != null) {
			sFile = fd.getDirectory() + fd.getFile();
		}
		//sFile = fd.getDirectory() + System.getProperty("file.separator") + fd.getFile();
		locked = false;
		return sFile;
	}

	/*
	 *   Show a Save file dialog
	 */
	public String saveFile(String title, String defDir, String fileType)
	{
		locked = true;
		String sFile = null;

		log("title:" + title);
		log("defdir:" + defDir);
		log("filetype:" + fileType);

		FileDialog fd = new FileDialog(new Frame(), title, FileDialog.SAVE);
		fd.setFile(fileType);
		fd.setDirectory(defDir);

		//fd.show();
		try{
			fd.setVisible(true);
		}catch(Exception e){
		}

		if (fd.getFile() != null) {
			sFile = fd.getDirectory() + fd.getFile();
		}
		//sFile = fd.getDirectory() + System.getProperty("file.separator") + fd.getFile();
		locked = false;
		return sFile;
	}

	public void setLog(boolean b)
	{
		bLog = b;
	}

	void log(String sMessage)
	{
		if (bLog) {
			System.out.println(sMessage);
		}
	}
}
