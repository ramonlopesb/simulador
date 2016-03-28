package info;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class Controller extends Observable implements Observer {
	
	private Map<Long, List<Workload>> workloads;
	private long time = 0;
	
	public Controller(Map<Long, List<Workload>> wl) {
		this.workloads = wl;
	}
	
	@Override
	public void update(Observable arg0, Object arg) {
		if (arg instanceof Long) {
			time = (Long) arg;
			List<Request> requests = new ArrayList<Request>();
			if (workloads.containsKey(time)) {
				for (Workload w: workloads.get(time)) {
					requests.add(new Request(w.getID(), w));
				}
			}
			if (requests.size() > 0) {
				setChanged();
				notifyObservers(requests);
			}
		}
	}

}
