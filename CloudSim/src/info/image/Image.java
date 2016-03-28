package info.image;

public class Image implements Comparable<Image> {
	
	private String name;
	private String os;
	private String version;
	private int id;
	private int size;
	
	public Image(String name, String os, String version, 
			int id, int size) {
		this.name = name;
		this.os = os;
		this.version = version;
		this.id = id;
		this.size = size;
	}

	public String getName() {
		return this.name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Image)) {
			return false;
		}
		Image img = (Image) obj;
		if (!getOS().equals(img.getOS())) {
			return false;
		}
		if (!getVersion().equals(img.getVersion())) {
			return false;
		}
		return true;
	}

	private String getVersion() {
		return this.version;
	}

	private String getOS() {
		return this.os;
	}

	public int getID() {
		return this.id;
	}

	@Override
	public int compareTo(Image o) {
		return getID() == o.getID() ? 0 : (getID() > o.getID() ? 1 : -1);
	}

	public int getSize() {
		return this.size;
	}

}
