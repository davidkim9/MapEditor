package stormgate.editor.ui.forms.browser.components;

import java.awt.BasicStroke;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import stormgate.editor.ui.forms.browser.Thumbnails;

public class LibraryThumbnailButton extends JButton implements MouseListener, MouseMotionListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Squared fixed size of this component.
	 */
	public static final int sqsize = 100;
	private Image thumbimg;
	private String name;
	private boolean mouseover;
	public boolean hasinifile;
	private Thumbnails parent;
	private boolean selected;
	private File file;
	private String extension;
	private File iniFile;

	public LibraryThumbnailButton(Thumbnails thumbnails, File file)
	{
		parent = thumbnails;
		this.file = file;

		setPreferredSize(new Dimension(sqsize, sqsize));
		setMaximumSize(new Dimension(sqsize, sqsize));

		thumbimg = null;
		name = "";

		mouseover = false;

		hasinifile = false;

		addMouseListener(this);
		addMouseMotionListener(this);
		String filename = file.getName();

		int dotIndex = filename.lastIndexOf('.');

		extension = "";

		if (dotIndex >= 0) {
			extension = filename.substring(dotIndex).toLowerCase();
		}

		setFocusable(false);
	}

	public String getExtension()
	{
		return extension;
	}

	public void selected(Boolean set)
	{
		selected = set;
	}

	public void select()
	{
		parent.select(this);
	}

	public void selectDouble()
	{
		parent.selectDouble(this);
	}

	public void setThumbnail(Image thumb)
	{
		thumbimg = thumb;
		repaint();
	}

	public void unload()
	{
		if (thumbimg != null) {
			thumbimg.flush();
		}
		thumbimg = null;
	}

	public Image getThumbnail()
	{
		return thumbimg;
	}

	public void setFileName(String name)
	{
		this.name = name;
		repaint();
	}

	public File getFile()
	{
		return file;
	}

	public String getFileName()
	{
		return name;
	}

	public static Image loadImage(File f1)
	{
		Image img = null;

		try {
			img = ImageIO.read(f1);
			if (img != null) {
				img.flush();
			}
		} catch (IOException e) {
			System.out.println(e + " " + f1);
		}

		return img;
	}

	private String getPath()
	{
		File parentPath = file.getParentFile();
		if (parentPath != null) {
			return parentPath.toString() + "\\";
		}
		return "";
	}

	private void checkImage()
	{
		if (thumbimg == null) {
			Image img = loadImage(file);

			if (img != null) {
				if (isVisible()) {
					setThumbnail(img);
				}
			}
		}
		String filename = file.getName();

		String iniFilename = filename + ".ini";

		iniFile = new File(getPath() + iniFilename);

		hasinifile = iniFile.exists();

		/*
		 * Fix later
		else{
		Rectangle visibleBoundary = getBounds();
		visibleBoundary.y -= parent.getY();
		
		if(! Graphic.aabb(getBounds(), parent.getVisibleRect()) ){
		unload();
		parent.checkGC();
		System.out.println("UNLOAD!");
		}
		}
		 */
	}

	@Override
	public void paint(Graphics g)
	{
		checkImage();
		//Do NOT paint the original button.
		//super.paint(g);
		Graphics2D p = (Graphics2D) g;

		p.clearRect(0, 0, getSize().width, getSize().height);

		if (thumbimg == null) {
			//Draw background.
			p.setColor(SystemColor.control);
			p.fillRect(0, 0, getSize().width, getSize().height);

			//Set Line color and Thickness.
			p.setColor(new Color(255, 0, 0));
			p.setStroke(new BasicStroke(4));

			boolean specialIcon = false;
			Image img = null;

			if (extension.equals(".swf")) {
				//Check if there is a reference image
				//if(hasinifile){
				//     iniFileWTF
				//}
				img = loadImage(new File(getClass().getResource("/stormgate/editor/ui/forms/browser/icons/swf.png").getFile()));

				specialIcon = true;
			} else if (extension.equals(".mp3")) {
				img = loadImage(new File(getClass().getResource("/stormgate/editor/ui/forms/browser/icons/mp3.png").getFile()));
				specialIcon = true;
			} else if (extension.equals(".wav")) {
				img = loadImage(new File(getClass().getResource("/stormgate/editor/ui/forms/browser/icons/wav.png").getFile()));
				specialIcon = true;
			}

			if (specialIcon) {
				if (img != null) {
					int imgw = img.getWidth(null);
					int imgh = img.getHeight(null);
					int middleX = getSize().width / 2 - imgw / 2;
					int middleY = getSize().height / 2 - imgh / 2;
					p.drawImage(img, middleX, middleY, imgw, imgh, null);
				}
			} else {
				//Draw a Cross.
				p.drawLine(0, 0, getSize().width, getSize().height);
				p.drawLine(getSize().width, 0, 0, getSize().height);
			}
		} else {
			int imgw = thumbimg.getWidth(null);
			int imgh = thumbimg.getHeight(null);

			if (imgw > imgh) {
				//Landscape
				if (imgw > getSize().width) {
					//Constrain
					imgh = (int) ((getSize().width / ((double) imgw)) * imgh);
					imgw = getSize().width;
				}
				if (imgh > getSize().height) {
					//Constrain
					imgw = (int) ((getSize().height / ((double) imgh)) * imgw);
					imgh = getSize().height;
				}
			} else {
				//Portrait
				if (imgh > getSize().height) {
					//Constrain
					imgw = (int) ((getSize().height / ((double) imgh)) * imgw);
					imgh = getSize().height;
				}
				if (imgw > getSize().width) {
					//Constrain
					imgh = (int) ((getSize().width / ((double) imgw)) * imgh);
					imgw = getSize().width;
				}
			}
			int middleX = getSize().width / 2 - imgw / 2;
			int middleY = getSize().height / 2 - imgh / 2;
			p.drawImage(thumbimg, middleX, middleY, imgw, imgh, null);
			if (mouseover) {
				p.setColor(new Color(255, 255, 255, 150));
				p.fillRect(0, 0, getSize().width, getSize().height);
			}
		}

		if (selected) {
			p.setColor(new Color(0, 0, 0, 150));
			p.fillRect(0, 0, getSize().width, getSize().height);
		}

		if (!hasinifile) {
			p.setStroke(new BasicStroke(4));
			p.setColor(new Color(0, 0, 180));

			p.drawRect(1, 1, getSize().width - 2, getSize().height - 2);
		}

		p.setColor(new Color(255, 255, 255, 150));
		p.drawString(file.getName(), 11, 21);

		p.setColor(new Color(0, 0, 0, 255));
		p.drawString(file.getName(), 10, 20);

	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
	}

	public boolean allowableExtension()
	{
		return extension.equals(".png") || extension.equals(".jpg") || extension.equals(".jpeg") || extension.equals(".gif") || extension.equals(".bmp") || extension.equals(".swf");
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		if (allowableExtension()) {
			if (e.getButton() == 3) {
				//DEFINE
				parent.defineSelected();
				return;
			}

			if (e.getClickCount() > 1) {
				selectDouble();
				repaint();
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		mouseover = true;
		repaint();
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		mouseover = false;
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		select();
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		// TODO Auto-generated method stub
	}
}
