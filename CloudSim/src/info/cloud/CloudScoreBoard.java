package info.cloud;

import java.util.HashMap;
import java.util.Map;

public class CloudScoreBoard {

	// from, to, score
	private Map<Integer, Map<Integer, Long>> byCloud = new HashMap<Integer, Map<Integer, Long>>();
	// from, image, score
	private Map<Integer, Map<Integer, Long>> byImage = new HashMap<Integer, Map<Integer, Long>>();
	private static CloudScoreBoard instance;

	private CloudScoreBoard() {
	}

	public static CloudScoreBoard getInstance() {
		if (instance == null) {
			instance = new CloudScoreBoard();
		}
		return instance;
	}

	public synchronized void scoreByCloud(int cloudID, int toCloudID, long score) {
		if (byCloud.get(cloudID) == null) {
			byCloud.put(cloudID, new HashMap<Integer, Long>());
		}
		byCloud.get(cloudID).put(toCloudID, byCloud.get(cloudID).get(toCloudID) + score);
	}
	
	public synchronized void scoreByImage(int cloudID, int imageID, long score) {
		if (byImage.get(cloudID) == null) {
			byImage.put(cloudID, new HashMap<Integer, Long>());
		}
		byImage.get(cloudID).put(imageID, byImage.get(cloudID).get(imageID) + score);
	}

	public synchronized long getScoreByCloud(int cloudID, int toCloudID) {
		return Math.max(0, byCloud.get(cloudID).get(toCloudID) - byCloud.get(toCloudID).get(cloudID));
	}
	
	public synchronized long getScoreByImage(int cloudID, int imageID) {
		return byImage.get(cloudID).get(imageID);
	}
	
	public synchronized double calculateQuote(Integer ownerID, Integer cloudID, double space) {
		double reputation = calculateReputation(ownerID, cloudID);
		return reputation == 0.0 ? 0.0 : reputation * space;
	}
	
	public synchronized double calculateReputation(Integer ownerID, Integer cloudID) {
		long dividend = CloudScoreBoard.getInstance().getScoreByCloud(ownerID, cloudID);
		long divisor = 0;
		for (Integer id: this.byCloud.keySet()) {
			if (id != ownerID) {
				divisor += getScoreByCloud(ownerID, id);
			}
		}
		return divisor == 0.0 ? 0.0 : dividend / divisor;
	}
	
}
