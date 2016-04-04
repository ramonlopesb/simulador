package extraction;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.paukov.combinatorics.Factory;
import org.paukov.combinatorics.Generator;
import org.paukov.combinatorics.ICombinatoricsVector;

import com.google.common.collect.Collections2;
import com.google.common.io.Files;

public class Main {

	static Map<String, Integer> individual;
	static Map<String, Integer> finalRes;
	static Map<String, Integer> intersection;

	public static void main(String[] args) throws IOException {
		File output = new File("/home/stack/git/CloudSim/Deduped_Images.txt");
		List<String> lines = Files.readLines(output, Charset.defaultCharset());
		individual = new HashMap<String, Integer>();
		finalRes = new HashMap<String, Integer>();

		for (String line : lines) {
			String[] split = line.split(" ");
			individual.put(split[0].trim(), Integer.valueOf(split[2]));
		}

		output = new File("/home/stack/git/CloudSim/resultado_dois_a_dois.txt");
		lines = Files.readLines(output, Charset.defaultCharset());
		List<String> out = new ArrayList<String>();

		for (String line : lines) {
			if (line.contains("Adicionando ") || line.contains("sdfs:/etc/sdfs/")) {
				out.add(line);
			}
		}

		intersection = new HashMap<String, Integer>();

		for (String image : individual.keySet()) {
			finalRes.put(image, individual.get(image));
		}

		int pos = 0;
		while (pos < out.size()) {
			String[] split = out.get(pos).split(" ");
			String[] image1path = split[1].split("/");
			String image1 = image1path[image1path.length - 1].trim();

			String[] image2path = split[3].split("/");
			String image2 = image2path[image2path.length - 1].trim();

			Integer result = Integer.valueOf(out.get(pos + 1).split(" ")[2].replace("M", ""));

			intersection.put(image1 + " " + image2, Math.abs(individual.get(image1) + individual.get(image2) - result));
			finalRes.put(image1 + " " + image2, result);
			pos += 2;
		}

		ICombinatoricsVector<String> imagens = Factory.createVector(individual.keySet());
		for (int i = 3; i < 8; i++) {
			if (i <= imagens.getSize()) {
				Generator<String> by = Factory.createSimpleCombinationGenerator(imagens, i);
				for (ICombinatoricsVector<String> combination : by) {
					int valueOne = intersect(true, combination.getVector().toArray(new String[combination.getSize()]));
					int valueTwo = intersect(false, combination.getVector().toArray(new String[combination.getSize()]));
					String id = "";
					for (String s: combination.getVector()) {
						id += s + " ";
					}
					id = id.trim();
					finalRes.put(id, (valueOne + valueTwo) / 2);
				}
			}
			System.out.println(i);
		}

		 BufferedWriter writer = new BufferedWriter(new FileWriter(new
		 File("final_result.txt")));
		 for (String i : finalRes.keySet()) {
			 String[] split = i.split(" ");
			 for (List<String> perm: Collections2.orderedPermutations(Arrays.asList(split))) {
				 for (String p: perm) {
					 writer.write(p);
					 writer.write(" ");
				 }
				 writer.write(": ");
				 writer.write(String.valueOf(finalRes.get(i)));
				 writer.write("\n");
			 }
		 }
		 writer.flush();
		 writer.close();
	}

	private static Integer min(String... images) {
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < images.length; i++) {
			for (int j = i + 1; j < images.length; j++) {
				int intersect;
				if (intersection.get(images[i] + " " + images[j]) != null) {
					intersect = intersection.get(images[i] + " " + images[j]);
				} else {
					intersect = intersection.get(images[j] + " " + images[i]);
				}
				if (intersect < min) {
					min = intersect;
				}
			}
		}
		return min;
	}

	private static Integer intersect(boolean empty, String... images) {
		Integer result = 0;
		for (String i : images) {
			result += individual.get(i);
		}
		
		ICombinatoricsVector<String> imagens = Factory.createVector(images);
		Generator<String> by2 = Factory.createSimpleCombinationGenerator(imagens, 2);
		for (ICombinatoricsVector<String> comb: by2) {
			result -= min(comb.getVector().toArray(new String[comb.getSize()]));
		}
		
		if (!empty) {
			for (int i = 3; i < 26; i++) {
				if (i <= images.length) {
					Generator<String> by = Factory.createSimpleCombinationGenerator(imagens, i);
					for (ICombinatoricsVector<String> comb: by) {
						if (i % 2 == 0) {
							result -= min(comb.getVector().toArray(new String[comb.getSize()]));
						} else {
							result += min(comb.getVector().toArray(new String[comb.getSize()]));
						}
					}
				}
			}
		}
		
		return Math.abs(result);
	}

}
