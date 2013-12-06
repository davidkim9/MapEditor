package stormgate.editor.ui.forms.fileDialog;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.io.File;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.*;

public class FolderChooser extends JTree implements TreeSelectionListener, MouseListener
{

	private static FileSystemView fsv = FileSystemView.getFileSystemView();

	public boolean isSelected = false;

	/*--- Begin Public API -----*/
	public FolderChooser()
	{
		this(null);
	}

	public FolderChooser(File dir)
	{
		super(new DirNode(fsv.getRoots()[0]));
		getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		setSelectedDirectory(dir);
		addTreeSelectionListener(this);
		addMouseListener(this);
	}

	public void setSelectedDirectory(File dir)
	{
		if (dir == null) {
			dir = fsv.getDefaultDirectory();
		}
		setSelectionPath(mkPath(dir));
	}

	public File getSelectedDirectory()
	{
		DirNode node = (DirNode) getLastSelectedPathComponent();
		if (node != null) {
			File dir = node.getDir();
			if (fsv.isFileSystem(dir)) {
				return dir;
			}
		}
		return null;
	}

	public void addActionListener(ActionListener l)
	{
		listenerList.add(ActionListener.class, l);
	}

	public void removeActionListener(ActionListener l)
	{
		listenerList.remove(ActionListener.class, l);
	}

	public ActionListener[] getActionListeners()
	{
		return (ActionListener[]) listenerList.getListeners(ActionListener.class);
	}

	/*--- End Public API -----*/
	/*--- TreeSelectionListener Interface -----*/
	public void valueChanged(TreeSelectionEvent ev)
	{
		File oldDir = null;
		TreePath oldPath = ev.getOldLeadSelectionPath();
		if (oldPath != null) {
			oldDir = ((DirNode) oldPath.getLastPathComponent()).getDir();
			if (!fsv.isFileSystem(oldDir)) {
				oldDir = null;
			}
		}
		File newDir = getSelectedDirectory();
		firePropertyChange("selectedDirectory", oldDir, newDir);
	}

	/*--- MouseListener Interface -----*/
	public void mousePressed(MouseEvent e)
	{
		if (e.getClickCount() == 2) {
			TreePath path = getPathForLocation(e.getX(), e.getY());
			if (path != null && path.equals(getSelectionPath())
					&& getSelectedDirectory() != null) {

				fireActionPerformed("dirSelected", e);
			}
		}
	}

	public void mouseReleased(MouseEvent e)
	{
	}

	public void mouseClicked(MouseEvent e)
	{
	}

	public void mouseEntered(MouseEvent e)
	{
	}

	public void mouseExited(MouseEvent e)
	{
	}


	/*--- Private Section ------*/
	private TreePath mkPath(File dir)
	{
		DirNode root = (DirNode) getModel().getRoot();
		if (root.getDir().equals(dir)) {
			return new TreePath(root);
		}

		TreePath parentPath = mkPath(fsv.getParentDirectory(dir));
		DirNode parentNode = (DirNode) parentPath.getLastPathComponent();
		Enumeration enumeration = parentNode.children();
		while (enumeration.hasMoreElements()) {
			DirNode child = (DirNode) enumeration.nextElement();
			if (child.getDir().equals(dir)) {
				return parentPath.pathByAddingChild(child);
			}
		}
		return null;
	}

	private void fireActionPerformed(String command, InputEvent evt)
	{
		ActionEvent e =
				new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
				command, evt.getWhen(), evt.getModifiers());
		ActionListener[] listeners = getActionListeners();
		for (int i = listeners.length - 1; i >= 0; i--) {
			listeners[i].actionPerformed(e);
		}
	}

	private static class DirNode extends DefaultMutableTreeNode
	{

		DirNode(File dir)
		{
			super(dir);
		}

		public File getDir()
		{
			return (File) userObject;
		}

		public int getChildCount()
		{
			populateChildren();
			return super.getChildCount();
		}

		public Enumeration children()
		{
			populateChildren();
			return super.children();
		}

		public boolean isLeaf()
		{
			return false;
		}

		private void populateChildren()
		{
			if (children == null) {
				File[] files = fsv.getFiles(getDir(), true);
				Arrays.sort(files);
				for (int i = 0; i < files.length; i++) {
					File f = files[i];
					if (fsv.isTraversable(f).booleanValue()) {
						insert(new DirNode(f),
								(children == null) ? 0 : children.size());
					}
				}
			}
		}

		public String toString()
		{
			return fsv.getSystemDisplayName(getDir());
		}

		public boolean equals(Object o)
		{
			return (o instanceof DirNode
					&& userObject.equals(((DirNode) o).userObject));
		}
	}

	public static File showFolderDialog()
	{
		return showFolderDialog(new File("."));
	}

	public static File showFolderDialog(File f)
	{
		final JDialog dialog = new JDialog((JFrame) null, true);
		try {
			f = f.getCanonicalFile();
		} catch (Exception e) {
		}
		final FolderChooser dc = new FolderChooser(f);
		final JButton okButton = new JButton("OK");
		final JButton cancelButton = new JButton("Cancel");

		dialog.getContentPane().add(new JScrollPane(dc), BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		dialog.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

		ActionListener actionListener = new ActionListener()
		{

			File selectedDirectory = null;

			public void actionPerformed(ActionEvent e)
			{
				Object c = e.getSource();
				if (c == okButton || c == dc) {
					dc.isSelected = true;
				}

				dialog.hide();
			}
		};

		dc.addActionListener(actionListener);
		okButton.addActionListener(actionListener);
		cancelButton.addActionListener(actionListener);

		dc.addPropertyChangeListener(new PropertyChangeListener()
		{

			public void propertyChange(PropertyChangeEvent ev)
			{
				if (ev.getPropertyName().equals("selectedDirectory")) {
					okButton.setEnabled(dc.getSelectedDirectory() != null);
				}
			}
		});

		dialog.setBounds(200, 200, 300, 350);
		dc.scrollRowToVisible(Math.max(0, dc.getMinSelectionRow() - 4));
		dialog.show();

		if (dc.isSelected) {
			return dc.getSelectedDirectory();
		}
		return null;
	}
}
