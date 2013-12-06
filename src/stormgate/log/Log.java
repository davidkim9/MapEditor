package stormgate.log;

/**
 *
 * @author David
 */
public class Log {

	private static Logger logger = new Logger();

	public static void addLog(String message){
		logger.addLog(message);
	}

	public static void addLogNoShow(String message){
		logger.addLogNoShow(message);
	}

	public static void showLog(){
		logger.setVisible(true);
	}
}
