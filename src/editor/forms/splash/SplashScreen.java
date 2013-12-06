package editor.forms.splash;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class SplashScreen extends JWindow {

	private int duration;

	private static final String SPLASH = "/editor/forms/splash/splash.png";

	private Image splashImage;

	public SplashScreen(int d) {
		duration = d;
		
		try {
			splashImage = ImageIO.read( getClass().getResource(SPLASH) );
		} catch (IOException ex) {

		}
	}

	public void paint(Graphics g) {
		g.drawImage(splashImage, 0, 0, null);
	}

	// A simple little method to show a title screen in the center
	// of the screen for the amount of time given in the constructor
	public void showSplash() {

		JPanel content = (JPanel) getContentPane();
		content.setBackground(Color.BLACK);

		// Set the window's bounds, centering the window
		int width = splashImage.getWidth(null);
		int height = splashImage.getHeight(null);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screen.width - width) / 2;
		int y = (screen.height - height) / 2;
		setBounds(x, y, width, height);

		// Build the splash screen

		// Display it
		setVisible(true);

		// Wait a little while, maybe while loading resources
		try {
			Thread.sleep(duration);
		} catch (Exception e) {
		}

		setVisible(false);

		dispose();
	}
}
