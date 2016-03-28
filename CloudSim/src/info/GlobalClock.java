package info;

import java.util.Observable;

public class GlobalClock extends Observable implements Runnable {
	
	private long time;
	private Thread t;
	private static GlobalClock instance;
	
	private GlobalClock() {
		time = 0;
		t = new Thread(this);
		t.start(); 
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
				synchronized (t) {
					t.wait(1000);
				}
				setChanged();
				notifyObservers(new Long(++time));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
