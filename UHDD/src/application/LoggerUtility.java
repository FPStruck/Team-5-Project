package application;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

/*	Types of messages Logger can use
 * logger.severe("This is a severe error message."); 
 * logger.warning("This is a warning message.");
 * logger.info("This is an informational message.");
 * logger.config("This is a configuration message.");
 * logger.fine("This is a fine-level message.");
 * logger.finer("This is a finer-level message.");
 * logger.finest("This is a finest-level message.");
 */

public class LoggerUtility {
	private static final Logger logger = Logger.getLogger(Main.class.getName());

	static {
		try {
			String documentsFolderPath = System.getProperty("user.home") + "\\Documents\\UHDD_Log_Files";
			
			Path path = Paths.get(documentsFolderPath);
	            if (!Files.exists(path)) {
	                Files.createDirectories(path);
	            }
			String logFilePath = documentsFolderPath + "\\mylog.log";
		    FileHandler fileHandler = new FileHandler(logFilePath);
		    fileHandler.setLevel(Level.ALL);
		    fileHandler.setFormatter(new SimpleFormatter());
		    logger.addHandler(fileHandler);
		} catch (IOException e) {
			 e.printStackTrace();
		}
	}
	
	public static Logger getLogger() {
		return logger;
	}
	
}
