package info.cloud;

import java.util.List;

import info.Request;

public class CloudRequests {
	
	private int cloudID;
	private List<Request> requests;
	
	public CloudRequests(int id, List<Request> r) {
		this.cloudID = id;
		this.requests = r;
	}
	
	public int getCloudID() {
		return this.cloudID;
	}
	
	public List<Request> getRequests() {
		return this.requests;
	}

}
