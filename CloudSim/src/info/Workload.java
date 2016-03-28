package info;

public class Workload {

	private int id;
	// representa de onde vem o usuário
	private int fromCloudID;
	private long startingTime;
	private long durationTime;
	private int imageID;
	private int vcpus;
	private int ram;
	private double diskSize;

	public Workload(int id, int cloudID, long startTime, long durationTime, int imageID) {
		this.id = id;
		this.fromCloudID = cloudID;
		this.startingTime = startTime;
		this.durationTime = durationTime;
		this.imageID = imageID;
	}

	public Workload(Workload workload) {
		this(workload.getID(), workload.getFromCloudID(), workload.getStartingTime(), workload.getDurationTime(),
				workload.getImageID());
	}
	
	public void setStartingTime(long time) {
		this.startingTime = time;
	}

	public long getStartingTime() {
		return this.startingTime;
	}

	public int getVCPUs() {
		return this.vcpus;
	}

	public int getRam() {
		return this.ram;
	}

	public double getDiskSize() {
		return this.diskSize;
	}

	public int getImageID() {
		return this.imageID;
	}

	public long getDurationTime() {
		return this.durationTime;
	}

	public int getID() {
		return this.id;
	}

	public int getFromCloudID() {
		return fromCloudID;
	}

}
