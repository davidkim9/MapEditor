/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stormgate.compile;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/**
 *
 * @author David
 */
public class Compile
{

	public static String compilerDirectory = "compiler";

	public static boolean compile(String src)
	{
		return compile(src, "bin");
	}

	public static boolean compile(String src, String bin)
	{
		if(src == null){
			return false;
		}
		
		try {
                       File binFile = new File(bin);
                       if(!binFile.exists()){
                           binFile.mkdirs();
                       }
                       String currentLocation = System.getProperty("user.dir");
			// Run a java app in a separate system process
                        stormgate.log.Log.addLogNoShow("java -jar compiler/Compiler.jar -editor " + currentLocation + " -src " + src + " -bin " + bin);
			Process proc = Runtime.getRuntime().exec("java -jar compiler/Compiler.jar -editor " + currentLocation + " -src " + src + " -bin " + bin);
			// Then retreive the process output
			java.io.InputStream in = proc.getInputStream();
			java.io.InputStream err = proc.getErrorStream();

			String line;

			//Output
			BufferedReader input = new BufferedReader(new InputStreamReader(in));
			while ((line = input.readLine()) != null) {
				System.out.println(line);
				stormgate.log.Log.addLogNoShow(line);
			}
			input.close();

			//Errors
			BufferedReader error = new BufferedReader(new InputStreamReader(err));
			while ((line = error.readLine()) != null) {
				System.out.println(line);
				stormgate.log.Log.addLogNoShow(line);
			}

			proc.waitFor();
			int exitValue = proc.exitValue();
			proc.destroy();

			if (exitValue == 0) {
				return true;
			}

			//Error Occured
			return false;
		} catch (Exception e) {

			e.printStackTrace();

			return false;
		}
	}
}
