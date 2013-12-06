package stormgate.editor.ui.library;

import stormgate.image.ImageWrapper;
import stormgate.image.data.ImageData;

public interface LibraryInterface {
	public ImageWrapper getSelectedGraphic();
	public Folder getSelectedFolder();
	public void setGraphicIni(ImageWrapper graphic, ImageData data);
}