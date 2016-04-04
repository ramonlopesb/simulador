package info;

import java.util.Observable;

public class GlobalClock extends Observable implements Runnable {
	
	private long time;
	private static GlobalClock instance;
	
	private GlobalClock() {
		time = 0;
	}
	
	public static GlobalClock getInstance() {
		if (instance == null) {
			instance = new GlobalClock();
		}
		return instance;
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				synchronized (this) {
					this.wait(100);
				}
				setChanged();
				notifyObservers(new Long(++time));
				System.out.println(time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
