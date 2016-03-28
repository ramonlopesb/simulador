package info.image;

import java.util.List;

public class WebImageRepository {
	
	private static WebImageRepository instance;
	private List<Image> images;
	
	private WebImageRepository() {
	}
	
	public static WebImageRepository getInstance() {
		if (instance == null) {
			instance = new WebImageRepository();
		}
		return instance;
	}
	
	public void setImages(List<Image> imagesList) {
		images = imagesList;
	}
	
	public Image getImage(int imageID) {
		for (Image i: images) {
			if (i.getID() == imageID) {
				return i;
			}
		}
		return null;
	}

}
