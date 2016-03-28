package info.cloud;

import info.GlobalClock;
import info.ImagesSize;
import info.image.Image;

public class CacheSystem {

	private int ownerID;
	private CloudImageRepository repository;

	public CacheSystem(int ownerID, CloudImageRepository repository) {
		this.ownerID = ownerID;
		this.repository = repository;
		GlobalClock.getInstance().addObserver(repository);
	}

	public void add(Integer cloudID, Image image, Long time) {
		double imageSize = ImagesSize.getInstance().getSizeOf(this.repository.getImagesIDs(), image.getID());
		if (imageSize <= this.repository.getTotalSpace()) {
			if (this.repository.getAvailableSpace() < imageSize) {
				if (imageSize > this.repository.getAvailableSpace() + this.repository.getExpiredSpace()) {
					if (this.ownerID == cloudID) {
						addLocalImage(cloudID, image, imageSize, time);
					} else {
						addRemoteImage(cloudID, image, imageSize, time);
					}
				} else {
					this.repository.removeExpired();
					this.repository.addImage(cloudID, image, time);
				}
			} else {
				this.repository.addImage(cloudID, image, time);
			}
		}
	}

	private void addRemoteImage(Integer cloudID, Image image, double imageSize, Long time) {
		// TODO Auto-generated method stub
		double quote = CloudScoreBoard.getInstance().calculateQuote(ownerID, cloudID,
				this.repository.getAvailableSpace() + this.repository.getRemoteSpace());
		if (imageSize <= quote + this.repository.getAvailableSpace()) {
			int id = getLessReputationOverQuotaID(cloudID); // id == 0 não tem
															// cota excedente
			while (imageSize > this.repository.getAvailableSpace() + this.repository.getUsedSpace(cloudID) && id != 0) {
				removeImageWithLessCredit(id);
			}
			
			if (imageSize > this.repository.getAvailableSpace()) {
				while (imageSize > this.repository.getAvailableSpace()) {
					removeImageWithLessCredit(cloudID);
				}
			}
			
			this.repository.removeExpired();
			this.repository.addImage(cloudID, image, time);
		}
	}

	private void removeImageWithLessCredit(int cloudID) {
		long min = Long.MAX_VALUE;
		int toRemove = 0;
		for (Integer id : this.repository.getImagesIDs(cloudID)) {
			long score = CloudScoreBoard.getInstance().getScoreByImage(this.ownerID, id);
			if (min < score) {
				min = score;
				toRemove = id;
			}
		}
		this.repository.removeImage(toRemove);
	}

	private int getLessReputationOverQuotaID(Integer cloudID) {
		Integer selectedID = 0;
		double quota = Double.MAX_VALUE;
		for (Integer id : this.repository.getRemoteImagesIDs()) {
			if (id != cloudID) {
				double quote = CloudScoreBoard.getInstance().calculateQuote(ownerID, id,
						this.repository.getAvailableSpace() + this.repository.getRemoteSpace());
				if (quote < quota && quote < this.repository.getUsedSpace(id)) {
					quota = quote;
					selectedID = id;
				}
			}
		}
		return selectedID;
	}

	private void addLocalImage(Integer cloudID, Image image, double imageSize, Long time) {
		if (imageSize > this.repository.getAvailableSpace() + this.repository.getRemoteSpace()) {
			this.repository.removeRemote();
			while (imageSize > this.repository.getAvailableSpace()) {
				long min = Long.MAX_VALUE;
				int toRemove = 0;
				for (Integer id : this.repository.getLocalImagesByID()) {
					long score = CloudScoreBoard.getInstance().getScoreByImage(this.ownerID, id);
					if (min < score) {
						min = score;
						toRemove = id;
					}
				}
				this.repository.removeImage(toRemove);
			}
		} else {
			while (imageSize > this.repository.getAvailableSpace()) {
				long min = Long.MAX_VALUE;
				int toRemove = 0;
				for (Integer id : this.repository.getRemoteImagesIDs()) {
					long score = CloudScoreBoard.getInstance().getScoreByImage(this.ownerID, id);
					if (min < score) {
						min = score;
						toRemove = id;
					}
				}
				this.repository.removeImage(toRemove);
			}
		}
		this.repository.removeExpired();
		this.repository.addImage(cloudID, image, time);
	}

	public boolean contains(int imageID) {
		return this.repository.contains(imageID);
	}

	// verifica se o sistema está em contenção e armazena o estado da
	// verificação
	public void verifyContention(int... requestsImagesIDs) {
		this.repository.verifyContention(requestsImagesIDs);
	}

	public int getImageOwnerID(int imageID) {
		return this.repository.getImageOwnerID(imageID);
	}

	public void setImageOwnerID(int fromCloudID, int imageID) {
		this.repository.setImageOwnerID(fromCloudID, imageID);
	}

	public void setImageInsertionTime(int imageID, Long time) {
		this.repository.setImageInsertionTime(imageID, time);
	}

}
