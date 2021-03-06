/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * BrowserControl.java
 *
 * Created on Mar 22, 2011, 6:32:21 PM
 */
package stormgate.editor.ui.forms.browser;

import java.awt.Frame;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;
import stormgate.common.Common;
import stormgate.editor.ui.forms.dialog.GraphicDialog;
import stormgate.editor.ui.forms.fileDialog.AWTFileDialog;
import stormgate.editor.ui.forms.fileDialog.LibraryPathAction;
import stormgate.editor.ui.forms.fileDialog.LibraryPathDialog;
import stormgate.image.ImageManager;
import stormgate.image.LibraryResource;

/**
 *
 * @author David
 */
public class BrowserControl extends javax.swing.JPanel
{

	private LibraryViewer libraryViewer;
	private Thumbnails thumbnails;
	private ImageManager manager;

	/** Creates new form BrowserControl */
	public BrowserControl()
	{
		initComponents();
	}

	public void setManager(ImageManager manager)
	{
		this.manager = manager;
	}

	public void setThumbnails(Thumbnails thumbnails, LibraryViewer libraryViewer)
	{
		this.libraryViewer = libraryViewer;
		this.thumbnails = thumbnails;
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();

        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jToolBar1.setFloatable(false);
        jToolBar1.setOrientation(1);
        jToolBar1.setRollover(true);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stormgate/editor/ui/forms/icons/picture_edit.png"))); // NOI18N
        jButton1.setToolTipText("Edit Graphic Definition");
        jButton1.setMaximumSize(new java.awt.Dimension(25, 25));
        jButton1.setMinimumSize(new java.awt.Dimension(25, 25));
        jButton1.setPreferredSize(new java.awt.Dimension(25, 25));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stormgate/editor/ui/forms/icons/picture_add.png"))); // NOI18N
        jButton3.setToolTipText("Import Graphic");
        jButton3.setMargin(new java.awt.Insets(2, 12, 2, 12));
        jButton3.setMaximumSize(new java.awt.Dimension(25, 25));
        jButton3.setMinimumSize(new java.awt.Dimension(25, 25));
        jButton3.setPreferredSize(new java.awt.Dimension(25, 25));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton3);

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stormgate/editor/ui/forms/icons/picture_delete.png"))); // NOI18N
        jButton4.setToolTipText("Delete Graphic");
        jButton4.setMaximumSize(new java.awt.Dimension(25, 25));
        jButton4.setMinimumSize(new java.awt.Dimension(25, 25));
        jButton4.setPreferredSize(new java.awt.Dimension(25, 25));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton4);

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stormgate/editor/ui/forms/icons/picture_go.png"))); // NOI18N
        jButton5.setToolTipText("Move Graphic");
        jButton5.setMaximumSize(new java.awt.Dimension(25, 25));
        jButton5.setMinimumSize(new java.awt.Dimension(25, 25));
        jButton5.setPreferredSize(new java.awt.Dimension(25, 25));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton5);

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stormgate/editor/ui/forms/icons/folder_add.png"))); // NOI18N
        jButton7.setToolTipText("Add Folder");
        jButton7.setMaximumSize(new java.awt.Dimension(25, 25));
        jButton7.setMinimumSize(new java.awt.Dimension(25, 25));
        jButton7.setPreferredSize(new java.awt.Dimension(25, 25));
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton7);

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stormgate/editor/ui/forms/icons/folder_delete.png"))); // NOI18N
        jButton8.setToolTipText("Remove Folder");
        jButton8.setMaximumSize(new java.awt.Dimension(25, 25));
        jButton8.setMinimumSize(new java.awt.Dimension(25, 25));
        jButton8.setPreferredSize(new java.awt.Dimension(25, 25));
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton8);

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stormgate/editor/ui/forms/icons/folder_edit.png"))); // NOI18N
        jButton6.setToolTipText("Rename Folder");
        jButton6.setMaximumSize(new java.awt.Dimension(25, 25));
        jButton6.setMinimumSize(new java.awt.Dimension(25, 25));
        jButton6.setPreferredSize(new java.awt.Dimension(25, 25));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton6);

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stormgate/editor/ui/forms/icons/brick.png"))); // NOI18N
        jButton9.setToolTipText("Define All Graphics");
        jButton9.setMargin(new java.awt.Insets(2, 12, 2, 12));
        jButton9.setMaximumSize(new java.awt.Dimension(25, 25));
        jButton9.setMinimumSize(new java.awt.Dimension(25, 25));
        jButton9.setPreferredSize(new java.awt.Dimension(25, 25));
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton9);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

	private File getSelectedThumb()
	{
		return thumbnails.getSelected();
	}

	public void editGraphic()
	{
		jButton1ActionPerformed(null);
	}

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
		//EDIT
		File selected = getSelectedThumb();
		if (selected != null) {
			//LibraryResource can also be a non image so this might not work
			LibraryResource resource = manager.getResource(selected.getAbsolutePath());
			GraphicDialog graphicDialog = new GraphicDialog(selected, resource, manager);
			graphicDialog.setVisible(true);
		}
		libraryViewer.refresh();
	}//GEN-LAST:event_jButton1ActionPerformed

	public void importGraphic()
	{
		jButton3ActionPerformed(null);
	}

	private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
		//IMPORT GRAPHIC
		AWTFileDialog fd = new AWTFileDialog();
		String fileSelected = fd.openFile("Open", ".", "");
		if (fileSelected != null && fileSelected.length() > 0) {
			File f = new File(fileSelected);
			if (f.exists()) {
				//File destination = new File();
				//Common.copyFile(f, );
				TreePath path = libraryViewer.getSelected();
				if (path == null) {
					LibraryPathAction action = new LibraryPathActionImpl(f);
					LibraryPathDialog libPicker = new LibraryPathDialog(action);
					libPicker.setVisible(true);
				} else {
					File copyTo = new File(libraryViewer.returnPath(path) + "/" + f.getName());
					Common.copyFile(f, copyTo);
				}
			}
		}
		libraryViewer.refresh();
	}//GEN-LAST:event_jButton3ActionPerformed

	public void defineAllGraphics()
	{
		jButton9ActionPerformed(null);
	}

	private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
		ArrayList<File> files = libraryViewer.getAllFiles();

		for (int i = 0; i < files.size(); i++) {
			File file = files.get(i);
			String url = file.getAbsolutePath();
			LibraryResource resource = manager.getResource(url);
			resource.saveIni();
			manager.updateResource(url);
		}
		libraryViewer.refresh();
	}//GEN-LAST:event_jButton9ActionPerformed

	public void deleteGraphic()
	{
		jButton4ActionPerformed(null);
	}

	private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
		//DELETE GRAPHIC + INI
		File selected = getSelectedThumb();
		if (selected != null) {
			int r = JOptionPane.showConfirmDialog(new Frame(), "Do you really want to delete this?");
			if (r == 0) {
				//DELETE!
				String filename = selected.getName();

				String iniFilename = filename + ".ini";

				File parentPath = selected.getParentFile();

				String path = parentPath.toString() + "\\";

				File iniFile = new File(path + iniFilename);

				if (iniFile.exists()) {
					iniFile.delete();
				}

				if (selected.exists()) {
					selected.delete();
				}
			}
		}
		libraryViewer.refresh();
	}//GEN-LAST:event_jButton4ActionPerformed

	public void moveGraphic()
	{
		jButton5ActionPerformed(null);
	}

	private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
		//MOVE GRAPHIC
		File selected = getSelectedThumb();
		if (selected != null) {
			LibraryPathAction action = new LibraryPathActionMove(selected);
			LibraryPathDialog libPicker = new LibraryPathDialog(action);
			libPicker.setVisible(true);
		}
		libraryViewer.refresh();
	}//GEN-LAST:event_jButton5ActionPerformed

	public void newFolder()
	{
		jButton7ActionPerformed(null);
	}

	private void jButton7ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton7ActionPerformed
	{//GEN-HEADEREND:event_jButton7ActionPerformed
		//new folder
		String input = JOptionPane.showInputDialog("Enter the folder name");

		if (input != null) {

			File selected = libraryViewer.getPathFile();
			String folderPath = selected.getAbsolutePath() + "\\" + input;
			File newFolder = new File(folderPath);

			if (!newFolder.exists()) {
				if (newFolder.mkdir()) {
					libraryViewer.addSelected(input);
				} else {
					JOptionPane.showMessageDialog(new Frame(), "Failed to create directory");
				}
			} else {
				JOptionPane.showMessageDialog(new Frame(), "This folder already exists");
			}

		}
		libraryViewer.refresh();
	}//GEN-LAST:event_jButton7ActionPerformed

	public void deleteFolder()
	{
		jButton8ActionPerformed(null);
	}

	private void jButton8ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton8ActionPerformed
	{//GEN-HEADEREND:event_jButton8ActionPerformed
		//delete folder
		File selected = libraryViewer.getPathFile();

		if (selected.getName().equals(LibraryViewer.mainDirectory)) {
			JOptionPane.showMessageDialog(new Frame(), "You must select a folder to rename");
			return;
		}

		if (selected != null) {
			int r = JOptionPane.showConfirmDialog(new Frame(), selected.getAbsolutePath() + "\nDo you really want to delete folder and everything in it?");
			if (r == 0) {
				//DELETE!
				if (selected.exists()) {
					removeDirectory(selected);
					libraryViewer.removeSelected();
				} else {
					JOptionPane.showMessageDialog(new Frame(), "This folder does not exist");
				}
			}
		}
		libraryViewer.refresh();
	}//GEN-LAST:event_jButton8ActionPerformed

	public boolean removeDirectory(File directory)
	{
		if (directory == null) {
			return false;
		}
		if (!directory.exists()) {
			return true;
		}
		if (!directory.isDirectory()) {
			return false;
		}

		String[] list = directory.list();

		if (list != null) {
			for (int i = 0; i < list.length; i++) {
				File entry = new File(directory, list[i]);

				if (entry.isDirectory()) {
					if (!removeDirectory(entry)) {
						return false;
					}
				} else {
					if (!entry.delete()) {
						return false;
					}
				}
			}
		}
		return directory.delete();
	}

	public void renameFolder()
	{
		jButton6ActionPerformed(null);
	}

	private void jButton6ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton6ActionPerformed
	{//GEN-HEADEREND:event_jButton6ActionPerformed
		//rename folder
		File selected = libraryViewer.getPathFile();

		if (selected.getName().equals(LibraryViewer.mainDirectory)) {
			JOptionPane.showMessageDialog(new Frame(), "You must select a folder to rename");
			return;
		}

		String input = JOptionPane.showInputDialog("Enter the new folder name", selected.getName());

		if (input != null) {
			File parentPath = selected.getParentFile();

			String path = parentPath.toString() + "\\";

			String folderPath = path + "\\" + input;
			File newFolder = new File(folderPath);

			if (!newFolder.exists()) {
				if (selected.renameTo(newFolder)) {
					libraryViewer.renameSelected(input);
				} else {
					JOptionPane.showMessageDialog(new Frame(), "Failed to rename folder");
				}
			} else {
				JOptionPane.showMessageDialog(new Frame(), "This folder already exists");
			}

		}
		libraryViewer.refresh();
	}//GEN-LAST:event_jButton6ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration                   

	public void renameGraphic()
	{
		File selected = getSelectedThumb();
		if (selected != null) {

			String input = JOptionPane.showInputDialog("Enter the new graphic name", selected.getName());

			if (input != null) {

				File parentPath = selected.getParentFile();

				String path = parentPath.toString() + "\\";

				File newFile = new File(path + input);

				String filename = selected.getName();

				String iniFilename = filename + ".ini";

				File iniFile = new File(path + iniFilename);

				File newIniFile = new File(path + input + ".ini");

				if (!newFile.exists()) {
					if (selected.renameTo(newFile)) {

						iniFile.renameTo(newIniFile);

					} else {
						JOptionPane.showMessageDialog(new Frame(), "Failed to rename file");
					}

				} else {
					JOptionPane.showMessageDialog(new Frame(), "This folder already file");
				}

			}

		}
		libraryViewer.refresh();
	}

	public void moveFolder()
	{
		//rename folder
		File selected = libraryViewer.getPathFile();

		if (selected.getName().equals(LibraryViewer.mainDirectory)) {
			JOptionPane.showMessageDialog(new Frame(), "You must select a folder to move");
			return;
		}

		if (selected != null) {
			LibraryFolderActionMove action = new LibraryFolderActionMove(selected);
			LibraryPathDialog libPicker = new LibraryPathDialog(action);
			libPicker.setVisible(true);
		}
		libraryViewer.refresh();
	}
    // End of variables declaration//GEN-END:variables

	private class LibraryPathActionImpl implements LibraryPathAction
	{

		File f;

		public LibraryPathActionImpl(File copy)
		{
			f = copy;
		}

		public boolean select(String path)
		{

			File copyTo = new File(path + "/" + f.getName());
			//System.out.println(f.exists() + " " + copyTo.exists());
			Common.copyFile(f, copyTo);
			return true;
		}
	}

	private class LibraryPathActionMove implements LibraryPathAction
	{

		File f;

		public LibraryPathActionMove(File copy)
		{
			f = copy;
		}

		public boolean moveFile(File original, File newLocation)
		{
			//Move ini file and file, delete any old ones

			String filename = newLocation.getName();

			String iniFilename = filename + ".ini";

			//New Path
			File parentPath = newLocation.getParentFile();
			String path = parentPath.toString() + "\\";

			File iniFile = new File(path + iniFilename);

			if (iniFile.exists()) {
				iniFile.delete();
			}

			if (newLocation.exists()) {
				newLocation.delete();
			}

			//New Path
			File oldParentPath = original.getParentFile();
			String oldPath = oldParentPath.toString() + "\\";
			File oldIniFile = new File(oldPath + iniFilename);
			if (oldIniFile.exists()) {
				oldIniFile.renameTo(iniFile);
			}

			if (!original.renameTo(newLocation)) {
				JOptionPane.showMessageDialog(new Frame(), "Failed to move the file");
			}

			return true;
		}

		public boolean select(String path)
		{
			File copyTo = new File(path + "/" + f.getName());

			if (copyTo.exists()) {
				Object[] options = {"Move Replace File",
									"Move and rename",
									"Don't Move"};
				int r = JOptionPane.showOptionDialog(new Frame(),
						"There is already a file with the same name in this location",
						"Move File",
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE,
						null,
						options,
						options[2]);
				if (r == 0) {
					//Do it anyway
					moveFile(f, copyTo);
				} else {
					if (r == 1) {
						boolean success = false;
						while (!success) {
							//Rename
							String input = JOptionPane.showInputDialog("Enter the new file name");
							if (input != null) {
								String folderPath = path + "\\" + input;
								File newFile = new File(folderPath);

								if (!newFile.exists()) {
									moveFile(f, newFile);
									success = true;
								} else {
									JOptionPane.showMessageDialog(new Frame(), "That file already exists, enter another name");
								}
							} else {
								success = true;
							}
						}
					}
				}
			} else {
				//Move file
				moveFile(f, copyTo);
			}

			return true;
		}
	}

	private class LibraryFolderActionMove implements LibraryPathAction
	{

		File f;

		public LibraryFolderActionMove(File copy)
		{
			f = copy;
		}

		public boolean moveFile(File original, File newLocation)
		{
			//New Path
			if (newLocation.exists()) {
				newLocation.delete();
			}

			//New Path
			if (!original.renameTo(newLocation)) {
				JOptionPane.showMessageDialog(new Frame(), "Failed to move the file");
			}

			return true;
		}

		public boolean copyDirectory(File original, File newLocation)
		{
			if (original == null) {
				return false;
			}
			if (!original.exists()) {
				return true;
			}
			if (!original.isDirectory()) {
				return false;
			}

			if (!newLocation.exists()) {
				newLocation.mkdirs();
			}

			String[] list = original.list();

			if (list != null) {
				for (int i = 0; i < list.length; i++) {
					File entry = new File(original, list[i]);
					File newEntry = new File(newLocation, list[i]);

					if (entry.isDirectory()) {

						if (!copyDirectory(entry, newEntry)) {
							return false;
						}
					} else {
						if (newEntry.exists()) {
							newEntry.delete();
						}
						if (!entry.renameTo(newEntry)) {
							return false;
						}
					}
				}
			}
			return original.delete();
		}

		public boolean select(String path)
		{
			File copyTo = new File(path + "/" + f.getName());

			if (copyTo.exists()) {
				Object[] options = {"Rename Folder",
									"Copy and replace contents to new folder",
									"Don't Move"};
				int r = JOptionPane.showOptionDialog(new Frame(),
						"There is already a folder with the same name in this location",
						"Move File",
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE,
						null,
						options,
						options[2]);
				if (r == 0) {
					boolean success = false;
					while (!success) {
						//Rename
						String input = JOptionPane.showInputDialog("Enter the new folder name");
						if (input != null) {
							String folderPath = path + "\\" + input;
							File newFile = new File(folderPath);

							if (!newFile.exists()) {
								moveFile(f, newFile);
								success = true;
							} else {
								JOptionPane.showMessageDialog(new Frame(), "That folder already exists, enter another name");
							}
						} else {
							success = true;
						}
					}
				} else if (r == 1) {
					//Copy Files Over, Delete Folder After
					copyDirectory(f, copyTo);
				}
			} else {
				//Move file
				moveFile(f, copyTo);
			}

			return true;
		}
	}
}
