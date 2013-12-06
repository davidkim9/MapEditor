package stormgate.editor.ui.forms.browser;

import it.cnr.imaa.essi.lablib.gui.checkboxtree.CheckboxTree;
import it.cnr.imaa.essi.lablib.gui.checkboxtree.DefaultCheckboxTreeCellRenderer;
import it.cnr.imaa.essi.lablib.gui.checkboxtree.TreeCheckingEvent;
import it.cnr.imaa.essi.lablib.gui.checkboxtree.TreeCheckingListener;
import it.cnr.imaa.essi.lablib.gui.checkboxtree.TreeCheckingModel;
import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class LibraryViewer extends JPanel
{

	private static final long serialVersionUID = 1L;
	private CheckboxTree thumbfiltertree;
	private MutableTreeNode root;
	private Thumbnails thumbnailBrowser;
	public static String mainDirectory = "library";
	private JScrollPane sp;

	public LibraryViewer()
	{
		setPreferredSize(new Dimension(512, 256));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		root = new DefaultMutableTreeNode("root");
		listDirectory();

		sp = getCheckboxTree();//new JScrollPane(thumbfiltertree);
		sp.setMinimumSize(new Dimension(200, Integer.MAX_VALUE));
		sp.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

		add(sp);
	}

	private void addNode(TreePath path, MutableTreeNode node)
	{
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode) path.getLastPathComponent();
		parent.add(node);
		DefaultTreeModel dtm = (DefaultTreeModel) thumbfiltertree.getModel();
		dtm.nodesWereInserted(parent, new int[]{parent.getIndex(node)});
	}

	private void removeNode(MutableTreeNode node)
	{
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
		if (parent != null) {
			int index = parent.getIndex(node);
			node.removeFromParent();
			DefaultTreeModel dtm = (DefaultTreeModel) thumbfiltertree.getModel();
			dtm.nodesWereRemoved(parent, new int[]{index}, new TreeNode[]{node});
		}
	}

	private MutableTreeNode getChildByName(MutableTreeNode node, String name)
	{
		for (int i = 0; i < node.getChildCount(); i++) {
			MutableTreeNode child = (MutableTreeNode) node.getChildAt(i);
			if (child.toString().equals(name)) {
				return child;
			}
		}
		return null;
	}

	public TreePath getPath(TreeNode treeNode)
	{
		ArrayList<Object> nodes = new ArrayList<Object>();
		if (treeNode != null) {
			nodes.add(treeNode);
			treeNode = treeNode.getParent();
			while (treeNode != null) {
				nodes.add(0, treeNode);
				treeNode = treeNode.getParent();
			}
		}

		return nodes.isEmpty() ? null : new TreePath(nodes.toArray());
	}

	private boolean checkTreeFiles(MutableTreeNode node, File directory)
	{
		//x.x
                boolean treeModified = false;
		//Check files to see if tree elements exist. If an element does not exist, add it
		String[] children = directory.list();
		if (children != null) {
			for (int i = 0; i < children.length; i++) {
				// Get filename of file or directory
				String filename = children[i];
				File f = new File(directory.getPath() + "/" + filename);
				if (f.isDirectory()) {
					//Check if it's in root, if not add it
					MutableTreeNode child = getChildByName(node, filename);
					if (child == null) {
						child = new DefaultMutableTreeNode(filename);
						addNode(getPath(node), child);
                                                treeModified = true;
					}
					treeModified = checkTreeFiles(child, f) || treeModified;
					child = null;
				}
				f = null;
			}
		}
		children = null;
                
		//Check directories if the files exists. If an file does not exist, remove it
		for (int i = 0; i < node.getChildCount(); i++) {
			MutableTreeNode child = (MutableTreeNode) node.getChildAt(i);

			File checkFile = new File(directory.getPath() + "\\" + child.toString());

			if (checkFile.exists() && checkFile.isDirectory()) {
				treeModified = checkTreeFiles(child, checkFile) || treeModified;
			} else {
				node.remove(i);
				DefaultTreeModel dtm = (DefaultTreeModel) thumbfiltertree.getModel();
				dtm.nodesWereRemoved(node, new int[]{i}, new TreeNode[]{child});
				i--;
			}
		}
                
                return treeModified;
	}

	public void resetLibrary()
	{
            while(root.getChildCount() > 0){
                    removeNode((MutableTreeNode) root.getChildAt(0));
            }
            
            checkTreeFiles(root, new File(mainDirectory));
            
            TreeCheckingModel model = thumbfiltertree.getCheckingModel();
            model.clearChecking();

            onFilter(); //slow, fix this if possible I think it has to do with image loading

            revalidate();
	}

	public void refresh()
	{
            //Note: Sometimes root is empty(occurs when there is a save as dialog and you click "Desktop"
            boolean modified = checkTreeFiles(root, new File(mainDirectory));
            if(modified){
                TreeCheckingModel model = thumbfiltertree.getCheckingModel();
                model.clearChecking();

                onFilter(); //slow, fix this if possible I think it has to do with image loading
            }
            revalidate();
	}

	public void removeSelected()
	{
		TreePath selected = thumbfiltertree.getSelectionPath();
		if (selected != null) {
			DefaultMutableTreeNode child = (DefaultMutableTreeNode) selected.getLastPathComponent();
			DefaultMutableTreeNode parent = (DefaultMutableTreeNode) child.getParent();
			if (parent != null) {
				int index = parent.getIndex(child);
				child.removeFromParent();
				DefaultTreeModel dtm = (DefaultTreeModel) thumbfiltertree.getModel();
				dtm.nodesWereRemoved(parent, new int[]{index}, new TreeNode[]{child});
			}
		}
	}

	public void addSelected(String name)
	{
		TreePath selected = thumbfiltertree.getSelectionPath();
		if (selected != null) {
			DefaultMutableTreeNode parent = (DefaultMutableTreeNode) selected.getLastPathComponent();
			DefaultMutableTreeNode child = new DefaultMutableTreeNode(name);
			parent.add(child);
			DefaultTreeModel dtm = (DefaultTreeModel) thumbfiltertree.getModel();
			dtm.nodesWereInserted(parent, new int[]{parent.getIndex(child)});
			thumbfiltertree.expandPath(selected);
		}
	}

	public void renameSelected(String name)
	{
		TreePath selected = thumbfiltertree.getSelectionPath();
		if (selected != null) {
			MutableTreeNode node = (MutableTreeNode) selected.getLastPathComponent();
			node.setUserObject(name);

			DefaultTreeModel dtm = (DefaultTreeModel) thumbfiltertree.getModel();
			dtm.nodeChanged(node);
		}
	}

	public TreePath getSelected()
	{
		return thumbfiltertree.getSelectionPath();
	}

	public void setThumbnail(Thumbnails thumbnails)
	{
		thumbnailBrowser = thumbnails;
	}

	private void listDirectory()
	{
		//root = new DefaultMutableTreeNode("WTF!");
		listDirectory(root, new File(mainDirectory));
		revalidate();
	}

	private void listDirectory(MutableTreeNode node, File dir)
	{
		String[] children = dir.list();
		if (children != null) {
			for (int i = 0; i < children.length; i++) {
				// Get filename of file or directory
				String filename = children[i];
				File f = new File(dir.getPath() + "/" + filename);
				if (f.isDirectory()) {
					DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(filename);
					node.insert(newNode, 0);
					listDirectory(newNode, f);
				}
			}
		}
	}

	public File getPathFile()
	{
		return new File(returnPath(getSelected()));
	}

	public String returnPath(TreePath path)
	{
		Object lastComponent = root;

		if (path != null) {
			lastComponent = path.getLastPathComponent();
		}

		if (lastComponent == root) {
			return mainDirectory;
		}

		String last = "/" + lastComponent;
		TreePath parentPath = path.getParentPath();

		String parent = mainDirectory;
		if (parentPath != null && parentPath.getLastPathComponent() != root) {
			parent = returnPath(parentPath);
		}

		return parent + last;
	}

	public void onFilter()
	{
		TreePath[] paths = thumbfiltertree.getCheckingPaths();
		ArrayList<TreePath> directories = new ArrayList<TreePath>();
		if (paths != null) {
			for (int i = 0; i < paths.length; i++) {
				TreePath path = paths[i];
				if (!directories.contains(path)) {
					directories.add(path);
				}
			}
		}

		paths = thumbfiltertree.getGreyingPaths();
		TreeCheckingModel model = thumbfiltertree.getCheckingModel();

		if (paths != null) {
			for (int i = 0; i < paths.length; i++) {
				TreePath path = paths[i];
				if (!directories.contains(path)) {
					if (model.isPathChecked(path)) {
						directories.add(path);
					}
				}
			}
		}

		ArrayList<File> files = new ArrayList<File>();
		for (int i = 0; i < directories.size(); i++) {
			File path = new File(returnPath(directories.get(i)));
			ArrayList<File> allFiles = getFiles(path);
			files.addAll(allFiles);
		}

		Collections.sort(files);

		thumbnailBrowser.displayFiles(files);
	}

	private ArrayList<File> getFiles(File path)
	{
		ArrayList<File> fileList = new ArrayList<File>();
		String[] list = path.list();
		if (list != null) {
			for (int i = 0; i < list.length; i++) {
				File local = new File(path + "/" + list[i]);
				if (!local.isDirectory()) {
					String fileName = local.getName();
					int dotIndex = fileName.lastIndexOf('.');

					String extension = "";

					if (dotIndex != -1) {
						extension = fileName.substring(dotIndex);
					}

					if (!extension.toLowerCase().equals(".ini") && !extension.toLowerCase().equals(".ref")) {
						fileList.add(local);
					}
				}
			}
		}
		return fileList;
	}

	private ArrayList<File> getAllFiles(File path)
	{
		ArrayList<File> fileList = new ArrayList<File>();
		String[] list = path.list();
		if (list != null) {
			for (int i = 0; i < list.length; i++) {
				File local = new File(path + "/" + list[i]);
				if (local.isDirectory()) {
					ArrayList<File> recursive = getAllFiles(local);
					fileList.addAll(recursive);
				} else {
					String fileName = local.getName();
					int dotIndex = fileName.lastIndexOf('.');

					String extension = "";

					if (dotIndex != -1) {
						extension = fileName.substring(dotIndex);
					}

					if (!extension.toLowerCase().equals(".ini")) {
						fileList.add(local);
					}
				}
			}
		}
		return fileList;
	}

	public ArrayList<File> getAllFiles()
	{
		TreePath[] paths = thumbfiltertree.getCheckingRoots();
		ArrayList<File> directories = new ArrayList<File>();
		if (paths != null) {
			for (int i = 0; i < paths.length; i++) {
				TreePath path = paths[i];
				directories.add(new File(returnPath(path)));
			}
		}

		ArrayList<File> files = new ArrayList<File>();
		for (int i = 0; i < directories.size(); i++) {
			File path = directories.get(i);
			ArrayList<File> allFiles = getAllFiles(path);
			files.addAll(allFiles);
		}

		return files;
	}

	/**
	 * This method initializes checkboxTree
	 */
	private JScrollPane getCheckboxTree()
	{
		if (thumbfiltertree == null) {
			thumbfiltertree = new CheckboxTree(root);
			//thumbfiltertree.getCheckingModel().setCheckingMode(TreeCheckingModel.CheckingMode.PROPAGATE_PRESERVING_UNCHECK);
			thumbfiltertree.getCheckingModel().setCheckingMode(TreeCheckingModel.CheckingMode.SIMPLE);
			thumbfiltertree.setRootVisible(true);
			thumbfiltertree.setEnabled(true);
			//thumbfiltertree.expandAll();
			DefaultCheckboxTreeCellRenderer renderer = new DefaultCheckboxTreeCellRenderer();
			renderer.setIcons();
			thumbfiltertree.setCellRenderer(renderer);
			thumbfiltertree.addTreeCheckingListener(new TreeCheckingListener()
			{

				public void valueChanged(TreeCheckingEvent e)
				{
					//System.out.println("checking set changed, leading path: " + ((TreeNode) e.getPath().getLastPathComponent()).toString());
					onFilter();
				}
			});
		}
		return new JScrollPane(thumbfiltertree);
	}

	public static boolean fileInLibrary(File child)
	{
		if (child != null) {
			File parent = new File(LibraryViewer.mainDirectory);
			;
			try {
				child = child.getCanonicalFile();
				parent = parent.getCanonicalFile();
			} catch (Exception e) {
			}

			//Tile must be within the library
			if (isChildofFile(parent, child)) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	private static boolean isChildofFile(File parent, File child)
	{
		if (child == null) {
			return false;
		}
		if (parent.equals(child)) {
			return true;
		}

		return isChildofFile(parent, child.getParentFile());
	}
}
