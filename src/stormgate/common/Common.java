package stormgate.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class Common
{

	public static boolean copyFile(File f1, File f2)
	{
		try {
			InputStream in = new FileInputStream(f1);
			OutputStream out = new FileOutputStream(f2);

			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}

			in.close();
			out.close();

			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static int parseInt(String number)
	{
		try {
			return Integer.parseInt(number);
		} catch (NumberFormatException ex) {
			return 0;
		}
	}

	public static double parseDouble(String number)
	{
		try {
			return Double.parseDouble(number);
		} catch (NumberFormatException ex) {
			return 0;
		}
	}

	public static boolean abb(int aX, int aY, int bX1, int bY1, int bX2, int bY2)
	{

		if (aX > bX2 || aX < bX1 || aY > bY2 || aY < bY1) {
			return false;
		}

		return true;
	}
}
