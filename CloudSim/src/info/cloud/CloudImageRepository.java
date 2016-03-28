package info.cloud;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import com.google.common.primitives.Ints;

import info.ImagesSize;
import info.SystemProperties;
import info.image.Image;

public class CloudImageRepository implements Observer {

	private int ownerID;
	private double capacity;
	private Map<Integer, Image> images;
	private boolean onContention;
	// mapa dos donos das imagens <image, cloud>
	private Map<Integer, Integer> imagesCloudID;
	private Map<Integer, Long> imagesInsertionTimes;
	private Set<Integer> expiredImages;

	private final Long timeToExpire = Long.valueOf(SystemProperties.getInstance().getPropertyValue("time.to.expire"));

	// time of global clock
	private Long time;

	public CloudImageRepository(int id, double capacity) {
		this.ownerID = id;
		this.capacity = capacity;
		this.images = new HashMap<Integer, Image>();
		this.imagesCloudID = new HashMap<Integer, Integer>();
		this.imagesInsertionTimes = new HashMap<Integer, Long>();
		this.expiredImages = new HashSet<Integer>();
	}

	public synchronized void addImage(int cloudID, Image img, Long time) {
		if (!this.images.keySet().contains(img.getID())) {
			this.images.put(img.getID(), img);
			this.imagesCloudID.put(img.getID(), cloudID);
			this.imagesInsertionTimes.put(img.getID(), time);
		}
	}

	public synchronized double getExpiredSpace() {
		return ImagesSize.getInstance().getSizeOf(getImagesIDs(), Ints.toArray(expiredImages));
	}

	public synchronized double getRemoteSpace() {
		return ImagesSize.getInstance().getSizeOf(getImagesIDs(), Ints.toArray(getRemoteImagesIDs()));
	}

	public synchronized List<Integer> getRemoteImagesIDs() {
		List<Integer> remoteIDs = new ArrayList<Integer>();
		for (Integer id : images.keySet()) {
			if (id != ownerID) {
				remoteIDs.add(id);
			}
		}
		return remoteIDs;
	}
	
	public synchronized List<Integer> getLocalImagesByID() {
		List<Integer> localIDs = new ArrayList<Integer>();
		for (Integer id : images.keySet()) {
			if (id == ownerID) {
				localIDs.add(id);
			}
		}
		return localIDs;
	}

	public synchronized double getAvailableSpace() {
		return this.capacity - ImagesSize.getInstance().getSizeOf(getImagesIDs());
	}

	public synchronized void verifyContention(int... id) {
		double size = ImagesSize.getInstance().getSizeOf(getImagesIDs(), id);
		onContention = size > getAvailableSpace();
	}

	public synchronized boolean isOnContention() {
		return onContention;
	}

	public synchronized boolean removeImage(Image img) {
		Image removed = this.images.remove(img.getID());
		this.expiredImages.remove(img.getID());
		this.imagesInsertionTimes.remove(img.getID());
		this.imagesCloudID.remove(img.getID());
		return removed != null;
	}

	public int[] getImagesIDs() {
		int[] ids = new int[images.size()];
		int pos = 0;
		for (Integer id : images.keySet()) {
			ids[pos++] = id;
		}
		return ids;
	}

	public synchronized boolean contains(int imageID) {
		return this.images.keySet().contains(imageID);
	}

	public synchronized boolean contains(Image i) {
		return this.images.keySet().contains(i.getID());
	}

	public synchronized double getTotalSpace() {
		return this.capacity;
	}

	public synchronized int getImageOwnerID(int imageID) {
		return this.imagesCloudID.get(imageID);
	}

	public synchronized void setImageOwnerID(int newOwnerID, int imageID) {
		this.imagesCloudID.put(imageID, newOwnerID);
	}

	public void setImageInsertionTime(int imageID, Long time) {
		this.imagesInsertionTimes.put(imageID, time);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg1 instanceof Long) {
			time = (Long) arg1;
			for (Integer id : imagesInsertionTimes.keySet()) {
				if (time - imagesInsertionTimes.get(id) > timeToExpire) {
					this.expiredImages.add(id);
				} else {
					if (this.expiredImages.contains(id)) {
						this.expiredImages.remove(id);
					}
				}
			}
		}
	}

	public synchronized void removeExpired() {
		for (Integer id : this.expiredImages) {
			this.images.remove(id);
			this.imagesInsertionTimes.remove(id);
			this.imagesCloudID.remove(id);
		}
		this.expiredImages = new HashSet<Integer>();
	}

	public synchronized void removeRemote() {
		for (Integer id: getRemoteImagesIDs()) {
			this.images.remove(id);
			this.imagesInsertionTimes.remove(id);
			this.imagesCloudID.remove(id);
			this.expiredImages.remove(id);
		}
	}

	public void removeImage(int toRemove) {
		this.images.remove(toRemove);
		this.imagesInsertionTimes.remove(toRemove);
		this.imagesCloudID.remove(toRemove);
		this.expiredImages.remove(toRemove);
	}

	public double getUsedSpace(Integer cloudID) {
		double space = 0.0;
		for (Integer id: this.images.keySet()) {
			if (id == cloudID) {
				space += ImagesSize.getInstance().getSizeOf(getImagesIDs(), id);
			}
		}
		return space;
	}

	public List<Integer> getImagesIDs(int cloudID) {
		List<Integer> imagesIDs = new ArrayList<Integer>();
		for (Integer id: this.images.keySet()) {
			if (id == cloudID) {
				imagesIDs.add(id);
			}
		}
		return imagesIDs;
	}

}
