package extraction;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.google.common.io.Files;

public class WorkloadExtraction {

	public static void main(String[] args) throws IOException {
		File dir = new File("/home/stack/workspace/CloudSim");

		File[] files = new File[6];

		int pos = 0;
		for (int i = 0; i < dir.listFiles().length; i++) {
			if (dir.listFiles()[i].getName().startsWith("work")) {
				files[pos++] = dir.listFiles()[i];
			}
		}

		List<String> output = new ArrayList<String>();
		for (File f : files) {
			List<String> lines = Files.readLines(f, Charset.defaultCharset());
			for (String line : lines) {
				if (!line.startsWith("Submit")) {
					output.add(line);
				}
			}
		}

		
		List<String> output2 = new ArrayList<String>();
		for (String o : output) {
			int k = 30;
			int i = 0;
			while (i < 5) {
				for (int j = k + 1; j <= k + 15; j++) {
					if (o.contains("P" + j)) {
						output2.add(o.replace("P" + j, "P" + (j - k)));
					}
				}
				k += 15;
				i++;
			}
		}
		output = new ArrayList<String>();
		
		for (String o: output2) {
			int k = 300;
			int i = 0;
			while (i < 5) {
				for (int j = k + 1; j <= k + 15; j++) {
					if (o.contains("U" + j)) {
						output.add(o.replace("U" + j, "U" + (j - k)));
					}
				}
				k += 300;
				i++;
			}
		}
		
		output2 = new ArrayList<String>();
		for (String o: output) {
			String[] split = o.split(" ");
			output2.add(new String(split[2] + " " + split[0] + " " + split[1] + " " + split[4].replace("P", "")));
		}

		BufferedWriter writer = new BufferedWriter(new FileWriter(new File("result.txt")));
		for (String o : output2) {
			writer.write(o);
			writer.write("\n");
		}
		writer.flush();
		writer.close();
	}

}
