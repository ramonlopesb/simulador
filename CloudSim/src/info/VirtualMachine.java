package info;

import java.util.Observable;
import java.util.Observer;

public class VirtualMachine extends Observable implements Runnable, Observer {
	
	private Request request;
	private Long time;
	private long endingTime;
	
	public VirtualMachine(Request r) {
		this.request = r;
		this.time = r.getStartTime();
		this.endingTime = r.getStartTime() + r.getDurationTime();
	}

	@Override
	public void run() {
		LogWritter.getInstance().write(request.getStartTime() + ": Iniciada a VM do pedido " + request.getRequestID());
		while (time < endingTime) {
		}
		setChanged();
		notifyObservers(request);
		LogWritter.getInstance().write(endingTime + ": Finalizada a VM do pedido " + request.getRequestID());
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg1 instanceof Long) {
			time = (Long) arg1;
		}
	}

	public Long getEndingTime() {
		return this.endingTime;
	}

}
