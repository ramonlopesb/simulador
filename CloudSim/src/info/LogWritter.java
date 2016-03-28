package info;

import java.util.logging.Logger;

public class LogWritter {
	
	private Logger log;
	private static LogWritter instance;
	
	private LogWritter() {
		log = Logger.getAnonymousLogger();
	}
	
	public static LogWritter getInstance() {
		if (instance == null) {
			instance = new LogWritter();
		}
		return instance;
	}
	
	public synchronized void write(String msg) {
		this.log.fine(msg);
	}

}
