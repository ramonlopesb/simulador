package info;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImagesSize {

	private static ImagesSize instance;
	private Map<String, Map<Integer, Double>> dedupedImagesMap;

	private ImagesSize() {
		dedupedImagesMap = new HashMap<String, Map<Integer, Double>>();
	}

	public static ImagesSize getInstance() {
		if (instance == null) {
			instance = new ImagesSize();
		}
		return instance;
	}

	public void addInformation(Map<Integer, Double> info) {
		String id = "";
		for (Integer i : info.keySet()) {
			id += i;
		}
		if (!dedupedImagesMap.containsKey(id)) {
			this.dedupedImagesMap.put(id, new HashMap<Integer, Double>());
			for (Integer i : info.keySet()) {
				this.dedupedImagesMap.get(id).put(i, info.get(i));
			}
		}
	}

	public double getSizeOf(int[] storedIDs, int... toStore) {
		String ids = getID(storedIDs, toStore);
		double value = 0.0;
		if (!ids.equals("")) {
			Map<Integer, Double> map = this.dedupedImagesMap.get(ids);
			for (int id: toStore) {
				if (map.keySet().contains(id)) {
					value += this.dedupedImagesMap.get(ids).get(id);
				}
			}
		}
		return value;
	}
	
	public double getSizeOf(int[] storedIDs) {
		String id = getID(storedIDs);
		if (!id.equals("")) {
			double sum = 0.0;
			for (Integer i: this.dedupedImagesMap.get(id).keySet()) {
				sum += this.dedupedImagesMap.get(id).get(i);
			}
			return sum;
		}
		return -1.0;
	}
	
	private String getID(int[] storedIDs, int... toStore) {
		List<Integer> list = new ArrayList<Integer>();
		for (int i: storedIDs) {
			list.add(i);
		}
		for (int i: toStore) {
			if (!list.contains(i)) {
				list.add(i);
			}
		}
		String id = "";
		for (int i: list) {
			id += i;
		}
		return id;
	}
	
	private String getID(int[] storedIDs) {
		List<Integer> list = new ArrayList<Integer>();
		for (int i: storedIDs) {
			list.add(i);
		}
		String id = "";
		for (int i: list) {
			id += i;
		}
		return id;
	}

	public Map<String, Map<Integer, Double>> getDedupedImages() {
		return this.dedupedImagesMap;
	}

}
