package info;

public class Request {
	
	private int requestID;
	private Workload w;
	private boolean pending;

	public Request(int requestID, Workload workload) {
		this.requestID = requestID;
		this.w = workload;
		this.pending = true;
	}
	
	public Request(Request request) {
		this(request.getRequestID(), new Workload(request.getWorkload()));
		this.setPending(request.isPending());
	}

	private Workload getWorkload() {
		return this.w;
	}

	public int getRequestID() {
		return this.requestID;
	}

	public int getVCPUs() {
		return this.w.getVCPUs();
	}

	public int getRam() {
		return this.w.getRam();
	}

	public double getDiskSize() {
		return this.w.getDiskSize();
	}
	
	public int getImageID() {
		return this.w.getImageID();
	}
	
	public synchronized void setPending(boolean situation) {
		this.pending = situation;
	}
	
	public synchronized boolean isPending() {
		return this.pending;
	}

	public long getDurationTime() {
		return this.w.getDurationTime();
	}

	public long getStartTime() {
		return this.w.getStartingTime();
	}
	
	public int getFromCloudID() {
		return this.w.getFromCloudID();
	}
	
	public void setStartingTime(long time) {
		this.w.setStartingTime(time);
	}
	
}
